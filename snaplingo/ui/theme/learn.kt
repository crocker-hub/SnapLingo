package com.example.snaplingo.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.snaplingo.data.model.LearningMaterial
import com.example.snaplingo.navigation.Screen
import com.example.snaplingo.ui.theme.viewmodel.UserViewModel
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

@Composable
fun LearnScreen(
    paddingValues: PaddingValues,
    navController: NavController,
    userViewModel: UserViewModel
) {

    val learningMaterials by userViewModel.learningMaterials
    val challenges by userViewModel.challenges
    val isLoading by userViewModel.isLearningMaterialsLoading

    LaunchedEffect(Unit) {
        userViewModel.loadLearningMaterials()
        userViewModel.loadChallenges()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFCF8))
            .padding(paddingValues)
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {

            Text(
                text = "Learn",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = "Choose a text and start learning."
            )

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            if (isLoading) {

                Text(
                    text = "Loading..."
                )

            } else if (learningMaterials.isEmpty()) {

                Text(
                    text = "You don't have any learning materials yet."
                )

            } else {

                learningMaterials.forEach { material ->

                    val materialChallenges = challenges.filter {
                        it.materialId == material.id
                    }

                    val isCompleted =
                        materialChallenges.isNotEmpty() &&
                                materialChallenges.all {
                                    it.completed
                                }

                    LearningMaterialCard(
                        material = material,
                        isCompleted = isCompleted,
                        onClick = {

                            if (!isCompleted) {

                                navController.navigate(
                                    Screen.LearningSession.createRoute(
                                        material.id
                                    )
                                )
                            }
                        },
                        onDelete = {

                            userViewModel.deleteLearningSession(
                                materialId = material.id
                            )
                        }
                    )

                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )
                }
            }
        }
    }
}


@Composable
fun LearningMaterialCard(
    material: LearningMaterial,
    isCompleted: Boolean,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isCompleted) {
                Color(0xFFE8F5E9)
            } else {
                Color.White
            }
        ),
        onClick = onClick
    ) {

        Column(
            modifier = Modifier.padding(18.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    text = material.language,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                if (isCompleted) {

                    Text(
                        text = "✕",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable {
                            onDelete()
                        }
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = material.text,
                maxLines = 3
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            if (isCompleted) {

                Text(
                    text = "✓ Completed",
                    color = Color(0xFF2E7D32),
                    fontWeight = FontWeight.Bold
                )

            } else {

                Text(
                    text = "Start Learning",
                    color = Color(0xFF0F5E4D),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
