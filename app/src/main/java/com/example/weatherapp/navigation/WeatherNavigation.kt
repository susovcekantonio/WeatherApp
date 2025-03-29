package com.example.weatherapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.weatherapp.screen.CitiesScreen
import com.example.weatherapp.screen.CityDetailsScreen

@Composable
fun WeatherNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "cities") {
        composable("cities") {
            CitiesScreen(navController)
        }
        composable("details/{cityId}") { backStackEntry ->
            CityDetailsScreen(cityId = backStackEntry.arguments?.getString("cityId") ?: "")
        }
    }
}