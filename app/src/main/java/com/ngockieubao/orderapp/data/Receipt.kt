package com.ngockieubao.orderapp.data

import java.io.Serializable

data class Receipt(
    val name: String,
    val contact: String,
    val note: String,
    val price: Double,
    val address: String
) : Serializable {
    constructor() : this("", "", "", 0.0, "")

    fun toHashMap(): HashMap<String, Any?> {
        return hashMapOf(
            "name" to name,
            "contact" to contact,
            "note" to note,
            "price" to price,
            "address" to address
        )
    }
}
