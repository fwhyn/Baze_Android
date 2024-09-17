package com.fwhyn.data.model

sealed class Status(val code: Int, val msg: String) {
    data object Success : Status(200, "Success")
    data object NotFound : Status(404, "Not Found")
    data object BadRequest : Status(400, "Bad Request")
    data object RequestTimeOut : Status(408, "Request Timeout")
    data object InitialState : Status(0, "Initial State")
    data class Instance(val statusCode: Int, var message: String) : Status(statusCode, message)
}