package com.fwhyn.lib.baze.common.data.helper.extension

import com.fwhyn.lib.baze.common.domain.helper.Rezult
import com.fwhyn.lib.baze.common.domain.usecase.BaseUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.stateIn
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

// TODO add java doc
fun <PARAM, RESULT> BaseUseCase<PARAM, RESULT>.getStateFlowResult(
    scope: CoroutineScope = CoroutineScope(Dispatchers.IO),
    started: SharingStarted = WhileSubscribed(5_000),
    initialValue: Rezult<RESULT, Throwable>
): StateFlow<Rezult<RESULT, Throwable>> {
    return getFlowResult().stateIn(
        scope = scope,
        started = started,
        initialValue = initialValue,
    )
}

// TODO add java doc
fun <PARAM, RESULT> BaseUseCase<PARAM, RESULT>.getFlowResult(): Flow<Rezult<RESULT, Throwable>> = callbackFlow {
    setResultNotifier {
        trySend(it)
    }

    awaitClose {
        setResultNotifier(null)
    }
}