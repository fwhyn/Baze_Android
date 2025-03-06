package com.fwhyn.baze.data.helper.string

import org.junit.Assert
import org.junit.Test

class ReversedStringTest {

    @Test
    fun reverseStringTest() {
        val input = "abcde"
        Assert.assertEquals("edcba", reverseString(input))
        Assert.assertEquals("edcba", reverseStringV2(input))

        // ----------------------------------------------------------------
        val input1 = "ab"
        Assert.assertEquals("ba", reverseString(input1))
        Assert.assertEquals("ba", reverseStringV2(input1))

        // ----------------------------------------------------------------
        val input2 = "abc"
        Assert.assertEquals("cba", reverseString(input2))
        Assert.assertEquals("cba", reverseStringV2(input2))
    }

    private fun reverseString(input: String): String {
        val size = input.length
        var output = ""

        for (i in (size - 1) downTo 0) {
            output += input[i]
        }

        return output
    }

    private fun reverseStringV2(input: String): String {
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