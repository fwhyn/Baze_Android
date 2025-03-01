package com.fwhyn.baze.data.helper

import com.fwhyn.baze.data.model.Exzeption
import com.fwhyn.baze.data.model.Status
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.UUID

object Util {

    @JvmStatic
    fun getUniqueId(): String {
        val uuid = UUID.randomUUID().toString()
        return uuid.filterNot { it == '-' }
    }

    @JvmStatic
    fun getDateTime(): String {
        // Get the current date and time
        val currentDateTime = Calendar.getInstance().time

        // Define the desired date and time format
        val dateFormat = SimpleDateFormat("yyyy-MM-dd-HHmmss", Locale.getDefault())

        // Format the date and time
        return dateFormat.format(currentDateTime)
    }

    @JvmStatic
    fun throwExceptionIfOutOfRange(value: Double, range: ClosedRange<Double>) {
        require(
            value >= range.start && value <= range.endInclusive,
            Exzeption(Status.SettingError, Throwable("Outbound Value"))
        )
    }

    @JvmStatic
    fun throwTestMustNotFailed() {
        throw Exception("Test must not failed")
    }

    @JvmStatic
    fun throwMustNoLoading() {
        throw Exception("Test must no loading")
    }

    @JvmStatic
    fun throwMustNotSuccess() {
        throw Exception("Test must not success")
    }

    @JvmStatic
    fun require(isOk: Boolean, exception: Exzeption = Exzeption()) {
        if (!isOk) throw exception
    }
}