package com.codechakra.codechakrastore.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.codechakra.codechakrastore.R
import com.codechakra.codechakrastore.activity.LoginActivity
import com.codechakra.codechakrastore.adapter.AllOrderAdapter
import com.codechakra.codechakrastore.databinding.FragmentMoreBinding
import com.codechakra.codechakrastore.model.AllOrderModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MoreFragment : Fragment() {
    private lateinit var binding: FragmentMoreBinding
    private lateinit var list: ArrayList<AllOrderModel>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMoreBinding.inflate(layoutInflater)
        val preferences =
            requireActivity().getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE)
        val userId = preferences.getString("number", "")
        Firebase.firestore.collection("users").document(userId!!)
            .get().addOnSuccessListener {
            binding.userNameTx.text = it.getString("name")
        }
        binding.logOutBt.setOnClickListener {
            Firebase.auth.signOut()
            startActivity(Intent(requireContext(), LoginActivity::class.java))
        }
        list = ArrayList()
        Firebase.firestore.collection("allOrders").whereEqualTo("userId", userId)
            .get().addOnSuccessListener {
                list.clear()
                for (doc in it) {
                    val data = doc.toObject(AllOrderModel::class.java)
                    list.add(data)
                }
                binding.recyclerView.adapter = AllOrderAdapter(list, requireContext())
            }


        return binding.root
    }

}