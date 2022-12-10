package com.ngockieubao.orderapp.data

import java.io.Serializable

data class Receipt(
    val address: String,
    val code: String,
    val contact: String,
    val name: String,
    val note: String,
    val receipts: List<Order>,
    val status: String,
    val time: String,
    val total: Double,
    val type: String
) : Serializable {

    var id: Int = 1

    constructor() : this(
        address = "",
        code = "",
        contact = "",
        name = "",
        note = "",
        receipts = emptyList(),
        status = "",
        time = "",
        total = 0.0,
        type = ""
    )

    fun toHashMap(): HashMap<String, Any?> {
        return hashMapOf(
            "address" to address,
            "code" to code,
            "contact" to contact,
            "name" to name,
            "note" to note,
            "receipts" to receipts,
            "status" to status,
            "time" to time,
            "total" to total,
            "type" to type
        )
    }
}
