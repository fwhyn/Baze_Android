package com.fwhyn.deandro.ui.feature.main

import androidx.lifecycle.ViewModel
import com.fwhyn.baze.data.helper.network.NetworkMonitor
import com.fwhyn.baze.ui.main.ActivityState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    val networkMonitor: NetworkMonitor,
    val activityState: ActivityState,
//    private val memoryUseCase: BaseUseCase<List<ManageMemoryParam>, Any?>,
) : ViewModel() {

//    val activityState: ActivityState = ActivityState()

//    init {
//        clearPreviousMemory()
//    }

//    private fun clearPreviousMemory() {
//        activityState.showLoading(memoryUseCase.getId())
//        memoryUseCase.setResultNotifier {
//            when (it) {
//                is Rezult.Failure -> activityState.showNotification(R.string.cleaning_memory_error)
//                is Rezult.Success -> activityState.setIdle()
//            }
//        }.executeOnBackground(listOf(ManageMemoryParam(ManageMemoryAction.CLEAR)), viewModelScope)
//    }
}