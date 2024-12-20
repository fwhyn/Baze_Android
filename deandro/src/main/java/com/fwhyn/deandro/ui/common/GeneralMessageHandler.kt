package com.fwhyn.deandro.ui.common

import androidx.annotation.StringRes
import com.fwhyn.data.model.Status
import com.fwhyn.deandro.R
import com.fwhyn.ui.helper.MessageHandler

class GeneralMessageHandler : MessageHandler<Status> {

    @StringRes
    override fun getMessage(input: Status): Int {
        return when (input) {
            Status.BadRequest -> R.string.bad_request
            Status.InitialState -> R.string.initial_state
            Status.NotFound -> R.string.success
            Status.OutOfMemoryError -> R.string.out_memory_error
            Status.RequestTimeOut -> R.string.request_time_out
            Status.SettingError -> R.string.setting_error
            Status.Success -> R.string.success
            Status.WriteError -> R.string.write_error
            else -> R.string.unknown_error
        }
    }
}