package com.example.virasat.data.service

import android.graphics.Bitmap
import android.util.Base64
import com.example.virasat.data.model.HeritageSite
import com.example.virasat.data.model.QuizQuestion
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import java.io.ByteArrayOutputStream
import java.util.concurrent.TimeUnit

object AIHeritageService {
    private var apiKey: String = ""
    private var deepseekApiKey: String = ""
    private var novaApiUrl: String = ""
    private var novaApiKey: String = ""

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    fun initialize(key: String) { apiKey = key }
    fun initializeDeepSeek(key: String) { deepseekApiKey = key }
    fun initializeNova(url: String, key: String = "") { novaApiUrl = url; novaApiKey = key }

    fun isInitialized(): Boolean = apiKey.isNotBlank() || deepseekApiKey.isNotBlank() || novaApiUrl.isNotBlank()

    private fun getSiteContext(site: HeritageSite?): String {
        if (site == null) return ""
        return """
Site: ${site.name} (${site.nameLocal})
Location: ${site.location}, ${site.district}
Coordinates: ${site.latitude}, ${site.longitude}
Type: ${site.type}
Description: ${site.shortDescription}
History: ${site.history}
Architecture: ${site.architecture}
Legends: ${site.legends}
Key Facts:
${site.facts.joinToString("\n") { "- ${it.title}: ${it.description}" }}
""".trimIndent()
    }

    internal fun buildPayload(prompt: String): String {
        val part = org.json.JSONObject().put("text", prompt)
        val parts = org.json.JSONArray().put(part)
        val content = org.json.JSONObject().put("parts", parts)
        val contents = org.json.JSONArray().put(content)
        return org.json.JSONObject().put("contents", contents).toString()
    }

    internal suspend fun callAI(prompt: String): String = withContext(Dispatchers.IO) {
        return@withContext callNova(prompt)
    }

    private suspend fun callNova(prompt: String, imageBase64: String? = null, imageMimeType: String? = null): String = withContext(Dispatchers.IO) {
        try {
            val bodyObj = org.json.JSONObject().put("prompt", prompt)
            if (imageBase64 != null) {
                bodyObj.put("imageBase64", imageBase64)
                bodyObj.put("imageMimeType", imageMimeType ?: "image/jpeg")
            }
            val request = Request.Builder()
                .url(novaApiUrl)
                .apply { if (novaApiKey.isNotBlank()) addHeader("x-api-key", novaApiKey) }
                .post(bodyObj.toString().toRequestBody("application/json".toMediaType()))
                .build()
            client.newCall(request).execute().use { response ->
                val bodyStr = response.body?.string() ?: ""
                if (!response.isSuccessful) {
                    android.util.Log.w("AIService", "Nova HTTP ${response.code}")
                    return@withContext "NOVA_HTTP_${response.code}"
                }
                val text = org.json.JSONObject(bodyStr).let {
                    it.optString("text", "").ifBlank { it.optString("reply", "") }
                }
                if (text.isBlank()) android.util.Log.w("AIService", "Nova returned empty response")
                text
            }
        } catch (e: Exception) {
            android.util.Log.w("AIService", "Nova request failed")
            "NOVA_ERROR"
        }
    }

