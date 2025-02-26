package com.fwhyn.baze.data.helper.array

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class GetSecondLargestTest {

    @Test
    fun getSecondLargestTest() {
        val input = arrayListOf(1, 2, 3, 5, 6)
        assertEquals(5, getSecondLargest(input))

        // ----------------------------------------------------------------
        val input1 = arrayListOf(1, 2, 8, -5, 6)
        assertEquals(6, getSecondLargest(input1))

        // ----------------------------------------------------------------
        val input2 = arrayListOf(4, 4, 4, 4, 4)
        assertNull(getSecondLargest(input2))

        // ----------------------------------------------------------------
        val input3 = arrayListOf(4, 4, 10, 4, 4)
        assertEquals(4, getSecondLargest(input3))
    }

    private fun getSecondLargest(input: List<Int>): Int? {
        var firstLargest = input[0]
        var secondLargest: Int = Int.MIN_VALUE

        for (i in 1 until input.size) {
            val curValue = input[i]

            if (curValue > firstLargest) {
                secondLargest = firstLargest
                firstLargest = curValue
            } else if (curValue > secondLargest && curValue != firstLargest) {
                secondLargest = curValue
            }
        }

        return if (secondLargest == Int.MIN_VALUE) null else secondLargest
    }
}