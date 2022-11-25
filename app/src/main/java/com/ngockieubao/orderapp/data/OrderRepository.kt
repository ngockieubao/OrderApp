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

    suspend fun insertListOrder(list: List<Order>) = orderDao.insertList(list)

    suspend fun deleteOrder(order: Order) = orderDao.delete(order)

    fun getAllOrder(): Flow<List<Order>> = orderDao.getAllOrder()

    suspend fun fullOrder(): List<Order> = orderDao.fullOrder()
}