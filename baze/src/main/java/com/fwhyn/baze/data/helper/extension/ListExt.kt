package com.fwhyn.baze.data.helper.extension

// ----------------------------------------------------------------
fun <T> Set<T>.copy(): Set<T> {
    return HashSet(this)
}

fun <T> List<T>.copy(): List<T> {
    return ArrayList(this)
}

fun <T> List<T>.deepCopy(onCopyElement: (T) -> T): List<T> {
    return this.map { onCopyElement(it) }
}

fun <T> List<T>.set(index: Int, data: T): List<T> {
    val newList = ArrayList(this)
    newList[index] = data

    return newList
}

fun <T> List<T>.add(data: T): List<T> {
    val newList = ArrayList(this)
    newList.add(data)

    return newList
}

fun <T> List<T>.clear(): List<T> {
    return listOf()
}

// ----------------------------------------------------------------
fun <T> Int.createList(onAdd: (index: Int) -> T): List<T> {
    val output = ArrayList<T>()

    for (i in 0 until this) {
        output.add(onAdd(i))
    }

    return output
}