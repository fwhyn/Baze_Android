package com.fwhyn.app.deandro.feature.presentation.home

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.fwhyn.app.deandro.feature.func.access.data.remote.GoogleDriveAccess
import com.fwhyn.app.deandro.feature.func.access.domain.model.GetAccessParam
import com.fwhyn.app.deandro.feature.func.access.domain.usecase.GetAccessUseCaseInterface
import com.fwhyn.app.deandro.feature.func.auth.data.model.UserToken
import com.fwhyn.lib.baze.data.model.Status
import com.fwhyn.lib.baze.domain.helper.Rezult
import com.fwhyn.lib.baze.domain.usecase.BaseUseCase
import com.fwhyn.lib.baze.ui.helper.MessageHandler
import com.fwhyn.lib.baze.ui.main.ActivityRetainedState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val activityRetainedState: ActivityRetainedState,
    private val messageHandler: MessageHandler<Status>,
    private val setTokenUseCase: BaseUseCase<UserToken?, Unit>,
    private val getAccessUseCase: GetAccessUseCaseInterface,
) : HomeVmInterface() {

    companion object {
        const val MAX_PHOTOS_LIMIT = 1
    }

    val uiData = HomeUiData()
    val uiState = HomeUiState()

    var getGoogleDriveAccessParam: GetAccessParam.GoogleDrive? = null

    override var onActivityResult: ((Activity, Int, Int, Intent?) -> Unit)? =
        { activity, requestCode, resultCode, data ->
            when (requestCode) {
                GoogleDriveAccess.REQUEST_AUTHORIZE -> {
                    data?.let { getGoogleDriveAccessParam?.onRetrieveResult?.invoke(it) }
                }

                else -> {
                    // do nothing
                }
            }
        }

    override fun onLogout() {
        setTokenUseCase
            .setResultNotifier {
                when (it) {
                    is Rezult.Failure -> {
                        activityRetainedState.showNotification(
                            messageHandler.getMessage(Status.Instance(-1, it.err.message ?: ""))
                        )
                    }

                    is Rezult.Success -> uiState.state = HomeUiState.State.LoggedOut()
                }
            }
            .setLifeCycleNotifier {
                when (it) {
                    BaseUseCase.LifeCycle.OnStart -> activityRetainedState.showLoading()
                    BaseUseCase.LifeCycle.OnFinish -> activityRetainedState.dismissLoading()
                }
            }
            .execute(null, viewModelScope)
    }

    override fun onAddPhoto(activity: Activity) {
//        uiData.imagePicker?.let {
//            ImageStorageUtil.launchMediaPicker(it)
//        }
        val param = GetAccessParam.GoogleDrive(activity).also { getGoogleDriveAccessParam = it }

        getAccessUseCase
            .setResultNotifier {

            }
            .setLifeCycleNotifier {

            }
            .execute(param, viewModelScope)
    }

    override fun onPhotoSelected(photos: List<Uri>) {
//        if (photos.isNotEmpty()) {
//            uiData.run {
//                imageLinks = photos.size.createList { index -> Link(photos[index]) }
//                imageLinksKey = DataUtil.getUniqueId()
//
//                setLinkUseCase.setResultNotifier {
//                    when (it) {
//                        is Results.Failure -> HomeUiState.State.OnNotification(it.err.status.code.toString())
//                        is Results.Loading -> HomeUiState.State.Loading
//                        is Results.Success -> uiState.state =
//                            HomeUiState.State.CallPhotoEdit(imageLinksKey!!)
//                    }
//                }.execute(LinkSetParam(imageLinksKey!!, imageLinks), viewModelScope)
//            }
//        }
    }

    override fun onCleared() {
        clearImageLinks()
        clearImageCache()
    }

    private fun clearImageLinks() {
//        uiData.imageLinksKey?.let {
//            setLinkUseCase.execute(
//                LinkSetParam(it, null),
//                CoroutineScope(Dispatchers.IO)
//            )
//        }
    }

    private fun clearImageCache() {
//        uiData.imageLinks?.let {
//            val size = it.size
//            if (size > 0) {
//                val imageSetParamList = size.createList { index -> ImageSetParam(it[index], null) }
//                setImagesUseCase.execute(imageSetParamList, CoroutineScope(Dispatchers.IO))
//            }
//        }
    }
}