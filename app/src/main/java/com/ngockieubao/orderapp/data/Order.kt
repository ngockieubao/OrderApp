package com.ngockieubao.orderapp.data

data class Order(
    val url: String,
    val name: String,
    val description: String,
    val price: Double,
    val quantity: Int
) {
    var id: Int = 1
}
