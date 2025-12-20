package com.fwhyn.lib.baze.codec.helper

import com.google.gson.Gson
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CodecParcelableTest {

    // Assuming ScreeningParam sealed class exists in production code as provided.
    // Tests will round-trip individual subclasses via base-36 codec.

    @Test
    fun testScreeningParam_ConnectByQr_RoundTrip() {
        val original = ScreeningParam.ConnectByQr(kitId = "KIT-12345")
        val encoded = original.encodeBase36()
        val decoded: ScreeningParam.ConnectByQr? = encoded.decodeBase36()
        assertEquals(original, decoded)
    }

    @Test
    fun testScreeningParam_ConnectByNik_RoundTrip() {
        val original = ScreeningParam.ConnectByNik(nik = "1234567890123456")
        val encoded = original.encodeBase36()
        val decoded: ScreeningParam.ConnectByNik? = encoded.decodeBase36()
        assertEquals(original, decoded)
    }

    @Test
    fun testScreeningParam_ConnectByEmail_RoundTrip() {
        val original = ScreeningParam.ConnectByEmail(email = "user@example.com")
        val encoded = original.encodeBase36()
        val decoded: ScreeningParam.ConnectByEmail? = encoded.decodeBase36()
        assertEquals(original, decoded)
    }

    @Test
    fun testScreeningParam_MixedList_RoundTrip_WithExplicitDecoding() {
        val original = listOf(
            ScreeningParam.ConnectByQr("KIT-001"),
            ScreeningParam.ConnectByNik("9876543210987654"),
            ScreeningParam.ConnectByEmail("a@b.com")
        )
        val encoded = original.encodeBase36()
        // Decode to a generic list of maps first, then reconstruct expected types
        val decodedGeneric: List<Map<String, Any?>>? = encoded.decodeBase36()
        assertTrue(decodedGeneric != null && decodedGeneric.size == 3)

        // Re-decode each element to its expected concrete type using individual encoding
        val reEncoded0 = Codec.encodeBase36(Gson().toJson(decodedGeneric!![0]))
        val reEncoded1 = Codec.encodeBase36(Gson().toJson(decodedGeneric[1]))
        val reEncoded2 = Codec.encodeBase36(Gson().toJson(decodedGeneric[2]))

        val d0: ScreeningParam.ConnectByQr? = reEncoded0.decodeBase36()
        val d1: ScreeningParam.ConnectByNik? = reEncoded1.decodeBase36()
        val d2: ScreeningParam.ConnectByEmail? = reEncoded2.decodeBase36()

        assertEquals(original[0], d0)
        assertEquals(original[1], d1)
        assertEquals(original[2], d2)
    }

    // ----------------------------------------------------------------
    // Test-only sealed class without Parcelable/Parcelize to avoid unit test plugin issues
    sealed class ScreeningParam {
        data class ConnectByQr(val kitId: String) : ScreeningParam()
        data class ConnectByNik(val nik: String) : ScreeningParam()
        data class ConnectByEmail(val email: String) : ScreeningParam()
    }
}