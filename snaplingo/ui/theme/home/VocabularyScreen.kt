package com.example.snaplingo.ui.theme.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import com.example.snaplingo.ui.theme.viewmodel.UserViewModel
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

@Composable
fun VocabularyScreen(
    paddingValues: PaddingValues,
    userViewModel: UserViewModel
) {

    val vocabularyWords by userViewModel.vocabularyWords
    var selectedLanguage by remember {
        mutableStateOf("English")
    }

    val languages = listOf(
        "English",
        "Spanish",
        "German",
        "French",
        "Italian"
    )

    val filteredWords =
        vocabularyWords
            .filter {
                it.language == selectedLanguage
            }
            .sortedBy {
                it.word.lowercase()
            }

    LaunchedEffect(Unit) {

        userViewModel.loadVocabularyWords()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFCF8))
            .padding(paddingValues)
            .padding(20.dp)
    ) {

        Text(
            text = "Vocabulary",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text = "Words you learned from your texts."
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(
                    rememberScrollState()
                ),
            horizontalArrangement =
                Arrangement.spacedBy(8.dp)
        ) {

            languages.forEach { language ->

                Button(
                    onClick = {
                        selectedLanguage = language
                    }
                ) {

                    Text(
                        text = language
                    )
                }
            }
        }

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        if (filteredWords.isEmpty()) {

            Text(
                text = "No words learned in $selectedLanguage yet."
            )

        } else {

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                items(filteredWords) { vocabularyWord ->

                    VocabularyWordCard(
                        word = vocabularyWord.word,
                        translation = vocabularyWord.translation,
                        language = vocabularyWord.language
                    )
                }
            }
        }
    }
}


@Composable
fun VocabularyWordCard(

    word: String,

    translation: String,

    language: String

) {

    Card(
        modifier = Modifier.fillMaxWidth(),

        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = word,
                style =
                    MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = translation,
                style =
                    MaterialTheme.typography.bodyLarge,
                color = Color.Gray
            )

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            Text(
                text = language,
                style =
                    MaterialTheme.typography.bodySmall,
                color = Color(0xFF0F5E4D)
            )
        }
    }
}