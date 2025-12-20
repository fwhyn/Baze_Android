package com.fwhyn.lib.baze.codec.helper

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import java.lang.reflect.ParameterizedType

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
    val gson = Gson()
    val targetType = object : TypeToken<T>() {}.type

    // First attempt: normal typed deserialization
    try {
        val parsed: T? = gson.fromJson(jsonString, targetType)
        if (parsed != null) {
            // If T is a parameterized Collection/Map with Enum value type, ensure elements are Enums
            when (targetType) {
                is ParameterizedType -> {
                    val raw = targetType.rawType
                    val args = targetType.actualTypeArguments

                    // Collection<E : Enum>
                    if (raw is Class<*> && java.util.Collection::class.java.isAssignableFrom(raw) && args.size == 1) {
                        val elem = args[0]
                        if (elem is Class<*> && elem.isEnum && parsed is Collection<*>) {
                            val allEnums = parsed.all { it == null || elem.isInstance(it) }
                            if (!allEnums) {
                                @Suppress("UNCHECKED_CAST")
                                val enumClass = elem as Class<out Enum<*>>
                                val converted = parsed.map { v ->
                                    v?.toString()?.let { name -> java.lang.Enum.valueOf(enumClass, name) }
                                }
                                if (converted.any { it == null }) return null
                                @Suppress("UNCHECKED_CAST")
                                return converted as T
                            }
                        }
                    }

                    // Map<K, V : Enum>
                    if (raw is Class<*> && java.util.Map::class.java.isAssignableFrom(raw) && args.size == 2) {
                        val valueType = args[1]
                        if (valueType is Class<*> && valueType.isEnum && parsed is Map<*, *>) {
                            val allEnums = parsed.values.all { it == null || valueType.isInstance(it) }
                            if (!allEnums) {
                                @Suppress("UNCHECKED_CAST")
                                val enumClass = valueType as Class<out Enum<*>>
                                val converted = parsed.mapValues { (_, v) ->
                                    v?.toString()?.let { name -> java.lang.Enum.valueOf(enumClass, name) }
                                }
                                if (converted.values.any { it == null }) return null
                                @Suppress("UNCHECKED_CAST")
                                return converted as T
                            }
                        }
                    }
                }
            }
            return parsed
        }
    } catch (_: JsonSyntaxException) {
        // continue to fallback
    }

    // Fallbacks for common generic cases (Collections/Maps of Enums) when element type isn't honored
    return try {
        val anyParsed: Any = gson.fromJson(jsonString, Any::class.java) ?: return null

        when (targetType) {
            is ParameterizedType -> {
                val raw = targetType.rawType
                val args = targetType.actualTypeArguments

                // Handle Collection<E : Enum>
                if (raw is Class<*> && java.util.Collection::class.java.isAssignableFrom(raw) && args.size == 1) {
                    val elem = args[0]
                    if (elem is Class<*> && elem.isEnum && anyParsed is Collection<*>) {
                        @Suppress("UNCHECKED_CAST")
                        val enumClass = elem as Class<out Enum<*>>
                        val converted = anyParsed.map { v ->
                            v?.toString()?.let { name -> java.lang.Enum.valueOf(enumClass, name) }
                        }
                        if (converted.any { it == null }) return null
                        @Suppress("UNCHECKED_CAST")
                        return converted as T
                    }
                }

                // Handle Map<K, V : Enum>
                if (raw is Class<*> && java.util.Map::class.java.isAssignableFrom(raw) && args.size == 2) {
                    val valueType = args[1]
                    if (valueType is Class<*> && valueType.isEnum && anyParsed is Map<*, *>) {
                        @Suppress("UNCHECKED_CAST")
                        val enumClass = valueType as Class<out Enum<*>>
                        val converted = anyParsed.mapValues { (_, v) ->
                            v?.toString()?.let { name -> java.lang.Enum.valueOf(enumClass, name) }
                        }
                        if (converted.values.any { it == null }) return null
                        @Suppress("UNCHECKED_CAST")
                        return converted as T
                    }
                }
            }
        }
        null
    } catch (_: Exception) {
        null
    }
}

fun <T> T.encodeBase36(): String {
    val jsonString = Gson().toJson(this)
    return Codec.encodeBase36(jsonString)
}