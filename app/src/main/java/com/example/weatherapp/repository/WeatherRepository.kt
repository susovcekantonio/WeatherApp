package com.example.weatherapp.repository

import com.example.weatherapp.api.WeatherApi
import com.example.weatherapp.model.CitiesWeatherResponse
import com.example.weatherapp.model.CityWeather
import com.example.weatherapp.model.CityWeatherDetails
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val weatherApi: WeatherApi
) {
    suspend fun getCitiesWeather(cityIds: List<String>): List<CityWeather> {
        return try {
            weatherApi.getCitiesWeather(cityIds.joinToString(",")).list
        } catch (e: Exception) {
            throw WeatherDataException("Failed to fetch cities: ${e.message}")
        }
    }

    suspend fun getCityWeather(cityId: String): CityWeatherDetails {
        return try {
            weatherApi.getCityWeather(cityId)
        } catch (e: Exception) {
            throw WeatherDataException("Failed to fetch city details: ${e.message}")
        }
    }
}

class WeatherDataException(message: String) : Exception(message)