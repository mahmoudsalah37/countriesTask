package com.example.countriesapi.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseUser

import android.R
import android.content.Intent
import android.util.Log
import android.view.MenuItem
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_signup.*


class SignupActivity : AppCompatActivity() {
    private var mAuth: FirebaseAuth? = null
    fun signUp(view: View) {
        val email: EditText = emailEditText
        val password: EditText = passwordEditText
        if (email.text.toString().trim().isNotEmpty() && password.text.toString().trim().isNotEmpty())
            mAuth!!.createUserWithEmailAndPassword(email.text.toString().trim(), password.toString().trim())
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("", "createUserWithEmail:success")
                            val user = mAuth?.currentUser
                            updateUI(user)
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("", "createUserWithEmail:failure", task.exception)
                            Toast.makeText(this@SignupActivity, "write correct email and strong password.",
                                    Toast.LENGTH_SHORT).show()
                            updateUI(null)
                        }

                        // ...
                    }
        else
            Toast.makeText(this@SignupActivity, "write email and password.",
                    Toast.LENGTH_SHORT).show()
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = mAuth?.currentUser
        updateUI(currentUser)
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser != null) {
            val changePage = Intent(this, LoginActivity::class.java)
            startActivity(changePage)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.countriesapi.R.layout.activity_signup)
//        actionBar.setDisplayHomeAsUpEnabled(true)
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance()
    }

    override fun onOptionsItemSelected(menuItem: MenuItem): Boolean {
        onBackPressed()
        mAuth?.signOut()

        finish()
        return true
    }
}
