package com.fwhyn.app.deandro.feature.presentation.home

import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest

class HomeUiData {
    var imagePicker: ActivityResultLauncher<PickVisualMediaRequest>? = null
    var imageLinksKey: String? = null
//    var imageLinks: List<Link>? = null
}