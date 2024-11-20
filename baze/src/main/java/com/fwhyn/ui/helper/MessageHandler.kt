package com.fwhyn.ui.helper

import androidx.annotation.StringRes

interface MessageHandler<T> {
    @StringRes
    fun getMessage(input: T): Int
}