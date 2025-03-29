package com.example.weatherapp.model

data class CitiesWeatherResponse(
    val list: List<CityWeather>
)

data class CityWeather(
    val id: Long,
    val name: String,
    val weather: List<Weather>,
    val main: Main,
    val wind: Wind
)

data class CityWeatherDetails(
    val id: Long,
    val name: String,
    val weather: List<Weather>,
    val main: Main,
    val wind: Wind
)

data class Weather(
    val description: String,
    val icon: String
)

data class Main(
    val temp: Double,
    val humidity: Int
)

data class Wind(
    val speed: Double
)