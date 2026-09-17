package com.example.snaplingo.ui.theme.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.snaplingo.data.model.UserProfile
import com.example.snaplingo.data.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.example.snaplingo.data.model.Challenge
import com.example.snaplingo.data.model.LearningMaterial
import com.example.snaplingo.data.model.VocabularyWord
import com.example.snaplingo.data.repository.ChallengeRepository
import com.example.snaplingo.data.repository.LearningMaterialRepository
import com.example.snaplingo.data.repository.VocabularyRepository

class UserViewModel : ViewModel() {

    private val challengeRepository = ChallengeRepository()
    var challenges = mutableStateOf<List<Challenge>>(emptyList())
        private set

    var isChallengesLoading = mutableStateOf(false)
        private set
    private val auth = FirebaseAuth.getInstance()
    private val userRepository = UserRepository()
    private val learningMaterialRepository =
        LearningMaterialRepository()

    var userProfile = mutableStateOf<UserProfile?>(null)
        private set

    var isLoading = mutableStateOf(false)
        private set

    var errorMessage = mutableStateOf("")
        private set

    var learningMaterials =
        mutableStateOf<List<LearningMaterial>>(emptyList())
        private set

    var vocabularyWords =
        mutableStateOf<List<VocabularyWord>>(emptyList())
        private set

    var isLearningMaterialsLoading =
        mutableStateOf(false)
        private set

    private val vocabularyRepository =
        VocabularyRepository()

