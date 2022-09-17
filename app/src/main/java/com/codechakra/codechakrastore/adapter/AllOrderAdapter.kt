package com.codechakra.codechakrastore.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codechakra.codechakrastore.activity.OrderDetailsActivity
import com.codechakra.codechakrastore.databinding.AllOrderItemLayoutBinding
import com.codechakra.codechakrastore.model.AllOrderModel

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AllOrderAdapter(val list: ArrayList<AllOrderModel>, val context: Context) :
    RecyclerView.Adapter<AllOrderAdapter.AllOrderViewHolder>() {
    inner class AllOrderViewHolder(val binding: AllOrderItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllOrderViewHolder {
        return (
                AllOrderViewHolder(
                    AllOrderItemLayoutBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
                )
    }

    override fun onBindViewHolder(holder: AllOrderViewHolder, position: Int) {
        holder.binding.productName.text = list[position].productName
        holder.binding.productPrice.text = list[position].price
        holder.binding.productPrice.text = list[position].status
        holder.binding.productOrderDt.text = list[position].orderedOn.toDate().toString()
        Glide.with(context).load(list[position].productCoverImage)
            .into(holder.binding.productCoverImg)

        holder.itemView.setOnClickListener {
            val intent = Intent(context,OrderDetailsActivity::class.java)
            intent.putExtra("orderId",list[position].orderId)
            context.startActivity(intent)
        }


    }

    private fun updateStatus(str: String, doc: String) {
        val data = hashMapOf<String, Any>()
        data["status"] = str
        Firebase.firestore.collection("allOrders")
            .document(doc)
            .update(data)
            .addOnSuccessListener {
                Toast.makeText(context, "Success on changing status", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {

                Toast.makeText(context, "Failure on changing status", Toast.LENGTH_SHORT).show()
            }
    }

    override fun getItemCount(): Int = list.size
}