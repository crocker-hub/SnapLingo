package com.example.snaplingo.data.repository

import com.example.snaplingo.data.model.VocabularyWord
import com.google.firebase.firestore.FirebaseFirestore

class VocabularyRepository {

    private val firestore =
        FirebaseFirestore.getInstance()


    fun saveVocabularyWord(

        userId: String,

        word: String,

        translation: String,

        language: String,

        onSuccess: () -> Unit,

        onError: (Exception) -> Unit

    ) {

        // Napravimo jedinstveni ID
        // da se ista riječ ne može spremiti dvaput
        val wordId =
            "${language}_${word.lowercase()}"
                .replace(" ", "_")

        val wordReference = firestore
            .collection("users")
            .document(userId)
            .collection("vocabulary")
            .document(wordId)


        // Prvo provjeravamo postoji li već riječ
        wordReference
            .get()
            .addOnSuccessListener { document ->

                // Ako već postoji,
                // ne spremamo duplikat
                if (document.exists()) {

                    onSuccess()

                    return@addOnSuccessListener
                }


                val vocabularyWord =
                    VocabularyWord(

                        id = wordId,

                        word = word,

                        translation = translation,

                        language = language,

                        learnedAt =
                            System.currentTimeMillis()
                    )


                wordReference
                    .set(vocabularyWord)
                    .addOnSuccessListener {

                        onSuccess()
                    }
                    .addOnFailureListener { exception ->

                        onError(exception)
                    }
            }
            .addOnFailureListener { exception ->

                onError(exception)
            }
    }
    fun getVocabularyWords(
        userId: String,
        onSuccess: (List<VocabularyWord>) -> Unit,
        onError: (Exception) -> Unit
    ) {

        firestore
            .collection("users")
            .document(userId)
            .collection("vocabulary")
            .get()
            .addOnSuccessListener { result ->

                val words =
                    result.documents.mapNotNull { document ->

                        document.toObject(
                            VocabularyWord::class.java
                        )
                    }

                onSuccess(words)
            }
            .addOnFailureListener { exception ->

                onError(exception)
            }
    }
}