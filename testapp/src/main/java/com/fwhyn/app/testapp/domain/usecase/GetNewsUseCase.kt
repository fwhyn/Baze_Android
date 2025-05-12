package com.fwhyn.app.testapp.domain.usecase

import com.fwhyn.app.testapp.domain.helper.Result
import com.fwhyn.app.testapp.domain.model.GetNewsParam
import com.fwhyn.app.testapp.domain.model.News
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow

interface GetNewsUseCase {
    fun execute(
        scope: CoroutineScope,
        param: GetNewsParam,
    ): StateFlow<Result<List<News>>>
}