package com.codechakra.codechakrastore.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.codechakra.codechakrastore.R
import com.codechakra.codechakrastore.adapter.CategoryProductAdapter
import com.codechakra.codechakrastore.adapter.ProductAdapter
import com.codechakra.codechakrastore.model.AddProductModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CategoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)
        getProducts(intent.getStringExtra("cat"));
    }

    private fun getProducts(category: String?) {

        val list = ArrayList<AddProductModel>()
        Firebase.firestore.collection("products").whereEqualTo("productCategory",category)
            .get().addOnSuccessListener {
                list.clear()
                for (doc in it){
                    val data = doc.toObject(AddProductModel::class.java)
                    list.add(data)
                }
                val recycler = findViewById<RecyclerView>(R.id.recyclerView)
                recycler.adapter = CategoryProductAdapter(this, list)
            }
    }
}