package com.fwhyn.data.helper

import com.fwhyn.data.model.Exzeption
import com.fwhyn.data.model.Status
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.UUID

object Util {

    fun getUniqueId(): String {
        val uuid = UUID.randomUUID().toString()
        return uuid.filterNot { it == '-' }
    }

    fun getDateTime(): String {
        // Get the current date and time
        val currentDateTime = Calendar.getInstance().time

        // Define the desired date and time format
        val dateFormat = SimpleDateFormat("yyyy-MM-dd-HHmmss", Locale.getDefault())

        // Format the date and time
        return dateFormat.format(currentDateTime)
    }

    fun throwExceptionIfOutOfRange(value: Double, range: ClosedRange<Double>) {
        require(
            value >= range.start && value <= range.endInclusive,
            Exzeption(Status.SettingError, Throwable("Outbound Value"))
        )
    }

    fun throwTestMustNotFailed() {
        throw Exception("Test must not failed")
    }

    fun throwMustNoLoading() {
        throw Exception("Test must no loading")
    }

    fun throwMustNotSuccess() {
        throw Exception("Test must not success")
    }

    fun require(isOk: Boolean, exception: Exzeption = Exzeption()) {
        if (!isOk) throw exception
    }
}