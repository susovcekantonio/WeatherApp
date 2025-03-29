package com.example.weatherapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.model.CityForecastDetails
import com.example.weatherapp.model.CityWeather
import com.example.weatherapp.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository
) : ViewModel() {

    private val _citiesState = MutableStateFlow<UiState<List<CityWeather>>>(UiState.Loading)
    val citiesState: StateFlow<UiState<List<CityWeather>>> = _citiesState

    private val _cityDetailsState = MutableStateFlow<UiState<CityForecastDetails>>(UiState.Loading)
    val cityDetailsState: StateFlow<UiState<CityForecastDetails>> = _cityDetailsState

    init {
        loadCities(listOf("2643743", "2968815", "5368361"))
    }

    private fun loadCities(cityIds: List<String>) {
        viewModelScope.launch {
            _citiesState.value = UiState.Loading
            try {
                val cities = repository.getCitiesWeather(cityIds)
                _citiesState.value = UiState.Success(cities)
            } catch (e: Exception) {
                _citiesState.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun loadCityDetails(cityId: String) {
        viewModelScope.launch {
            _cityDetailsState.value = UiState.Loading
            try {
                val details = repository.getCityForecast(cityId)
                _cityDetailsState.value = UiState.Success(details)
            } catch (e: Exception) {
                _cityDetailsState.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}

sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    data class Success<out T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}