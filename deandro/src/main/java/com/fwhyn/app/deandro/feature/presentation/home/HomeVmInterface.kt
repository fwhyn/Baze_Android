package com.fwhyn.app.deandro.feature.presentation.home

import android.app.Activity
import android.net.Uri
import androidx.lifecycle.ViewModel
import com.fwhyn.lib.baze.common.ui.model.ActivityResult
import kotlinx.coroutines.flow.SharedFlow

abstract class HomeVmInterface : ViewModel() {

    open var activityResult: SharedFlow<ActivityResult>? = null
    open fun onLogout() {}
    open fun onAddPhoto(activity: Activity) {}
    open fun onPhotoSelected(photos: List<Uri>) {}
}