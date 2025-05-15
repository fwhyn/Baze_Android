package com.fwhyn.lib.baze.common.data.helper.extension

import com.fwhyn.lib.baze.common.data.helper.Util.throwExceptionIfBelowZero
import com.fwhyn.lib.baze.common.domain.helper.Rezult
import com.fwhyn.lib.baze.common.domain.usecase.BaseUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine

/**
 * Extension function to get the result of the use case in a coroutine.
 *
 * @param param The input parameter for the use case.
 * @param scope The coroutine scope for execution.
 * @return The result of the use case.
 * @throws Throwable if the execution fails.
 */
@OptIn(ExperimentalCoroutinesApi::class)
suspend fun <PARAM, RESULT> BaseUseCase<PARAM, RESULT>.getResult(
    param: PARAM,
    scope: CoroutineScope = CoroutineScope(Dispatchers.IO),
): RESULT = suspendCancellableCoroutine { continuation ->
    setResultNotifier {
        when (it) {
            is Rezult.Failure -> throw it.err
            is Rezult.Success -> continuation.continueIfActive(it.dat)
        }
    }
    execute(param, scope)
}

/**
 * Extension function to get the result of the use case as a SharedFlow.
 *
 * @param replay The number of values to replay to new subscribers.
 * @return A SharedFlow containing the result of the use case.
 */
fun <PARAM, RESULT> BaseUseCase<PARAM, RESULT>.getSharedFlowResult(
    replay: Int = 1
): SharedFlow<Rezult<RESULT, Throwable>> {
    throwExceptionIfBelowZero(replay)

    val mutableSharedFlow = MutableSharedFlow<Rezult<RESULT, Throwable>>(replay)
    setResultNotifier { mutableSharedFlow.tryEmit(it) }

    return mutableSharedFlow
}

/**
 * Extension function to get the result of the use case as a StateFlow.
 *
 * @param initialValue The initial value of the StateFlow.
 * @return A StateFlow containing the result of the use case.
 */
fun <PARAM, RESULT> BaseUseCase<PARAM, RESULT>.getStateFlowResult(
    initialValue: Rezult<RESULT, Throwable>
): StateFlow<Rezult<RESULT, Throwable>> {
    val mutableStateFlow = MutableStateFlow(initialValue)
    setResultNotifier { mutableStateFlow.tryEmit(it) }

    return mutableStateFlow
}

/**
 * Extension function to get the result of the use case as a Flow.
 *
 * @return A Flow containing the result of the use case.
 */
fun <PARAM, RESULT> BaseUseCase<PARAM, RESULT>.getFlowResult(): Flow<Rezult<RESULT, Throwable>> = callbackFlow {
    setResultNotifier {
        trySend(it)
    }

    awaitClose {
        setResultNotifier(null)
    }
}