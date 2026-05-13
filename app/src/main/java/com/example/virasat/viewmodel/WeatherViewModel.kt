package com.example.virasat.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.virasat.data.service.WeatherData
import com.example.virasat.data.service.WeatherService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WeatherViewModel(application: Application) : AndroidViewModel(application) {

    private val _weather = MutableStateFlow<WeatherData?>(null)
    val weather: StateFlow<WeatherData?> = _weather

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val defaultLat = 12.9716 // Bangalore default
    private val defaultLon = 77.5946

    init {
        loadWeather(defaultLat, defaultLon)
    }

    fun refreshWithLocation(context: Context) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val location = WeatherService.fetchCurrentLocation(context)
                if (location != null) {
                    val weather = WeatherService.fetchWeather(location.latitude, location.longitude)
                    _weather.value = weather
                } else {
                    _error.value = "Location unavailable"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Weather fetch failed"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadWeather(lat: Double, lon: Double) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val weather = WeatherService.fetchWeather(lat, lon)
                _weather.value = weather
            } catch (e: Exception) {
                _error.value = e.message ?: "Weather fetch failed"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
