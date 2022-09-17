package com.codechakra.codechakrastore.roomdb

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.codechakra.codechakrastore.model.CartModel

@Dao
interface ProductDao {

    @Insert
    suspend fun insertProduct(product: CartModel)

    @Delete
    suspend fun deleteProduct(product: CartModel)

    @Query("SELECT * FROM products")
    fun getAllProducts(): LiveData<List<CartModel>>

    @Query("SELECT * FROM products")
    fun getAllProductsList(): List<CartModel>

    @Query("DELETE  FROM products")
    fun deleteAllProducts()

    @Query("SELECT * FROM products WHERE productId == :id")
    fun isExists(id: String) : CartModel?
}