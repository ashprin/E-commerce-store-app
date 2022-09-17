package com.codechakra.codechakrastore.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.codechakra.codechakrastore.R
import com.codechakra.codechakrastore.databinding.ActivityOrderDetailsBinding
import com.codechakra.codechakrastore.model.AllOrderModel
import com.codechakra.codechakrastore.model.UserModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class OrderDetailsActivity : AppCompatActivity() {
private lateinit var binding: ActivityOrderDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar!!.title = "Order Details"
        val orderId = intent.getStringExtra("orderId")

        Firebase.firestore.collection("allOrders").document(orderId!!)
            .get().addOnSuccessListener {
                val orderDetail =  it.toObject(AllOrderModel::class.java)
                Glide.with(this).load(orderDetail!!.productCoverImage)
                    .into( binding.productCoverImg)
                binding.productNameTx.text = orderDetail.productName
                binding.orderStatusTx.text = orderDetail.status
                binding.orderedOnTx.text = orderDetail.orderedOn.toDate().toString()
                binding.quantityTx.text =  "Quantity ordered: ${orderDetail.quantityOrdered.toString()}"
                binding.totalAmountTx.text = "Total Amount paid: ₹ ${orderDetail.price}"
                Firebase.firestore.collection("users")
                    .document(orderDetail.userId!!).get()
                    .addOnSuccessListener {
                        val userInfo = it.toObject(UserModel::class.java)!!
                        if (orderDetail.addressType.equals("1")) {
                            binding.deliverAdTx.text = userInfo.name + ", " + userInfo.number + "\n" +
                                    userInfo.houseNo + ", " + userInfo.village + ", " + userInfo.city + ", " + userInfo.state + ", " + userInfo.pinCode
                        } else {
                            binding.deliverAdTx.text =
                                userInfo.al_houseNo + " " + userInfo.al_village + " " + userInfo.al_city + " " + userInfo.al_state + " " + userInfo.al_pinCode
                        }


                    }.addOnFailureListener {

                    }






            }.addOnFailureListener {

                Toast.makeText(this, "Failed to fetch order details!", Toast.LENGTH_SHORT)
                    .show()

            }
    }
}