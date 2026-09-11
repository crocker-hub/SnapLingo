package com.example.snaplingo.data.repository

import com.example.snaplingo.data.model.Challenge
import com.google.firebase.firestore.FirebaseFirestore
class ChallengeRepository {

    private fun getLanguageCode(language: String): String? {

        return when (language.lowercase()) {

            "english" -> "en"

            "spanish" -> "es"

            "german" -> "de"

            "french" -> "fr"

            "italian" -> "it"

            else -> null
        }
    }

    private val firestore = FirebaseFirestore.getInstance()

    private val translationRepository =
        TranslationRepository()

    fun getChallenges(
        userId: String,
        onSuccess: (List<Challenge>) -> Unit,
        onError: (Exception) -> Unit
    ) {

        firestore
            .collection("users")
            .document(userId)
            .collection("challenges")
            .get()
            .addOnSuccessListener { result ->

                val challenges = result.documents.mapNotNull { document ->
                    document.toObject(Challenge::class.java)
                }

                onSuccess(challenges)
            }
            .addOnFailureListener { exception ->

                onError(exception)
            }
    }
    fun updateChallengeProgress(
        userId: String,
        challengeId: String,
        onSuccess: (Challenge) -> Unit,
        onError: (Exception) -> Unit
    ) {

        val challengeReference = firestore
            .collection("users")
            .document(userId)
            .collection("challenges")
            .document(challengeId)

        challengeReference
            .get()
            .addOnSuccessListener { document ->

                val challenge =
                    document.toObject(Challenge::class.java)

                if (challenge == null) {
                    onError(Exception("Challenge not found"))
                    return@addOnSuccessListener
                }

                // Ako je challenge već završen,
                // ne povećavamo progress ponovno.
                if (challenge.completed) {
                    onSuccess(challenge)
                    return@addOnSuccessListener
                }

                val newProgress = challenge.progress + 1

                val isCompleted =
                    newProgress >= challenge.target

                val updatedChallenge = challenge.copy(
                    progress = newProgress,
                    completed = isCompleted
                )

                challengeReference
                    .set(updatedChallenge)
                    .addOnSuccessListener {

                        onSuccess(updatedChallenge)
                    }
                    .addOnFailureListener { exception ->

                        onError(exception)
                    }
            }
            .addOnFailureListener { exception ->

                onError(exception)
            }
    }
    fun getChallengesForMaterial(
        userId: String,
        materialId: String,
        onSuccess: (List<Challenge>) -> Unit,
        onError: (Exception) -> Unit
    ) {

        firestore.collection("users")
            .document(userId)
            .collection("challenges")
            .whereEqualTo("materialId", materialId)
            .get()
            .addOnSuccessListener { result ->

                val challenges =
                    result.documents.mapNotNull { document ->

                        document.toObject(
                            Challenge::class.java
                        )
                    }

                onSuccess(challenges)
            }
            .addOnFailureListener { exception ->

                onError(exception)
            }
    }

    fun createChallengesForMaterial(
        userId: String,
        materialId: String,
        text: String,
        language: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit


    ) {

        val sourceLanguageCode =
            getLanguageCode(language)

        if (sourceLanguageCode == null) {

            onError(
                Exception(
                    "Unsupported language: $language"
                )
            )

            return
        }

        val stopWords = setOf(



            "the", "and", "for", "with", "this",
            "that", "from", "have", "has",
            "are", "was", "were", "will",
            "would", "could", "should",
            "into", "your", "you", "our",
            "but", "not", "all", "can",
            "they", "their", "them",
            "his", "her", "its",
            "is", "in", "on", "at",
            "to", "of", "a", "an",
            "be", "or", "as", "by",



            "el", "la", "los", "las",
            "un", "una", "unos", "unas",
            "y", "o", "pero", "porque",
            "que", "de", "del", "al",
            "en", "con", "para", "por",
            "es", "son", "era", "eran",
            "ser", "estar", "está",
            "están", "como", "más",
            "muy", "también", "su",
            "sus", "mi", "tu",



            "der", "die", "das",
            "den", "dem", "des",
            "ein", "eine", "einen",
            "einem", "einer",
            "und", "oder", "aber",
            "nicht", "ist", "sind",
            "war", "waren",
            "mit", "für", "von",
            "zu", "im", "in",
            "auf", "an", "bei",
            "als", "auch", "wie",
            "ich", "du", "er",
            "sie", "wir", "ihr",



            "le", "la", "les",
            "un", "une", "des",
            "et", "ou", "mais",
            "que", "qui", "de",
            "du", "dans", "en",
            "pour", "par", "avec",
            "sur", "est", "sont",
            "être", "avoir", "a",
            "au", "aux", "ce",
            "cette", "ces", "il",
            "elle", "nous", "vous",
            "ils", "elles",



            "il", "lo", "la",
            "i", "gli", "le",
            "un", "uno", "una",
            "e", "o", "ma",
            "che", "di", "del",
            "della", "dei", "delle",
            "in", "con", "per",
            "su", "da", "a",
            "è", "sono", "era",
            "essere", "avere",
            "come", "anche",
            "non", "più", "molto",
            "mi", "ti", "si"
        )

        val words = text
            .replace(
                Regex("[^\\p{L}\\s]"),
                " "
            )
            .split("\\s+".toRegex())
            .map { it.trim() }
            .filter {
                it.length >= 3 &&
                        it.lowercase() !in stopWords
            }
            .distinctBy {
                it.lowercase()
            }
            .shuffled()
            .take(10)

        if (words.isEmpty()) {

            onError(
                Exception("No suitable words found in text")
            )

            return
        }

        val translatedWords =
            mutableListOf<Pair<String, String>>()

        fun translateNext(index: Int) {

            if (index >= words.size) {

                if (translatedWords.isEmpty()) {

                    onError(
                        Exception(
                            "Could not translate any words"
                        )
                    )

                    return
                }

                saveTranslatedChallenges(
                    userId = userId,
                    materialId = materialId,
                    translatedWords = translatedWords,
                    onSuccess = onSuccess,
                    onError = onError
                )

                return
            }

            val word = words[index]

            translationRepository.translateWord(
                word = word,

                sourceLanguage = sourceLanguageCode,

                targetLanguage = "hr",

                onSuccess = { translation ->

                    translatedWords.add(
                        Pair(word, translation)
                    )

                    translateNext(index + 1)
                },

                onError = {

                    // Ako jedna riječ ne uspije,
                    // nastavljamo na sljedeću
                    translateNext(index + 1)
                }
            )
        }

        translateNext(0)
    }

