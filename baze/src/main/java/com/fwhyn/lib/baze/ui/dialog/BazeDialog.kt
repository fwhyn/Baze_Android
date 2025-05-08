package com.fwhyn.lib.baze.ui.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.fwhyn.lib.baze.ui.model.ButtonModel
import com.fwhyn.lib.baze.ui.model.DialogModel

@Composable
fun BazeDialog(
    model: DialogModel,
) {
    BazeDialog(
        message = model.message,
        title = model.title,
        onConfirmation = model.onConfirmation,
        onCancellation = model.onCancellation,
        onOtherConfirmation = model.onOtherConfirmation,
        onDismissRequest = model.onDismissRequest
    )
}

@Composable
private fun BazeDialog(
    message: String,
    title: String? = null,
    onConfirmation: ButtonModel,
    onCancellation: ButtonModel? = null,
    onOtherConfirmation: ButtonModel? = null,
    onDismissRequest: () -> Unit,
) {
    Dialog(
        onDismissRequest = {
            // Do nothing
        }
    ) {
        Column(
            modifier = Modifier
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(1)
                )
                .width(400.dp)
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
        ) {
            title?.let {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(Modifier.height(10.dp))
            }

            Text(
                text = message,
                color = Color.Black
            )

            Spacer(Modifier.height(10.dp))

            Column(
                modifier = Modifier.align(Alignment.End),
            ) {
                DialogButton(
                    button = onConfirmation,
                    onDismissRequest = onDismissRequest
                )

                onCancellation?.let {
                    DialogButton(
                        button = it,
                        onDismissRequest = onDismissRequest
                    )
                }

                onOtherConfirmation?.let {
                    DialogButton(
                        button = it,
                        onDismissRequest = onDismissRequest
                    )
                }
            }
        }
    }
}

@Composable
private fun DialogButton(
    button: ButtonModel,
    onDismissRequest: () -> Unit,
) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable {
                onDismissRequest()
                button.onClick()
            },
        text = button.label,
        textAlign = TextAlign.End,
        color = Color.Blue,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}

// ----------------------------------------------------------------
@Preview
@Composable
fun DialogPreview() {
    BazeDialog(
        message = "Message Test...",
        title = "Title",
        onConfirmation = ButtonModel("Ok", {}),
        onCancellation = ButtonModel("Cancel", {}),
        onOtherConfirmation = ButtonModel("Do not show again", {}),
        onDismissRequest = {}
    )
}

@Preview
@Composable
fun DialogPreview1() {
    BazeDialog(
        message = "Message Test...",
        onConfirmation = ButtonModel("Ok", {}),
        onDismissRequest = {}
    )
}