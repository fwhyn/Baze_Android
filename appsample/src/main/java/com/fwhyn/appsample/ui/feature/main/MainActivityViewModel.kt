package com.fwhyn.appsample.ui.feature.main

import androidx.lifecycle.ViewModel
import com.fwhyn.data.helper.network.NetworkMonitor
import com.fwhyn.ui.main.MainUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    val networkMonitor: NetworkMonitor,
    val mainUiState: MainUiState,
//    private val memoryUseCase: BaseUseCase<List<ManageMemoryParam>, Any?>,
) : ViewModel() {

//    val mainUiState: MainUiState = MainUiState()

//    init {
//        clearPreviousMemory()
//    }

//    private fun clearPreviousMemory() {
//        mainUiState.showLoading(memoryUseCase.getId())
//        memoryUseCase.setResultNotifier {
//            when (it) {
//                is Rezult.Failure -> mainUiState.showNotification(R.string.cleaning_memory_error)
//                is Rezult.Success -> mainUiState.setIdle()
//            }
//        }.executeOnBackground(listOf(ManageMemoryParam(ManageMemoryAction.CLEAR)), viewModelScope)
//    }
}