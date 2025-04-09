package com.fwhyn.baze.domain.usecase

import android.util.Log
import com.fwhyn.baze.data.helper.Util
import com.fwhyn.baze.data.helper.extension.getTestTag
import com.fwhyn.baze.domain.helper.Rezult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
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
abstract class BaseUseCase<PARAM, RESULT> {

    private val debugTag = BaseUseCase::class.java.getTestTag()
    protected open var jobId = Util.getUniqueId()

    private var timeOutMillis: Long = 0

    private var resultNotifier: ((Rezult<RESULT, Throwable>) -> Unit)? = null
    private var lifeCycleNotifier: ((LifeCycle) -> Unit)? = null

    private var uiContext: CoroutineContext = Dispatchers.Main
    private var workerContext: CoroutineContext = Dispatchers.IO

    private var job: Job? = null

    // ----------------------------------------------------------------
    fun setResultNotifier(
        resultNotifier: (Rezult<RESULT, Throwable>) -> Unit,
    ): BaseUseCase<PARAM, RESULT> {
        this.resultNotifier = resultNotifier

        return this
    }

    fun setLifeCycleNotifier(
        lifeCycleNotifier: (LifeCycle) -> Unit,
    ): BaseUseCase<PARAM, RESULT> {
        this.lifeCycleNotifier = lifeCycleNotifier

        return this
    }

    protected fun setTimeOut(time: Long): BaseUseCase<PARAM, RESULT> {
        timeOutMillis = time

        return this
    }

    fun cancelPreviousActiveJob(): BaseUseCase<PARAM, RESULT> {
        if (job?.isActive == true) {
            Log.d(debugTag, "Cancel Job: $job")
            job?.cancel()

            jobId = Util.getUniqueId()
        }

        return this
    }

    fun getId(): String = jobId

    fun setUiContext(context: CoroutineContext): BaseUseCase<PARAM, RESULT> {
        uiContext = context

        return this
    }

    fun setWorkerContext(context: CoroutineContext): BaseUseCase<PARAM, RESULT> {
        workerContext = context

        return this
    }

    fun cancel() = job?.cancel()

    suspend fun join() = job?.join()

    // ----------------------------------------------------------------
    open fun execute(param: PARAM, scope: CoroutineScope = CoroutineScope(Dispatchers.IO)) {}

    protected open fun runWithResult(scope: CoroutineScope, runAPi: suspend () -> RESULT) = runInternally(scope, runAPi)

    protected open fun run(scope: CoroutineScope, runAPi: suspend () -> Unit) = runInternally(scope, runAPi, false)

    private fun <T> runInternally(
        scope: CoroutineScope,
        runAPi: suspend () -> T,
        withResult: Boolean = true,
    ) {
        notifyOnStart()

        job = scope.launch(workerContext + SupervisorJob()) {
            Log.d(debugTag, "Job Launched")

            try {
                val result = if (timeOutMillis > 0) {
                    withTimeout(timeOutMillis) {
                        Log.d(debugTag, "RunApi with Timeout Invoked")
                        runAPi()
                    }
                } else {
                    Log.d(debugTag, "RunApi Invoked")
                    runAPi()
                }

                if (withResult) {
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

    private suspend fun notifyResult(result: Rezult<RESULT, Throwable>) {
        withContext(uiContext) {
            resultNotifier?.let { it(result) }
            notifyOnFinish()
        }
    }

    private fun notifyOnStart() = lifeCycleNotifier?.let { it(LifeCycle.OnStart) }
    private fun notifyOnFinish() = lifeCycleNotifier?.let { it(LifeCycle.OnFinish) }

    // ----------------------------------------------------------------
    enum class LifeCycle {
        OnStart,
        OnFinish,
    }
}

// ----------------------------------------------------------------
@OptIn(ExperimentalCoroutinesApi::class)
suspend fun <PARAM, RESULT> BaseUseCase<PARAM, RESULT>.getResult(
    param: PARAM,
    scope: CoroutineScope = CoroutineScope(Dispatchers.IO),
): RESULT = suspendCancellableCoroutine { continuation ->
    setResultNotifier {
        when (it) {
            is Rezult.Failure -> throw it.err
            is Rezult.Success -> {
                if (continuation.isActive) {
                    continuation.resume(it.dat) { error ->
                        throw error
                    }
                }
            }
        }
    }
    execute(param, scope)
}