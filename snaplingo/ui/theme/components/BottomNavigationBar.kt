package com.example.snaplingo.ui.theme.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.snaplingo.navigation.Screen
import androidx.compose.material.icons.filled.MenuBook

@Composable
fun BottomNavigationBar(
    navController: NavController
) {

    val currentRoute =
        navController.currentBackStackEntryAsState()
            .value
            ?.destination
            ?.route

    NavigationBar {

        // HOME
        NavigationBarItem(
            selected = currentRoute == Screen.Home.route,

            onClick = {
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Home.route) {
                        inclusive = false
                    }
                    launchSingleTop = true
                }
            },

            icon = {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Home"
                )
            },

            label = {
                Text("Home")
            }
        )

        // LEARN
        NavigationBarItem(
            selected = currentRoute == Screen.Learn.route,

            onClick = {
                navController.navigate(Screen.Learn.route)
            },

            icon = {
                Icon(
                    imageVector = Icons.Default.School,
                    contentDescription = "Learn"
                )
            },

            label = {
                Text("Learn")
            }
        )

        // VOCABULARY
        NavigationBarItem(
            selected = currentRoute == Screen.Vocabulary.route,

            onClick = {
                navController.navigate(Screen.Vocabulary.route) {
                    launchSingleTop = true
                }
            },

            icon = {
                Icon(
                    imageVector = Icons.Default.MenuBook,
                    contentDescription = "Vocabulary"
                )
            },

            label = {
                Text("Words")
            }
        )

        // STATS
        NavigationBarItem(
            selected = currentRoute == Screen.Stats.route,

            onClick = {
                navController.navigate(Screen.Stats.route)
            },

            icon = {
                Icon(
                    imageVector = Icons.Default.BarChart,
                    contentDescription = "Stats"
                )
            },

            label = {
                Text("Stats")
            }
        )

        // PROFILE
        NavigationBarItem(
            selected = currentRoute == Screen.Profile.route,

            onClick = {
                navController.navigate(Screen.Profile.route)
            },

            icon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile"
                )
            },

            label = {
                Text("Profile")
            }
        )
    }
}