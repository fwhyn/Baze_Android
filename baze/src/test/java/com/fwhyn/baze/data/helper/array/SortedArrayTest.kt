package com.fwhyn.baze.data.helper.array

import org.junit.Assert.assertEquals
import org.junit.Test

class SortedArrayTest {

    @Test
    fun bubbleSorted_Happy_Test() {
        val input = arrayListOf(6, 1, 3, 2, 5)
        bubbleSorted(input)
        assertEquals(1, input[0])
        assertEquals(2, input[1])
        assertEquals(3, input[2])
        assertEquals(5, input[3])
        assertEquals(6, input[4])
    }

    @Test
    fun bubbleSorted_SameValueDoesNotMatter_Test() {
        val input1 = arrayListOf(6, 1, 3, 2, 5, 4, 4)
        bubbleSorted(input1)
        assertEquals(1, input1[0])
        assertEquals(2, input1[1])
        assertEquals(3, input1[2])
        assertEquals(4, input1[3])
        assertEquals(4, input1[4])
        assertEquals(5, input1[5])
        assertEquals(6, input1[6])
    }

    private fun bubbleSorted(input: ArrayList<Int>) {

        // iterate 0 until max index: i
        // iterate index 0 until max index - index i: j
        // if element j > next index -> swap

        var n = 0
        for (i in input.indices) {
            for (j in 0 until input.size - i - 1) {
                val prev = input[j]
                val next = input[j + 1]
                if (prev > next) {
                    input[j] = next
                    input[j + 1] = prev
                }
                println(++n)
            }
        }
    }
}