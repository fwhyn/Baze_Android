package others.array

import org.junit.Assert
import org.junit.Test


class ChocolateBarTest {

    @Test
    fun main() {
        val result = birthday(
            s = listOf(4),
            d = 4,
            m = 1
        )
        Assert.assertEquals(1, result)
    }

    fun birthday(s: List<Int>, d: Int, m: Int): Int {
        var result = 0

        for (i in s.indices) {
            var sum = 0
            for (j in 0..<m) {
                sum = sum + s[i + j]
            }
            if (sum == d) result++
        }

        return result
    }
}