package com.codechakra.codechakrastore.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.codechakra.codechakrastore.R
import com.codechakra.codechakrastore.adapter.ProductAdapter
import com.codechakra.codechakrastore.databinding.ActivityAllProductBinding
import com.codechakra.codechakrastore.model.AddProductModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import java.lang.Exception

class AllProductActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAllProductBinding
    private lateinit var list: ArrayList<AddProductModel>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllProductBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar!!.title = "Books"
        list = ArrayList()
        val cat = intent.getStringExtra("cat")
        if (cat != null){

            Firebase.firestore.collection("products").whereEqualTo("productCategory",cat).get()
                .addOnSuccessListener {
                    for (doc in it) {
                        val obj = doc.toObject(AddProductModel::class.java)
                        list.add(obj)

                    }
                    binding.recyclerView.adapter = ProductAdapter(this, list)

                }.addOnFailureListener {

                }


        }else{
            Firebase.firestore.collection("products").get()
                .addOnSuccessListener {
                    for (doc in it) {
                        val obj = doc.toObject(AddProductModel::class.java)
                        list.add(obj)

                    }
                    binding.recyclerView.adapter = ProductAdapter(this, list)

                }.addOnFailureListener {

                }
        }

    }
}