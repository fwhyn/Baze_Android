package com.fwhyn.lib.baze.common.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withTimeout

// ----------------------------------------------------------------
/**
 * Abstract class representing a base use case for executing business logic.
 * This class provides a structure for handling asynchronous operations with
 * support for lifecycle notifications, result handling, and timeout management.
 *
 * @param <PARAM> The type of the input parameter required to execute the use case.
 * @param <RESULT> The type of the result produced by the use case.
 */
abstract class FlowUseCase<PARAM, RESULT> {

    private var timeOutMillis: Long = 0

    // ----------------------------------------------------------------
    /**
     * Sets a timeout for the use case execution.
     *
     * @param time Timeout duration in milliseconds.
     * @return The current instance of the use case.
     */
    fun setTimeOutMillis(time: Long): FlowUseCase<PARAM, RESULT> {
        timeOutMillis = time

        return this
    }

    // ----------------------------------------------------------------
    /**
     * Executes the use case with the given parameter and coroutine scope.
     *
     * @param param The input parameter for the use case.
     */
    suspend operator fun invoke(param: PARAM): Flow<RESULT> {
        return return if (timeOutMillis > 0) {
            withTimeout(timeOutMillis) {
                onRunning(param)
            }
        } else {
            onRunning(param)
        }
    }

    /**
     * Executes the use case with the given parameter and coroutine scope.
     *
     * @param param The input parameter for the use case.
     */
    protected abstract suspend fun onRunning(param: PARAM): Flow<RESULT>
}