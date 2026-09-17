package com.example.snaplingo.ui.theme


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import com.example.snaplingo.ui.theme.viewmodel.UserViewModel
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import com.example.snaplingo.data.model.LevelSystem

@Composable
fun StatsScreen(
    paddingValues: PaddingValues,
    userViewModel: UserViewModel
) {

    val userProfile by userViewModel.userProfile
    val challenges by userViewModel.challenges

    LaunchedEffect(Unit) {
        userViewModel.loadUserProfile()
        userViewModel.loadChallenges()
    }

    val xp = userProfile?.xp ?: 0
    val level = userProfile?.level ?: 1

    val currentLevelXp = LevelSystem.getXpForCurrentLevel(xp)
    val nextLevelXp = LevelSystem.getXpForNextLevel(xp)

    val xpIntoLevel = xp - currentLevelXp
    val xpNeededForLevel = nextLevelXp - currentLevelXp

    val progress =
        if (xpNeededForLevel > 0) {
            xpIntoLevel.toFloat() / xpNeededForLevel.toFloat()
        } else {
            1f
        }

    val completedChallenges =
        userProfile?.completedChallenges ?: 0

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFCF8))
            .padding(paddingValues)
            .padding(20.dp)
    ) {

        Text(
            text = "Statistics",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        // LEVEL CARD

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF0F5E4D)
            )
        ) {

            Column(
                modifier = Modifier.padding(20.dp)
            ) {

                Text(
                    text = "Level $level",
                    color = Color.White,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "$xp / $nextLevelXp XP",
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(12.dp))

                LinearProgressIndicator(
                    progress = { progress.coerceIn(0f, 1f) },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                if (level < 6) {

                    val remainingXp =
                        nextLevelXp - xp

                    Text(
                        text = "$remainingXp XP until Level ${level + 1}",
                        color = Color.White
                    )

                } else {

                    Text(
                        text = "Maximum level reached",
                        color = Color.White
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // GENERAL STATS

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            StatCard(
                modifier = Modifier.weight(1f),
                icon = {
                    Icon(
                        imageVector = Icons.Default.LocalFireDepartment,
                        contentDescription = null,
                        tint = Color(0xFFFF9800)
                    )
                },
                value = "${userProfile?.streak ?: 0}",
                label = "Streak"
            )

            StatCard(
                modifier = Modifier.weight(1f),
                icon = {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = Color(0xFFFFC107)
                    )
                },
                value = "$xp",
                label = "XP"
            )

            StatCard(
                modifier = Modifier.weight(1f),
                icon = {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = Color(0xFF4CAF50)
                    )
                },
                value = "$completedChallenges",
                label = "Challenges"
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        Text(
            text = "Completed Challenges",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        challenges
            .filter { it.completed }
            .forEach { challenge ->

                ChallengeStat(
                    title = challenge.title,
                    xp = "+${challenge.xpReward} XP"
                )

                Spacer(
                    modifier = Modifier.height(10.dp)
                )
            }

        if (completedChallenges == 0) {

            Text(
                text = "No challenges completed yet.",
                color = Color.Gray
            )
        }
    }
}
@Composable
fun StatCard(
    modifier: Modifier = Modifier,
    icon: @Composable () -> Unit,
    value: String,
    label: String
) {

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            icon()

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = value,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge
            )

            Text(
                text = label
            )
        }
    }
}
@Composable
fun ChallengeStat(
    title: String,
    xp: String
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = Color(0xFF4CAF50)
                )

                Spacer(
                    modifier = Modifier.width(6.dp)
                )

                Text(
                    text = title,
                    fontWeight = FontWeight.Medium
                )
            }

            Text(
                text = xp,
                color = Color(0xFF4CAF50),
                fontWeight = FontWeight.Bold
            )
        }
    }
}