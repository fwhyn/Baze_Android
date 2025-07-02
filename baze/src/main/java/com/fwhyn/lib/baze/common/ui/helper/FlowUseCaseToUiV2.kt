package com.fwhyn.lib.baze.common.ui.helper

import com.fwhyn.lib.baze.common.domain.usecase.FlowUseCaseV2
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

abstract class FlowUseCaseToUiV2<PARAM, RESULT_DOMAIN, RESULT_UI>(
    override val initialValue: RESULT_UI,
    override val useCase: FlowUseCaseV2<PARAM, RESULT_DOMAIN>,
) : UseCaseToUi<PARAM, Flow<RESULT_DOMAIN>, RESULT_UI>(initialValue, useCase) {

    // ----------------------------------------------------------------
    /**
     * Executes the use case with the provided parameter in the given coroutine scope.
     *
     * @param scope The coroutine scope in which to execute the use case.
     * @param param The input parameter for the use case.
     */
    override operator fun invoke(
        scope: CoroutineScope,
        param: PARAM,
    ) {
        job = scope.launch(workerContext) {
            runCatching {
                data.value = UiState.Loading
                useCase(param)
            }.onSuccess { result ->
                result.map { domain ->
                    onCovertData(domain)
                }.collect { ui ->
                    data.value = UiState.Success(ui)
                }

            }.onFailure { error ->
                data.value = UiState.Error(error)
            }
        }
    }

    abstract suspend fun onCovertData(domain: RESULT_DOMAIN): RESULT_UI

    override suspend fun onCovertData(domain: Flow<RESULT_DOMAIN>): RESULT_UI {
        throw UnsupportedOperationException("This method should not be called directly.")
    }
}