package others

import org.junit.Assert
import org.junit.Test

//Given a string s, find the length of the longest substring without repeating characters.
//
//Length of the longest substring
//e.g.
//
//1)
//Input: s = “aaabccbbb”
//Output: 3
//
//Explanation: The answer is “abc”, with the length of 3.
//
//2)
//Input: s = “pay2paypay”
//Output: 4
//
//Explanation: The answer is “pay2” or “ay2p” or “y2pa” or  “2pay” with the length of 4.
//
//3)
//Input: s = “ccccc”
//Output: 1
//
//Explanation: The answer is “c”, with the length of 1.
//
//4)
//Input: s = "pwwkew"
//Output: 3
//
//Explanation: The answer is "wke", with the length of 3.
//
//Notice that the answer must be a substring, "pwke" is a subsequence and not a substring.
//
//
//Constraints:
//
//* 0 <= s.length <= 5 * 104
//* s consists of English letters, digits, symbols and spaces.

class AnyTest {


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