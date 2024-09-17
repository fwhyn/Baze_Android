package com.fwhyn.data.helper

class Extension

fun <T> Class<T>.getTestTag(): String {
    return this.simpleName + "_fwhyn_test"
}