package com.fwhyn.app.deandro.feature.presentation.home

import android.app.Activity
import android.net.Uri
import androidx.lifecycle.ViewModel

abstract class HomeVmInterface : ViewModel() {
    open fun onLogout() {}
    open fun onAddPhoto(activity: Activity) {}
    open fun onPhotoSelected(photos: List<Uri>) {}
}