package com.example.weatherapp.api

import com.example.weatherapp.model.CitiesWeatherResponse
import com.example.weatherapp.model.CityWeatherDetails
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("group")
    suspend fun getCitiesWeather(
        @Query("id") cityIds: String,
        @Query("units") units: String = "metric",
        @Query("appid") apiKey: String = "5dbb823af5295f6ec3a7f495c8b1e4dd"
    ): CitiesWeatherResponse

    @GET("weather")
    suspend fun getCityWeather(
        @Query("id") cityId: String,
        @Query("units") units: String = "metric",
        @Query("appid") apiKey: String = "5dbb823af5295f6ec3a7f495c8b1e4dd"
    ): CityWeatherDetails
}