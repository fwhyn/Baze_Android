package com.fwhyn.lib.baze.compose.model

import com.fwhyn.lib.baze.common.model.PairData
import com.fwhyn.lib.baze.common.model.remove
import com.fwhyn.lib.baze.common.model.set
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

data class CommonProperties(
    val event: SharedFlow<CommonEvent>? = null,
) {

    companion object {
        private const val TAG_IDLE = "TAG_IDLE"
    }

    private val _state: MutableStateFlow<CommonState> = MutableStateFlow(CommonState.Idle)
    val state: StateFlow<CommonState> = _state

    private val idleSate: List<PairData<String, CommonState>> = listOf(PairData(TAG_IDLE, CommonState.Idle))
    private var stateManager: List<PairData<String, CommonState>> = idleSate
        set(value) {
            field = value
            _state.value = field.last().second
        }

    // ----------------------------------------------------------------
    fun <T, D> showDialog(tag: T, dat: D) {
        val key = tag.toString()
        val data: PairData<String, CommonState> = PairData(key, CommonState.Dialog(key, dat))
        stateManager = stateManager.set(data)
    }

    fun <T> dismissDialog(tag: T) {
        stateManager = stateManager.remove(tag.toString())
    }

    fun setIdle() {
        stateManager = idleSate
    }
}