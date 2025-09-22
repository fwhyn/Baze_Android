package com.fwhyn.lib.baze.compose.helper

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fwhyn.lib.baze.compose.dialog.CircularProgressDialog
import com.fwhyn.lib.baze.compose.model.CommonProperties
import com.fwhyn.lib.baze.compose.model.CommonState
import kotlinx.coroutines.flow.StateFlow

data object Loading

@Composable
fun StateFlow<CommonState>.CollectLoadingState() {
    val state: CommonState by this.collectAsStateWithLifecycle()

    if (state.isLoading()) {
        CircularProgressDialog()
    }
}

fun CommonProperties.showLoadingDialog(tag: String) {
    this.showDialog(tag = tag, dat = Loading)
}

fun CommonState.isLoading(): Boolean {
    return (this as? CommonState.Dialog<*>)?.dat is Loading
}