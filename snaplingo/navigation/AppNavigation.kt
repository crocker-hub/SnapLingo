package com.example.snaplingo.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.snaplingo.ui.home.HomeScreen
import com.example.snaplingo.ui.theme.LearnScreen
import com.example.snaplingo.ui.theme.ProfileScreen
import com.example.snaplingo.ui.theme.StatsScreen
import com.example.snaplingo.ui.theme.LearnScreen
import com.example.snaplingo.ui.theme.ProfileScreen
import com.example.snaplingo.ui.theme.StatsScreen
import com.example.snaplingo.ui.theme.components.MainScaffold
import com.example.snaplingo.ui.theme.home.HomeScreen
import com.example.snaplingo.ui.theme.viewmodel.UserViewModel
import com.yourpackage.snaplingo.ui.login.LoginScreen
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.snaplingo.ui.theme.challenge.ChallengeScreen
import com.example.snaplingo.ui.theme.camera.CameraScreen
import com.example.snaplingo.ui.theme.home.VocabularyScreen
import com.example.snaplingo.ui.theme.language.LanguageSelectionScreen
import com.example.snaplingo.ui.theme.learning.LearningSessionScreen


@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    val userViewModel: UserViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(
            route = Screen.LearningSession.route
        ) { backStackEntry ->

            val materialId =
                backStackEntry.arguments?.getString("materialId")
                    ?: return@composable

            MainScaffold(
                navController = navController
            ) { paddingValues ->

                LearningSessionScreen(
                    paddingValues = paddingValues,
                    navController = navController,
                    userViewModel = userViewModel,
                    materialId = materialId
                )
            }
        }


        composable(Screen.Camera.route) {

            MainScaffold(
                navController = navController
            ) { paddingValues ->

                CameraScreen(
                    paddingValues = paddingValues,
                    navController = navController
                )
            }
        }

        // LOGIN

        composable(Screen.Login.route) {

            LoginScreen(

                onLoginClick = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) {
                            inclusive = true
                        }
                    }
                },

                onRegisterClick = {
                    navController.navigate(Screen.Register.route)
                }
            )
        }


        // REGISTER

        composable(Screen.Register.route) {

            RegisterScreen(
                navController = navController
            )
        }


        // HOME

        composable(Screen.Home.route) {

            MainScaffold(
                navController = navController
            ) { paddingValues ->

                HomeScreen(
                    paddingValues = paddingValues,
                    navController = navController,
                    userViewModel = userViewModel
                )
            }
        }


        // LEARN

        composable(Screen.Learn.route) {

            MainScaffold(
                navController = navController
            ) { paddingValues ->

                LearnScreen(
                    paddingValues = paddingValues,
                    navController = navController,
                    userViewModel = userViewModel
                )
            }
        }

        // VOCABULARY

        composable(Screen.Vocabulary.route) {

            MainScaffold(
                navController = navController
            ) { paddingValues ->

                VocabularyScreen(
                    paddingValues = paddingValues,
                    userViewModel = userViewModel
                )
            }
        }


        // STATS

        composable(Screen.Stats.route) {

            MainScaffold(
                navController = navController
            ) { paddingValues ->

                StatsScreen(
                    paddingValues = paddingValues,
                    userViewModel = userViewModel
                )
            }
        }


        // PROFILE

        composable(Screen.Profile.route) {

            MainScaffold(
                navController = navController
            ) { paddingValues ->

                ProfileScreen(
                    paddingValues = paddingValues,
                    navController = navController,
                    userViewModel = userViewModel
                )
            }
        }
        composable(
            route = Screen.Challenge.route,
            arguments = listOf(
                navArgument("challengeId") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->

            val challengeId =
                backStackEntry.arguments?.getString("challengeId")
                    ?: ""

            MainScaffold(
                navController = navController
            ) { paddingValues ->

                ChallengeScreen(
                    paddingValues = paddingValues,
                    navController = navController,
                    userViewModel = userViewModel,
                    challengeId = challengeId
                )
            }
        }

        composable(Screen.LanguageSelection.route) {

            MainScaffold(
                navController = navController
            ) { paddingValues ->

                LanguageSelectionScreen(
                    paddingValues = paddingValues,
                    navController = navController,
                    userViewModel = userViewModel
                )
            }
        }
    }
}