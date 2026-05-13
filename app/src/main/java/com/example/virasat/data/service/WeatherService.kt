package com.example.virasat.data.service

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

data class WeatherData(
    val temperature: Double,
    val condition: WeatherCondition,
    val description: String
)

enum class WeatherCondition {
    CLEAR, CLOUDY, RAIN, SNOW, THUNDERSTORM, DRIZZLE, FOG
}

object WeatherService {

    @SuppressLint("MissingPermission")
    suspend fun fetchCurrentLocation(context: Context): Location? = suspendCancellableCoroutine { cont ->
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                cont.resume(location)
            }
            .addOnFailureListener { exception ->
                cont.resumeWithException(exception)
            }
    }

    suspend fun fetchWeather(latitude: Double, longitude: Double): WeatherData = withContext(Dispatchers.IO) {
        val url =
            "https://api.open-meteo.com/v1/forecast?latitude=$latitude&longitude=$longitude&current_weather=true"
        val response = URL(url).readText()
        val json = JSONObject(response)
        val current = json.getJSONObject("current_weather")
        val temp = current.getDouble("temperature")
        val code = current.getInt("weathercode")

        val condition = mapWeatherCode(code)
        WeatherData(
            temperature = temp,
            condition = condition,
            description = describeCondition(condition)
        )
    }

    private fun mapWeatherCode(code: Int): WeatherCondition {
        return when (code) {
            0 -> WeatherCondition.CLEAR
            1, 2, 3 -> WeatherCondition.CLOUDY
            45, 48 -> WeatherCondition.FOG
            51, 53, 55, 56, 57 -> WeatherCondition.DRIZZLE
            61, 63, 65, 66, 67, 80, 81, 82 -> WeatherCondition.RAIN
            71, 73, 75, 77, 85, 86 -> WeatherCondition.SNOW
            95, 96, 99 -> WeatherCondition.THUNDERSTORM
            else -> WeatherCondition.CLEAR
        }
    }

    private fun describeCondition(condition: WeatherCondition): String {
        return when (condition) {
            WeatherCondition.CLEAR -> "Sunny"
            WeatherCondition.CLOUDY -> "Cloudy"
            WeatherCondition.RAIN -> "Rainy"
            WeatherCondition.SNOW -> "Snowy"
            WeatherCondition.THUNDERSTORM -> "Stormy"
            WeatherCondition.DRIZZLE -> "Drizzle"
            WeatherCondition.FOG -> "Foggy"
        }
    }
}
