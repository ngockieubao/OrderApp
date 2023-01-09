package com.ngockieubao.orderapp.data

import java.io.Serializable

data class Product(
    val category: Int,
    val delivery: String,
    val description: String,
    val docID: String,
    val expiry: String,
    val instock: String,
    val name: String,
    val price: Double,
    val rating: Double,
    val sold: Int,
    val type: String,
    val url: String,
    val weight: String
) : Serializable {
    var id: Int = 1

    constructor() : this(0, "", "", "", "", "", "", 0.0, 0.0, 0, "", "", "")

    fun toHashMap(): HashMap<String, Any?> {
        return hashMapOf(
            "category" to category,
            "delivery" to delivery,
            "description" to description,
            "docID" to docID,
            "expiry" to expiry,
            "instock" to instock,
            "name" to name,
            "price" to price,
            "rating" to rating,
            "sold" to sold,
            "type" to type,
            "url" to url,
            "weight" to weight
        )
    }
}
