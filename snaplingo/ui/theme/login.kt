package com.yourpackage.snaplingo.ui.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.snaplingo.navigation.Screen
import com.google.firebase.auth.FirebaseAuth

@Composable
fun LoginScreen(
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit
) {

    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    var errorMessage by remember {
        mutableStateOf("")
    }

    var isLoading by remember {
        mutableStateOf(false)
    }

    val auth = FirebaseAuth.getInstance()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFCF8))
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Welcome back!",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Log in to continue learning"
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
            },
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text("Email")
            },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
            },
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text("Password")
            },
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (errorMessage.isNotEmpty()) {

            Text(
                text = errorMessage,
                color = Color.Red
            )

            Spacer(modifier = Modifier.height(8.dp))
        }

        Button(
            onClick = {

                if (email.isBlank()) {

                    errorMessage = "Enter an email"

                } else if (password.isBlank()) {

                    errorMessage = "Enter a password"

                } else {

                    errorMessage = ""
                    isLoading = true

                    auth.signInWithEmailAndPassword(
                        email,
                        password
                    ).addOnCompleteListener { task ->

                        isLoading = false

                        if (task.isSuccessful) {

                            onLoginClick()

                        } else {

                            errorMessage =
                                task.exception?.localizedMessage
                                    ?: "Login failed"
                        }
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            shape = RoundedCornerShape(16.dp),
            enabled = !isLoading,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF0F5E4D)
            )
        ) {

            Text(
                text = if (isLoading) {
                    "Logging in..."
                } else {
                    "Log in"
                }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                onRegisterClick()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color(0xFF0F5E4D)
            )
        ) {

            Text(
                text = "Create account"
            )
        }
    }
}