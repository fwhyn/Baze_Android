package com.fwhyn.lib.baze.common.ui.model

data class DialogModel(
    val message: String,
    val title: String? = null,
    val onConfirmation: ButtonModel,
    val onCancellation: ButtonModel? = null,
    val onOtherConfirmation: ButtonModel? = null,
    val onDismissRequest: () -> Unit,
)