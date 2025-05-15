package com.fwhyn.lib.baze.common.data.helper

import com.fwhyn.lib.baze.common.data.model.Exzeption
import com.fwhyn.lib.baze.common.data.model.Status
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
    fun throwExceptionIfBelowZero(value: Int) {
        require(
            value <= 0,
            Exzeption(Status.SettingError, Throwable("Value must be greater than 0"))
        )
    }

    @JvmStatic
    fun throwMustNotFailed() {
        throw Exception("Must not failed")
    }

    @JvmStatic
    fun throwMustNoLoading() {
        throw Exception("Must no loading")
    }

    @JvmStatic
    fun throwMustNotSuccess() {
        throw Exception("Must not success")
    }

    @JvmStatic
    fun require(isOk: Boolean, exception: Exzeption = Exzeption()) {
        if (!isOk) throw exception
    }
}