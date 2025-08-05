package com.fwhyn.lib.baze.common.helper

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import java.util.concurrent.atomic.AtomicReference
import kotlin.coroutines.CoroutineContext

/**
 * Abstract class representing a base use case for executing business logic.
 * This class provides a structure for handling asynchronous operations with
 * support for lifecycle notifications, result handling, and timeout management.
 *
 * @param <PARAM> The type of the input parameter required to execute the use case.
 * @param <RESULT> The type of the result produced by the use case.
 */
abstract class BaseRunner<PARAM, RESULT> {

    protected open var jobId = Util.getUniqueId()

    @Volatile
    private var timeOutMillis: Long = 0

    private var isForcedCancelPreviousActiveJob = false

    private var uiContext: CoroutineContext = Dispatchers.Main
    private var workerContext: CoroutineContext = Dispatchers.Default

    private val jobRef = AtomicReference<Job?>()
    protected open var job: Job?
        get() = jobRef.get()
        set(value) {
            cancelPreviousActiveJob()
            jobId = Util.getUniqueId()
            jobRef.set(value)
        }

    // ----------------------------------------------------------------
    /**
     * Sets a timeout for the use case execution.
     *
     * @param time Timeout duration in milliseconds.
     * @return The current instance of the use case.
     */
    fun setTimeOutMillis(time: Long): BaseRunner<PARAM, RESULT> {
        timeOutMillis = time

        return this
    }

    /**
     * Retrieves the unique identifier of the current job.
     *
     * @return The unique job identifier.
     */
    fun getId(): String = jobId

    /**
     * Sets the coroutine context for worker-related operations.
     *
     * @param context The coroutine context for worker operations.
     * @return The current instance of the use case.
     */
    fun setWorkerContext(context: CoroutineContext): BaseRunner<PARAM, RESULT> {
        workerContext = context

        return this
    }

    /**
     * Sets the coroutine context for UI-related operations.
     *
     * @param context The coroutine context for UI operations.
     * @return The current instance of the use case.
     */
    fun setUiContext(context: CoroutineContext): BaseRunner<PARAM, RESULT> {
        uiContext = context

        return this
    }

    /**
     * Cancels the currently active job, if any.
     *
     * @return The current instance of the use case.
     */
    fun cancelPreviousActiveJob(): BaseRunner<PARAM, RESULT> {
        if (job?.isActive == true) {
            job?.cancel()
        }

        return this
    }

    /**
     * Sets whether to forcibly cancel the previous active job when a new job is started.
     *
     * @param value If true, the previous active job will be canceled when a new job is invoked.
     * @return The current instance of the use case.
     */
    fun setForcedCancelPreviousActiveJob(value: Boolean): BaseRunner<PARAM, RESULT> {
        isForcedCancelPreviousActiveJob = value

        return this
    }

    /**
     * Waits for the current job to complete.
     */
    suspend fun join() = job?.join()

    /**
     * Executes the use case with the provided parameter and coroutine scope.
     *
     * @param scope The coroutine scope in which to run the use case.
     * @param onStart A function to be called when the use case starts.
     * @param onFetchParam A suspend function that provides the input parameter for the use case.
     * @param onOmitResult A suspend function to handle the result of the use case execution.
     * @param onFinish A function to be called when the use case finishes execution.
     */
    operator fun invoke(
        scope: CoroutineScope,
        onStart: () -> Unit = {},
        onFetchParam: suspend () -> PARAM,
        onOmitResult: suspend (Result<RESULT>) -> Unit = {},
        onFinish: () -> Unit = {},
    ) {
        if (!isForcedCancelPreviousActiveJob && job?.isActive == true) return

        job = scope.launch(workerContext + SupervisorJob()) {
            withContext(uiContext) { onStart() }

            runCatching {
                val param: PARAM = onFetchParam()
                val block: suspend () -> Unit = {
                    onRunning(param) {
                        onOmitResult(Result.success(it))
                        withContext(uiContext) { onFinish() }
                    }
                }

                if (timeOutMillis > 0) {
                    withTimeout(timeOutMillis) { block() }
                } else {
                    block()
                }
            }.onFailure { error ->
                onOmitResult(Result.failure(error))
                withContext(uiContext) { onFinish() }
            }
        }
    }

    /**
     * Abstract method to be implemented by subclasses to define the business logic
     * that will be executed when the use case is invoked.
     *
     * @param param The input parameter required for the use case execution.
     * @param result A suspend function to handle the result of the use case execution.
     */
    protected abstract suspend fun onRunning(
        param: PARAM,
        result: suspend (RESULT) -> Unit,
    )
}