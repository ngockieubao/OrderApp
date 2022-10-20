package com.ngockieubao.orderapp.data

data class Product(
    val description: String,
    val expiry: String,
    val name: String,
    val price: Double,
    val quantity: Int,
    val rating: Double,
    val type: String,
    val url: String,
    val weight: String
) {
    var id: Int = 1
}