    fun loadUserProfile() {

        val user = auth.currentUser

        if (user == null) {

            errorMessage.value = "User is not logged in"
            return
        }

        isLoading.value = true
        errorMessage.value = ""

        userRepository.getUserProfile(
            userId = user.uid,

            onSuccess = { profile ->

                userProfile.value = profile
                isLoading.value = false
            },

            onError = { exception ->

                errorMessage.value =
                    exception.localizedMessage
                        ?: "Failed to load profile"

                isLoading.value = false
            }
        )
    }
    fun addXp(amount: Int) {

        val user = auth.currentUser

        if (user == null) {
            errorMessage.value = "User is not logged in"
            return
        }

        userRepository.addXp(
            userId = user.uid,
            xpAmount = amount,

            onSuccess = {
                loadUserProfile()
            },

            onError = { exception ->

                errorMessage.value =
                    exception.localizedMessage
                        ?: "Failed to add XP"
            }
        )
    }
    fun createDefaultChallenges() {

        val user = auth.currentUser

        if (user == null) {
            errorMessage.value = "User is not logged in"
            return
        }

        userRepository.createDefaultChallenges(
            userId = user.uid,

            onSuccess = {
                // Challenges successfully created
            },

            onError = { exception ->

                errorMessage.value =
                    exception.localizedMessage
                        ?: "Failed to create challenges"
            }
        )
    }
    fun loadChallenges() {

        val user = auth.currentUser

        if (user == null) {
            errorMessage.value = "User is not logged in"
            return
        }

        isChallengesLoading.value = true

        challengeRepository.getChallenges(
            userId = user.uid,

            onSuccess = { challengeList ->

                challenges.value = challengeList
                isChallengesLoading.value = false
            },

            onError = { exception ->

                errorMessage.value =
                    exception.localizedMessage
                        ?: "Failed to load challenges"

                isChallengesLoading.value = false
            }
        )
    }
    fun updateChallengeProgress(
        challenge: Challenge,
        onCompleted: () -> Unit = {}
    ) {

        val user = auth.currentUser

        if (user == null) {
            errorMessage.value = "User is not logged in"
            return
        }

        challengeRepository.updateChallengeProgress(
            userId = user.uid,
            challengeId = challenge.id,

            onSuccess = { updatedChallenge ->

                startLearningSession(
                    materialId = challenge.materialId
                )

                if (updatedChallenge.completed) {

                    addXp(updatedChallenge.xpReward)

                    onCompleted()
                }
            },

            onError = { exception ->

                errorMessage.value =
                    exception.localizedMessage
                        ?: "Failed to update challenge"
            }
        )
    }
    fun saveLearningMaterial(
        text: String,
        language: String,
        onSuccess: () -> Unit = {}
    ) {

        val user = auth.currentUser

        if (user == null) {

            errorMessage.value = "User is not logged in"
            return
        }

        learningMaterialRepository.saveLearningMaterial(
            userId = user.uid,
            text = text,
            language = language,

            onSuccess = {
                onSuccess()
            },

            onError = { exception ->

                errorMessage.value =
                    exception.localizedMessage
                        ?: "Failed to save learning material"
            }
        )
    }
    fun loadLearningMaterials() {

        val user = auth.currentUser

        if (user == null) {

            errorMessage.value = "User is not logged in"
            return
        }

        isLearningMaterialsLoading.value = true

        learningMaterialRepository.getLearningMaterials(
            userId = user.uid,

            onSuccess = { materials ->

                learningMaterials.value = materials
                isLearningMaterialsLoading.value = false
            },

            onError = { exception ->

                errorMessage.value =
                    exception.localizedMessage
                        ?: "Failed to load learning materials"

                isLearningMaterialsLoading.value = false
            }
        )
    }
    fun startLearningSession(
        materialId: String,
        onSuccess: (List<Challenge>) -> Unit = {}
    ) {

        val user = auth.currentUser

        if (user == null) {
            errorMessage.value = "User is not logged in"
            return
        }

        challengeRepository.getChallengesForMaterial(
            userId = user.uid,
            materialId = materialId,

            onSuccess = { challengeList ->

                challenges.value = challengeList

                onSuccess(challengeList)
            },

            onError = { exception ->

                errorMessage.value =
                    exception.localizedMessage
                        ?: "Failed to load challenges"
            }
        )
    }
    fun generateChallengesForMaterial(
        material: LearningMaterial,
        onSuccess: () -> Unit = {}
    ) {

        val user = auth.currentUser

        if (user == null) {
            errorMessage.value = "User is not logged in"
            return
        }

        challengeRepository.createChallengesForMaterial(
            userId = user.uid,
            materialId = material.id,
            text = material.text,
            language = material.language,

            onSuccess = {

                startLearningSession(
                    materialId = material.id
                ) {
                    onSuccess()
                }
            },

            onError = { exception ->

                errorMessage.value =
                    exception.localizedMessage
                        ?: "Failed to generate challenges"
            }
        )
    }
    fun deleteLearningSession(
        materialId: String,
        onSuccess: () -> Unit = {}
    ) {

        val user = auth.currentUser

        if (user == null) {
            errorMessage.value = "User is not logged in"
            return
        }

        // Prvo brišemo sve challengeove tog materijala
        challengeRepository.deleteChallengesForMaterial(
            userId = user.uid,
            materialId = materialId,

            onSuccess = {

                // Nakon challengeova brišemo Learning Material
                learningMaterialRepository.deleteLearningMaterial(
                    userId = user.uid,
                    materialId = materialId,

                    onSuccess = {

                        // Makni ga odmah i iz lokalnog prikaza
                        learningMaterials.value =
                            learningMaterials.value.filter {
                                it.id != materialId
                            }

                        // Makni i njegove challengeove iz memorije
                        challenges.value =
                            challenges.value.filter {
                                it.materialId != materialId
                            }

                        onSuccess()
                    },

                    onError = { exception ->

                        errorMessage.value =
                            exception.localizedMessage
                                ?: "Failed to delete learning material"
                    }
                )
            },

            onError = { exception ->

                errorMessage.value =
                    exception.localizedMessage
                        ?: "Failed to delete challenges"
            }
        )
    }
    fun updateDefaultChallenge(
        challengeId: String
    ) {

        val user = auth.currentUser

        if (user == null) {
            errorMessage.value = "User is not logged in"
            return
        }

        challengeRepository.updateDefaultChallengeProgress(
            userId = user.uid,
            challengeId = challengeId,

            onSuccess = {

                loadChallenges()
            },

            onError = { exception ->

                errorMessage.value =
                    exception.localizedMessage
                        ?: "Failed to update default challenge"
            }
        )
    }
    fun deleteChallenge(
        challengeId: String
    ) {

        val user = auth.currentUser

        if (user == null) {
            errorMessage.value = "User is not logged in"
            return
        }

        challengeRepository.deleteChallenge(
            userId = user.uid,
            challengeId = challengeId,

            onSuccess = {
                loadChallenges()
            },

            onError = { exception ->

                errorMessage.value =
                    exception.localizedMessage
                        ?: "Failed to delete challenge"
            }
        )
    }
    fun incrementCompletedChallenges() {

        val user = auth.currentUser

        if (user == null) {
            errorMessage.value = "User is not logged in"
            return
        }

        userRepository.incrementCompletedChallenges(
            userId = user.uid,

            onSuccess = {

                loadUserProfile()
            },

            onError = { exception ->

                errorMessage.value =
                    exception.localizedMessage
                        ?: "Failed to update completed challenges"
            }
        )
    }
    fun saveVocabularyWord(
        word: String,
        translation: String,
        language: String
    ) {

        val user = auth.currentUser

        if (user == null) {

            errorMessage.value =
                "User is not logged in"

            return
        }

        vocabularyRepository.saveVocabularyWord(
            userId = user.uid,
            word = word,
            translation = translation,
            language = language,

            onSuccess = {
                // Riječ je uspješno spremljena
            },

            onError = { exception ->

                errorMessage.value =
                    exception.localizedMessage
                        ?: "Failed to save vocabulary word"
            }
        )
    }
    fun loadVocabularyWords() {

        val user = auth.currentUser

        if (user == null) {

            errorMessage.value =
                "User is not logged in"

            return
        }

        vocabularyRepository.getVocabularyWords(
            userId = user.uid,

            onSuccess = { words ->

                vocabularyWords.value = words
            },

            onError = { exception ->

                errorMessage.value =
                    exception.localizedMessage
                        ?: "Failed to load vocabulary"
            }
        )
    }
}