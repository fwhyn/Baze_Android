package com.fwhyn.deandro.ui.feature.home

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.fwhyn.baze.data.model.Status
import com.fwhyn.baze.domain.helper.Rezult
import com.fwhyn.baze.domain.usecase.BaseUseCase
import com.fwhyn.baze.ui.helper.MessageHandler
import com.fwhyn.baze.ui.main.ActivityRetainedState
import com.fwhyn.deandro.data.model.auth.UserToken
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val activityRetainedState: ActivityRetainedState,
    private val messageHandler: MessageHandler<Status>,
    private val setTokenUseCase: BaseUseCase<UserToken?, Unit>,
//    private val setLinkUseCase: BaseUseCase<LinkSetParam, Unit>,
//    private val setImagesUseCase: BaseUseCase<List<ImageSetParam>, Unit>,
) : HomeVmInterface() {

    companion object {
        const val MAX_PHOTOS_LIMIT = 1
    }

    val uiData = HomeUiData()
    val uiState = HomeUiState()

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

    override fun onAddPhoto() {
//        uiData.imagePicker?.let {
//            ImageStorageUtil.launchMediaPicker(it)
//        }
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