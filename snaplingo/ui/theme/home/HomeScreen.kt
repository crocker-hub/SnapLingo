package com.example.snaplingo.ui.theme.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.snaplingo.navigation.Screen
import com.example.snaplingo.ui.theme.components.CameraCard
import com.example.snaplingo.ui.theme.components.ChallengeCard
import com.example.snaplingo.ui.theme.viewmodel.UserViewModel
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

@Composable
fun HomeScreen(
    paddingValues: PaddingValues,
    navController: NavController,
    userViewModel: UserViewModel
) {

    val userProfile by userViewModel.userProfile
    val challenges by userViewModel.challenges

    LaunchedEffect(Unit) {
        userViewModel.loadUserProfile()
        userViewModel.loadChallenges()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(paddingValues)
            .padding(20.dp)
    ) {

        Text(
            text = "Welcome, ${userProfile?.username ?: "Learner"}!",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text = "Level ${userProfile?.level ?: 1} • ${userProfile?.xp ?: 0} XP"
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )


        CameraCard(
            onClick = {
                navController.navigate(Screen.Camera.route)
            }
        )

        Spacer(
            modifier = Modifier.height(28.dp)
        )


        Text(
            text = "Today's Challenges",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(
            modifier = Modifier.height(14.dp)
        )

        challenges.forEach { challenge ->

            ChallengeCard(
                challenge = challenge,
                onClick = {
                    navController.navigate(
                        Screen.Challenge.createRoute(challenge.id)
                    )
                },
                onDelete = {
                    userViewModel.deleteChallenge(
                        challengeId = challenge.id
                    )
                }
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )
        }
    }
}