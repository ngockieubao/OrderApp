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
    const val NUMBER_PHONE = "0382320936"
    const val URL_YOUTUBE = "https://www.youtube.com/channel/UCPgRPx1ESky1aReceoUxEVw"

    fun getUsernameFromEmail(email: String?): String? {
        if (email == null) return null
        val s = email.split("@")
        return s[0].trim()
    }

    const val LIBRARY_PICKER = 12312
}