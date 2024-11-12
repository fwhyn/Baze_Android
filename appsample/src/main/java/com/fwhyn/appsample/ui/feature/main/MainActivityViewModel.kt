package com.fwhyn.appsample.ui.feature.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fwhyn.data.helper.network.NetworkMonitor
import com.fwhyn.data.model.memory.ManageMemoryAction
import com.fwhyn.data.model.memory.ManageMemoryParam
import com.fwhyn.domain.helper.Rezult
import com.fwhyn.domain.usecase.BaseUseCase
import com.fwhyn.ui.main.MainUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    val networkMonitor: NetworkMonitor,
    private val memoryUseCase: BaseUseCase<List<ManageMemoryParam>, Any?>,
) : ViewModel() {

    val mainUiState: MainUiState = MainUiState()

    init {
        clearPreviousMemory()
    }

    private fun clearPreviousMemory() {
        mainUiState.showLoading(memoryUseCase.getId())
        memoryUseCase.setResultNotifier {
            when (it) {
                is Rezult.Failure -> mainUiState.showNotification("Cleaning Memory Error")
                is Rezult.Success -> mainUiState.setIdle()
            }
        }.executeOnBackground(listOf(ManageMemoryParam(ManageMemoryAction.CLEAR)), viewModelScope)
    }
}