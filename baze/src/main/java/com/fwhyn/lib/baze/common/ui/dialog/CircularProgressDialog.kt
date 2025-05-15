package com.fwhyn.lib.baze.common.ui.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun CircularProgressDialog(
    progress: Int? = null,
    onDismiss: () -> Unit = {},
) {
    Dialog(
        onDismissRequest = onDismiss,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(80.dp)
                    .background(Color.White, shape = CircleShape)
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(60.dp),
                    color = Color.Blue,
                    strokeWidth = 6.dp
                )
            }

            progress?.let {
                Spacer(
                    modifier = Modifier.height(5.dp)
                )

                Text(
                    text = "$it %",
                    textAlign = TextAlign.Center,
                    color = Color.White,
                )
            }
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
    CircularProgressDialog(70)
}