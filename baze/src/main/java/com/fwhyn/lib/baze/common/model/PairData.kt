package com.fwhyn.lib.baze.common.model

import android.util.Log
import com.fwhyn.lib.baze.common.helper.extension.getDebugTag
import java.io.Serializable

open class PairData<A, B>(
    val first: A,
    val second: B,
) : Serializable {
    override fun toString(): String = "($first, $second)"
}

private val debugTag = PairData::class.java.getDebugTag()

fun <Key, Data> PairData<Key, Data>.copy(
    first: Key = this.first,
    second: Data = this.second,
): PairData<Key, Data> {
    return PairData(first, second)
}

fun <Key, Data> List<PairData<Key, Data>>.replace(
    data: PairData<Key, Data>,
): List<PairData<Key, Data>> {
    val newData = ArrayList(this)
    for (i in this.indices) {
        if (this[i].first == data.first) {
            try {
                Log.d(debugTag, "pair data found: ${data.first}")

                newData[i] = data
            } catch (e: Exception) {
                Log.d(debugTag, "replace pair data error")

                // Do nothing
            }
            break
        }
    }

    return newData
}

fun <Key, Data> List<PairData<Key, Data>>.get(
    key: Key,
): PairData<Key, Data>? {
    var result: PairData<Key, Data>? = null
    for (item in this) {
        if (item.first == key) {
            result = item
            break
        }
    }

    return result
}

/**
 * Add data with specified key.
 *
 * Ignore if the key is already exist
 */
fun <Key, Data> List<PairData<Key, Data>>.set(
    data: PairData<Key, Data>,
    forceSet: Boolean = false,
): List<PairData<Key, Data>> {
    val result: ArrayList<PairData<Key, Data>> = ArrayList(this)
    var addData = true
    var i = 0
    for (item in this) {
        if (item.first === data.first || item.first == data.first) {
            addData = false
            break
        }
        i++
    }

    if (!addData && forceSet) {
        result[i] = data
    }
    if (this.isEmpty() || addData) {
        result.add(data)
    }

    return result
}

fun <Key, Data> List<PairData<Key, Data>>.remove(
    key: Key,
): List<PairData<Key, Data>> {
    val result = ArrayList(this)
    for (item in this) {
        if (item.first == key) {
            result.remove(item)
            break
        }
    }

    return result
}