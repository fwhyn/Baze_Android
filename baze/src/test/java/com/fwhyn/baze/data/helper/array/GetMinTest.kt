package com.fwhyn.baze.data.helper.array

import org.junit.Assert.assertEquals
import org.junit.Test

class GetMinTest {

    @Test
    fun getMinTest() {
        val input = arrayListOf(1, 2, 3, 5, 6)

        assertEquals(1, getMin(input))

        // ----------------------------------------------------------------
        val input1 = arrayListOf(1, 2, 3, -5, 6)
        getMin(input)

        assertEquals(-5, getMin(input1))
    }

    private fun getMin(input: List<Int>): Int {
        var min = input[0]
        for (i in 1 until input.size) {
            val value = input[i]
            if (value < min) min = value
        }

        return min
    }
}