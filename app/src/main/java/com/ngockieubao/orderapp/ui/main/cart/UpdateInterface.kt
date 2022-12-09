package com.ngockieubao.orderapp.ui.main.cart

import com.ngockieubao.orderapp.data.Order

interface UpdateInterface {
    fun deleteItemOrder(order: Order?)
    fun increaseQuantity(name: String, quantity: Int)
    fun decreaseQuantity(name: String, quantity: Int)
}