package com.fwhyn.lib.baze.codec.helper

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class CodecEnumTest {
    // ==================== Enum Tests ====================

    private enum class Status { NEW, IN_PROGRESS, DONE }

    @Test
    fun testEnumSimpleEncodeDecode() {
        val original = Status.IN_PROGRESS
        val encoded = original.encodeBase36()
        val decoded: Status? = encoded.decodeBase36()
        assertEquals(original, decoded)
    }

    enum class Priority { LOW, MEDIUM, HIGH }

    @Test
    fun testEnumInDataClass() {
        data class Ticket(val id: Int, val title: String, val status: Status, val priority: Priority)

        val original = Ticket(101, "Fix bug", Status.NEW, Priority.HIGH)
        val encoded = original.encodeBase36()
        val decoded: Ticket? = encoded.decodeBase36()
        assertEquals(original, decoded)
    }

    @Test
    fun testEnumListEncodeDecode() {
        val original: java.util.ArrayList<Status> = ArrayList()
        original.add(Status.NEW)
        original.add(Status.IN_PROGRESS)
        original.add(Status.DONE)

        val encoded = original.encodeBase36()
        val decoded: java.util.ArrayList<Status> = encoded.decodeBase36() ?: throw Exception("Decoding failed")
        assertEquals(original, decoded)
    }

    @Test
    fun testEnumMapEncodeDecode() {
        val original = mapOf("task1" to Status.NEW, "task2" to Status.DONE)
        val encoded = original.encodeBase36()
        val decoded: Map<String, Status>? = encoded.decodeBase36()
        assertEquals(original, decoded)
    }

    @Test
    fun testInvalidEnumDecodeReturnsNull() {
        // Manually craft a base36 string representing invalid enum JSON: "INVALID"
        val invalidEnumJson = "\"INVALID\"" // JSON string with value INVALID
        val encoded = Codec.encodeBase36(invalidEnumJson)
        val decoded: Status? = encoded.decodeBase36()
        assertNull(decoded)
    }

    // ==================== Enum With Properties Tests ====================

    private enum class Lang(val code: String, val nativeName: String) {
        EN("en", "English"),
        ZH("zh", "中文"),
        AR("ar", "العربية");

        fun display(): String = "$nativeName ($code)"
    }

    @Test
    fun testEnumWithPropertiesSimpleEncodeDecode() {
        val original = Lang.ZH
        val encoded = original.encodeBase36()
        val decoded: Lang? = encoded.decodeBase36()
        assertEquals(original, decoded)
        // Ensure properties/methods still behave correctly after decode
        assertEquals("中文 (zh)", decoded?.display())
        assertEquals("zh", decoded?.code)
        assertEquals("中文", decoded?.nativeName)
    }

    @Test
    fun testEnumWithPropertiesInDataClass() {
        data class LocalePref(val primary: Lang, val secondary: Lang?)

        val original = LocalePref(Lang.EN, Lang.AR)
        val encoded = original.encodeBase36()
        val decoded: LocalePref? = encoded.decodeBase36()
        assertEquals(original, decoded)
        assertEquals("English (en)", decoded!!.primary.display())
        assertEquals("العربية (ar)", decoded.secondary?.display())
    }

    @Test
    fun testEnumWithPropertiesListEncodeDecode() {
        val original = listOf(Lang.EN, Lang.ZH, Lang.AR)
        val encoded = original.encodeBase36()
        val decoded: List<Lang>? = encoded.decodeBase36()
        // Compare contents; decoder coerces to enum instances
        assertEquals(original, decoded?.toList())
        // Verify property access on elements
        assertEquals(listOf("en", "zh", "ar"), decoded!!.map { it.code })
    }

    @Test
    fun testEnumWithPropertiesMapEncodeDecode() {
        val original = mapOf("primary" to Lang.EN, "fallback" to Lang.ZH)
        val encoded = original.encodeBase36()
        val decoded: Map<String, Lang>? = encoded.decodeBase36()
        assertEquals(original, decoded)
        assertEquals("English", decoded!!["primary"]?.nativeName)
        assertEquals("中文", decoded["fallback"]?.nativeName)
    }
}