    private suspend fun callDeepSeek(prompt: String): String = withContext(Dispatchers.IO) {
        try {
            val body = org.json.JSONObject()
                .put("model", "deepseek-v4-flash")
                .put("messages", org.json.JSONArray()
                    .put(org.json.JSONObject().put("role", "user").put("content", prompt)))
                .put("stream", false)
                .toString()
            val request = Request.Builder()
                .url("https://api.deepseek.com/chat/completions")
                .addHeader("Authorization", "Bearer $deepseekApiKey")
                .post(body.toRequestBody("application/json".toMediaType()))
                .build()
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    android.util.Log.w("AIService", "DeepSeek HTTP ${response.code}")
                    return@withContext "DEEPSEEK_HTTP_${response.code}"
                }
                val json = org.json.JSONObject(response.body?.string() ?: return@withContext "")
                json.getJSONArray("choices")
                    .getJSONObject(0)
                    .getJSONObject("message")
                    .optString("content", "")
            }
        } catch (e: Exception) {
            android.util.Log.w("AIService", "DeepSeek request failed")
            "DEEPSEEK_ERROR"
        }
    }

    private suspend fun callMultimodal(
        promptText: String,
        imageBytes: ByteArray
    ): String = withContext(Dispatchers.IO) {
        if (!isInitialized()) return@withContext ""
        try {
            val base64Image = Base64.encodeToString(imageBytes, Base64.NO_WRAP)
            callNova(promptText, base64Image, "image/jpeg")
        } catch (_: Exception) {
            ""
        }
    }

    suspend fun generateNarration(site: HeritageSite?, language: String = "English"): String = withContext(Dispatchers.IO) {
        if (!isInitialized()) return@withContext site?.description ?: ""
        try {
            val context = getSiteContext(site)
            val prompt = "You are a knowledgeable heritage tour guide. Create a warm, engaging 60-second audio narration script in $language about this heritage site.\n\nSite Context:\n$context\n\nRequirements:\n- Write in $language only\n- Natural, conversational tone like a live tour guide\n- 150-200 words, approx 60 seconds spoken\n- Start with \"Welcome to...\" or equivalent in $language\n- Mention the site's historical significance and one fascinating architectural detail\n- End with an invitation to explore"
            val response = callAI(prompt) ?: ""
            response.ifBlank { site?.description ?: "" }
        } catch (e: Exception) {
            site?.shortDescription ?: "Explore this magnificent heritage site."
        }
    }

    suspend fun describeView(site: HeritageSite?, focus: String = "overview", language: String = "English"): String = withContext(Dispatchers.IO) {
        if (site == null) return@withContext ""
        if (!isInitialized()) return@withContext "Take a moment to observe the intricate details of this site."
        try {
            val focusText = when (focus.lowercase()) {
                "architecture" -> site.architecture; "history" -> site.history; "legends" -> site.legends
                else -> site.description
            }
            val prompt = "You are looking at ${site.name} in Karnataka, India. Your current focus is on the $focus of this site.\n\nContext about $focus:\n${focusText}\n\nIn 2-3 sentences in $language, describe what makes this view special. Mention one specific detail the viewer might notice."
            val response = callAI(prompt) ?: ""
            response.ifBlank { "Observe the remarkable craftsmanship of this heritage site." }
        } catch (e: Exception) {
            "Each corner of this site holds centuries of history waiting to be discovered."
        }
    }

    suspend fun generateTrivia(site: HeritageSite?, count: Int = 3, language: String = "English"): List<TriviaQuestion> = withContext(Dispatchers.IO) {
        if (!isInitialized()) return@withContext defaultTrivia(site)
        try {
            val context = getSiteContext(site)
            val prompt = "Based on this heritage site context, create $count engaging trivia questions in $language.\n\nContext:\n$context\n\nReturn ONLY a JSON array with no markdown formatting, no code fences:\n[\n  {\"question\": \"...\", \"options\": [\"A. ...\", \"B. ...\", \"C. ...\", \"D. ...\"], \"correctAnswer\": 0, \"explanation\": \"...\"}\n]\nCorrectAnswer is the 0-based index of the correct option."
            val response = callAI(prompt) ?: return@withContext defaultTrivia(site)
            parseTriviaJson(response, count)
        } catch (e: Exception) {
            defaultTrivia(site)
        }
    }

    private fun parseTriviaJson(text: String, count: Int): List<TriviaQuestion> {
        val cleaned = text.replace("```json", "").replace("```", "").trim()
        return try {
            val json = JSONArray(cleaned)
            (0 until minOf(json.length(), count)).map { i ->
                val obj = json.getJSONObject(i)
                val options = mutableListOf<String>()
                val optArr = obj.getJSONArray("options")
                for (j in 0 until optArr.length()) options.add(optArr.getString(j))
                TriviaQuestion(
                    question = obj.getString("question"),
                    options = options,
                    correctAnswer = obj.getInt("correctAnswer"),
                    explanation = obj.optString("explanation", "")
                )
            }
        } catch (e: Exception) {
            defaultTrivia(null)
        }
    }

    suspend fun generateSnapshotNarration(
        site: HeritageSite?,
        viewLabel: String,
        language: String = "English"
    ): String = withContext(Dispatchers.IO) {
        if (!isInitialized()) return@withContext fallbackSnapshotNarration(language)
        try {
            val context = getSiteContext(site)
            val prompt = """You are an expert heritage guide at ${site?.name ?: "a Karnataka heritage site"}. The visitor is looking at this specific view: "$viewLabel".

Site context:
$context

In a warm, conversational tone in $language, describe what the visitor is seeing in 3-4 sentences. Point out one fascinating detail they might miss. Keep it to about 30 seconds of spoken time. Start with an engaging observation."""
            val response = callAI(prompt) ?: return@withContext fallbackSnapshotNarration(language)
            response.ifBlank { fallbackSnapshotNarration(language) }
        } catch (_: Exception) {
            fallbackSnapshotNarration(language)
        }
    }

    private fun fallbackSnapshotNarration(language: String): String =
        if (language.contains("Kannada", ignoreCase = true))
            "ಈ ದೃಶ್ಯದಲ್ಲಿ ಶತಮಾನಗಳ ಕಥೆಗಳು ಅಡಗಿವೆ. ಸೂಕ್ಷ್ಮ ವಿವರಗಳನ್ನು ಗಮನಿಸಿ."
        else
            "Each view here holds centuries of stories waiting to be discovered."

    suspend fun generateCuriosityQuestions(
        site: HeritageSite?,
        viewLabel: String,
        language: String = "English"
    ): List<String> = withContext(Dispatchers.IO) {
        if (!isInitialized()) return@withContext curiosityFallback(site)
        try {
            val context = getSiteContext(site)
            val prompt = """You are a curious heritage guide at $viewLabel. The visitor has just heard about this view.

Site context:
$context

Generate exactly 3 short, thought-provoking curiosity questions in $language that encourage the visitor to look closer at this view. Each question should be 1 sentence, no longer than 15 words. Return as a plain numbered list, one per line. No markdown, no quotes."""
            val response = callAI(prompt) ?: return@withContext curiosityFallback(site)
            response.lines()
                .map { it.replace(Regex("^\\d+[.)]\\s*"), "").trim() }
                .filter { it.isNotBlank() && it.endsWith("?") }
                .take(3)
        } catch (e: Exception) {
            curiosityFallback(site)
        }
    }

    suspend fun analyzeStreetViewSnapshot(
        site: HeritageSite?,
        bitmap: Bitmap,
        language: String = "English",
        cameraHeading: Float = 0f,
        cameraPitch: Float = 0f
    ): String = withContext(Dispatchers.IO) {
        if (!isInitialized()) return@withContext fallbackSnapshotNarration(language)
        try {
            val context = getSiteContext(site)

            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, stream)
            val imageBytes = stream.toByteArray()

            val promptText = """You are an expert heritage guide at ${site?.name ?: "a Karnataka heritage site"}. The visitor is currently looking at a live Street View capture from this historic location.

The camera is facing heading $cameraHeading degrees, tilt $cameraPitch degrees.

Site context:
$context

Look at this Street View image carefully. In a warm, conversational tone in $language, describe what the visitor is seeing — point out specific architectural details, textures, materials, or historical features visible in this exact view. Mention one fascinating detail they might miss. Keep it to about 30 seconds of spoken time."""

            val response = callMultimodal(promptText, imageBytes)
            response.ifBlank { fallbackSnapshotNarration(language) }
        } catch (_: Exception) {
            fallbackSnapshotNarration(language)
        }
    }

    suspend fun chatWithHeritageGuide(userMessage: String, language: String = "English", siteContext: String = "", sitesSummary: String = ""): String = withContext(Dispatchers.IO) {
        if (!isInitialized()) return@withContext "I'm currently offline. Please check your connection and try again."
        try {
            val prompt = """You are Virasat Guide, a knowledgeable and warm heritage tour guide for Karnataka, India.

Available heritage sites in Karnataka:
${sitesSummary.ifBlank { "Various temples, palaces, forts and monuments across Karnataka." }}

User question: $userMessage

Instructions:
- Reply in $language
- Be conversational, warm, and enthusiastic about Karnataka's heritage
- If asking about a specific site, give detailed historical and architectural info
- If asking for recommendations, suggest 2-3 relevant sites with brief reasons
- If asking about entry fees, timings, or travel tips, give practical advice
- Keep responses concise but informative (under 150 words)
- If the user greets you, respond warmly and offer to help explore Karnataka's heritage

Site-specific context provided: ${siteContext.ifBlank { "None" }}"""
            val response = callAI(prompt) ?: ""
            response.ifBlank { "I'm exploring the heritage archives for you. Could you rephrase your question?" }
        } catch (e: Exception) {
            "I'm having trouble connecting to the heritage database. Please try again in a moment."
        }
    }

    private fun curiosityFallback(site: HeritageSite?): List<String> {
        return listOf(
            "What stories do these stone walls hold?",
            "Can you spot the craftsmanship detail in this view?",
            "How many generations walked this very spot?"
        )
    }

    private fun defaultTrivia(site: HeritageSite?): List<TriviaQuestion> {
        if (site == null) return emptyList()
        return site.facts.take(3).mapIndexed { i, fact ->
            TriviaQuestion(
                question = "Fact about ${site.name}",
                options = listOf("A. ${fact.title}", "B. It is a modern addition", "C. Not related to this site", "D. Found in another state"),
                correctAnswer = 0,
                explanation = fact.description
            )
        }
    }

    suspend fun generateQuizForSite(
        site: HeritageSite?,
        count: Int = 8,
        language: String = "English",
        allSiteNames: List<String> = emptyList()
    ): List<QuizQuestion> = withContext(Dispatchers.IO) {
        if (!isInitialized() || site == null) return@withContext emptyList()
        try {
            val context = getSiteContext(site)
            val distractors = allSiteNames.filter { it != site.name }.shuffled().take(20)
                .joinToString(", ")
            val prompt = """You are a heritage quiz creator for the Karnataka heritage app.
Based on this site, create exactly $count diverse multiple-choice quiz questions in $language.
Cover history, architecture, legends, district, facts, and notable features.

Site context:
$context

Other Karnataka site names for wrong options (use freely):
$distractors

Return ONLY a JSON array, no markdown, no code fences:
[
  {
    "question": "...",
    "options": ["...", "...", "...", "..."],
    "correctAnswer": 0,
    "explanation": "..."
  }
]
Rules:
- correctAnswer is 0-based index of the right option
- All 4 options plausible, only 1 correct
- Questions in $language only
- No duplicate questions"""
            val response = callAI(prompt)
            if (response.isBlank()) return@withContext emptyList()
            parseQuizJson(response, count)
        } catch (_: Exception) {
            emptyList()
        }
    }

    suspend fun generateGeneralKarnatakaQuiz(
        count: Int = 8,
        language: String = "English",
        allSites: List<HeritageSite> = emptyList()
    ): List<QuizQuestion> = withContext(Dispatchers.IO) {
        if (!isInitialized()) return@withContext emptyList()
        try {
            val siteSummary = allSites.take(20).joinToString("\n") {
                "- ${it.name} (${it.nameLocal}): ${it.district}, ${it.type.name}, ${it.shortDescription.take(80)}"
            }
            val prompt = """You are a heritage quiz creator for the Karnataka heritage app.
Create exactly $count diverse multiple-choice questions in $language about Karnataka's heritage sites.
Cover UNESCO sites, famous temples, forts, palaces, districts, history, and architecture.

Available sites:
$siteSummary

Return ONLY a JSON array, no markdown, no code fences:
[
  {
    "question": "...",
    "options": ["...", "...", "...", "..."],
    "correctAnswer": 0,
    "explanation": "..."
  }
]
Rules:
- correctAnswer is 0-based index of the right option
- All 4 options plausible, only 1 correct
- Questions in $language only"""
            val response = callAI(prompt)
            if (response.isBlank()) return@withContext emptyList()
            parseQuizJson(response, count)
        } catch (_: Exception) {
            emptyList()
        }
    }

    private fun parseQuizJson(text: String, count: Int): List<QuizQuestion> {
        val cleaned = text.replace("```json", "").replace("```", "").trim()
        return try {
            val json = JSONArray(cleaned)
            (0 until minOf(json.length(), count)).mapNotNull { i ->
                try {
                    val obj = json.getJSONObject(i)
                    val options = mutableListOf<String>()
                    val optArr = obj.getJSONArray("options")
                    for (j in 0 until optArr.length()) options.add(optArr.getString(j))
                    if (options.size < 2) return@mapNotNull null
                    val correctIdx = obj.getInt("correctAnswer").coerceIn(0, options.lastIndex)
                    QuizQuestion(
                        question = obj.getString("question"),
                        options = options,
                        correctAnswer = correctIdx,
                        explanation = obj.optString("explanation", "")
                    )
                } catch (_: Exception) { null }
            }
        } catch (_: Exception) {
            emptyList()
        }
    }

    suspend fun generateChapterNarration(
        site: HeritageSite?,
        chapterKey: String,
        language: String = "English"
    ): String = withContext(Dispatchers.IO) {
        if (!isInitialized() || site == null) return@withContext ""
        try {
            val baseText = when (chapterKey) {
                "history"      -> site.history
                "architecture" -> site.architecture
                "legends"      -> site.legends
                "facts"        -> site.facts.take(4).joinToString(" | ") { "${it.title}: ${it.description}" }
                else           -> site.shortDescription
            }
            if (baseText.isBlank()) return@withContext ""
            val siteName = if (language.contains("Kannada", ignoreCase = true)) site.nameLocal.ifBlank { site.name } else site.name
            val chapterLabel = when (chapterKey) {
                "history"      -> if (language.contains("Kannada", ignoreCase = true)) "ಇತಿಹಾಸ" else "History"
                "architecture" -> if (language.contains("Kannada", ignoreCase = true)) "ವಾಸ್ತುಶಿಲ್ಪ" else "Architecture"
                "legends"      -> if (language.contains("Kannada", ignoreCase = true)) "ದಂತಕಥೆಗಳು" else "Legends"
                "facts"        -> if (language.contains("Kannada", ignoreCase = true)) "ಸ್ವಾರಸ್ಯಕರ ಸಂಗತಿಗಳು" else "Interesting Facts"
                else           -> if (language.contains("Kannada", ignoreCase = true)) "ಪರಿಚಯ" else "Introduction"
            }
            val prompt = """You are a live heritage audio guide for Karnataka, India.
Speak naturally in $language about the $chapterLabel of $siteName.

Source material:
$baseText

Requirements:
- Write ONLY in $language — no English if the language is Kannada
- Conversational, warm spoken-word narration (not an essay)
- 120-160 words — exactly right for a 60-second audio clip
- No bullet points, no headings, no markdown
- Naturally mention the site name $siteName once
- End with one sentence inviting visitors to observe or explore"""
            val response = callAI(prompt)
            response.ifBlank { baseText }
        } catch (_: Exception) {
            ""
        }
    }
}

data class TriviaQuestion(
    val question: String,
    val options: List<String>,
    val correctAnswer: Int,
    val explanation: String
)
