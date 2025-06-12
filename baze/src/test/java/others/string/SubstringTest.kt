package others.string

import org.junit.Assert
import org.junit.Test

class SubstringTest {


    @Test
    fun test1() {
        val input = "aaabccbbb"
        val output = longestSubString(input)

        Assert.assertEquals(3, output)
    }

    @Test
    fun test2() {
        val input = "pay2paypay"
        val output = longestSubString(input)

        Assert.assertEquals(4, output)
    }

    @Test
    fun test3() {
        val input = "ccccc"
        val output = longestSubString(input)

        Assert.assertEquals(1, output)
    }

    @Test
    fun test4() {
        val input = "pwwkew"
        val output = longestSubString(input)

        Assert.assertEquals(3, output)
    }

    private fun longestSubString(string: String): Int {
        var tempResult = ""
        var result = ""

        for (i in string.indices) {
            for (j in i..<string.length) {
                val c = string[j]
                if (!tempResult.contains(c)) {
                    tempResult += c
                } else {
                    if (result.length < tempResult.length) {
                        result = tempResult
                    }
                    tempResult = ""
                    break
                }
            }

        }

        return result.length
    }
}