package com.fwhyn.lib.baze.compose.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun CircularProgressDialog(
    foregroundModifier: Modifier = Modifier,
    backgroundModifier: Modifier = Modifier,
    color: Color = ProgressIndicatorDefaults.circularColor,
    strokeWidth: Dp = ProgressIndicatorDefaults.CircularStrokeWidth,
    trackColor: Color = ProgressIndicatorDefaults.circularIndeterminateTrackColor,
    strokeCap: StrokeCap = ProgressIndicatorDefaults.CircularIndeterminateStrokeCap,
    onDismiss: () -> Unit = {},
    trailingContent: (@Composable () -> Unit)? = null
) {
    Dialog(
        onDismissRequest = onDismiss,
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = backgroundModifier
            ) {
                CircularProgressIndicator(
                    modifier = foregroundModifier,
                    color = color,
                    strokeWidth = strokeWidth,
                    trackColor = trackColor,
                    strokeCap = strokeCap
                )
            }

            trailingContent?.invoke()
        }
    }
}

@Preview
@Composable
fun CircularProgressDialogPreview0() {
    CircularProgressDialog()
}

@Preview
@Composable
fun CircularProgressDialogPreview1() {
    CircularProgressDialog(
        foregroundModifier = Modifier
            .padding(10.dp)
            .background(color = Color.White, shape = CircleShape),
        backgroundModifier = Modifier
            .background(color = Color.White, shape = CircleShape),
        strokeWidth = 8.dp
    ) {
        Text(
            text = "$70 %",
            textAlign = TextAlign.Center,
            color = Color.White,
        )
    }
}