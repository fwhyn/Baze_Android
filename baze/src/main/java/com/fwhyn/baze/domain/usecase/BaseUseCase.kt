package com.fwhyn.baze.domain.usecase

import android.util.Log
import com.fwhyn.baze.data.helper.Util
import com.fwhyn.baze.data.helper.extension.getTestTag
import com.fwhyn.baze.data.model.Exzeption
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

    private var resultNotifier: ((Rezult<RESULT, Exzeption>) -> Unit)? = null
    private var lifeCycleNotifier: ((LifeCycle) -> Unit)? = null

    private var uiContext: CoroutineContext = Dispatchers.Main
    private var workerContext: CoroutineContext = Dispatchers.IO

    private var job: Job? = null

    // ----------------------------------------------------------------
    fun setResultNotifier(
        resultNotifier: (Rezult<RESULT, Exzeption>) -> Unit,
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

    fun getId(): String {
        return jobId
    }

    fun setUiContext(context: CoroutineContext): BaseUseCase<PARAM, RESULT> {
        uiContext = context

        return this
    }

    fun setWorkerContext(context: CoroutineContext): BaseUseCase<PARAM, RESULT> {
        workerContext = context

        return this
    }

    // ----------------------------------------------------------------
    open fun executeOnBackground(param: PARAM, scope: CoroutineScope) {}
    open fun execute(param: PARAM) {}

    protected open fun runWithResult(scope: CoroutineScope, runAPi: suspend () -> RESULT) = runInternally(scope, runAPi)
    protected fun runWithResult(runAPi: () -> RESULT) = runInternally(runAPi)

    protected open fun run(scope: CoroutineScope, runAPi: suspend () -> Unit) = runInternally(scope, runAPi, false)
    protected fun run(runAPi: () -> Unit) = runInternally(runAPi, false)

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
                    provideResultFromBackground(result as RESULT)
                }
            } catch (e: Throwable) {
                handleExceptionFromBackground(e)
            }
        }
    }

    private fun <T> runInternally(
        runAPi: () -> T,
        withResult: Boolean = true,
    ) {
        notifyOnStart()

        try {
            Log.d(debugTag, "RunApi Invoked")
            val result = runAPi()
            if (withResult) {
                provideResult(result as RESULT)
            }
        } catch (e: Throwable) {
            handleException(e)
        }
    }

    private suspend fun handleExceptionFromBackground(e: Throwable) {
        withContext(uiContext) {
            handleException(e)
        }
    }

    private fun handleException(e: Throwable) {
        when (e) {
            is Exzeption -> {
                Log.d(debugTag, "Exzeption ${e.status.msg} ${e.status.code} ${e.throwable?.message}")
                notifyResult(Rezult.Failure(e))
            }

            is Exception -> {
                Log.d(debugTag, "Exception ${e.message}")
                notifyResult(Rezult.Failure(Exzeption(throwable = e)))
            }

            else -> {
                Log.d(debugTag, "Throwable ${e.message}")
                notifyResult(Rezult.Failure(Exzeption(throwable = e)))
            }
        }
    }

    private suspend fun provideResultFromBackground(result: RESULT) {
        withContext(uiContext) {
            provideResult(result)
        }
    }

    private fun provideResult(result: RESULT) {
        Log.d(debugTag, "Rezult: $result")

        if (result != null) {
            notifyResult(
                Rezult.Success(result)
            )
        } else {
            notifyResult(
                Rezult.Failure(Exzeption())
            )
        }
    }

    fun notifyResult(
        result: Rezult<RESULT, Exzeption>,
    ) {
        resultNotifier?.let { it(result) }
        notifyOnFinish()
    }

    private fun notifyOnStart() {
        lifeCycleNotifier?.let { it(LifeCycle.OnStart) }
    }

    private fun notifyOnFinish() {
        lifeCycleNotifier?.let { it(LifeCycle.OnFinish) }
    }

    fun cancel() {
        job?.cancel()
    }

    suspend fun join() {
        job?.join()
    }

    // ----------------------------------------------------------------
    enum class LifeCycle {
        OnStart,
        OnFinish,
    }
}

// ----------------------------------------------------------------
@OptIn(ExperimentalCoroutinesApi::class)
suspend fun <PARAM, RESULT> BaseUseCase<PARAM, RESULT>.getResultInBackground(
    param: PARAM,
    scope: CoroutineScope,
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
    executeOnBackground(param, scope)
}