package com.ngockieubao.orderapp.data

import java.io.Serializable

data class Category(
    val logo: String,
    val name: String
) : Serializable {
    var id: Int = 1
}
