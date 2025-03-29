package com.example.weatherapp.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.example.weatherapp.model.CityWeatherDetails
import com.example.weatherapp.viewmodel.UiState
import com.example.weatherapp.viewmodel.WeatherViewModel

@Composable
fun CityDetailsScreen(cityId: String) {
    val viewModel: WeatherViewModel = hiltViewModel()
    val state by viewModel.cityDetailsState.collectAsStateWithLifecycle()

    LaunchedEffect(cityId) {
        viewModel.loadCityDetails(cityId)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
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
                CityDetails(city = (state as UiState.Success<CityWeatherDetails>).data)
            }
        }
    }
}

@Composable
private fun CityDetails(city: CityWeatherDetails) {
    val weather = city.weather.firstOrNull()

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = city.name,
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        weather?.let {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = rememberAsyncImagePainter(
                        model = "https://openweathermap.org/img/wn/${it.icon}@2x.png"
                    ),
                    contentDescription = it.description,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(80.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = it.description.replaceFirstChar { char -> char.uppercase() },
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "${city.main.temp}°C",
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                WeatherDetailRow(
                    label = "Humidity",
                    value = "${city.main.humidity}%"
                )
                WeatherDetailRow(
                    label = "Wind Speed",
                    value = "${city.wind.speed} m/s"
                )
            }
        }
    }
}

@Composable
private fun WeatherDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}