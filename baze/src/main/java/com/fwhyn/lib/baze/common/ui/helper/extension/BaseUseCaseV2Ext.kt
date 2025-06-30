package com.fwhyn.lib.baze.common.ui.helper.extension

import com.fwhyn.lib.baze.common.domain.usecase.BaseUseCaseV2
import com.fwhyn.lib.baze.common.ui.helper.UiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

fun <PARAM, RESULT> BaseUseCaseV2<PARAM, RESULT>.toUiState(
    scope: CoroutineScope,
    param: PARAM,
): Flow<UiState<RESULT>> {
    val flowData: MutableStateFlow<UiState<RESULT>> = MutableStateFlow(UiState.Loading)
    invoke(scope, param)

    return flowData
}