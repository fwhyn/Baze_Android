package com.fwhyn.app.deandro.feature.presentation.home

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModel

abstract class HomeVmInterface : ViewModel() {

    open var onActivityResult: ((activity: Activity, requestCode: Int, resultCode: Int, data: Intent?) -> Unit)? = null
    open fun onLogout() {}
    open fun onAddPhoto(activity: Activity) {}
    open fun onPhotoSelected(photos: List<Uri>) {}
}