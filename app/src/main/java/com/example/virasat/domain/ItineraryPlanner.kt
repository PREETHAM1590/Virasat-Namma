package com.example.virasat.domain

import com.example.virasat.data.model.HeritageSite
import kotlin.math.*

object ItineraryPlanner {

    fun haversineDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val R = 6371.0
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = sin(dLat / 2).pow(2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) * sin(dLon / 2).pow(2)
        return R * 2 * asin(sqrt(a))
    }

    fun totalDistance(sites: List<HeritageSite>): Double =
        sites.zipWithNext().sumOf { (a, b) ->
            haversineDistance(a.latitude, a.longitude, b.latitude, b.longitude)
        }

    fun nearestNeighbour(sites: List<HeritageSite>): List<HeritageSite> {
        if (sites.size < 2) return sites
        val remaining = sites.toMutableList()
        val ordered = mutableListOf(remaining.removeAt(0))
        while (remaining.isNotEmpty()) {
            val last = ordered.last()
            val nearest = remaining.minBy { haversineDistance(last.latitude, last.longitude, it.latitude, it.longitude) }
            ordered.add(nearest)
            remaining.remove(nearest)
        }
        return ordered
    }
}
