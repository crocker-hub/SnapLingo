package com.example.snaplingo.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")

    object Register : Screen("register")

    object Home : Screen("home")

    object Learn : Screen("learn")

    object Vocabulary : Screen("vocabulary")

    object Stats : Screen("stats")

    object Profile : Screen("profile")

    data object Camera : Screen("camera")

    data object Challenge : Screen("challenge/{challengeId}") {

        fun createRoute(challengeId: String): String {
            return "challenge/$challengeId"
        }
    }
    data object LanguageSelection : Screen("language_selection")

    data object LearningSession : Screen("learning_session/{materialId}") {

        fun createRoute(materialId: String): String {
            return "learning_session/$materialId"
        }
    }
}