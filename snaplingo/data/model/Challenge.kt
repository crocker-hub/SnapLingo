package com.example.snaplingo.data.model

data class Challenge(
    val id: String = "",
    val materialId: String = "",
    val title: String = "",
    val description: String = "",
    val question: String = "",
    val correctAnswer: String = "",
    val options: List<String> = emptyList(),
    val target: Int = 0,
    val progress: Int = 0,
    val xpReward: Int = 0,
    val completed: Boolean = false
)