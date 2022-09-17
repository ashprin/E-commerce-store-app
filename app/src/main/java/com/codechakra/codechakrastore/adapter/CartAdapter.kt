package com.codechakra.codechakrastore.adapter

import android.content.Context
import android.content.Intent
import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codechakra.codechakrastore.activity.ProductDetailsActivity
import com.codechakra.codechakrastore.databinding.LayoutCartItemBinding
import com.codechakra.codechakrastore.databinding.LayoutProductItemBinding
import com.codechakra.codechakrastore.model.CartModel
import com.codechakra.codechakrastore.roomdb.AppDatabase
import com.codechakra.codechakrastore.roomdb.ProductModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.okhttp.Dispatcher
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CartAdapter(val context: Context, val list: List<CartModel>) :
    RecyclerView.Adapter<CartAdapter.CartViewHolder>() {
    inner class CartViewHolder(val binding: LayoutProductItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = LayoutProductItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return CartViewHolder(binding)
    }

    override fun getItemCount(): Int = list.size


    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val data = list[position]

        Glide.with(context).load(data.productCoverImg).into(holder.binding.imageView2)
        holder.binding.productName.text = data.productName
        holder.binding.productCategory.text = data.productCategory
        holder.binding.productPrice.text = "Rs.${data.productMrp}"
        holder.binding.priceIndBt.text = "Rs.${data.productSp}"

        holder.binding.orderNowBt.setOnClickListener {
            val intent = Intent(context, ProductDetailsActivity::class.java)
            intent.putExtra("cartId",list[position].cartId)
            intent.putExtra("id", list[position].productId)
            context.startActivity(intent)
        }
        holder.binding.deleteBt.visibility = View.VISIBLE
        holder.binding.deleteBt.setOnClickListener {
            val cartItem = list[position]
            Firebase.firestore.collection("cart").document(cartItem.cartId).delete()
                .addOnSuccessListener {
                    val dao = AppDatabase.getInstance(context).productDao()
                    GlobalScope.launch (Dispatchers.IO) {
                        dao.deleteProduct(cartItem)
                    }
                    Toast.makeText(context,"Item removed from cart!",Toast.LENGTH_SHORT)
                        .show()
                }.addOnFailureListener {
                    Toast.makeText(context,"Failed to remove from cart!",Toast.LENGTH_SHORT)
                        .show()
            }
        }

    }
}