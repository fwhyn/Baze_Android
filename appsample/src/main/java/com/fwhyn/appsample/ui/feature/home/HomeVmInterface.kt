package com.fwhyn.appsample.ui.feature.home

import android.net.Uri
import androidx.lifecycle.ViewModel

abstract class HomeVmInterface : ViewModel() {
    open fun onLogout() {}
    open fun onAddPhoto() {}
    open fun onPhotoSelected(photos: List<Uri>) {}
}