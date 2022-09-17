package com.codechakra.codechakrastore.activity

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.codechakra.codechakrastore.MainActivity
import com.codechakra.codechakrastore.R
import com.codechakra.codechakrastore.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var dialog: Dialog
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // Initialize Firebase Auth
        auth = Firebase.auth


        binding.signupBt.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }

        binding.signinBt.setOnClickListener {
            val userEmail = binding.userNumber.text.toString()
            val userPass = binding.userPass.text.toString()

            if (userEmail.isNotEmpty() && userPass.isNotEmpty()) {
                signInWithEmail(userEmail, userEmail)
            } else {
                Toast.makeText(this, "Please provide number!", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }


    private fun signInWithEmail(email: String, password: String) {
        dialog = Dialog(this)
        dialog.setContentView(R.layout.progress_layout)
        dialog.setCancelable(false)
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information

                    //val user = auth.currentUser
                    dialog.dismiss()
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    //intent.putExtra("verificationId", verificationId)
                    intent.putExtra(MainActivity.STORE.USER_COLLECTION, binding.userNumber.text.toString())
                    startActivity(intent)

                } else {
                    // If sign in fails, display a message to the user.
                    //  Log.w(TAG, "signInWithEmail:failure", task.exception)
                    dialog.dismiss()
                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }

    }

}