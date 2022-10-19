package com.ngockieubao.orderapp.util

import java.util.regex.Pattern

object Constants {
    val EMAIL_ADDRESS_PATTERN: Pattern? = Pattern.compile(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
    )

    fun getUsernameFromEmail(email: String?): String? {
        if (email == null) return null
        val s = email.split("@")
        return s[0].trim()
    }
}