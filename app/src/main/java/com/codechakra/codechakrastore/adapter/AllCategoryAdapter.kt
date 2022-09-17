package com.codechakra.codechakrastore.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codechakra.codechakrastore.activity.AllProductActivity
import com.codechakra.codechakrastore.activity.CategoryActivity
import com.codechakra.codechakrastore.databinding.AllCategoryItemLayoutBinding

import com.codechakra.codechakrastore.model.CategoryModel

class AllCategoryAdapter(val context: Context, val list: ArrayList<CategoryModel>) :
    RecyclerView.Adapter<AllCategoryAdapter.AllCategoryViewHolder>() {

    inner class AllCategoryViewHolder(val binding: AllCategoryItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllCategoryViewHolder =
        AllCategoryViewHolder(
            AllCategoryItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: AllCategoryViewHolder, position: Int) {
        val category = list[position]
        Glide.with(holder.binding.categoryImg).load(category.img).into(holder.binding.categoryImg)
        holder.binding.categoryName.text = category.cate
        holder.binding.root.setOnClickListener {
            val intent = Intent(context, AllProductActivity :: class.java)
            intent.putExtra("cat",list[position].cate)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = list.size
}