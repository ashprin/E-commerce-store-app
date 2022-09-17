package com.codechakra.codechakrastore.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.codechakra.codechakrastore.R
import com.codechakra.codechakrastore.activity.AddressActivity
import com.codechakra.codechakrastore.activity.CategoryActivity
import com.codechakra.codechakrastore.activity.ProductDetailsActivity
import com.codechakra.codechakrastore.adapter.CartAdapter
import com.codechakra.codechakrastore.adapter.ProductAdapter
import com.codechakra.codechakrastore.databinding.FragmentCartBinding
import com.codechakra.codechakrastore.model.CartModel
import com.codechakra.codechakrastore.roomdb.AppDatabase
import com.codechakra.codechakrastore.roomdb.ProductModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase


class CartFragment : Fragment() {

    private lateinit var binding: FragmentCartBinding
    private var total : Int =  0
    private lateinit var list: ArrayList<CartModel>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentCartBinding.inflate(layoutInflater)
        val preferences =
            requireActivity().getSharedPreferences("info", AppCompatActivity.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putBoolean("isCart", false)
        editor.apply()
        val user = requireActivity().getSharedPreferences("user",AppCompatActivity.MODE_PRIVATE);
        val userId = user.getString("number","");

        val dao = AppDatabase.getInstance(requireActivity()).productDao()
        list = ArrayList()
//        Firebase.firestore.collection("cart").whereEqualTo("userId",userId).get()
//            .addOnSuccessListener {
//                for (doc in it){
//                    val product =  doc.toObject(CartModel::class.java)
//                    list.add(product)
//                }
//                binding.cartRecycler.adapter = CartAdapter(requireContext(),list)
//            }.addOnFailureListener {
//
//            }


        dao.getAllProducts().observe(requireActivity()){
            binding.cartRecycler.adapter = CartAdapter(requireActivity(),it)
            list.clear()
            for (data in it){
                list.add(data)
            }
            //totalCost(it)
        }


        return binding.root
    }

    private fun totalCost(data : List<ProductModel>){

        for (item in data){
            total += item.productSp!!.toInt()
        }
        binding.textView12.text = "Total items in cart : ${data.size}"
        binding.textView13.text = "Total cost : $total"
        binding.checkout.setOnClickListener {
            val intent = Intent(context, AddressActivity::class.java)
            intent.putExtra("totalCost",total)
            startActivity(intent)
        }

    }
}