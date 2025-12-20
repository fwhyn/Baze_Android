package com.fwhyn.lib.baze.codec.helper

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class CodecDataClassTest {
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
}