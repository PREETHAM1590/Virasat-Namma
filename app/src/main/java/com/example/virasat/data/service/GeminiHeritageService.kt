package com.example.virasat.data.service

import com.example.virasat.data.source.KarnatakaSites
import com.google.genai.Client
import com.google.genai.types.GenerateContentResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray

object GeminiHeritageService {
    private var apiKey: String = ""
    private var client: Client? = null

    fun initialize(key: String) {
        apiKey = key
        client = Client.builder().apiKey(key).build()
    }

    fun isInitialized(): Boolean = apiKey.isNotBlank()

    private fun getSiteContext(siteId: String): String {
        val site = KarnatakaSites.allSites.find { it.id == siteId } ?: return ""
        return """
Site: ${site.name} (${site.nameLocal})
Location: ${site.location}, ${site.district}
Type: ${site.type}
Description: ${site.shortDescription}
History: ${site.history}
Architecture: ${site.architecture}
Legends: ${site.legends}
Key Facts:
${site.facts.joinToString("\n") { "- ${it.title}: ${it.description}" }}
""".trimIndent()
    }

    suspend fun generateNarration(siteId: String, language: String = "English"): String = withContext(Dispatchers.IO) {
        if (!isInitialized()) return@withContext KarnatakaSites.allSites.find { it.id == siteId }?.description ?: ""
        try {
            val context = getSiteContext(siteId)
            val prompt = "You are a knowledgeable heritage tour guide. Create a warm, engaging 60-second audio narration script in $language about this heritage site.\n\nSite Context:\n$context\n\nRequirements:\n- Write in $language only\n- Natural, conversational tone like a live tour guide\n- 150-200 words, approx 60 seconds spoken\n- Start with \"Welcome to...\" or equivalent in $language\n- Mention the site's historical significance and one fascinating architectural detail\n- End with an invitation to explore"
            val response = client!!.models.generateContent("gemini-2.5-flash-preview", prompt, null)
            response.text() ?: KarnatakaSites.allSites.find { it.id == siteId }?.description ?: ""
        } catch (e: Exception) {
            KarnatakaSites.allSites.find { it.id == siteId }?.shortDescription ?: "Explore this magnificent heritage site."
        }
    }

    suspend fun describeView(siteId: String, focus: String = "overview", language: String = "English"): String = withContext(Dispatchers.IO) {
        if (!isInitialized()) return@withContext "Take a moment to observe the intricate details of this site."
        try {
            val site = KarnatakaSites.allSites.find { it.id == siteId } ?: return@withContext ""
            val focusText = when (focus.lowercase()) {
                "architecture" -> site.architecture; "history" -> site.history; "legends" -> site.legends
                else -> site.description
            }
            val prompt = "You are looking at ${site.name} in Karnataka, India. Your current focus is on the $focus of this site.\n\nContext about $focus:\n${focusText}\n\nIn 2-3 sentences in $language, describe what makes this view special. Mention one specific detail the viewer might notice."
            val response = client!!.models.generateContent("gemini-2.5-flash-preview", prompt, null)
            response.text() ?: "Observe the remarkable craftsmanship of this heritage site."
        } catch (e: Exception) {
            "Each corner of this site holds centuries of history waiting to be discovered."
        }
    }

    suspend fun generateTrivia(siteId: String, count: Int = 3, language: String = "English"): List<TriviaQuestion> = withContext(Dispatchers.IO) {
        if (!isInitialized()) return@withContext defaultTrivia(siteId)
        try {
            val context = getSiteContext(siteId)
            val prompt = "Based on this heritage site context, create $count engaging trivia questions in $language.\n\nContext:\n$context\n\nReturn ONLY a JSON array with no markdown formatting, no code fences:\n[\n  {\"question\": \"...\", \"options\": [\"A. ...\", \"B. ...\", \"C. ...\", \"D. ...\"], \"correctAnswer\": 0, \"explanation\": \"...\"}\n]\nCorrectAnswer is the 0-based index of the correct option."
            val response = client!!.models.generateContent("gemini-2.5-flash-preview", prompt, null)
            val text = response.text() ?: return@withContext defaultTrivia(siteId)
            parseTriviaJson(text, count)
        } catch (e: Exception) {
            defaultTrivia(siteId)
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
            defaultTrivia(text.hashCode().toString().take(3))
        }
    }

    private fun defaultTrivia(siteId: String): List<TriviaQuestion> {
        val site = KarnatakaSites.allSites.find { it.id == siteId } ?: return emptyList()
        return site.facts.take(3).mapIndexed { i, fact ->
            TriviaQuestion(
                question = "Fact about ${site.name}",
                options = listOf("A. ${fact.title}", "B. It is a modern addition", "C. Not related to this site", "D. Found in another state"),
                correctAnswer = 0,
                explanation = fact.description
            )
        }
    }
}

data class TriviaQuestion(
    val question: String,
    val options: List<String>,
    val correctAnswer: Int,
    val explanation: String
)