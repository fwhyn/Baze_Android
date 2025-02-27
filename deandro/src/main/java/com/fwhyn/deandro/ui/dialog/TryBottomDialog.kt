package com.fwhyn.deandro.ui.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TryBottomDialog(
    onDismissRequest: () -> Unit,
) {
    val skipPartiallyExpanded by rememberSaveable { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = skipPartiallyExpanded)

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = bottomSheetState,
    ) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Button(
                // Note: If you provide logic outside of onDismissRequest to remove the sheet,
                // you must additionally handle intended state cleanup, if any.
                onClick = {
                    scope
                        .launch { bottomSheetState.hide() }
                        .invokeOnCompletion {
                            if (!bottomSheetState.isVisible) {
                                onDismissRequest()
                            }
                        }
                }
            ) {
                Text("Hide Bottom Sheet")
            }
        }
        var text by remember { mutableStateOf("") }
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            modifier = Modifier.padding(horizontal = 16.dp),
            label = { Text("Text field") }
        )
        LazyColumn {
            items(25) {
                ListItem(
                    headlineContent = { Text("Item $it") },
                    leadingContent = {
                        Icon(
                            Icons.Default.Favorite,
                            contentDescription = "Localized description"
                        )
                    },
                    colors =
                    ListItemDefaults.colors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerLow
                    ),
                )
            }
        }
    }
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
fun MyBottomDialogPreview() {
    var isShowing by rememberSaveable { mutableStateOf(false) }

    // App content
    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Button(
            onClick = { isShowing = !isShowing },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Show Bottom Sheet")
        }
    }

    if (isShowing) {
        TryBottomDialog(
            onDismissRequest = {
                isShowing = false
            }
        )
    }
}