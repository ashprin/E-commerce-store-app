package com.codechakra.codechakrastore.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.codechakra.codechakrastore.MainActivity
import com.codechakra.codechakrastore.databinding.ActivityCheckoutBinding
import com.codechakra.codechakrastore.model.AddProductModel
import com.codechakra.codechakrastore.model.UserModel
import com.codechakra.codechakrastore.roomdb.AppDatabase
import com.codechakra.codechakrastore.roomdb.ProductModel
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class CheckoutActivity : AppCompatActivity(), PaymentResultListener {
    private lateinit var binding: ActivityCheckoutBinding
    private lateinit var productObj: AddProductModel
    var addressOne: String? = ""
    private var proId: String? = ""
    var price:Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
      //  intentAddressAct.putExtra("totalCost",cartItem.productSp)
        proId = intent.getStringExtra("productId")
        val preferences = getSharedPreferences("user", MODE_PRIVATE)
        val number = preferences.getString("number", "")

        Firebase.firestore.collection("users").document(number!!)
            .get().addOnSuccessListener {
                val userInfo = it.toObject(UserModel::class.java)
                fetchOrderDetails(userInfo!!, proId!!)

            }.addOnFailureListener {
                Toast.makeText(this, "Check internet connection! ", Toast.LENGTH_LONG).show()
            }

        binding.returnBt.setOnClickListener {
          val returnIntent = Intent(this,MainActivity::class.java)
            returnIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(returnIntent)
            finish()
        }
    }

    private fun fetchOrderDetails(userInfo: UserModel, proId: String) {
        Firebase.firestore.collection("products").document(proId)
            .get().addOnSuccessListener {
                productObj = it.toObject(AddProductModel::class.java)!!
                addressOne = intent.getStringExtra("addressTwo")
                Glide.with(this).load(productObj.productCoverImg).into(binding.productImage)
                binding.productNameTx.text = productObj.productName
                binding.productPriceTx.text = productObj.productSp
                price =  productObj.productSp!!.toInt()
              //  binding.quantityTx.text = "Available in stock: ${productObj.inStock}"
                binding.productDescriptionTx.text = productObj.productDescription
                binding.totalAmountTx.text = "Rs.${productObj.productSp}"
                if (addressOne.equals("1")) {
                    binding.deliverAdTx.text = userInfo.name + ", " + userInfo.number + "\n" +
                            userInfo.houseNo + ", " + userInfo.village + ", " + userInfo.city + ", " + userInfo.state + ", " + userInfo.pinCode
                } else {
                    binding.deliverAdTx.text =
                        userInfo.al_houseNo + " " + userInfo.al_village + " " + userInfo.al_city + " " + userInfo.al_state + " " + userInfo.al_pinCode
                }
                binding.placeOrderPayBt.setOnClickListener {
                    placeOrderPay()
                }

            }.addOnFailureListener {
                Toast.makeText(this, "Check internet connection! ", Toast.LENGTH_LONG).show()
            }
    }

    private fun placeOrderPay() {
        val co = Checkout()

        co.setKeyID("rzp_test_JbFULEGaLzoUs3")
       // val price = intent.getIntExtra("totalCost", 5000)
        try {
            val options = JSONObject()
            options.put("name", "Akshay Vilas Shinde")
            options.put("description", "Online store")
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png")
            options.put("theme.color", "#3399cc")
            options.put("currency", "INR")

            options.put("amount", (price * 100).toString())//pass amount in currency subunits

            val prefill = JSONObject()
            prefill.put("email", "akshayvshinde98@gmail.com")
            prefill.put("contact", "7028169971")

            options.put("prefill", prefill)
            co.open(this, options)
        } catch (e: Exception) {
            Toast.makeText(this, "Error in payment: " + e.message, Toast.LENGTH_LONG).show()

        }
    }

    override fun onPaymentSuccess(p0: String?) {
        Toast.makeText(this, "Payment Success!", Toast.LENGTH_LONG).show()
        saveData(productObj.productName, productObj.productSp, productObj.productId!!, addressOne,productObj.productCoverImg)
        //uploadData()
    }

    private fun uploadData() {
        val id = intent.getStringArrayListExtra("productIds")

        for (currentId in id!!) {
            fetchData(currentId)
        }
    }
    //private fun upload

    private fun fetchData(productId: String?) {
        val dao = AppDatabase.getInstance(this).productDao()
        lifecycleScope.launch(Dispatchers.IO) {

           // dao.deleteProduct(ProductModel(productId = productId!!))
        }
//
    }

    private fun saveData(
        productName: String?,
        productSp: String?,
        productId: String,
        addressType: String?,
        productCoverImage:String?
    ) {
        val preferences = getSharedPreferences("user", MODE_PRIVATE)
        val data = hashMapOf<String, Any>()
        data["productName"] = productName!!
        data["price"] = productSp!!
        data["productId"] = productId
        data["status"] = "Ordered"
        data["userId"] = preferences.getString("number", "")!!
        data["addressType"] = addressType!!
        data["productCoverImage"] = productCoverImage!!
        data["orderedOn"] = Timestamp.now()

        val firestore = Firebase.firestore.collection("allOrders")
        val key = firestore.document().id
        data["orderId"] = key

        firestore.document(key).set(data)
            .addOnSuccessListener {
                Toast.makeText(this, "Order placed!", Toast.LENGTH_LONG).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()

            }
            .addOnFailureListener {
                Toast.makeText(this, "Something went wrong!", Toast.LENGTH_LONG).show()

            }
    }

    override fun onPaymentError(p0: Int, p1: String?) {
        Log.d("PayError", "Error : $p1")
        Toast.makeText(this, "Something went wrong!", Toast.LENGTH_LONG).show()
    }

//    private fun storeOrderDetails(){
//        Firebase.firestore.collection("allOrders").document().
//    }
}