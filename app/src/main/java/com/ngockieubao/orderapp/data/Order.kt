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
    var id: Int = 0
}
