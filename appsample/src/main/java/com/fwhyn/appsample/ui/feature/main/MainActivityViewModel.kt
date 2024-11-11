package com.fwhyn.atmsehat.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fwhyn.data.helper.network.NetworkMonitor
import com.fwhyn.data.model.ManageMemoryAction
import com.fwhyn.data.model.ManageMemoryParam
import com.fwhyn.domain.helper.Results
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
        memoryUseCase.setResultNotifier {
            when (it) {
                is Results.Failure -> mainUiState.showNotification("Cleaning Memory Error")
                is Results.Loading -> mainUiState.showLoading()
                is Results.Success -> mainUiState.setIdle()
            }
        }.executeOnBackground(listOf(ManageMemoryParam(ManageMemoryAction.CLEAR)), viewModelScope)
    }
}