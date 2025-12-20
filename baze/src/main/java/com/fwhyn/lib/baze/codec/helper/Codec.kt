package com.fwhyn.lib.baze.codec.helper

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException

object Codec {

    /**
     * Encodes a string to base-36 representation (only contains 0-9 and a-z).
     *
     * @param input The string to encode
     * @return The encoded string containing only 0-9 and a-z characters
     */
    fun encodeBase36(input: String): String {
        if (input.isEmpty()) return ""

        // Convert string to bytes
        val bytes = input.toByteArray(Charsets.UTF_8)

        // Convert bytes to a BigInteger (treating it as a big-endian number)
        val bigInt = java.math.BigInteger(1, bytes)

        // Convert to base-36 representation
        return bigInt.toString(36)
    }

    /**
     * Decodes a base-36 string back to the original string.
     *
     * @param encoded The base-36 encoded string (containing only 0-9 and a-z)
     * @return The decoded original string
     * @throws IllegalArgumentException if the input contains invalid base-36 characters
     */
    fun decodeBase36(encoded: String): String {
        if (encoded.isEmpty()) return ""

        return try {
            // Convert from base-36 to BigInteger
            val bigInt = java.math.BigInteger(encoded, 36)

            // Convert BigInteger to bytes
            val bytes = bigInt.toByteArray()

            // Handle leading zero bytes that might be lost in the conversion
            val result = if (bytes.isNotEmpty() && bytes[0] == 0.toByte()) {
                bytes.drop(1).toByteArray()
            } else {
                bytes
            }

            // Convert bytes back to string
            String(result, Charsets.UTF_8)
        } catch (e: NumberFormatException) {
            throw IllegalArgumentException("Invalid base-36 encoded string: $encoded", e)
        }
    }
}

inline fun <reified T> String.decodeBase36(): T? {
    val jsonString = Codec.decodeBase36(this)
    return try {
        Gson().fromJson(jsonString, T::class.java)
    } catch (_: JsonSyntaxException) {
        null
    }
}

fun <T> T.encodeBase36(): String {
    val jsonString = Gson().toJson(this)
    return Codec.encodeBase36(jsonString)
}