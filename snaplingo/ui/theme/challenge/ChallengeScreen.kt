package com.example.snaplingo.ui.theme.challenge

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
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.snaplingo.data.model.Challenge
import com.example.snaplingo.ui.theme.viewmodel.UserViewModel
import androidx.compose.runtime.getValue

@Composable
fun ChallengeScreen(
    paddingValues: PaddingValues,
    navController: NavController,
    userViewModel: UserViewModel,
    challengeId: String
) {
    val challenges by userViewModel.challenges
    val challenge =
        challenges.find {
            it.id == challengeId
        }

    if (challenge == null) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(20.dp)
        ) {

            Text(
                text = "Challenge not found",
                style = MaterialTheme.typography.headlineSmall
            )
        }

        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(20.dp),
        verticalArrangement = Arrangement.Top
    ) {

        Text(
            text = challenge.title,
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text = challenge.description
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        Text(
            text = "Progress: ${challenge.progress} / ${challenge.target}"
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {

            Column(
                modifier = Modifier.padding(20.dp)
            ) {

                Text(
                    text = "Test question",
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                Text(
                    text = "What is the correct translation?"
                )

                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                Button(
                    onClick = {

                        userViewModel.updateChallengeProgress(
                            challenge = challenge
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Text("Correct answer")
                }
            }
        }
    }
}