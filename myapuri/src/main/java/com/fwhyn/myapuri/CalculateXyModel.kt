package com.fwhyn.myapuri

class CalculateXyModel : Calculate<CalculateXyModel.Input, Int> {

    override fun calculate(input: Input): Int {
        return input.x + input.y
    }

    data class Input(val x: Int, val y: Int)
}