package com.codechakra.codechakrastore.roomdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.codechakra.codechakrastore.model.CartModel

@Database(entities = [CartModel::class], version = 2)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        private var database: AppDatabase? = null
        private const val DATABASE_NAME = "estore"


        @Synchronized
        fun getInstance(context: Context): AppDatabase {
            if (database == null) {

                database = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME
                ).allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()

            }

            return database!!
        }
    }

    abstract fun productDao(): ProductDao
}