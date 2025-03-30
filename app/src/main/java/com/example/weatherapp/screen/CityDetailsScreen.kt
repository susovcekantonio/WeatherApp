package com.example.weatherapp.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.example.weatherapp.model.CityForecastDetails
import com.example.weatherapp.model.DailyForecast
import com.example.weatherapp.viewmodel.UiState
import com.example.weatherapp.viewmodel.WeatherViewModel
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.draw.scale

@Composable
fun CityDetailsScreen(cityId: String) {
    val viewModel: WeatherViewModel = hiltViewModel()
    val state by viewModel.cityDetailsState.collectAsStateWithLifecycle()

    LaunchedEffect(cityId) {
        viewModel.loadCityDetails(cityId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        when (state) {
            is UiState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(16.dp)
                )
            }
            is UiState.Error -> {
                Text(
                    text = "Error: ${(state as UiState.Error).message}",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            is UiState.Success -> {
                val cityData = (state as UiState.Success<CityForecastDetails>).data
                CityForecastView(city = cityData)
            }
        }
    }
}

@Composable
private fun CityForecastView(city: CityForecastDetails) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = city.name,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = "5-Day Forecast",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        city.forecasts.forEach { forecast ->
            ForecastDayItem(forecast = forecast)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun ForecastDayItem(forecast: DailyForecast) {
    val infiniteTransition = rememberInfiniteTransition()
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        )
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = forecast.date,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
                Box(modifier = Modifier.scale(pulseScale)) {
                    Image(
                        painter = rememberAsyncImagePainter(
                            model = "https://openweathermap.org/img/wn/${forecast.icon}@2x.png"
                        ),
                        contentDescription = forecast.description,
                        modifier = Modifier.size(48.dp))
                }

                Text(
                    text = "${forecast.temp}°C",
                    style = MaterialTheme.typography.titleMedium)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                WeatherInfoItem(
                    label = "Humidity",
                    value = "${forecast.humidity}%"
                )
                WeatherInfoItem(
                    label = "Wind",
                    value = "${forecast.windSpeed} m/s"
                )
            }
        }
    }
}