package com.reza.mbahlaptop.ui.setting

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.lifecycle.lifecycleScope
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.reza.mbahlaptop.databinding.ActivitySettingBinding
import com.reza.mbahlaptop.ui.login.LoginActivity
import kotlinx.coroutines.launch

class SettingActivity : AppCompatActivity() {
    private var _binding: ActivitySettingBinding? = null
    private val binding get() = _binding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.myToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        auth = Firebase.auth

        setupAction()

    }

    private fun setupAction() {
        binding?.buttonLogout?.setOnClickListener {
            signOut()
        }
    }

    private fun signOut() {
        showLoading(true)
        lifecycleScope.launch {
            val credentialManager = CredentialManager.create(this@SettingActivity)
            auth.signOut()
            Toast.makeText(this@SettingActivity, "Signed out successfully", Toast.LENGTH_SHORT).show()
            credentialManager.clearCredentialState(ClearCredentialStateRequest())
            startActivity(Intent(this@SettingActivity, LoginActivity::class.java))
            finish()
            showLoading(false)
        }
    }

    private fun showLoading(active: Boolean) {
        if (active) {
            binding?.progressBar?.visibility = View.VISIBLE
        } else {
            binding?.progressBar?.visibility = View.GONE
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}