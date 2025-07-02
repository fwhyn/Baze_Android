package com.fwhyn.lib.baze.common.ui.helper

import com.fwhyn.lib.baze.common.data.helper.Util
import com.fwhyn.lib.baze.common.domain.usecase.BaseUseCaseV2
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class UseCaseToUi<PARAM, RESULT_DOMAIN, RESULT_UI>(
    initialValue: RESULT_UI,
    private val useCase: BaseUseCaseV2<PARAM, RESULT_DOMAIN>,
    private val onCovertData: (RESULT_DOMAIN) -> RESULT_UI
) {
    val data: MutableStateFlow<UiState<RESULT_UI>> = MutableStateFlow(UiState.Success(initialValue))

    private var jobId = Util.getUniqueId()
    private var mainContext: CoroutineContext = Dispatchers.Main
    private var workerContext: CoroutineContext = Dispatchers.Default
    private var job: Job? = null
        set(value) {
            cancelPreviousActiveJob()
            jobId = Util.getUniqueId()
            field = value
        }

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

    /**
     * Retrieves the unique identifier of the current job.
     *
     * @return The unique job identifier.
     */
    fun getId(): String = jobId

    /**
     * Sets the coroutine context for UI-related operations.
     *
     * @param context The coroutine context for UI operations.
     * @return The current instance of the use case.
     */
    fun setUiContext(context: CoroutineContext): UseCaseToUi<PARAM, RESULT_DOMAIN, RESULT_UI> {
        mainContext = context

        return this
    }

    /**
     * Sets the coroutine context for worker-related operations.
     *
     * @param context The coroutine context for worker operations.
     * @return The current instance of the use case.
     */
    fun setWorkerContext(context: CoroutineContext): UseCaseToUi<PARAM, RESULT_DOMAIN, RESULT_UI> {
        workerContext = context

        return this
    }

    /**
     * Cancels the currently active job, if any.
     *
     * @return The current instance of the use case.
     */
    fun cancelPreviousActiveJob(): UseCaseToUi<PARAM, RESULT_DOMAIN, RESULT_UI> {
        if (job?.isActive == true) {
            job?.cancel()
        }

        return this
    }

    /**
     * Waits for the current job to complete.
     */
    suspend fun join() = job?.join()
}