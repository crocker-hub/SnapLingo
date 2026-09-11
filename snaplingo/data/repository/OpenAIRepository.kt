package com.example.snaplingo.data.repository

import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class OpenAIRepository {

    private val client = OkHttpClient()

    // PRIVREMENO ZA TESTIRANJE
    private val apiKey = "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"

    fun generateChallenges(
        text: String,
        language: String,
        onSuccess: (JSONArray) -> Unit,
        onError: (Exception) -> Unit
    ) {

        val prompt = """
            You are generating vocabulary learning challenges.

            The user is learning: $language

            Analyze this text:

            "$text"

            Generate exactly 5 vocabulary challenges based on important words from the text.

            Return ONLY a JSON array.

            Format:

            [
              {
                "title": "Vocabulary Challenge",
                "question": "What does the word TEST mean?",
                "correctAnswer": "Test",
                "options": [
                  "Test",
                  "House",
                  "Water",
                  "School"
                ]
              }
            ]

            Rules:
            - Generate exactly 5 challenges.
            - Every challenge must have exactly 4 options.
            - correctAnswer must be one of the options.
            - Return valid JSON only.
            - Do not use markdown.
            - Do not write explanations outside JSON.
        """.trimIndent()

        val jsonBody = JSONObject().apply {

            put(
                "model",
                "gpt-5.6-luna"
            )

            put(
                "input",
                prompt
            )
        }

        val requestBody =
            jsonBody
                .toString()
                .toRequestBody(
                    "application/json"
                        .toMediaType()
                )

        val request = Request.Builder()
            .url(
                "https://api.openai.com/v1/responses"
            )
            .addHeader(
                "Authorization",
                "Bearer $apiKey"
            )
            .addHeader(
                "Content-Type",
                "application/json"
            )
            .post(requestBody)
            .build()

        client.newCall(request)
            .enqueue(object : Callback {

                override fun onFailure(
                    call: Call,
                    e: IOException
                ) {

                    onError(e)
                }

                override fun onResponse(
                    call: Call,
                    response: okhttp3.Response
                ) {

                    response.use {

                        if (!it.isSuccessful) {

                            val errorBody =
                                it.body?.string() ?: "No error body"

                            onError(
                                Exception(
                                    "OpenAI error ${it.code}: $errorBody"
                                )
                            )

                            return
                        }

                        val responseText =
                            it.body?.string()
                                ?: ""

                        try {

                            val jsonResponse =
                                JSONObject(responseText)

                            val output =
                                jsonResponse.getJSONArray(
                                    "output"
                                )

                            var generatedText = ""

                            for (
                            i in 0 until output.length()
                            ) {

                                val outputItem =
                                    output.getJSONObject(i)

                                if (
                                    outputItem.optString("type") ==
                                    "message"
                                ) {

                                    val content =
                                        outputItem.getJSONArray(
                                            "content"
                                        )

                                    for (
                                    j in 0 until content.length()
                                    ) {

                                        val contentItem =
                                            content.getJSONObject(j)

                                        if (
                                            contentItem.optString("type") ==
                                            "output_text"
                                        ) {

                                            generatedText =
                                                contentItem.getString(
                                                    "text"
                                                )

                                            break
                                        }
                                    }
                                }
                            }

                            val challengesJson =
                                JSONArray(generatedText)

                            onSuccess(challengesJson)

                        } catch (e: Exception) {

                            onError(e)
                        }
                    }
                }
            })
    }
}