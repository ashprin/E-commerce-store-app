package com.codechakra.codechakrastore.model

data class AddProductModel(
    val productName: String? = "",
    val productDescription: String? = "",
    val productCoverImg: String? = "",
    val productCategory: String? = "",
    val productId:String? = "",
    val productMrp: String? = "",
    val productSp: String? = "",
    val inStock: Long? = 0,
    val productImages: ArrayList<String> = ArrayList()
)