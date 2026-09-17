package com.example.snaplingo.data.model

data class UserProfile(
    val username: String = "",
    val email: String = "",
    val xp: Int = 0,
    val level: Int = 1,
    val streak: Int = 0,
    val completedChallenges: Int = 0
)