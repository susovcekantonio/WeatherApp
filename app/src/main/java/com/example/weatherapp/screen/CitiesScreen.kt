package com.example.weatherapp.screen

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.weatherapp.model.CityWeather
import com.example.weatherapp.viewmodel.UiState
import com.example.weatherapp.viewmodel.WeatherViewModel

@Composable
fun CitiesScreen(navController: NavController) {
    val viewModel: WeatherViewModel = hiltViewModel()
    val state by viewModel.citiesState.collectAsStateWithLifecycle()
    var searchText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Search Cities",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = searchText,
            onValueChange = {
                searchText = it
                if (it.length >= 3) {
                    viewModel.searchCities(it)
                }
            },
            placeholder = { Text("Enter city name") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = {
                viewModel.searchCities(searchText)
            })
        )

        Spacer(modifier = Modifier.height(16.dp))

        when (state) {
            is UiState.Loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            is UiState.Error -> Text(
                text = "Error: ${(state as UiState.Error).message}",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            is UiState.Success -> {
                CityList(cities = (state as UiState.Success<List<CityWeather>>).data, navController = navController)
            }
        }
    }
}


@Composable
private fun CityList(cities: List<CityWeather>, navController: NavController) {
    LazyColumn {
        items(cities) { city ->
            CityItem(city = city, navController = navController)
            Divider(
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f),
                thickness = 1.dp
            )
        }
    }
}

@Composable
private fun CityItem(city: CityWeather, navController: NavController) {
    val weather = city.weather.firstOrNull()
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
        modifier = Modifier
            .fillMaxWidth()
            .clickable { navController.navigate("details/${city.id}") }
            .padding(vertical = 4.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = city.name,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.End
            )

            Text(
                text = "Click for more details",
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(modifier = Modifier.height(8.dp))

            weather?.let {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.scale(pulseScale)) {
                        Image(
                            painter = rememberAsyncImagePainter(
                                model = "https://openweathermap.org/img/wn/${it.icon}.png"
                            ),
                            contentDescription = it.description,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.size(40.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = it.description.replaceFirstChar { char -> char.uppercase() },
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                WeatherInfoItem(
                    label = "Temp",
                    value = "${city.main.temp}°C"
                )
                WeatherInfoItem(
                    label = "Humidity",
                    value = "${city.main.humidity}%"
                )
                WeatherInfoItem(
                    label = "Wind",
                    value = "${city.wind.speed} m/s"
                )
            }
        }
    }
}


@Composable
fun WeatherInfoItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}