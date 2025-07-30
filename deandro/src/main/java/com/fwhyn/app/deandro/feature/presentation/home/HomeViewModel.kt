package com.fwhyn.app.deandro.feature.presentation.home

import android.app.Activity
import androidx.lifecycle.viewModelScope
import com.fwhyn.app.deandro.R
import com.fwhyn.app.deandro.feature.func.access.domain.model.GetAccessParam
import com.fwhyn.app.deandro.feature.func.access.domain.usecase.GetAccessUseCase
import com.fwhyn.app.deandro.feature.func.auth.domain.model.AuthTokenModel
import com.fwhyn.app.deandro.feature.func.auth.domain.model.SetAuthTokenParam
import com.fwhyn.app.deandro.feature.func.auth.domain.usecase.SetAuthTokenUseCase
import com.fwhyn.lib.baze.common.model.Status
import com.fwhyn.lib.baze.compose.helper.ActivityRetainedState
import com.fwhyn.lib.baze.string.helper.StringIdManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val activityRetainedState: ActivityRetainedState,
    private val stringIdManager: StringIdManager<Status>,
    private val setTokenUseCase: SetAuthTokenUseCase,
    private val getAccessUseCase: GetAccessUseCase,
) : HomeVmInterface() {

    val uiData = HomeUiData()
    val uiState = HomeUiState()

    var getGoogleDriveAccessParam: GetAccessParam.GoogleDrive? = null

//    override var activityResult: ((Activity, Int, Int, Intent?) -> Unit)? =
//        { activity, requestCode, resultCode, data ->
//            when (requestCode) {
//                GoogleDriveAccess.REQUEST_AUTHORIZE -> {
//                    data?.let { getGoogleDriveAccessParam?.onRetrieveResult?.invoke(it) }
//                }
//
//                else -> {
//                    // do nothing
//                }
//            }
//        }

    override fun onLogout() {
        setTokenUseCase.invoke(
            scope = viewModelScope,
            onStart = { activityRetainedState.showLoading() },
            onFetchParam = { SetAuthTokenParam.Local(AuthTokenModel.None) },
            onOmitResult = {
                it.onSuccess {
                    uiState.state = HomeUiState.State.LoggedOut()
                }.onFailure { error ->
                    activityRetainedState.showNotification(
                        stringIdManager.getId(Status.Instance(-1, error.message ?: ""))
                    )
                }
            },
            onFinish = { activityRetainedState.dismissLoading() }
        )
    }

    override fun onAddPhoto(activity: Activity) {
        val param = GetAccessParam.GoogleDrive(activity).also { getGoogleDriveAccessParam = it }

        getAccessUseCase.invoke(
            scope = viewModelScope,
            onStart = { activityRetainedState.showLoading() },
            onFetchParam = { param },
            onOmitResult = {
                it.onSuccess {
                    activityRetainedState.showNotification(R.string.success)
                }.onFailure {
                    activityRetainedState.showNotification(R.string.unauthorized)
                }
            },
            onFinish = { activityRetainedState.dismissLoading() }
        )
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