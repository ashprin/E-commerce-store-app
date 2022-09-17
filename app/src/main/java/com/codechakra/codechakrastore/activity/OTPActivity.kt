package com.codechakra.codechakrastore.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.codechakra.codechakrastore.MainActivity
import com.codechakra.codechakrastore.R
import com.codechakra.codechakrastore.databinding.ActivityOtpactivityBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider

class OTPActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOtpactivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.verifyOtpButton.setOnClickListener {
            val userOTP = binding.userOTP.text.toString()
            if (userOTP.isNotEmpty()){
                verifyUser(userOTP)
            }else{
                Toast.makeText(this, "Please enter OTP!", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun verifyUser(userOTP: String) {
        val credential = PhoneAuthProvider.getCredential(intent.getStringExtra("verificationId")!!, userOTP)
        signInWithPhoneAuthCredential(credential)
    }


    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val preferences = getSharedPreferences("user", MODE_PRIVATE)
                    val editor = preferences.edit()
                    editor.putString("number",intent.getStringExtra("number"))
                    editor.apply()
                    Log.d("user_number",intent.getStringExtra("number")!!)
                    startActivity(Intent(this,MainActivity::class.java))
                    finish()

                } else {
                    Toast.makeText(this, "Login failed!", Toast.LENGTH_SHORT)
                        .show()
                }
            }
    }
}