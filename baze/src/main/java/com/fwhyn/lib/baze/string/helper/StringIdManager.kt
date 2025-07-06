package com.fwhyn.lib.baze.string.helper

import androidx.annotation.StringRes

interface StringIdManager<T> {
    @StringRes
    fun getId(input: T): Int
}