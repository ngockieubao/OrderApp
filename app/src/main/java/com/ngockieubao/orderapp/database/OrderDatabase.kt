package com.ngockieubao.orderapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ngockieubao.orderapp.data.Order

@Database(entities = [Order::class], version = 1, exportSchema = false)
abstract class OrderDatabase : RoomDatabase() {
    abstract fun getOrderDao(): OrderDao

    companion object {
        @Volatile
        private var instance: OrderDatabase? = null

        fun getDatabase(context: Context): OrderDatabase {
            if (instance == null) {
                instance =
                    Room.databaseBuilder(
                        context,
                        OrderDatabase::class.java,
                        "order_database"
                    ).build()
            }
            return instance!!
        }
    }
}