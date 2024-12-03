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
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.reza.mbahlaptop.databinding.ActivitySettingBinding
import com.reza.mbahlaptop.ui.main.HandleLoginActivity
import kotlinx.coroutines.launch

class SettingActivity : AppCompatActivity() {
    private var _binding: ActivitySettingBinding? = null
    private val binding get() = _binding
    private lateinit var auth: FirebaseAuth
    private var user: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.myToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        auth = Firebase.auth
        user = auth.currentUser

        user?.let {
            setupView()
            setupAction()
        }
    }

    private fun setupView() {
        binding?.apply {
            tvUserEmail.visibility = View.VISIBLE
            tvUserEmail.text = user?.email

            buttonLogout.visibility = View.VISIBLE
        }
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
            val logoutIntent = Intent(this@SettingActivity, HandleLoginActivity::class.java)
            logoutIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(logoutIntent)
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