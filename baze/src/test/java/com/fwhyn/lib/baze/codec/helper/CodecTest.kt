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

    // ==================== Data Class Tests ====================

    @Test
    fun testDataClassSimple() {
        data class User(val name: String, val age: Int)

        val original = User("John Doe", 30)
        val encoded = original.encodeBase36()
        val decoded: User? = encoded.decodeBase36()
        assertEquals(original, decoded)
    }

    @Test
    fun testDataClassWithMultipleFields() {
        data class Person(
            val id: Int,
            val name: String,
            val email: String,
            val age: Int,
            val isActive: Boolean
        )

        val original = Person(1, "Jane Smith", "jane@example.com", 28, true)
        val encoded = original.encodeBase36()
        val decoded: Person? = encoded.decodeBase36()
        assertEquals(original, decoded)
    }

    @Test
    fun testDataClassWithNestedObject() {
        data class Address(val street: String, val city: String, val zipCode: String)
        data class Customer(val name: String, val address: Address)

        val original = Customer(
            "Alice Johnson",
            Address("123 Main St", "New York", "10001")
        )
        val encoded = original.encodeBase36()
        val decoded: Customer? = encoded.decodeBase36()
        assertEquals(original, decoded)
    }

    @Test
    fun testDataClassWithNullableFields() {
        data class Product(
            val id: Int,
            val name: String,
            val description: String?,
            val price: Double?
        )

        val original = Product(1, "Laptop", null, 999.99)
        val encoded = original.encodeBase36()
        val decoded: Product? = encoded.decodeBase36()
        assertEquals(original, decoded)
    }

    @Test
    fun testDataClassWithListField() {
        data class Department(
            val name: String,
            val employees: List<String>
        )

        val original = Department("Engineering", listOf("Alice", "Bob", "Charlie"))
        val encoded = original.encodeBase36()
        val decoded: Department? = encoded.decodeBase36()
        assertEquals(original, decoded)
    }

    @Test
    fun testDataClassWithMapField() {
        data class Config(
            val appName: String,
            val settings: Map<String, String>
        )

        val original = Config(
            "MyApp",
            mapOf("theme" to "dark", "language" to "en", "timezone" to "UTC")
        )
        val encoded = original.encodeBase36()
        val decoded: Config? = encoded.decodeBase36()
        assertEquals(original, decoded)
    }

    @Test
    fun testDataClassWithSpecialCharacters() {
        data class Message(val sender: String, val content: String)

        val original = Message(
            "user@example.com",
            "Hello! This is a message with special chars: !@#$%^&*()"
        )
        val encoded = original.encodeBase36()
        val decoded: Message? = encoded.decodeBase36()
        assertEquals(original, decoded)
    }

    @Test
    fun testDataClassWithUnicodeFields() {
        data class Translation(val english: String, val chinese: String, val arabic: String)

        val original = Translation("Hello", "你好", "مرحبا")
        val encoded = original.encodeBase36()
        val decoded: Translation? = encoded.decodeBase36()
        assertEquals(original, decoded)
    }

    @Test
    fun testDataClassWithAllPrimitiveTypes() {
        data class AllTypes(
            val byteVal: Byte,
            val shortVal: Short,
            val intVal: Int,
            val longVal: Long,
            val floatVal: Float,
            val doubleVal: Double,
            val booleanVal: Boolean,
            val charVal: Char,
            val stringVal: String
        )

        val original = AllTypes(
            byteVal = 127.toByte(),
            shortVal = 32767.toShort(),
            intVal = 2147483647,
            longVal = 9223372036854775807L,
            floatVal = 3.14f,
            doubleVal = 2.71828,
            booleanVal = true,
            charVal = 'A',
            stringVal = "test"
        )
        val encoded = original.encodeBase36()
        val decoded: AllTypes? = encoded.decodeBase36()
        assertEquals(original, decoded)
    }

    @Test
    fun testDataClassEquality() {
        data class Item(val id: Int, val name: String)

        val obj1 = Item(1, "Widget")
        val obj2 = Item(1, "Widget")

        val encoded1 = obj1.encodeBase36()
        val encoded2 = obj2.encodeBase36()

        assertEquals("Same data should produce same encoding", encoded1, encoded2)
    }

    @Test
    fun testDataClassInequality() {
        data class Item(val id: Int, val name: String)

        val obj1 = Item(1, "Widget")
        val obj2 = Item(2, "Gadget")

        val encoded1 = obj1.encodeBase36()
        val encoded2 = obj2.encodeBase36()

        assertNotEquals("Different data should produce different encoding", encoded1, encoded2)
    }

    @Test
    fun testDataClassWithEmptyCollections() {
        data class Container(
            val items: List<String>,
            val metadata: Map<String, Int>
        )

        val original = Container(emptyList(), emptyMap())
        val encoded = original.encodeBase36()
        val decoded: Container? = encoded.decodeBase36()
        assertEquals(original, decoded)
    }

    @Test
    fun testDataClassReproducibility() {
        data class Record(val timestamp: String, val value: Double)

        val original = Record("2024-01-01", 42.5)

        val encoded1 = original.encodeBase36()
        val encoded2 = original.encodeBase36()

        assertEquals("Multiple encodings of same object should match", encoded1, encoded2)

        val decoded1: Record? = encoded1.decodeBase36()
        val decoded2: Record? = encoded2.decodeBase36()

        assertEquals(decoded1, decoded2)
    }

    @Test
    fun testDataClassWithComplexHierarchy() {
        data class Tag(val name: String, val color: String)
        data class Task(val title: String, val description: String, val tags: List<Tag>)
        data class Project(val name: String, val tasks: List<Task>)

        val original = Project(
            "My Project",
            listOf(
                Task(
                    "Task 1",
                    "First task",
                    listOf(Tag("urgent", "red"), Tag("backend", "blue"))
                ),
                Task(
                    "Task 2",
                    "Second task",
                    listOf(Tag("ui", "green"))
                )
            )
        )
        val encoded = original.encodeBase36()
        val decoded: Project? = encoded.decodeBase36()
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