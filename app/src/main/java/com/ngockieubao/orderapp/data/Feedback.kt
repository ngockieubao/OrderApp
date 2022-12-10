package com.ngockieubao.orderapp.data

data class Feedback(
    val name: String,
    val contact: String,
    val email: String,
    val message: String
) {
    constructor() : this("", "", "", "")
}
