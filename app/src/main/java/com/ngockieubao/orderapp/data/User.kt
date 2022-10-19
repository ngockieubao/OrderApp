package com.ngockieubao.orderapp.data

import java.io.Serializable

data class User(
    val userID: String?,
    val username: String?,
    val email: String?,
    val photoUrl: String?,
    val address: String?,
    val contact: Int?
) : Serializable {
    constructor() : this("", "", "", "", "", 0)

    fun toHashMap(): HashMap<String, Any?> {
        return hashMapOf(
            "userID" to userID,
            "username" to username,
            "email" to email,
            "photoUrl" to photoUrl,
            "address" to address,
            "contact" to contact
        )
    }
}