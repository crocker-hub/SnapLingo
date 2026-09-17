package com.example.snaplingo.data.repository

import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions

class TranslationRepository {

    fun translateWord(
        word: String,
        sourceLanguage: String,
        targetLanguage: String,
        onSuccess: (String) -> Unit,
        onError: (Exception) -> Unit
    ) {

        val sourceLanguageCode =
            TranslateLanguage.fromLanguageTag(sourceLanguage)

        val targetLanguageCode =
            TranslateLanguage.fromLanguageTag(targetLanguage)

        if (
            sourceLanguageCode == null ||
            targetLanguageCode == null
        ) {

            onError(
                Exception("Unsupported language")
            )

            return
        }

        val options =
            TranslatorOptions.Builder()
                .setSourceLanguage(sourceLanguageCode)
                .setTargetLanguage(targetLanguageCode)
                .build()

        val translator: Translator =
            com.google.mlkit.nl.translate.Translation
                .getClient(options)

        val conditions =
            DownloadConditions.Builder()
                .build()

        translator
            .downloadModelIfNeeded(conditions)
            .addOnSuccessListener {

                translator
                    .translate(word)
                    .addOnSuccessListener { translatedWord ->

                        onSuccess(translatedWord)

                        translator.close()
                    }
                    .addOnFailureListener { exception ->

                        onError(exception)

                        translator.close()
                    }
            }
            .addOnFailureListener { exception ->

                onError(exception)

                translator.close()
            }
    }
}