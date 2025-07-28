package com.fwhyn.lib.baze.common.helper

interface RunnerLifecycle<PARAM, RESULT> {
    fun onStart() {}
    suspend fun onFetchParam(): PARAM
    suspend fun onEmitResult(result: Result<RESULT>) {}
    fun onFinish() {}
}