package com.example.snaplingo.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.snaplingo.ui.theme.components.BottomNavigationBar
import com.example.snaplingo.ui.theme.components.CameraCard

@Composable
fun HomeScreen(
    navController: NavController,
    paddingValues: PaddingValues
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFCF8))
            .padding(paddingValues)
            .padding(20.dp)
    ) {

        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null
                )

                Spacer(modifier = Modifier.width(8.dp))

                Column {

                    Text(
                        text = "Level 1",
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    LinearProgressIndicator(
                        progress = { 0.25f },
                        modifier = Modifier.width(120.dp)
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    imageVector = Icons.Default.LocalFireDepartment,
                    contentDescription = null,
                    tint = Color(0xFFFF9800)
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text("0")
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        Text(
            text = "Hello! 👋",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Ready to learn from the world around you?"
        )

        Spacer(modifier = Modifier.height(24.dp))

        CameraCard(
            onClick = {
                // TODO: Open Camera
            }
        )

        Spacer(modifier = Modifier.height(28.dp))

        HorizontalDivider()

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Daily Challenges",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        Challenge(
            title = "Learn 10 new words",
            progress = 0.3f,
            text = "3 / 10",
            xp = "+15 XP"
        )

        Spacer(modifier = Modifier.height(12.dp))

        Challenge(
            title = "Solve 5 exercises",
            progress = 0.6f,
            text = "3 / 5",
            xp = "+25 XP"
        )

        Spacer(modifier = Modifier.height(12.dp))

        Challenge(
            title = "Complete 3 lessons",
            progress = 0.33f,
            text = "1 / 3",
            xp = "+20 XP"
        )
    }
}

@Composable
fun Challenge(
    title: String,
    progress: Float,
    text: String,
    xp: String
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    text = title,
                    fontWeight = FontWeight.Medium
                )

                Text(
                    text = xp,
                    color = Color(0xFF4CAF50),
                    fontWeight = FontWeight.Bold
                )

            }

            Spacer(modifier = Modifier.height(12.dp))

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = text
            )
        }
    }
}