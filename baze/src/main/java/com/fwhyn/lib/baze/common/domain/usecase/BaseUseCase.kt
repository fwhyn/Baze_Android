package com.fwhyn.lib.baze.common.domain.usecase

import android.util.Log
import com.fwhyn.lib.baze.common.data.helper.Util
import com.fwhyn.lib.baze.common.data.helper.extension.getDebugTag
import com.fwhyn.lib.baze.common.domain.helper.Rezult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import kotlin.coroutines.CoroutineContext

@Retention(value = AnnotationRetention.BINARY)
@RequiresOptIn(
    level = RequiresOptIn.Level.WARNING,
    message = "Please call super.execute..."
)

annotation class SuppressWarning

// ----------------------------------------------------------------
/**
 * Abstract class representing a base use case for executing business logic.
 * This class provides a structure for handling asynchronous operations with
 * support for lifecycle notifications, result handling, and timeout management.
 *
 * @param <PARAM> The type of the input parameter required to execute the use case.
 * @param <RESULT> The type of the result produced by the use case.
 */
abstract class BaseUseCase<PARAM, RESULT> {

    private val debugTag = BaseUseCase::class.java.getDebugTag()
    protected open var jobId = Util.getUniqueId()

    private var isWithResult: Boolean = true

    private var timeOutMillis: Long = 0

    private var uiContext: CoroutineContext = Dispatchers.Main
    private var workerContext: CoroutineContext = Dispatchers.IO
    protected open var job: Job? = null
        set(value) {
            cancelPreviousActiveJob()
            jobId = Util.getUniqueId()
            field = value
        }

    private var resultNotifier: ((Rezult<RESULT, Throwable>) -> Unit)? = null
    private var lifeCycleNotifier: ((LifeCycle) -> Unit)? = null

    // ----------------------------------------------------------------
    /**
     * Sets whether the use case should return a result.
     *
     * @param isWithResult A boolean indicating if the result should be returned.
     * @return The current instance of the use case.
     */
    fun setWithResult(isWithResult: Boolean): BaseUseCase<PARAM, RESULT> {
        this.isWithResult = isWithResult

        return this
    }

    /**
     * Sets a timeout for the use case execution.
     *
     * @param time Timeout duration in milliseconds.
     * @return The current instance of the use case.
     */
    fun setTimeOutMillis(time: Long): BaseUseCase<PARAM, RESULT> {
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
    fun setUiContext(context: CoroutineContext): BaseUseCase<PARAM, RESULT> {
        uiContext = context

        return this
    }

    /**
     * Sets the coroutine context for worker-related operations.
     *
     * @param context The coroutine context for worker operations.
     * @return The current instance of the use case.
     */
    fun setWorkerContext(context: CoroutineContext): BaseUseCase<PARAM, RESULT> {
        workerContext = context

        return this
    }

    /**
     * Sets the result notifier for the use case.
     *
     * @param resultNotifier A lambda function to handle the result of the use case.
     * @return The current instance of the use case.
     */
    fun setResultNotifier(
        resultNotifier: ((Rezult<RESULT, Throwable>) -> Unit)?,
    ): BaseUseCase<PARAM, RESULT> {
        this.resultNotifier = resultNotifier

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
    ): BaseUseCase<PARAM, RESULT> {
        this.lifeCycleNotifier = lifeCycleNotifier

        return this
    }

    /**
     * Cancels the currently active job, if any.
     *
     * @return The current instance of the use case.
     */
    fun cancelPreviousActiveJob(): BaseUseCase<PARAM, RESULT> {
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

    // ----------------------------------------------------------------
    /**
     * Executes the use case with the given parameter and coroutine scope.
     *
     * @param param The input parameter for the use case.
     * @param scope The coroutine scope for execution.
     */
    fun execute(param: PARAM, scope: CoroutineScope = CoroutineScope(Dispatchers.IO)) {
        runInternally(scope, { onRunning(param) }, isWithResult)
    }

    /**
     * Executes the use case with the given parameter and coroutine scope.
     *
     * @param param The input parameter for the use case.
     */
    protected abstract suspend fun onRunning(param: PARAM): RESULT

    private fun <T> runInternally(
        scope: CoroutineScope,
        runAPi: suspend () -> T,
        isWithResult: Boolean = true,
    ) {
        notifyOnStart()

        job = scope.launch(workerContext + SupervisorJob()) {
            Log.d(debugTag, "Job is launched")

            try {
                val result = if (timeOutMillis > 0) {
                    withTimeout(timeOutMillis) {
                        Log.d(debugTag, "RunApi with Timeout is invoked")
                        runAPi()
                    }
                } else {
                    Log.d(debugTag, "RunApi is invoked")
                    runAPi()
                }

                if (isWithResult) {
                    provideResult(result as RESULT)
                }
            } catch (e: Throwable) {
                notifyResult(Rezult.Failure(e))
            }
        }
    }

    private suspend fun provideResult(result: RESULT) {
        Log.d(debugTag, "Rezult: $result")

        if (result != null) {
            notifyResult(Rezult.Success(result))
        } else {
            notifyResult(Rezult.Failure(Throwable("Result is null")))
        }
    }

    suspend fun notifyResult(result: Rezult<RESULT, Throwable>) {
        withContext(uiContext) {
            resultNotifier?.let { it(result) }
            notifyOnFinish()
        }
    }

    private fun notifyOnStart() = lifeCycleNotifier?.let { it(LifeCycle.OnStart) }
    private fun notifyOnFinish() = lifeCycleNotifier?.let { it(LifeCycle.OnFinish) }

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