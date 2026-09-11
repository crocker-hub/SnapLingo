package com.example.snaplingo.ui.theme.learning

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.snaplingo.ui.theme.viewmodel.UserViewModel

@Composable
fun LearningSessionScreen(
    paddingValues: PaddingValues,
    navController: NavController,
    userViewModel: UserViewModel,
    materialId: String
) {

    val materials by userViewModel.learningMaterials
    val challenges by userViewModel.challenges
    val errorMessage by userViewModel.errorMessage

    val materialChallenges = challenges.filter {
        it.materialId == materialId
    }

    var sessionStarted by remember {
        mutableStateOf(false)
    }

    var currentChallengeIndex by remember {
        mutableIntStateOf(0)
    }

    var selectedAnswer by remember {
        mutableStateOf<String?>(null)
    }

    var answerChecked by remember {
        mutableStateOf(false)
    }
    var sessionCompletionRecorded by remember {
        mutableStateOf(false)
    }

    val material = materials.find {
        it.id == materialId
    }

    // Provjeri postoje li već challengeovi za ovaj materijal
    LaunchedEffect(materialId) {

        userViewModel.startLearningSession(
            materialId = materialId
        ) { challengeList ->

            if (challengeList.isNotEmpty()) {

                sessionStarted = true

                // Pronađi prvi nezavršeni challenge
                val firstIncompleteIndex =
                    challengeList.indexOfFirst {
                        !it.completed
                    }

                if (firstIncompleteIndex >= 0) {
                    currentChallengeIndex = firstIncompleteIndex
                }
            }
        }
    }

    if (material == null) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(20.dp)
        ) {

            Text(
                text = "Learning material not found",
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
            text = "Learning Session",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        Text(
            text = material.language,
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Text(
            text = material.text,
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(
            modifier = Modifier.height(32.dp)
        )
        if (userViewModel.errorMessage.value.isNotEmpty()) {

            Text(
                text = userViewModel.errorMessage.value,
                color = Color.Red
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )
        }
        if (!sessionStarted) {

            Button(
                onClick = {

                    userViewModel.generateChallengesForMaterial(
                        material = material
                    ) {

                        sessionStarted = true
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {

                Text("Start Learning")
            }

        } else {

            // NEMA CHALLENGEOVA
            if (materialChallenges.isEmpty()) {

                Text(
                    text = "Loading challenges..."
                )

            } else {

                // Ako su svi challengeovi završeni
                if (currentChallengeIndex >= materialChallenges.size) {

                    Text(
                        text = "All challenges completed!",
                        style = MaterialTheme.typography.headlineSmall
                    )

                    Spacer(
                        modifier = Modifier.height(16.dp)
                    )

                    Text(
                        text = "Great job! You completed this learning session."
                    )

                } else {

                    val challenge =
                        materialChallenges[currentChallengeIndex]

                    Text(
                        text = "Challenge ${currentChallengeIndex + 1} / ${materialChallenges.size}",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(
                        modifier = Modifier.height(16.dp)
                    )

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    ) {

                        Column(
                            modifier = Modifier.padding(20.dp)
                        ) {

                            Text(
                                text = challenge.title,
                                style = MaterialTheme.typography.titleLarge
                            )

                            Spacer(
                                modifier = Modifier.height(16.dp)
                            )

                            Text(
                                text = challenge.question.ifEmpty {
                                    challenge.description
                                },
                                style = MaterialTheme.typography.bodyLarge
                            )

                            Spacer(
                                modifier = Modifier.height(20.dp)
                            )

                            challenge.options.forEach { option ->

                                Button(
                                    onClick = {

                                        if (!answerChecked) {
                                            selectedAnswer = option
                                        }
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                ) {

                                    Text(
                                        text = option
                                    )
                                }
                            }

                            Spacer(
                                modifier = Modifier.height(20.dp)
                            )

                            // CHECK ANSWER
                            if (!answerChecked) {

                                Button(
                                    onClick = {

                                        if (selectedAnswer != null) {
                                            answerChecked = true
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    enabled = selectedAnswer != null
                                ) {

                                    Text("Check Answer")
                                }

                            } else {

                                val isCorrect =
                                    selectedAnswer == challenge.correctAnswer

                                if (isCorrect) {

                                    Text(
                                        text = "✓ Correct!",
                                        style = MaterialTheme.typography.titleLarge
                                    )

                                    Spacer(
                                        modifier = Modifier.height(8.dp)
                                    )

                                    Text(
                                        text = "+${challenge.xpReward} XP"
                                    )

                                    Spacer(
                                        modifier = Modifier.height(16.dp)
                                    )

                                    Button(
                                        onClick = {

                                            userViewModel.updateChallengeProgress(
                                                challenge = challenge
                                            )

                                            userViewModel.updateDefaultChallenge(
                                                challengeId = "learn_10_words"
                                            )

                                            // Izvuci riječ iz pitanja
                                            val wordRegex =
                                                Regex("\"([^\"]+)\"")

                                            val word =
                                                wordRegex.find(challenge.question)
                                                    ?.groupValues
                                                    ?.getOrNull(1)
                                                    ?: ""

                                            // Spremi riječ u Vocabulary
                                            if (word.isNotEmpty()) {

                                                userViewModel.saveVocabularyWord(
                                                    word = word,
                                                    translation = challenge.correctAnswer,
                                                    language = material.language
                                                )
                                            }

                                            selectedAnswer = null
                                            answerChecked = false

                                            currentChallengeIndex++
                                        },
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text("Next")
                                    }

                                } else {

                                    Text(
                                        text = "✗ Wrong answer",
                                        style = MaterialTheme.typography.titleLarge
                                    )

                                    Spacer(
                                        modifier = Modifier.height(8.dp)
                                    )

                                    Text(
                                        text = "Correct answer: ${challenge.correctAnswer}"
                                    )

                                    Spacer(
                                        modifier = Modifier.height(16.dp)
                                    )

                                    Button(
                                        onClick = {

                                            selectedAnswer = null
                                            answerChecked = false
                                        },
                                        modifier = Modifier.fillMaxWidth()
                                    ) {

                                        Text("Try Again")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}