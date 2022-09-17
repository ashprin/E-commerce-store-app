package com.codechakra.codechakrastore.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.codechakra.codechakrastore.MainActivity
import com.codechakra.codechakrastore.databinding.ActivityProductDetailsBinding
import com.codechakra.codechakrastore.model.CartModel
import com.codechakra.codechakrastore.roomdb.AppDatabase
import com.codechakra.codechakrastore.roomdb.ProductDao
import com.codechakra.codechakrastore.roomdb.ProductModel
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductDetailsBinding
    private lateinit var cartItem: CartModel
    val productDao = AppDatabase.getInstance(this).productDao()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getProductDetails(intent.getStringExtra("id"))


    }

    private fun getProductDetails(proId: String?) {
        val user = getSharedPreferences("user", MODE_PRIVATE)
        val userId = user.getString("number", "")

        Firebase.firestore.collection("products").document(proId!!)
            .get().addOnSuccessListener {

                val list = it.get("productImages") as ArrayList<String>
                val name = it.getString("productName")
                val productSp = "₹ ${it.getString("productSp")}"
                val productDescription = it.getString("productDescription")
                val cartProduct = Firebase.firestore.collection("cart").document()
                cartItem = CartModel(
                    cartProduct.id,
                    name, productDescription,
                    it!!.getString("productCoverImg"),
                    it.getString("productCategory"),
                    it.getString("productId"),
                    it.getString("productMrp"),
                    it.getString("productSp"),
                    it.getLong("inStock"),
                    userId

                )
                binding.productTitleTx.text = name
                binding.productPriceTx.text = productSp
                binding.productInfoTx.text = productDescription
                binding.inStockTx.text = "In stock: ${it.getLong("inStock").toString()}"
                binding.orderNowBt.setOnClickListener {
                    val intentAddressAct = Intent(this, AddressActivity::class.java)
                    intentAddressAct.putExtra("productId", proId)
                    //intentAddressAct.putExtra("totalCost",cartItem.productSp)
                    startActivity(intentAddressAct)
                }
                cartAction(
                    cartItem
                )

                val slideList = ArrayList<SlideModel>()

                for (data in list) {
                    slideList.add(SlideModel(data, ScaleTypes.CENTER_INSIDE))
                }


                // cartAction(proId, name, productSp, it.getString("productCoverImg"))
                binding.imageSlider.setImageList(slideList)
            }
            .addOnFailureListener {
                Toast.makeText(this, "Something went wrong!", Toast.LENGTH_SHORT)
                    .show()
            }
    }

    private fun cartAction(cartItem: CartModel) {


        setBtText()

        binding.AddToCartBt.setOnClickListener {
            if (productDao.isExists(cartItem.productId!!) != null) {
                openCart()
            } else {
                addToCard(cartItem)
            }

        }


    }

    private fun setBtText() {
        if (productDao.isExists(cartItem.productId!!) != null) {
            binding.AddToCartBt.text = "Go to Cart"
        } else {
            binding.AddToCartBt.text = "Add to Cart"

        }
    }

    private fun addToCard(
        cartItem: CartModel
    ) {

        if (productDao.isExists(cartItem.productId!!) == null) {
            Firebase.firestore.collection("cart")
                .document(cartItem.cartId).set(cartItem)
                .addOnSuccessListener {
                    lifecycleScope.launch(Dispatchers.IO) {

                        productDao.insertProduct(cartItem)
                        binding.AddToCartBt.text = "Go to Cart"
                    }

                }.addOnFailureListener {
                    Toast.makeText(this, "Failed to Add", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "Already exists", Toast.LENGTH_SHORT).show()
        }


    }

    private fun openCart() {
        val preferences = this.getSharedPreferences("info", MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putBoolean("isCart", true)
        editor.apply()

        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }


}