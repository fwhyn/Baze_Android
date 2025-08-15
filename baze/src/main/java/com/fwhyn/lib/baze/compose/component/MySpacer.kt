package com.fwhyn.lib.baze.compose.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

@Composable
fun MySpacer(
    sizeDp: Dp,
) {
    Spacer(Modifier.size(sizeDp))
}