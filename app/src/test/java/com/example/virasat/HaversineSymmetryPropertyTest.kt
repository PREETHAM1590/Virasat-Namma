package com.example.virasat

import com.example.virasat.domain.ItineraryPlanner
import io.kotest.matchers.doubles.shouldBeLessThan
import io.kotest.property.Arb
import io.kotest.property.arbitrary.double
import io.kotest.property.checkAll
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import kotlin.math.abs

// Feature: virasat-heritage-app, Property 5: Haversine distance symmetry
class HaversineSymmetryPropertyTest {

    @Test
    fun `haversineDistance is symmetric`() = runTest {
        checkAll(100,
            Arb.double(-90.0..90.0),
            Arb.double(-180.0..180.0),
            Arb.double(-90.0..90.0),
            Arb.double(-180.0..180.0)
        ) { lat1, lon1, lat2, lon2 ->
            val d1 = ItineraryPlanner.haversineDistance(lat1, lon1, lat2, lon2)
            val d2 = ItineraryPlanner.haversineDistance(lat2, lon2, lat1, lon1)
            abs(d1 - d2) shouldBeLessThan 1e-9
        }
    }
}
