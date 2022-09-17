package com.codechakra.codechakrastore.model

import com.google.firebase.Timestamp

data class AllOrderModel(
    val productName: String? = "",
    val productCoverImage: String? = "",
    val orderId: String? = "",
    val userId: String? = "",
    val status: String? = "",
    val productId: String? = "",
    val price: String? = "",
    val addressType:String? = "",
    val orderedOn: Timestamp= Timestamp.now(),
    val deliveredOn: String?= "",
    val estimatedDelivery: String? = "",
    val quantityOrdered: Int? = 1
)