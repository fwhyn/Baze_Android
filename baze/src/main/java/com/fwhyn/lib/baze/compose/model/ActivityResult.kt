package com.fwhyn.lib.baze.compose.model

import android.content.Intent

data class ActivityResult(
    val requestCode: Int,
    val resultCode: Int,
    val data: Intent?
)