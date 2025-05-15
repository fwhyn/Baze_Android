package com.fwhyn.lib.baze.common.ui.main

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.fwhyn.lib.baze.common.data.helper.Util
import com.fwhyn.lib.baze.common.data.helper.extension.clear
import com.fwhyn.lib.baze.common.data.helper.extension.copy
import com.fwhyn.lib.baze.common.data.helper.extension.getDebugTag
import com.fwhyn.lib.baze.common.data.model.PairData
import com.fwhyn.lib.baze.common.data.model.remove
import com.fwhyn.lib.baze.common.data.model.set
import com.fwhyn.lib.baze.common.domain.helper.SingleEvent
import com.fwhyn.lib.baze.common.ui.model.DialogModel

class ActivityRetainedState {

    companion object {
        private val debugTag = ActivityRetainedState::class.java.getDebugTag()
        private const val TAG_NOTIFICATION = "TAG_NOTIFICATION"
    }

    private val tagLoading: ArrayList<String> = arrayListOf()

    var state: State by mutableStateOf(State.Idle)
        private set

    var stateManager: List<PairData<String, State>> = listOf()
        private set

    fun showNotification(@StringRes messageId: Int) {
        setState(TAG_NOTIFICATION, State.OnNotification { isFinish ->
            if (isFinish) {
                removeState(TAG_NOTIFICATION)
            }

            messageId
        })
    }

    fun <T, D> showDialog(tag: T, model: DialogModel, dat: D) {
        setState(tag.toString(), State.OnShowDialog(tag.toString(), model, dat))
    }

    fun <T> dismissDialog(tag: T) {
        removeState(tag.toString())
    }

    fun showLoading(tag: String = Util.getUniqueId(), progress: Int? = null) {
        tagLoading.add(tag)
        setState(tag, State.Loading(progress), true)
    }

    // remove all leading if dismissAll is true, event the tag is not null or not empty
    fun dismissLoading(tag: String? = null, dismissAll: Boolean = false) {
        if (dismissAll) {
            removeState(tagLoading.copy())
            tagLoading.clear()
        } else {
            var removalTag = tag

            if (tagLoading.isNotEmpty() && removalTag == null) {
                removalTag = tagLoading.removeAt(0)
            }

            removalTag?.let { removeState(it) }
        }
    }

    fun setIdle() {
        setState("IDLE", State.Idle)
    }

    fun finish() {
        setState("FINISH", State.Idle)
    }

    private fun setState(key: String, state: State, forceSet: Boolean = false) {
        Log.d(debugTag, "tag: $key")

        if (state == State.Idle || state == State.OnFinish) {
            clearState()
        }

        val data = PairData(key, state)
        stateManager = stateManager.set(data, forceSet)
        manageState()
    }

    private fun removeState(key: String) {
        removeState(listOf(key))
    }

    private fun removeState(keys: List<String>) {
        keys.forEach {
            Log.d(debugTag, "tag: $it")

            stateManager = stateManager.remove(it)
        }

        manageState()
    }

    private fun clearState() {
        stateManager = stateManager.clear()
        manageState()
    }

    private fun manageState() {
        if (stateManager.isNotEmpty()) {
            for (i in stateManager.indices) {
                val it = stateManager[i]
                Log.d(debugTag, "tag: ${it.first}; state: ${it.second}; index: $i")
            }
            state = stateManager[0].second
        } else {
            state = State.Idle
            Log.d(debugTag, "tag: -; state: ${state}; index: -")
        }
    }

    // ----------------------------------------------------------------
    sealed class State : SingleEvent<Boolean>(true) {
        data object Idle : State()
        class OnNotification(val getMessageAndFinish: (Boolean) -> Int) : State()
        class OnShowDialog<T>(val tag: String, val model: DialogModel, val dat: T) : State()
        class Loading(val progress: Int? = null) : State()
        data object OnFinish : State()
    }
}