package com.example.weatherapp.repository

import com.example.weatherapp.api.WeatherApi
import com.example.weatherapp.model.CityForecastDetails
import com.example.weatherapp.model.CityWeather
import com.example.weatherapp.model.DailyForecast
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
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


    suspend fun getCityForecast(cityId: String): CityForecastDetails {
        val response = weatherApi.getCityForecast(cityId)


        val dailyForecasts = response.list
            .groupBy { entry ->
                SimpleDateFormat("yyyy-MM-dd", Locale.US)
                    .format(Date(entry.dt * 1000))
            }
            .values
            .take(5)
            .map { dailyEntries ->
                val firstEntry = dailyEntries.first()
                DailyForecast(
                    date = SimpleDateFormat("EEE", Locale.US)
                        .format(Date(firstEntry.dt * 1000)),
                    temp = firstEntry.main.temp,
                    icon = firstEntry.weather.first().icon,
                    description = firstEntry.weather.first().description,
                    humidity = firstEntry.main.humidity,
                    windSpeed = firstEntry.wind.speed
                )
            }

        return CityForecastDetails(
            id = response.city.id,
            name = response.city.name,
            forecasts = dailyForecasts
        )
    }
}

class WeatherDataException(message: String) : Exception(message)