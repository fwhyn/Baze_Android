package com.fwhyn.baze.data.helper.array

import org.junit.Assert.assertEquals
import org.junit.Test

class SortedArrayTest {

    @Test
    fun bubbleSortedTest() {
        val input = arrayListOf(6, 1, 3, 2, 5)
        bubbleSorted(input)
        assertEquals(1, input[0])
        assertEquals(2, input[1])
        assertEquals(3, input[2])
        assertEquals(5, input[3])
        assertEquals(6, input[4])

//        // ----------------------------------------------------------------
//        val input1 = arrayListOf(1, 2, 8, -5, 6)
//        assertEquals(6, bubbleSorted(input1))
//
//        // ----------------------------------------------------------------
//        val input2 = arrayListOf(4, 4, 4, 4, 4)
//        assertNull(bubbleSorted(input2))
//
//        // ----------------------------------------------------------------
//        val input3 = arrayListOf(4, 4, 10, 4, 4)
//        assertEquals(4, bubbleSorted(input3))
    }

    private fun bubbleSorted(input: ArrayList<Int>) {

        // iterate 0 until max index: i
        // iterate index 0 until max index - index i: j
        // if element j > next index -> swap

        for (i in input.indices) {
            for (j in 0 until input.size - i) {
                val prev = input[j]
                val next = input[j + 1]
                if (prev > next) {
                    input[i] = next
                    input[j + 1] = prev
                }
            }
        }
    }
}