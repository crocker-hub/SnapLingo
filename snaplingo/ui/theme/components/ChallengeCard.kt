package com.example.snaplingo.ui.theme.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.snaplingo.data.model.Challenge


@Composable
fun ChallengeCard(
    challenge: Challenge,
    onClick: () -> Unit,
    onDelete: () -> Unit = {}
) {

    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {

        Column(
            modifier = Modifier.padding(18.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {

                Text(
                    text = challenge.title,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )

                // X se prikazuje samo kada je challenge završen
                if (challenge.completed) {

                    Text(
                        text = "✕",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(start = 12.dp)
                            .clickable {
                                onDelete()
                            }
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            Text(
                text = challenge.description,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )

            Spacer(
                modifier = Modifier.height(14.dp)
            )

            LinearProgressIndicator(
                progress = {
                    if (challenge.target > 0) {
                        challenge.progress.toFloat() /
                                challenge.target.toFloat()
                    } else {
                        0f
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    text = "${challenge.progress} / ${challenge.target}"
                )

                Text(
                    text = "+${challenge.xpReward} XP",
                    color = Color(0xFF0F5E4D)
                )
            }
        }
    }
}