package com.fwhyn.baze.ui.model

data class ButtonModel(
    val label: String,
    val onClick: () -> Unit,
)