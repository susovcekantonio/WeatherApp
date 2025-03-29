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

data class CityForecastDetails(
    val id: String,
    val name: String,
    val forecasts: List<DailyForecast>
)

data class DailyForecast(
    val date: String,
    val temp: Double,
    val icon: String,
    val description: String,
    val humidity: Int,
    val windSpeed: Double
)

data class Main(
    val temp: Double,
    val humidity: Int
)

data class Weather(
    val description: String,
    val icon: String
)

data class Wind(
    val speed: Double
)


data class ForecastResponse(
    val list: List<ForecastEntry>,
    val city: ApiCity
)

data class ApiCity(
    val id: String,
    val name: String
)

data class ForecastEntry(
    val dt: Long,
    val main: Main,
    val weather: List<Weather>,
    val wind: Wind
)