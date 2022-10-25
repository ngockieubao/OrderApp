package com.ngockieubao.orderapp.data

import java.io.Serializable

data class Product(
    val description: String,
    val expiry: String,
    val instock: String,
    val name: String,
    val price: Double,
    val rating: Double,
    val type: String,
    val url: String,
    val weight: String
) : Serializable {
    var id: Int = 1

    constructor() : this("", "", "", "", 0.0, 0.0, "", "", "")
}