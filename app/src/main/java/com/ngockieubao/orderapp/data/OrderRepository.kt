package com.ngockieubao.orderapp.data

import android.app.Application
import com.ngockieubao.orderapp.database.OrderDao
import com.ngockieubao.orderapp.database.OrderDatabase
import kotlinx.coroutines.flow.Flow

class OrderRepository(application: Application) {
    private val orderDao: OrderDao

    init {
        val orderDatabase: OrderDatabase = OrderDatabase.getDatabase(application)
        orderDao = orderDatabase.getOrderDao()
    }

    suspend fun insertOrder(order: Order) = orderDao.insert(order)

    suspend fun deleteOrder(order: Order) = orderDao.delete(order)

     fun clear() = orderDao.clear()

    fun getAllOrder(): Flow<List<Order>> = orderDao.getAllOrder()

    suspend fun getOrders(): List<Order> = orderDao.getOrders()

    suspend fun increasing(name: String, quantity: Int) = orderDao.increasing(name, quantity)

    suspend fun decreasing(name: String, quantity: Int) = orderDao.decreasing(name, quantity)
}