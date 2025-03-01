package com.fwhyn.baze.domain.usecase

import com.fwhyn.baze.data.model.Exzeption
import com.fwhyn.baze.data.model.Status
import kotlinx.coroutines.CoroutineScope
import retrofit2.HttpException

abstract class BaseUseCaseRemote<PARAM, RESULT> : BaseUseCase<PARAM, RESULT>() {

    override fun runWithResult(
        scope: CoroutineScope,
        runAPi: suspend () -> RESULT,
    ) {
        super.runWithResult(scope) {
            try {
                runAPi()
            } catch (e: HttpException) {
                throw Exzeption(Status.Instance(e.code(), e.cause?.message ?: e.message()), e)
            }
        }
    }
}