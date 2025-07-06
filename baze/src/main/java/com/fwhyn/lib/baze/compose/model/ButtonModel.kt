package com.fwhyn.lib.baze.compose.model

data class ButtonModel(
    val label: String,
    val onClick: () -> Unit,
)