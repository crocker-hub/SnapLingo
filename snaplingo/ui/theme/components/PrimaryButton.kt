package com.example.snaplingo.ui.theme.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit
) {

    Button(
        onClick = onClick,

        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),

        shape = RoundedCornerShape(16.dp),

        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF0F5E4D)
        )
    ) {

        Text(text)

    }

}
