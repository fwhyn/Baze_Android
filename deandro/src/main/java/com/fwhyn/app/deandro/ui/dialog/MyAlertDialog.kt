package com.fwhyn.app.deandro.ui.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun BazeAlertDialog(
    message: String,
    title: String? = null,
    onConfirmation: () -> Unit,
    onCancellation: (() -> Unit)? = null,
    onDismissRequest: () -> Unit,
    icon: ImageVector? = null,
) {
    AlertDialog(
        text = {
            Text(text = message)
        },
        title = getTitle(title),
        confirmButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                    onConfirmation()
                }
            ) {
                Text("OK")
            }
        },
        onDismissRequest = {},
        dismissButton = getCancelButton(onCancellation, onDismissRequest),
        icon = getIcon(icon),
    )
}

private fun getTitle(text: String?): (@Composable () -> Unit)? {
    return if (text != null) {
        { Text(text = text) }
    } else {
        null
    }
}

private fun getCancelButton(
    onCancellation: (() -> Unit)?,
    onDismissRequest: () -> Unit,
): (@Composable () -> Unit)? {
    return if (onCancellation != null) {
        {
            TextButton(
                onClick = {
                    onDismissRequest()
                    onCancellation()
                }
            ) {
                Text("Cancel")
            }
        }
    } else {
        null
    }
}

private fun getIcon(image: ImageVector?): (@Composable () -> Unit)? {
    return if (image != null) {
        { Icon(image, contentDescription = "Example Icon") }
    } else {
        null
    }
}

// ----------------------------------------------------------------
@Preview
@Composable
fun MyAlertDialogPreview() {
    BazeAlertDialog(
        message = "tsadgasdgaext",
        onConfirmation = {},
        onCancellation = {},
        onDismissRequest = {}
    )
}