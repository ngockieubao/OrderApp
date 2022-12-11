package com.ngockieubao.orderapp.data

data class Admin(
    val username: String,
    val passwd: String
) {
    constructor() : this("", "")
}
