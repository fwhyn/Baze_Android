package com.fwhyn.lib.baze.codec.helper

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class CodecTest {

    // ==================== Basic Encoding/Decoding Tests ====================

    @Test
    fun testEncodeAndDecodeSimpleString() {
        val original = "Hello, World!"
        val encoded = Codec.encodeBase36(original)
        val decoded = Codec.decodeBase36(encoded)
        assertEquals(original, decoded)
    }

    @Test
    fun testEncodeAndDecodeEmptyString() {
        val original = ""
        val encoded = Codec.encodeBase36(original)
        val decoded = Codec.decodeBase36(encoded)
        assertEquals(original, decoded)
        assertEquals("", encoded)
    }

    @Test
    fun testEncodeAndDecodeNumbers() {
        val original = "1234567890"
        val encoded = Codec.encodeBase36(original)
        val decoded = Codec.decodeBase36(encoded)
        assertEquals(original, decoded)
    }

    @Test
    fun testEncodeAndDecodeSpecialCharacters() {
        val original = "!@#$%^&*()_+-=[]{}|;:',.<>?/"
        val encoded = Codec.encodeBase36(original)
        val decoded = Codec.decodeBase36(encoded)
        assertEquals(original, decoded)
    }

    @Test
    fun testEncodeAndDecodeUnicodeCharacters() {
        val original = "Hello 世界 مرحبا 🌍"
        val encoded = Codec.encodeBase36(original)
        val decoded = Codec.decodeBase36(encoded)
        assertEquals(original, decoded)
    }

    @Test
    fun testEncodeAndDecodeSpaces() {
        val original = "   multiple   spaces   "
        val encoded = Codec.encodeBase36(original)
        val decoded = Codec.decodeBase36(encoded)
        assertEquals(original, decoded)
    }

    @Test
    fun testEncodeAndDecodeNewlines() {
        val original = "Line 1\nLine 2\nLine 3"
        val encoded = Codec.encodeBase36(original)
        val decoded = Codec.decodeBase36(encoded)
        assertEquals(original, decoded)
    }

    @Test
    fun testEncodeAndDecodeTabs() {
        val original = "Column1\tColumn2\tColumn3"
        val encoded = Codec.encodeBase36(original)
        val decoded = Codec.decodeBase36(encoded)
        assertEquals(original, decoded)
    }

    // ==================== Encoding Output Tests ====================

    @Test
    fun testEncodedStringContainsOnlyValidCharacters() {
        val original = "Test@String#With\$Special%Chars!"
        val encoded = Codec.encodeBase36(original)

        // Check that encoded string contains only 0-9 and a-z
        val validPattern = Regex("^[0-9a-z]*$")
        assertTrue("Encoded string contains invalid characters", validPattern.matches(encoded))
    }

    @Test
    fun testEncodedStringIsLowercase() {
        val original = "UPPERCASE"
        val encoded = Codec.encodeBase36(original)

        // Should not contain uppercase letters
        assertFalse("Encoded string contains uppercase", encoded.any { it.isUpperCase() })
    }

    @Test
    fun testEncodedStringNoSpecialCharacters() {
        val original = "!@#$%^&*()"
        val encoded = Codec.encodeBase36(original)

        // Should not contain any special characters
        assertFalse("Encoded string contains special chars", encoded.any { !it.isLetterOrDigit() })
    }

    // ==================== Edge Cases ====================

    @Test
    fun testEncodeSingleCharacter() {
        val original = "A"
        val encoded = Codec.encodeBase36(original)
        val decoded = Codec.decodeBase36(encoded)
        assertEquals(original, decoded)
    }

    @Test
    fun testEncodeLongString() {
        val original =
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."
        val encoded = Codec.encodeBase36(original)
        val decoded = Codec.decodeBase36(encoded)
        assertEquals(original, decoded)
    }

    @Test
    fun testEncodeStringWithNullBytes() {
        val original = "String\u0000With\u0000Nulls"
        val encoded = Codec.encodeBase36(original)
        val decoded = Codec.decodeBase36(encoded)
        assertEquals(original, decoded)
    }

    @Test
    fun testEncodedStringIsReproducible() {
        val original = "Test String"
        val encoded1 = Codec.encodeBase36(original)
        val encoded2 = Codec.encodeBase36(original)
        assertEquals("Encoding should be reproducible", encoded1, encoded2)
    }

    @Test
    fun testDifferentStringsProduceDifferentEncodings() {
        val str1 = "String1"
        val str2 = "String2"
        val encoded1 = Codec.encodeBase36(str1)
        val encoded2 = Codec.encodeBase36(str2)
        assertNotEquals("Different strings should have different encodings", encoded1, encoded2)
    }

    // ==================== Error Handling ====================

    @Test(expected = IllegalArgumentException::class)
    fun testDecodeInvalidBase36WithInvalidCharacters() {
        // Contains uppercase which is not valid in base-36 lowercase format
        Codec.decodeBase36("INVALID_CHARS")
    }

    @Test(expected = IllegalArgumentException::class)
    fun testDecodeInvalidBase36WithSpecialCharacters() {
        Codec.decodeBase36("invalid!@#\$chars")
    }

    // ==================== Extension Function Tests ====================

    @Test
    fun testExtensionFunctionEncodeBase36() {
        val original = "Test Data"
        val encoded = original.encodeBase36()
        assertTrue("Should encode string to base36", encoded.all { it.isDigit() || it.isLowerCase() })
    }

    @Test
    fun testExtensionFunctionDecodeBase36() {
        val original = "Test Data"
        val encoded = original.encodeBase36()
        val decoded: String? = encoded.decodeBase36()
        assertEquals(original, decoded)
    }

    @Test
    fun testExtensionFunctionWithCustomObject() {
        data class TestData(val name: String, val value: Int)

        val original = TestData("test", 42)
        val encoded = original.encodeBase36()
        val decoded: TestData? = encoded.decodeBase36()
        assertEquals(original, decoded)
    }

    @Test
    fun testExtensionFunctionWithList() {
        val original = listOf("item1", "item2", "item3")
        val encoded = original.encodeBase36()
        val decoded: List<String>? = encoded.decodeBase36()
        assertEquals(original, decoded)
    }

    @Test
    fun testExtensionFunctionWithMap() {
        val original = mapOf("key1" to "value1", "key2" to "value2")
        val encoded = original.encodeBase36()
        val decoded: Map<String, String>? = encoded.decodeBase36()
        assertEquals(original, decoded)
    }

    // ==================== Null and Empty Handling ====================

    @Test
    fun testDecodeEmptyString() {
        val encoded = ""
        val decoded = Codec.decodeBase36(encoded)
        assertEquals("", decoded)
    }

    @Test
    fun testExtensionFunctionDecodeBase36ReturnsNullForInvalidJson() {
        val result: String? = "abc123".decodeBase36()
        assertNull("Invalid JSON should return null", result)
    }

    // ==================== Consistency Tests ====================

    @Test
    fun testRoundTripConsistency() {
        val testStrings = listOf(
            "simple",
            "with spaces",
            "123",
            "!@#$%^&*()",
            "mixed123MixedCASE",
            "Hello, 世界!",
            "",
            "a",
            "zzzzz"
        )

        for (original in testStrings) {
            val encoded = Codec.encodeBase36(original)
            val decoded = Codec.decodeBase36(encoded)
            assertEquals("Round-trip failed for: '$original'", original, decoded)
        }
    }

    @Test
    fun testBase36ConversionAccuracy() {
        val testString = "1"
        val encoded = Codec.encodeBase36(testString)
        val decoded = Codec.decodeBase36(encoded)
        assertEquals(testString, decoded)
    }
}