package com.fwhyn.appsample.ui.feature.home

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.fwhyn.appsample.data.model.auth.UserToken
import com.fwhyn.data.model.Status
import com.fwhyn.domain.helper.Rezult
import com.fwhyn.domain.usecase.BaseUseCase
import com.fwhyn.domain.usecase.BaseUseCaseRemote
import com.fwhyn.ui.helper.MessageHandler
import com.fwhyn.ui.main.MainUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val mainUiState: MainUiState,
    private val messageHandler: MessageHandler<Status>,
    private val setTokenUseCase: BaseUseCaseRemote<UserToken?, Unit>,
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
                    is Rezult.Failure -> mainUiState.showNotification(messageHandler.getMessage(it.err.status))
                    is Rezult.Success -> uiState.state = HomeUiState.State.LoggedOut()
                }
            }
            .setLifeCycleNotifier {
                when (it) {
                    BaseUseCase.LifeCycle.OnStart -> mainUiState.showLoading()
                    BaseUseCase.LifeCycle.OnFinish -> mainUiState.dismissLoading()
                }
            }
            .executeOnBackground(null, viewModelScope)
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
//                }.executeOnBackground(LinkSetParam(imageLinksKey!!, imageLinks), viewModelScope)
//            }
//        }
    }

    override fun onCleared() {
        clearImageLinks()
        clearImageCache()
    }

    private fun clearImageLinks() {
//        uiData.imageLinksKey?.let {
//            setLinkUseCase.executeOnBackground(
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
//                setImagesUseCase.executeOnBackground(imageSetParamList, CoroutineScope(Dispatchers.IO))
//            }
//        }
    }
}