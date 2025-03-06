package com.fwhyn.baze.data.helper.string

import org.junit.Assert
import org.junit.Test

class ReversedStringTest {

    @Test
    fun reverseStringTest() {
        val input = "abcde"
        Assert.assertEquals("edcba", reverseString(input))
    }

    private fun reverseString(input: String): String {
        val size = input.length
        val output = input.toCharArray()

        for (i in 0 until (size / 2)) {
            val temp = output[i]
            output[i] = output[size - i - 1]
            output[size - i - 1] = temp
        }

        return String(output)
    }
}