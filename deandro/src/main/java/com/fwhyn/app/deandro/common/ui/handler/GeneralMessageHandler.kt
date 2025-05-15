package com.fwhyn.app.deandro.common.ui.handler

import androidx.annotation.StringRes
import com.fwhyn.app.deandro.R
import com.fwhyn.lib.baze.common.data.model.Status
import com.fwhyn.lib.baze.common.ui.helper.MessageHandler

class GeneralMessageHandler : MessageHandler<Status> {

    @StringRes
    override fun getMessage(input: Status): Int {
        return when (input) {
            Status.Success -> R.string.success
            Status.Unauthorized -> R.string.unauthorized
            Status.BadRequest -> R.string.bad_request
            Status.InitialState -> R.string.initial_state
            Status.NotFound -> R.string.success
            Status.OutOfMemoryError -> R.string.out_memory_error
            Status.RequestTimeOut -> R.string.request_time_out
            Status.SettingError -> R.string.setting_error
            Status.WriteError -> R.string.write_error
            else -> R.string.unknown_error
        }
    }
}