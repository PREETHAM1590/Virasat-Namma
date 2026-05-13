package com.example.virasat

import com.example.virasat.data.model.HeritageSite
import com.example.virasat.data.model.SiteType
import com.example.virasat.domain.ItineraryPlanner
import io.kotest.matchers.doubles.shouldBeLessThanOrEqual
import io.kotest.property.Arb
import io.kotest.property.arbitrary.*
import io.kotest.property.checkAll
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

// Feature: virasat-heritage-app, Property 6: Nearest-neighbour optimisation invariant
class NearestNeighbourPropertyTest {

    private fun arbHeritageSite(): Arb<HeritageSite> = Arb.bind(
        Arb.string(5..10),
        Arb.double(-90.0..90.0),
        Arb.double(-180.0..180.0)
    ) { id, lat, lon ->
        HeritageSite(
            id = id, name = id, nameLocal = "", location = "", district = "",
            type = SiteType.MONUMENT, description = "", shortDescription = "",
            history = "", architecture = "", legends = "", imageUrl = "",
            latitude = lat, longitude = lon, visitingHours = "", entryFee = "", qrCodeId = ""
        )
    }

    @Test
    fun `nearestNeighbour total distance is at most original order distance`() = runTest {
        checkAll(100, Arb.list(arbHeritageSite(), 2..10)) { sites ->
            val originalDist = ItineraryPlanner.totalDistance(sites)
            val ordered = ItineraryPlanner.nearestNeighbour(sites)
            val orderedDist = ItineraryPlanner.totalDistance(ordered)
            orderedDist shouldBeLessThanOrEqual (originalDist + 1e-9)
        }
    }
}
