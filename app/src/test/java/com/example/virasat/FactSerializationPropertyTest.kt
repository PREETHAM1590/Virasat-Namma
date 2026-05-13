package com.example.virasat

import com.example.virasat.data.local.Converters
import com.example.virasat.data.model.Fact
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.*
import io.kotest.property.checkAll
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

// Feature: virasat-heritage-app, Property 2: Fact serialization round-trip
class FactSerializationPropertyTest {

    private fun arbFact(): Arb<Fact> = Arb.bind(
        Arb.string(1..50),
        Arb.string(1..50),
        Arb.string(1..100),
        Arb.boolean()
    ) { id, title, desc, unlocked -> Fact(id, title, desc, unlocked) }

    @Test
    fun `Converters fact round-trip preserves all fields`() = runTest {
        val converters = Converters()
        checkAll(100, Arb.list(arbFact(), 0..20)) { facts ->
            val encoded = converters.toFactList(facts)
            val decoded = converters.fromFactList(encoded)
            decoded shouldBe facts
        }
    }
}
