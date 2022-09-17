package com.codechakra.codechakrastore.fragment

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.codechakra.codechakrastore.R
import com.codechakra.codechakrastore.activity.AllCategoryActivity
import com.codechakra.codechakrastore.activity.AllProductActivity
import com.codechakra.codechakrastore.activity.CategoryActivity
import com.codechakra.codechakrastore.adapter.CategoryAdapter
import com.codechakra.codechakrastore.adapter.ProductAdapter
import com.codechakra.codechakrastore.databinding.FragmentHomeBinding
import com.codechakra.codechakrastore.model.AddProductModel
import com.codechakra.codechakrastore.model.CategoryModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var dialog:Dialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        dialog = Dialog(requireActivity())
        dialog.setContentView(R.layout.progress_layout)
        dialog.setCancelable(false)
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()
        getSliderImages()
        getProducts()
        getCategories()
        binding.seeAllProduct.setOnClickListener {

            val intent = Intent(context, AllProductActivity :: class.java)
            startActivity(intent)
        }
        binding.seeAllCategory.setOnClickListener {
            val intent = Intent(context, AllCategoryActivity :: class.java)
            startActivity(intent)
        }

        val preferences = requireActivity().getSharedPreferences("info",AppCompatActivity.MODE_PRIVATE)

        if (preferences.getBoolean("isCart", false)){
            findNavController().navigate(R.id.action_homeFragment_to_cartFragment)
        }
        return binding.root
    }

    private fun getSliderImages() {
        Firebase.firestore.collection("slider").document("item")
            .get()
            .addOnSuccessListener {
                Glide.with(requireActivity()).load(it.get("img")).into(binding.sliderImage)
            }
    }

    private fun getCategories() {
        val list = ArrayList<CategoryModel>()
        Firebase.firestore.collection("categories")
            .get().addOnSuccessListener {
                list.clear()
                for (doc in it){
                    val data = doc.toObject(CategoryModel::class.java)
                    list.add(data)
                }
                binding.categoryRecycler.adapter = CategoryAdapter(requireActivity(), list)
                dialog.dismiss()
            }.addOnFailureListener {
                dialog.dismiss()
            }
    }

    private fun getProducts(){

        val list = ArrayList<AddProductModel>()
        Firebase.firestore.collection("products")
            .get().addOnSuccessListener {
                list.clear()
                for (doc in it){
                    val data = doc.toObject(AddProductModel::class.java)
                    list.add(data)
                }
                binding.productRecycler.adapter = ProductAdapter(requireActivity(), list)
            }
    }


}