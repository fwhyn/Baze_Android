package com.fwhyn.lib.baze.common.ui.helper

import androidx.annotation.StringRes

interface StringResourceManager<T> {
    @StringRes
    fun getId(input: T): Int
}