package com.example.weatherapp.ui.theme

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ThemeSwitch(
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit
) {
    Switch(
        checked = isDarkTheme,
        onCheckedChange = onThemeChange,
        modifier = Modifier.padding(8.dp)
    )
}