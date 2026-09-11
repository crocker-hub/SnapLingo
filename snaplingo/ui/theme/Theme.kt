package com.example.snaplingo.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Color


@Composable
fun SnapLingoTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        content = content
    )
}

// Primary colors
val Primary = Color(0xFF0F5E4D)
val PrimaryLight = Color(0xFF18A999)

// Background
val Background = Color(0xFFFFFCF8)
val Surface = Color.White

// Text
val TextPrimary = Color(0xFF202124)
val TextSecondary = Color(0xFF6E6E6E)

// Accent
val Orange = Color(0xFFFF9800)
val Green = Color(0xFF4CAF50)
val Red = Color(0xFFE53935)

// Cards
val CardColor = Color(0xFFFFFFFF)

// Border
val Border = Color(0xFFE8E8E8)