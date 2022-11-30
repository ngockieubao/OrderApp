package com.ngockieubao.orderapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "order_table")
data class Order(
    var url: String,
    var name: String,
    var description: String,
    var price: Double,
    var quantity: Int
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 1

    constructor() : this("", "", "", 0.0, 0)

    fun toHashMap(): HashMap<String, Any?> {
        return hashMapOf(
            "id" to id,
            "url" to url,
            "name" to name,
            "description" to description,
            "price" to price,
            "quantity" to quantity
        )
    }
}
