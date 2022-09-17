package com.codechakra.codechakrastore.activity

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.codechakra.codechakrastore.MainActivity
import com.codechakra.codechakrastore.R
import com.codechakra.codechakrastore.databinding.ActivityRegisterBinding
import com.codechakra.codechakrastore.model.UserModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var dialog: Dialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Initialize Firebase Auth
        auth = Firebase.auth



        binding.signupBt.setOnClickListener {

            validUser()

        }

        binding.loginBt.setOnClickListener {

            openLogin()
        }
    }

    private fun validUser() {
        val userName = binding.userName.text.toString()
        val userEmail = binding.userNumber.text.toString()
        val userPass = binding.userPass.text.toString()
        val userPassConf = binding.userPassConf.text.toString()

        if (userName.isNotEmpty() && userEmail.isNotEmpty() && userPass.isNotEmpty() && userPassConf.isNotEmpty()) {
            if (userPass == userPassConf) {
                createNewAccWithEmail(userEmail, userPass)
            } else {
                Snackbar.make(binding.userPassConf, "Passwords don't match!", Toast.LENGTH_SHORT)
                    .show()
            }
        } else {
            Toast.makeText(this, "Please enter all fields!", Toast.LENGTH_SHORT)
                .show()
        }


    }

    private fun storeData() {


        val preferences = getSharedPreferences("user", MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString("number", binding.userNumber.text.toString())
        editor.putString("name", binding.userName.text.toString())
        editor.apply()

        val data = UserModel(
            name = binding.userName.text.toString(),
            number = binding.userNumber.text.toString(),
//            village = "",
//            state = "",
//            city = "",
//            pinCode = ""
        )

        Firebase.firestore.collection("users").document(binding.userNumber.text.toString())
            .set(data).addOnSuccessListener {
                dialog.dismiss()
                startActivity(Intent(this, MainActivity::class.java))
                Toast.makeText(this, "User registered!", Toast.LENGTH_SHORT)
                    .show()

            }.addOnFailureListener {
                dialog.dismiss()
                Toast.makeText(this, "Something went wrong!", Toast.LENGTH_SHORT)
                    .show()
            }
    }

    private fun openLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }


    private fun createNewAccWithEmail(email: String, password: String) {
        dialog = Dialog(this)
        dialog.setContentView(R.layout.progress_layout)
        dialog.setCancelable(false)
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    val user = auth.currentUser
                    storeData()

                } else {
                    // If sign in fails, display a message to the user.

                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                    //updateUI(null)
                }
            }

    }
}