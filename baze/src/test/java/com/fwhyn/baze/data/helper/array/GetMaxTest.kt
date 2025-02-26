package com.fwhyn.baze.data.helper.array

import org.junit.Assert.assertEquals
import org.junit.Test

class GetMaxTest {

    @Test
    fun getMinTest() {
        val input = arrayListOf(1, 2, 3, 5, 6)

        assertEquals(6, getMax(input))

        // ----------------------------------------------------------------
        val input1 = arrayListOf(1, 2, 8, -5, 6)
        getMax(input)

        assertEquals(8, getMax(input1))
    }

    private fun getMax(input: List<Int>): Int {
        var max = input[0]
        for (i in 1 until input.size) {
            val value = input[i]
            if (value > max) max = value
        }

        return max
    }
}