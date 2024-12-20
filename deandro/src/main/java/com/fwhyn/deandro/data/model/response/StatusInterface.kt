package com.fwhyn.deandro.data.model.response

// It does not work if we use:
// @get:SerializedName("status_code")
// @set:SerializedName("status_code")
// var statusCode: Int

// It also does not work if we use abstract class or open class

interface StatusInterface {
    val status_code: Int
    val message: String
}