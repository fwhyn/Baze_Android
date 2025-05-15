package com.fwhyn.lib.baze.common.ui.helper

import androidx.annotation.StringRes

interface MessageHandler<T> {
    @StringRes
    fun getMessage(input: T): Int
}