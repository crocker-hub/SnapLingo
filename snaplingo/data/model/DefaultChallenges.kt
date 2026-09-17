package com.example.snaplingo.data.model

object DefaultChallenges {

    fun getChallenges(): List<Challenge> {

        return listOf(

            Challenge(
                id = "learn_10_words",
                title = "Learn 10 new words",
                description = "Learn 10 words from your captured text",
                target = 10,
                progress = 0,
                xpReward = 15
            ),

            Challenge(
                id = "complete_5_exercises",
                title = "Complete 5 exercises",
                description = "Complete 5 language exercises",
                target = 5,
                progress = 0,
                xpReward = 25
            ),

            Challenge(
                id = "finish_3_lessons",
                title = "Finish 3 lessons",
                description = "Complete 3 learning sessions",
                target = 3,
                progress = 0,
                xpReward = 20
            )
        )
    }
}