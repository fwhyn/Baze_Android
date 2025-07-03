package com.fwhyn.lib.baze.common.ui.helper

import com.fwhyn.lib.baze.common.domain.usecase.UseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UseCaseToUi<RESULT_UI>(
    private val scope: CoroutineScope,
    initialValue: RESULT_UI,
) : JobManager() {

    private val _data: MutableStateFlow<UiState<RESULT_UI>> = MutableStateFlow(UiState.Success(initialValue))
    val data: StateFlow<UiState<RESULT_UI>>
        get() = _data

    // ----------------------------------------------------------------
    /**
     * Executes the use case with the provided parameter in the given coroutine scope.
     *
     * @param scope The coroutine scope in which to execute the use case.
     * @param param The input parameter for the use case.
     */
    operator fun <PARAM, RESULT_DOMAIN> invoke(
        useCase: UseCase<PARAM, RESULT_DOMAIN>,
        param: PARAM,
        onCovertData: (domain: RESULT_DOMAIN) -> RESULT_UI
    ) {
        job = scope.launch(workerContext) {
            runCatching {
                _data.value = UiState.Loading
                useCase(param)
            }.onSuccess { domain ->
                val ui = onCovertData(domain)
                _data.value = UiState.Success(ui)
            }.onFailure { error ->
                _data.value = UiState.Error(error)
            }
        }
    }
}