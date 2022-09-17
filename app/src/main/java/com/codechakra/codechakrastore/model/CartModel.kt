package com.codechakra.codechakrastore.model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class CartModel(
    @PrimaryKey
    @NonNull
    val cartId: String ,
    val productName: String? = "",
    val productDescription: String? = "",
    val productCoverImg: String? = "",
    val productCategory: String? = "",
    val productId: String? = "",
    val productMrp: String? = "",
    val productSp: String? = "",
    val inStock: Long? = 0,
   // val productImages: String? = "" ,
    val userId: String? = ""
)
