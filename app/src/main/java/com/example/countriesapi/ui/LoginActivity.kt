package com.example.countriesapi.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.animation.Animator
import android.content.Intent
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.view.View.VISIBLE
import android.view.WindowManager
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_login.*
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.FirebaseAuth
import android.widget.Toast
import com.example.countriesapi.ui.LoginActivity.*
import com.example.countriesapi.R
import kotlinx.android.synthetic.main.activity_login.view.*
import kotlinx.android.synthetic.main.item_list_of_country.view.*


class LoginActivity : AppCompatActivity() {
    private var mAuth: FirebaseAuth? = null
    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = mAuth!!.currentUser
        updateUI(currentUser)
    }

    fun signUp(view: View) {
        val changePage = Intent(this, SignupActivity::class.java)
        startActivity(changePage)
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser != null) {
            val changePage = Intent(this, MainActivity::class.java)
            startActivity(changePage)
            finish()
        }
    }

    fun loginButton(view: View) {
        val email: EditText = emailEditText
        val password: TextView = passwordEditText
        if (email.text.toString().trim().isNotEmpty() && password.text.toString().trim().isNotEmpty())
            mAuth?.signInWithEmailAndPassword(email.text.toString().trim(), password.text.toString().trim())
                    ?.addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("", "signInWithEmail:success")
                            val user = mAuth!!.currentUser
                            updateUI(user)
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("", "signInWithEmail:failure", task.exception)
                            Toast.makeText(this@LoginActivity, "write correct email and strong password.",
                                    Toast.LENGTH_SHORT).show()

                            updateUI(null)
                        }

                        // ...
                    }
        else
            Toast.makeText(this@LoginActivity, "write email and password.",
                    Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
//        requestWindowFeature(Window.FEATURE_NO_TITLE)
        mAuth = FirebaseAuth.getInstance()
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(com.example.countriesapi.R.layout.activity_login)
        object : CountDownTimer(5000, 1000) {
            override fun onFinish() {
                bookITextView.visibility = View.GONE
                loadingProgressBar.visibility = View.GONE
                rootView.setBackgroundColor(ContextCompat.getColor(this@LoginActivity, com.example.countriesapi.R.color.colorSplashText))
                bookIconImageView.setImageResource(com.example.countriesapi.R.drawable.ic_local_airport_color_24dp)
                startAnimation()
            }

            override fun onTick(p0: Long) {}
        }.start()

    }

    private fun startAnimation() {
        bookIconImageView.animate().apply {
            x(50f)
            y(100f)
            duration = 1000
        }.setListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(p0: Animator?) {

            }

            override fun onAnimationEnd(p0: Animator?) {
                afterAnimationView.visibility = VISIBLE
            }

            override fun onAnimationCancel(p0: Animator?) {

            }

            override fun onAnimationStart(p0: Animator?) {

            }
        })
    }
}
