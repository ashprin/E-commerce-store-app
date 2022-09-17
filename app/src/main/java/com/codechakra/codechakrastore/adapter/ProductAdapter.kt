package com.codechakra.codechakrastore.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codechakra.codechakrastore.activity.ProductDetailsActivity
import com.codechakra.codechakrastore.databinding.LayoutCategoryItemBinding
import com.codechakra.codechakrastore.databinding.LayoutProductItemBinding
import com.codechakra.codechakrastore.model.AddProductModel

class ProductAdapter(val context: Context,val list: ArrayList<AddProductModel>) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>(){


    inner class ProductViewHolder(val binding: LayoutProductItemBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = LayoutProductItemBinding.inflate(LayoutInflater.from(context),parent,false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val data = list[position]

        Glide.with(context).load(data.productCoverImg).into(holder.binding.imageView2)
        holder.binding.productName.text = data.productName
        holder.binding.productCategory.text = data.productCategory
        holder.binding.productPrice.text = "Rs.${data.productMrp}"
        holder.binding.priceIndBt.text = "Rs.${data.productSp}"

        holder.binding.orderNowBt.setOnClickListener{
            val intent = Intent(context, ProductDetailsActivity::class.java)
            intent.putExtra("id",list[position].productId)
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int = list.size
}