package com.fwhyn.app.deandro.feature.presentation.learncompose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview(showBackground = true)
@Composable
fun LearnModifierOrderA() {
    Column() {
        Text(
            text = "Hello Modifier Order A",
            modifier = Modifier
                .background(Color.Yellow)
                .padding(8.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LearnModifierOrderB() {
    Column() {
        Text(
            text = "Hello Modifier Order B",
            modifier = Modifier
                .padding(8.dp)
                .background(Color.Yellow)
        )
    }
}