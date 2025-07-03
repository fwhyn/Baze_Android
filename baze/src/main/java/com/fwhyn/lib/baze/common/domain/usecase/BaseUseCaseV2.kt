package com.fwhyn.lib.baze.common.domain.usecase

import android.util.Log
import com.fwhyn.lib.baze.common.data.helper.Util
import com.fwhyn.lib.baze.common.data.helper.extension.getDebugTag
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import kotlin.coroutines.CoroutineContext

// ----------------------------------------------------------------
/**
 * Abstract class representing a base use case for executing business logic.
 * This class provides a structure for handling asynchronous operations with
 * support for lifecycle notifications, result handling, and timeout management.
 *
 * @param <PARAM> The type of the input parameter required to execute the use case.
 * @param <RESULT> The type of the result produced by the use case.
 */
abstract class BaseUseCaseV2<PARAM, RESULT> {

    private val debugTag = BaseUseCaseV2::class.java.getDebugTag()

    protected open var jobId = Util.getUniqueId()
    private var timeOutMillis: Long = 0

    private var uiContext: CoroutineContext = Dispatchers.Main
    private var workerContext: CoroutineContext = Dispatchers.Default

    protected open var job: Job? = null
        set(value) {
            cancelPreviousActiveJob()
            jobId = Util.getUniqueId()
            field = value
        }

    private var resultNotifier: ((Result<RESULT>) -> Unit)? = null
    private var lifeCycleNotifier: ((LifeCycle) -> Unit)? = null

    // ----------------------------------------------------------------
    /**
     * Sets a timeout for the use case execution.
     *
     * @param time Timeout duration in milliseconds.
     * @return The current instance of the use case.
     */
    fun setTimeOutMillis(time: Long): BaseUseCaseV2<PARAM, RESULT> {
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
     * Sets the coroutine context for UI-related operations.
     *
     * @param context The coroutine context for UI operations.
     * @return The current instance of the use case.
     */
    fun setUiContext(context: CoroutineContext): BaseUseCaseV2<PARAM, RESULT> {
        uiContext = context

        return this
    }

    /**
     * Sets the coroutine context for worker-related operations.
     *
     * @param context The coroutine context for worker operations.
     * @return The current instance of the use case.
     */
    fun setWorkerContext(context: CoroutineContext): BaseUseCaseV2<PARAM, RESULT> {
        workerContext = context

        return this
    }

    /**
     * Sets the result notifier for the use case.
     *
     * @param result A lambda function to handle the result of the use case.
     * @return The current instance of the use case.
     */
    fun setResultNotifier(
        result: ((Result<RESULT>) -> Unit)?,
    ): BaseUseCaseV2<PARAM, RESULT> {
        this.resultNotifier = result

        return this
    }

    /**
     * Sets the lifecycle notifier for the use case.
     *
     * @param lifeCycleNotifier A lambda function to handle lifecycle events.
     * @return The current instance of the use case.
     */
    fun setLifeCycleNotifier(
        lifeCycleNotifier: (LifeCycle) -> Unit,
    ): BaseUseCaseV2<PARAM, RESULT> {
        this.lifeCycleNotifier = lifeCycleNotifier

        return this
    }

    /**
     * Cancels the currently active job, if any.
     *
     * @return The current instance of the use case.
     */
    fun cancelPreviousActiveJob(): BaseUseCaseV2<PARAM, RESULT> {
        if (job?.isActive == true) {
            Log.d(debugTag, "Cancelling job: $job")
            job?.cancel()
        }

        return this
    }

    /**
     * Waits for the current job to complete.
     */
    suspend fun join() = job?.join()

    /**
     * Notifies the result notifier of an error.
     *
     * @param error The error that occurred during execution.
     */
    protected fun notifyOnError(error: Throwable) = resultNotifier?.invoke(Result.failure(error))

    /**
     * Notifies the result notifier of a successful result.
     *
     * @param result The successful result of the use case execution.
     */
    protected fun notifyOnSuccess(result: RESULT) = resultNotifier?.invoke(Result.success(result))

    /**
     * Executes the use case with the given parameter and coroutine scope.
     *
     * @param param The input parameter for the use case.
     * @param scope The coroutine scope for execution.
     */
    operator fun invoke(
        scope: CoroutineScope = CoroutineScope(workerContext),
        param: PARAM
    ) {
        job = scope.launch(workerContext + SupervisorJob()) {
            Log.d(debugTag, "Job is launched")

            notifyOnStart()
            val result = runCatching {
                if (timeOutMillis > 0) {
                    withTimeout(timeOutMillis) {
                        onRunning(param)
                    }
                } else {
                    onRunning(param)
                }
            }
            resultNotifier?.invoke(result)
            notifyOnFinish()
        }
    }

    /**
     * Executes the use case with the given parameter and coroutine scope.
     *
     * @param param The input parameter for the use case.
     */
    protected abstract suspend fun onRunning(param: PARAM): RESULT

    private fun notifyOnStart() = lifeCycleNotifier?.invoke(LifeCycle.OnStart)
    private fun notifyOnFinish() = lifeCycleNotifier?.invoke(LifeCycle.OnFinish)

    // ----------------------------------------------------------------
    /**
     * Enum representing the lifecycle states of the use case execution.
     */
    enum class LifeCycle {
        /**
         * Indicates that the use case execution has started.
         */
        OnStart,

        /**
         * Indicates that the use case execution has finished.
         */
        OnFinish,
    }
}