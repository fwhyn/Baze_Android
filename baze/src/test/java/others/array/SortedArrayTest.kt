package others.array

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

    @Test
    fun bubbleSorted_TheIsMinus_Test() {
        val input1 = arrayListOf(-6, 1, 3, 2, 5, 4, 4, 10)
        bubbleSorted(input1)
        assertEquals(-6, input1[0])
        assertEquals(1, input1[1])
        assertEquals(2, input1[2])
        assertEquals(3, input1[3])
        assertEquals(4, input1[4])
        assertEquals(4, input1[5])
        assertEquals(5, input1[6])
        assertEquals(10, input1[7])
    }

    private fun bubbleSorted(input: ArrayList<Int>) {

        // iterate 0 until max index: i
        // iterate index 0 until max index - index i: j
        // if element j > next index -> swap

        var n = 0
        for (i in input.indices) {
            var swapped = false

            for (j in 0 until input.size - i - 1) {
                val prev = input[j]
                val next = input[j + 1]
                if (prev > next) {
                    input[j] = next
                    input[j + 1] = prev

                    swapped = true
                }
                println(++n)
            }

            if (!swapped) break
        }
    }

    @Test
    fun quickSorted_Happy_Test() {
        val input = intArrayOf(6, 1, 3, 2, 5)
        quickSorted(input)
        assertEquals(1, input[0])
        assertEquals(2, input[1])
        assertEquals(3, input[2])
        assertEquals(5, input[3])
        assertEquals(6, input[4])
    }

    @Test
    fun quickSorted_SameValueDoesNotMatter_Test() {
        val input = intArrayOf(6, 1, 3, 2, 5, 4, 4)
        quickSorted(input)
        assertEquals(1, input[0])
        assertEquals(2, input[1])
        assertEquals(3, input[2])
        assertEquals(4, input[3])
        assertEquals(4, input[4])
        assertEquals(5, input[5])
        assertEquals(6, input[6])
    }

    @Test
    fun quickSorted_TheItemIsMinus_Test() {
        val input = intArrayOf(-6, 1, 3, 2, 5, 4, 4, 10)
        quickSorted(input)
        assertEquals(-6, input[0])
        assertEquals(1, input[1])
        assertEquals(2, input[2])
        assertEquals(3, input[3])
        assertEquals(4, input[4])
        assertEquals(4, input[5])
        assertEquals(5, input[6])
        assertEquals(10, input[7])
    }

    fun quickSorted(arr: IntArray, low: Int = 0, high: Int = arr.size - 1) {
        if (low < high) {
            val pi = partition(arr, low, high)
            quickSorted(arr, low, pi - 1)   // sort left side
            quickSorted(arr, pi + 1, high) // sort right side
        }
    }

    fun partition(arr: IntArray, low: Int, high: Int): Int {
        val pivot = arr[high]
        var i = low - 1
        for (j in low until high) {
            if (arr[j] <= pivot) {
                i++
                arr[i] = arr[j].also { arr[j] = arr[i] } // swap
            }
        }
        arr[i + 1] = arr[high].also { arr[high] = arr[i + 1] } // swap pivot
        return i + 1
    }
}