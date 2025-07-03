package com.fwhyn.lib.baze.common.ui.helper

import com.fwhyn.lib.baze.common.domain.usecase.UseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class UseCaseToUi<PARAM, RESULT_DOMAIN, RESULT_UI>(
    initialValue: RESULT_UI,
    private val useCase: UseCase<PARAM, RESULT_DOMAIN>,
    private val onCovertData: (domain: RESULT_DOMAIN) -> RESULT_UI
) : JobManager() {

    val data: MutableStateFlow<UiState<RESULT_UI>> = MutableStateFlow(UiState.Success(initialValue))

    // ----------------------------------------------------------------
    /**
     * Executes the use case with the provided parameter in the given coroutine scope.
     *
     * @param scope The coroutine scope in which to execute the use case.
     * @param param The input parameter for the use case.
     */
    operator fun invoke(
        scope: CoroutineScope,
        param: PARAM,
    ) {
        job = scope.launch(workerContext) {
            runCatching {
                data.value = UiState.Loading
                useCase(param)
            }.onSuccess { domain ->
                val ui = onCovertData(domain)
                data.value = UiState.Success(ui)
            }.onFailure { error ->
                data.value = UiState.Error(error)
            }
        }
    }
}