    private fun saveTranslatedChallenges(
        userId: String,
        materialId: String,
        translatedWords: List<Pair<String, String>>,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {

        val batch = firestore.batch()

        translatedWords.forEachIndexed { index, pair ->

            val word = pair.first
            val translation = pair.second

            val challengeRef = firestore
                .collection("users")
                .document(userId)
                .collection("challenges")
                .document()

            // Uzmi prijevode drugih riječi kao netočne odgovore
            val wrongAnswers = translatedWords
                .map { it.second }
                .filter {
                    it.lowercase() != translation.lowercase()
                }
                .distinct()
                .shuffled()
                .take(3)
                .toMutableList()

            // Ako nemamo dovoljno drugih prijevoda,
            // dodajemo generičke odgovore
            val fallbackAnswers = listOf(
                "Kuća",
                "Voda",
                "Škola",
                "Knjiga",
                "Auto",
                "Prijatelj",
                "Hrana"
            )

            fallbackAnswers.forEach { answer ->

                if (
                    wrongAnswers.size < 3 &&
                    answer.lowercase() != translation.lowercase() &&
                    answer !in wrongAnswers
                ) {

                    wrongAnswers.add(answer)
                }
            }

            val options =
                (wrongAnswers.take(3) + translation)
                    .shuffled()

            val challenge = Challenge(
                id = challengeRef.id,
                materialId = materialId,
                title = "Vocabulary Challenge ${index + 1}",
                description = "Choose the correct translation.",
                question = "What does \"$word\" mean?",
                correctAnswer = translation,
                options = options,
                target = 1,
                progress = 0,
                xpReward = 10,
                completed = false
            )

            batch.set(challengeRef, challenge)
        }

        batch.commit()
            .addOnSuccessListener {

                onSuccess()
            }
            .addOnFailureListener { exception ->

                onError(exception)
            }
    }

    fun deleteChallengesForMaterial(
        userId: String,
        materialId: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {

        firestore
            .collection("users")
            .document(userId)
            .collection("challenges")
            .whereEqualTo("materialId", materialId)
            .get()
            .addOnSuccessListener { result ->

                val batch = firestore.batch()

                result.documents.forEach { document ->
                    batch.delete(document.reference)
                }

                batch.commit()
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
    fun updateDefaultChallengeProgress(
        userId: String,
        challengeId: String,
        onSuccess: (Challenge) -> Unit = {},
        onError: (Exception) -> Unit
    ) {

        val challengeReference = firestore
            .collection("users")
            .document(userId)
            .collection("challenges")
            .document(challengeId)

        challengeReference.get()
            .addOnSuccessListener { document ->

                val challenge =
                    document.toObject(Challenge::class.java)

                if (challenge == null) {

                    onError(
                        Exception("Default challenge not found")
                    )

                    return@addOnSuccessListener
                }

                if (challenge.completed) {

                    onSuccess(challenge)

                    return@addOnSuccessListener
                }

                val newProgress =
                    challenge.progress + 1

                val isCompleted =
                    newProgress >= challenge.target

                val updatedChallenge =
                    challenge.copy(
                        progress = newProgress.coerceAtMost(challenge.target),
                        completed = isCompleted
                    )

                challengeReference
                    .set(updatedChallenge)
                    .addOnSuccessListener {

                        onSuccess(updatedChallenge)
                    }
                    .addOnFailureListener { exception ->

                        onError(exception)
                    }
            }
            .addOnFailureListener { exception ->

                onError(exception)
            }
    }
    fun deleteChallenge(
        userId: String,
        challengeId: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {

        firestore
            .collection("users")
            .document(userId)
            .collection("challenges")
            .document(challengeId)
            .delete()
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onError(exception)
            }
    }

}
