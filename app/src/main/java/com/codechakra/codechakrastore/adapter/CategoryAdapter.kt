package com.codechakra.codechakrastore.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codechakra.codechakrastore.R
import com.codechakra.codechakrastore.activity.AllProductActivity
import com.codechakra.codechakrastore.activity.CategoryActivity
import com.codechakra.codechakrastore.databinding.LayoutCategoryItemBinding
import com.codechakra.codechakrastore.model.CategoryModel


class CategoryAdapter(var context: Context, var list: ArrayList<CategoryModel>) :
    RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    inner class CategoryViewHolder( view: View) : RecyclerView.ViewHolder(view) {
        var binding = LayoutCategoryItemBinding.bind(view)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(
            LayoutInflater.from(context)
                .inflate(R.layout.layout_category_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.binding.textView.text = list[position].cate
        Glide.with(context).load(list[position].img).into(holder.binding.imageView)
        holder.binding.root.setOnClickListener {
            val intent = Intent(context,AllProductActivity :: class.java)
            intent.putExtra("cat",list[position].cate)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}