package com.fwhyn.app.deandro.ui.feature.home

import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest

class HomeUiData {
    var imagePicker: ActivityResultLauncher<PickVisualMediaRequest>? = null
    var imageLinksKey: String? = null
//    var imageLinks: List<Link>? = null
}