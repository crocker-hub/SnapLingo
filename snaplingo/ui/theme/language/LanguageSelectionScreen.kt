package com.example.snaplingo.ui.theme.language

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.snaplingo.navigation.Screen
import com.example.snaplingo.ui.theme.viewmodel.UserViewModel

@Composable
fun LanguageSelectionScreen(
    paddingValues: PaddingValues,
    navController: NavController,
    userViewModel: UserViewModel
) {

    val recognizedText =
        navController
            .previousBackStackEntry
            ?.savedStateHandle
            ?.get<String>("recognizedText")
            ?: ""

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(20.dp),
        verticalArrangement = Arrangement.Top
    ) {

        Text(
            text = "Choose a language",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text = "Choose the language you want to learn from your text."
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        LanguageButton(
            language = "English",
            onClick = {

                userViewModel.saveLearningMaterial(
                    text = recognizedText,
                    language = "English"
                ) {

                    navController.navigate(
                        Screen.Home.route
                    ) {
                        popUpTo(
                            Screen.LanguageSelection.route
                        ) {
                            inclusive = true
                        }
                    }
                }
            }
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        LanguageButton(
            language = "Spanish",
            onClick = {

                userViewModel.saveLearningMaterial(
                    text = recognizedText,
                    language = "Spanish"
                ) {

                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.LanguageSelection.route) {
                            inclusive = true
                        }
                    }
                }
            }
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        LanguageButton(
            language = "German",
            onClick = {

                userViewModel.saveLearningMaterial(
                    text = recognizedText,
                    language = "German"
                ) {

                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.LanguageSelection.route) {
                            inclusive = true
                        }
                    }
                }
            }
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        LanguageButton(
            language = "French",
            onClick = {

                userViewModel.saveLearningMaterial(
                    text = recognizedText,
                    language = "French"
                ) {

                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.LanguageSelection.route) {
                            inclusive = true
                        }
                    }
                }
            }
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        LanguageButton(
            language = "Italian",
            onClick = {

                userViewModel.saveLearningMaterial(
                    text = recognizedText,
                    language = "Italian"
                ) {

                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.LanguageSelection.route) {
                            inclusive = true
                        }
                    }
                }
            }
        )
    }
}

@Composable
fun LanguageButton(
    language: String,
    onClick: () -> Unit
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors()
    ) {

        Button(
            onClick = onClick,
            modifier = Modifier.fillMaxWidth()
        ) {

            Text(
                text = language
            )
        }
    }
}