package com.ngockieubao.orderapp.util

object TextUtils {
    fun isValidEmail(email: String?): Boolean {
        if (email == null) return false
        return Constants.EMAIL_ADDRESS_PATTERN!!.matcher(email).matches()
    }

    fun checkEmailPasswdNull(email: String?, password: String?): Boolean {
        return email == null || password == null
    }

    fun checkPassword(password: String?): Boolean {
        if (password == null) return false
        return password.length >= 6
    }

    fun checkEdtNull(input: String?): Boolean {
        return input == null
    }

    fun checkPhoneNumber(input: String?): Boolean {
        return input == null
    }
}