package com.reza.mbahlaptop.ui.register

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.reza.mbahlaptop.R
import com.reza.mbahlaptop.databinding.ActivityRegisterBinding
import com.reza.mbahlaptop.ui.login.LoginActivity

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        auth = Firebase.auth

        binding.btnCancel.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        binding.btnRegister.setOnClickListener {
            val email = binding.edEmail.text.toString()
            val password = binding.edPassword.text.toString()
            if (email.isEmpty() || password.isEmpty()) {
                showToast(getString(R.string.please_fill_all_fields))
            } else {
                register()
            }
        }
    }

    private fun register() {
        val email = binding.edEmail.text.toString()
        val password = binding.edPassword.text.toString()
        showLoading(true)

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    showLoading(false)
                    Log.d(TAG, "Create user: Success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    showLoading(false)
                    Log.w(TAG, "Create user: Failure", task.exception)
                    showToast("Login Failed")
                    updateUI(null)
                }
            }

    }

    private fun showToast(message: String) {
        Toast.makeText(baseContext, message, Toast.LENGTH_SHORT).show()
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser != null) {
            val loginIntent = Intent(this@RegisterActivity, LoginActivity::class.java)
            loginIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(loginIntent)
            finish()
        }
    }

    private fun showLoading(active: Boolean) {
        if (active) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    companion object {
        private const val TAG = "RegisterActivity"
    }
}