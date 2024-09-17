package com.fwhyn.domain.usecase

import android.util.Log
import com.fwhyn.data.helper.getTestTag
import com.fwhyn.data.model.BazeException
import com.fwhyn.domain.helper.BazeResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout

abstract class BaseUseCase<PARAM, RESULT> {

    private val debugTag = BaseUseCase::class.java.getTestTag()

    private var timeOut: Long = 0 // millisecond

    private var resultNotifier: ((BazeResult<RESULT, BazeException>) -> Unit)? = null

    private var job: Job? = null

    // ----------------------------------------------------------------
    fun setResultNotifier(
        resultNotifier: (BazeResult<RESULT, BazeException>) -> Unit,
    ): BaseUseCase<PARAM, RESULT> {
        this.resultNotifier = resultNotifier

        return this
    }

    protected fun setTimeOut(time: Long): BaseUseCase<PARAM, RESULT> {
        timeOut = time

        return this
    }

    open fun executeOnBackground(
        param: PARAM,
        scope: CoroutineScope,
    ) {
    }

    open fun execute(
        param: PARAM,
    ) {
    }

    protected open fun runWithResult(
        scope: CoroutineScope,
        runAPi: suspend () -> RESULT,
    ) {
        runInternally(scope, runAPi)
    }

    protected open fun run(
        scope: CoroutineScope,
        runAPi: suspend () -> Unit,
    ) {
        runInternally(scope, runAPi, false)
    }

    private fun <T> runInternally(
        scope: CoroutineScope,
        runAPi: suspend () -> T,
        withResult: Boolean = true,
    ) {
        if (job?.isActive == true) {
            Log.d(debugTag, "Job Canceled")
            job?.cancel()
        }

        job = scope.launch(Dispatchers.IO + SupervisorJob()) {
            Log.d(debugTag, "Job Launched")

            try {
                val result = if (timeOut > 0) {
                    withTimeout(timeOut) {
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
            } catch (e: BazeException) {
                Log.d(debugTag, "BazeException ${e.status.msg} ${e.status.code} ${e.throwable?.message}")
                notifyResultFromBackground(
                    BazeResult.Failure(e)
                )
            } catch (e: Exception) {
                Log.d(debugTag, "Exception ${e.message}")
                notifyResultFromBackground(
                    BazeResult.Failure(BazeException(throwable = e))
                )
            }
        }
    }

    private suspend fun provideResultFromBackground(result: RESULT) {
        if (result != null) {
            Log.d(debugTag, "Result Exist")
            notifyResultFromBackground(
                BazeResult.Success(result)
            )
        } else {
            Log.d(debugTag, "Result Null")
            notifyResultFromBackground(
                BazeResult.Failure(BazeException())
            )
        }
    }

    protected open fun runWithResult(runAPi: () -> RESULT) = runInternally(runAPi)

    protected open fun run(runAPi: () -> Unit) = runInternally(runAPi, false)

    private fun <T> runInternally(
        runAPi: () -> T,
        withResult: Boolean = true,
    ) {
        try {
            Log.d(debugTag, "RunApi Invoked")
            val result = runAPi()
            if (withResult) {
                provideResult(result as RESULT)
            }
        } catch (e: BazeException) {
            Log.d(debugTag, "BazeException ${e.status.msg} ${e.status.code} ${e.throwable?.message}")
            notifyResult(
                BazeResult.Failure(e)
            )
        } catch (e: Exception) {
            Log.d(debugTag, "Exception ${e.message}")
            notifyResult(
                BazeResult.Failure(BazeException(throwable = e))
            )
        }
    }

    private fun provideResult(result: RESULT) {
        if (result != null) {
            notifyResult(
                BazeResult.Success(result)
            )
        } else {
            notifyResult(
                BazeResult.Failure(BazeException())
            )
        }
    }

    suspend fun notifyResultFromBackground(
        result: BazeResult<RESULT, BazeException>,
    ) {
        withContext(Dispatchers.Main) {
            notifyResult(result)
        }
    }

    fun notifyResult(
        result: BazeResult<RESULT, BazeException>,
    ) {
        resultNotifier?.let { it(result) }
    }

    fun cancel() {
        job?.cancel()
    }
}