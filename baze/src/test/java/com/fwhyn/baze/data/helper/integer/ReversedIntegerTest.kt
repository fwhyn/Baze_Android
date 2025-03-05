package com.fwhyn.baze.data.helper.integer

import org.junit.Assert
import org.junit.Test

class ReversedIntegerTest {

    @Test
    fun getReversedTest() {
        val input = 1234
        Assert.assertEquals(4321, getReversed(input))

        // ----------------------------------------------------------------
        val input1 = 3210
        Assert.assertEquals(123, getReversed(input1))

    }

    fun getReversed(input: Int): Int {
        // create currentValue from input
        // initialize output by 0
        // when currentValue not 0
        // get modulo currentValue / 10
        // output = output * 10 + modulo
        // currentValue = currentValue / 10 (get rid after comma value)

        var currentValue = input
        var output = 0

        while (currentValue != 0) {
            val mod = currentValue % 10
            output = output * 10 + mod
            currentValue /= 10
        }

        return output
    }
}