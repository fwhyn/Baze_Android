package com.fwhyn.lib.baze.common.ui.model

data class ButtonModel(
    val label: String,
    val onClick: () -> Unit,
)