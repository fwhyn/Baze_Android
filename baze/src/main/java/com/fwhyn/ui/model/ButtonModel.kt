package com.fwhyn.ui.model

data class ButtonModel(
    val label: String,
    val onClick: () -> Unit,
)