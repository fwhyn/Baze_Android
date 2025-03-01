package com.fwhyn.deandro.ui.feature.main

import androidx.lifecycle.ViewModel
import com.fwhyn.baze.data.helper.network.NetworkMonitor
import com.fwhyn.baze.ui.main.ActivityRetainedState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    val networkMonitor: NetworkMonitor,
    val activityRetainedState: ActivityRetainedState,
//    private val memoryUseCase: BaseUseCase<List<ManageMemoryParam>, Any?>,
) : ViewModel() {

//    val activityRetainedState: ActivityRetainedState = ActivityRetainedState()

//    init {
//        clearPreviousMemory()
//    }

//    private fun clearPreviousMemory() {
//        activityRetainedState.showLoading(memoryUseCase.getId())
//        memoryUseCase.setResultNotifier {
//            when (it) {
//                is Rezult.Failure -> activityRetainedState.showNotification(R.string.cleaning_memory_error)
//                is Rezult.Success -> activityRetainedState.setIdle()
//            }
//        }.executeOnBackground(listOf(ManageMemoryParam(ManageMemoryAction.CLEAR)), viewModelScope)
//    }
}