package com.example.virasat

import com.example.virasat.data.service.AIHeritageService
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.string
import io.kotest.property.checkAll
import kotlinx.coroutines.test.runTest
import org.json.JSONObject
import org.junit.jupiter.api.Test

// Feature: virasat-heritage-app, Property 1: AI payload JSON safety
class AIPayloadPropertyTest {

    @Test
    fun `buildPayload produces valid JSON and round-trips text field`() = runTest {
        checkAll(100, Arb.string(0..200)) { prompt ->
            val payload = AIHeritageService.buildPayload(prompt)
            val parsed = JSONObject(payload)
            val text = parsed.getJSONArray("contents")
                .getJSONObject(0).getJSONArray("parts")
                .getJSONObject(0).getString("text")
            text shouldBe prompt
        }
    }
}
