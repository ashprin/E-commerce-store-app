package com.codechakra.codechakrastore.activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.codechakra.codechakrastore.databinding.ActivityAddressBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AddressActivity : AppCompatActivity() {
    private lateinit var preferences: SharedPreferences
    private lateinit var binding: ActivityAddressBinding
    var isOneTwo = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddressBinding.inflate(layoutInflater)

        setContentView(binding.root)

        preferences = getSharedPreferences("user", MODE_PRIVATE)

        loadUserInfo()
        binding.proceedAdOneBt.setOnClickListener {
            isOneTwo = true
            validateDataAdOne(
                binding.userName.text.toString(),
                binding.userNumber.text.toString(),
                binding.userPinCode.text.toString(),
                binding.userHouseNo.text.toString(),
                binding.userVillage.text.toString(),
                binding.userLandMark.text.toString(),
                binding.userCity.text.toString(),
                binding.userState.text.toString()
            )
        }

        binding.proceed.setOnClickListener {
            isOneTwo = false
            validateDataAdOne(
                binding.userNameAl.text.toString(),
                binding.userNumberAl.text.toString(),
                binding.userPinCodeAl.text.toString(),
                binding.userHouseNoAl.text.toString(),
                binding.userVillageAl.text.toString(),
                binding.userLandMarkAl.text.toString(),
                binding.userCityAl.text.toString(),
                binding.userStateAl.text.toString()
            )
        }
    }

    private fun validateDataAdOne(
        name: String,
        number: String,
        pinCode: String,
        houseNo: String,
        village: String,
        landMark: String,
        city: String,
        state: String
    ) {
        if (name.isEmpty() || number.isEmpty() || pinCode.isEmpty()
            || houseNo.isEmpty() || village.isEmpty() || landMark.isEmpty()
            || city.isEmpty() || state.isEmpty()
        ) {
            Toast.makeText(this, "Please enter all fields!", Toast.LENGTH_SHORT)
                .show()
        } else {
            if (isOneTwo)
            storeDataOne(name, number, pinCode, houseNo, village, landMark, city, state)
            else
                storeDataTwo(name, number, pinCode, houseNo, village, landMark, city, state)
        }
    }

    private fun storeDataOne(
        name: String,
        number: String,
        pinCode: String,
        houseNo: String,
        village: String,
        landMark: String,
        city: String,
        state: String
    ) {
        val map = hashMapOf<String, Any>()
        map["pinCode"] = pinCode
        map["houseNo"] = houseNo
        map["village"] = village
        map["landMark"] = landMark
        map["city"] = city
        map["state"] = state
        Firebase.firestore.collection("users")
            .document(preferences.getString("number", "").toString())
            .update(map)
            .addOnSuccessListener {
                val b = Bundle()
                b.putString("totalCost", intent.getStringExtra("totalCost"))
                b.putString("addressTwo","1")
                b.putString("productId",intent.getStringExtra("productId"))
                b.putStringArrayList("productIds", intent.getStringArrayListExtra("productIds"))
               // b.putInt("totalCost",intent.getIntExtra("totalCost",0))
                val intent1 = Intent(this, CheckoutActivity::class.java)
                intent1.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                intent1.putExtras(b)
                startActivity(intent1)
            }
            .addOnFailureListener {
                Toast.makeText(this, "Something went wrong!", Toast.LENGTH_SHORT)
                    .show()
            }
    }

    private fun storeDataTwo(
        name: String,
        number: String,
        pinCode: String,
        houseNo: String,
        village: String,
        landMark: String,
        city: String,
        state: String
    ) {
        val map = hashMapOf<String, Any>()
        map["al_name"] = name
        map["al_number"] = number
        map["al_pinCode"] = pinCode
        map["al_houseNo"] = houseNo
        map["al_village"] = village
        map["al_landMark"] = landMark
        map["al_city"] = city
        map["al_state"] = state
        Firebase.firestore.collection("users")
            .document(preferences.getString("number", "").toString())
            .update(map)
            .addOnSuccessListener {
                val b = Bundle()
                b.putString("totalCost", intent.getStringExtra("totalCost"))
                b.putString("productId",intent.getStringExtra("productId"))
                b.putString("addressTwo","2")
                b.putStringArrayList("productIds", intent.getStringArrayListExtra("productIds"))
                val intent1 = Intent(this, CheckoutActivity::class.java)
                intent1.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK


                intent1.putExtras(b)

                startActivity(intent1)
            }
            .addOnFailureListener {
                Toast.makeText(this, "Something went wrong!", Toast.LENGTH_SHORT)
                    .show()
            }
    }

    private fun loadUserInfo() {

        Firebase.firestore.collection("users")
            .document(preferences.getString("number", "")!!)
            .get().addOnSuccessListener {
                binding.userName.setText(it.getString("name").toString())
                binding.userNumber.setText(it.getString("number").toString())
                binding.userVillage.setText(it.getString("village").toString())
                binding.userHouseNo.setText(it.getString("houseNo"))
                binding.userLandMark.setText(it.getString("landMark"))
                binding.userState.setText(it.getString("state").toString())
                binding.userCity.setText(it.getString("city").toString())
                binding.userPinCode.setText(it.getString("pinCode").toString())
            }
    }
}