package com.codechakra.codechakrastore

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.PopupMenu
import androidx.navigation.fragment.findNavController
import com.codechakra.codechakrastore.activity.LoginActivity
import com.codechakra.codechakrastore.databinding.ActivityMainBinding
import com.codechakra.codechakrastore.model.CartModel
import com.codechakra.codechakrastore.roomdb.AppDatabase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    object STORE{
        const val SHARED_PREF_USER = "user"
        const val USER_COLLECTION = "number"
        const val CART_COLLECTION = "cart"
    }
    private lateinit var binding: ActivityMainBinding
    var i = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (FirebaseAuth.getInstance().currentUser == null){
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        }

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer)
        val navController = navHostFragment!!.findNavController()

        val popupMenu = PopupMenu(this, binding.bottomBar)
        popupMenu.inflate(R.menu.bottom_nav)
        binding.bottomBar.setupWithNavController(popupMenu.menu, navController)
        navController.addOnDestinationChangedListener { controller, destination, arguments ->

            {
                title = when (destination.id) {
                    R.id.cartFragment -> "My Cart"
                    R.id.moreFragment -> "My Dashboard"
                    else -> "E store"


                }
            }

        }



        binding.bottomBar.onItemSelected = {
            when (it) {
                0 -> {
                    i = 0
                    navController.navigate(R.id.homeFragment)
                }
                1 -> i = 1
                2 -> i = 2
            }
        }
        val user = getSharedPreferences(STORE.SHARED_PREF_USER,MODE_PRIVATE)
        val userId = user.getString(STORE.USER_COLLECTION,"")
        val dao = AppDatabase.getInstance(this).productDao()
        //WE ARE FETCHING ITEMS FROM CART OF CURRENT USER
        //WE ALSO ADD THEM IN OUR LOCAL DATABASE
        Firebase.firestore.collection(STORE.CART_COLLECTION).whereEqualTo("userId",userId).get()
            .addOnSuccessListener {
                dao.deleteAllProducts()
                for (doc in it){
                    val product = CartModel(
                        doc.getString("cartId")!!,
                        doc.getString("productName"),
                        doc.getString("productDescription"),
                        doc.getString("productCoverImg"),
                        doc.getString("productCategory"),
                        doc.getString("productId"),
                        doc.getString("productMrp"),
                        doc.getString("productSp"),
                        doc.getLong("inStock"),
                        doc.getString("userId")
                        )
                       // doc.toObject(CartModel::class.java)
                    GlobalScope.launch (Dispatchers.IO){

                        dao.insertProduct(product)
                    }
                }
                //binding.cartRecycler.adapter = CartAdapter(requireContext(),list)
            }.addOnFailureListener {

            }
    }

    override fun onBackPressed() {
        super.onBackPressed()

        if (i == 0) {
            finish()
        }
    }
}