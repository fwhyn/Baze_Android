package com.fwhyn.lib.baze.compose.helper

import androidx.compose.ui.tooling.preview.Preview

/**
 * Multi-preview annotation that represents various device sizes. Add this annotation to a composable
 * to render various devices.
 */
@Preview(name = "Portrait", widthDp = 324, heightDp = 720, showBackground = true)
@Preview(name = "Landscape", widthDp = 720, heightDp = 324, showBackground = true)
annotation class DevicePreviews