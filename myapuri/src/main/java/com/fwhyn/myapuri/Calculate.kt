package com.fwhyn.myapuri

interface Calculate<I, O> {
    fun calculate(input: I): O
}