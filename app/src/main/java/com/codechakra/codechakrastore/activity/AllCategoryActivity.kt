package com.codechakra.codechakrastore.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.codechakra.codechakrastore.R
import com.codechakra.codechakrastore.adapter.AllCategoryAdapter
import com.codechakra.codechakrastore.databinding.ActivityAllCategoryBinding
import com.codechakra.codechakrastore.model.CategoryModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AllCategoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAllCategoryBinding
    private lateinit var list: ArrayList<CategoryModel>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar!!.title = "Category"
        list = ArrayList()
        Firebase.firestore.collection("categories").get().addOnSuccessListener {
            for (doc in it){
                val obj = doc!!.toObject(CategoryModel::class.java)
                list.add(obj)
            }
            binding.recyclerView.adapter = AllCategoryAdapter(this,list)
        }




    }
}