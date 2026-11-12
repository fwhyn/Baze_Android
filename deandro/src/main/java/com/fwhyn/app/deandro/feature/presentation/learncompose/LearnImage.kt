package com.fwhyn.app.deandro.feature.presentation.learncompose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fwhyn.app.deandro.R

@Preview
@Composable
fun ImageDemo() {
    Image(
        painter = painterResource(R.drawable.ic_launcher_background),
        contentDescription = "Image Demo"
    )
}

@Preview
@Composable
fun IconDemo() {
    Icon(
        imageVector = Icons.Default.Favorite,
        contentDescription = "Icon Demo",
        tint = Color.Blue,
        modifier = Modifier.size(48.dp)
    )
}