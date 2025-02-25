package com.fwhyn.baze.data.helper.array

import org.junit.Assert.assertEquals
import org.junit.Test

class ReversedTest {

    @Test
    fun reversedWithNewObjectTest() {
        val input = listOf(2, 4, 6, 7, 8)
        val output = reversedWithNewObject(input)
        for (i in output.indices) {
            val expected = input[i]
            val actual = output[output.size - 1 - i]

            assertEquals(expected, actual)
        }
    }

    private fun reversedWithNewObject(input: List<Int>): List<Int> {
        val output: ArrayList<Int> = arrayListOf()

        for (i in input.size - 1 downTo 0) {
            output.add(input[i])
        }

        return output
    }

    @Test
    fun reversedTest() {
        val input = arrayListOf(1, 2, 3, 5, 6)
        reversed(input)

        assertEquals(6, input[0])
        assertEquals(5, input[1])
        assertEquals(3, input[2])
        assertEquals(2, input[3])
        assertEquals(1, input[4])

        // ----------------------------------------------------------------
        val input1 = arrayListOf(1, 2, 3, 5, 6, 7)
        reversed(input1)

        assertEquals(7, input1[0])
        assertEquals(6, input1[1])
        assertEquals(5, input1[2])
        assertEquals(3, input1[3])
        assertEquals(2, input1[4])
        assertEquals(1, input1[5])
    }

    private fun reversed(input: ArrayList<Int>) {
        val size = input.size
        val halfSize = size / 2
        for (startIndex in 0 until halfSize) {
            val temp = input[startIndex]
            val endIndex = size - 1 - startIndex
            input[startIndex] = input[endIndex]
            input[endIndex] = temp
        }
    }

    private fun <T> reverseArrayList(list: ArrayList<T>) {
        val size = list.size
        for (i in 0 until size / 2) {
            val temp = list[i]
            list[i] = list[size - 1 - i]
            list[size - 1 - i] = temp
        }
    }
}