package com.example.snaplingo.data.repository


import com.example.snaplingo.data.model.LevelSystem
import com.example.snaplingo.data.model.UserProfile
import com.google.firebase.firestore.FirebaseFirestore
import com.example.snaplingo.data.model.Challenge
import com.example.snaplingo.data.model.DefaultChallenges
import com.google.firebase.firestore.FieldValue

class UserRepository {

    private val firestore = FirebaseFirestore.getInstance()

    fun createUserProfile(
        userId: String,
        userProfile: UserProfile,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {

        firestore
            .collection("users")
            .document(userId)
            .set(userProfile)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onError(exception)
            }
    }

    fun getUserProfile(
        userId: String,
        onSuccess: (UserProfile) -> Unit,
        onError: (Exception) -> Unit
    ) {

        firestore
            .collection("users")
            .document(userId)
            .get()
            .addOnSuccessListener { document ->

                if (document.exists()) {

                    val userProfile =
                        document.toObject(UserProfile::class.java)

                    if (userProfile != null) {
                        onSuccess(userProfile)
                    } else {
                        onError(
                            Exception("User profile could not be loaded")
                        )
                    }

                } else {

                    onError(
                        Exception("User profile does not exist")
                    )
                }
            }
            .addOnFailureListener { exception ->

                onError(exception)
            }
    }
    fun addXp(
        userId: String,
        xpAmount: Int,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {

        firestore
            .collection("users")
            .document(userId)
            .get()
            .addOnSuccessListener { document ->

                val currentXp =
                    document.getLong("xp")?.toInt() ?: 0

                val newXp = currentXp + xpAmount

                firestore
                    .collection("users")
                    .document(userId)
                    .update(
                        "xp", newXp,
                        "level", LevelSystem.getLevelFromXp(newXp)
                    )
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
    fun createDefaultChallenges(
        userId: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {

        val challenges = DefaultChallenges.getChallenges()

        val batch = firestore.batch()

        challenges.forEach { challenge ->

            val challengeReference = firestore
                .collection("users")
                .document(userId)
                .collection("challenges")
                .document(challenge.id)

            batch.set(
                challengeReference,
                challenge
            )
        }

        batch.commit()
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onError(exception)
            }
    }
    fun incrementCompletedChallenges(
        userId: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {

        firestore
            .collection("users")
            .document(userId)
            .update(
                "completedChallenges",
                FieldValue.increment(1)
            )
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onError(exception)
            }
    }
}