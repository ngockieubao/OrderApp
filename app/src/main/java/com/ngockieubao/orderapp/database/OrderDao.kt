package com.ngockieubao.orderapp.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.ngockieubao.orderapp.data.Order
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(order: Order)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(list: List<Order>)

    @Update
    suspend fun update(order: Order)

    @Delete
    suspend fun delete(order: Order)

    @Query("DELETE FROM order_table")
    fun clear()

    @Query("SELECT * FROM order_table")
    fun getAllOrder(): Flow<List<Order>>

    @Query("SELECT * FROM order_table")
    suspend fun getOrders(): List<Order>

    @Query("UPDATE order_table SET quantity =:quantity WHERE name =:name")
    suspend fun increasing(name: String, quantity: Int)

    @Query("UPDATE order_table SET quantity =:quantity WHERE name =:name")
    suspend fun decreasing(name: String, quantity: Int)
}