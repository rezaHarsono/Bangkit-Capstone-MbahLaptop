package com.reza.mbahlaptop.ui.setting

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.lifecycle.lifecycleScope
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.reza.mbahlaptop.R
import com.reza.mbahlaptop.databinding.ActivitySettingBinding
import com.reza.mbahlaptop.ui.about.AboutActivity
import com.reza.mbahlaptop.ui.main.HandleLoginActivity
import com.reza.mbahlaptop.ui.main.history.HistoryViewModel
import com.reza.mbahlaptop.utils.ViewModelFactory
import kotlinx.coroutines.launch

class SettingActivity : AppCompatActivity() {
    private var _binding: ActivitySettingBinding? = null
    private val binding get() = _binding

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private var user: FirebaseUser? = null

    private lateinit var preferences: SettingsPreferences
    private lateinit var settingFactory: SettingViewModelFactory
    private val settingViewModel: SettingViewModel by viewModels {
        settingFactory
    }

    private lateinit var historyFactory: ViewModelFactory
    private val historyViewModel: HistoryViewModel by viewModels {
        historyFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.myToolbar)
        supportActionBar?.title = getString(R.string.user_setting)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        auth = Firebase.auth
        user = auth.currentUser
        db = FirebaseFirestore.getInstance()

        preferences = SettingsPreferences.getInstance(this.dataStore)
        settingFactory = SettingViewModelFactory(preferences)
        historyFactory = ViewModelFactory.getInstance(this)

        setupView()
        setupAction()
    }

    private fun setupView() {
        var username: String? = null

        val userRef = db.collection("users")

        val queryEmail = userRef.whereEqualTo("email", user?.email)

        queryEmail.get()
            .addOnSuccessListener {
                if (!it.isEmpty) {
                    username = it.documents[0].getString("username")
                    binding?.tvUserName?.text = username
                } else {
                    username = user?.displayName
                }
            }

        binding?.apply {
            user?.let {
                tvUserName.text = username
                tvUserEmail.visibility = View.VISIBLE
                tvUserEmail.text = user?.email
                buttonLogout.visibility = View.VISIBLE
            }
        }
        changeTheme()
    }

    private fun setupAction() {
        binding?.apply {
            buttonLanguage.setOnClickListener {
                startActivity(Intent(this@SettingActivity, LanguageActivity::class.java))
            }
            switchTheme.setOnCheckedChangeListener { _, isChecked: Boolean ->
                settingViewModel.saveThemeSetting(isChecked)
            }
            buttonAbout.setOnClickListener {
                startActivity(Intent(this@SettingActivity, AboutActivity::class.java))
            }
            buttonClearHistory.setOnClickListener {
                historyViewModel.deleteAllResult()
            }
            user?.let {
                buttonLogout.setOnClickListener {
                    signOut()
                }
            }
        }
    }

    private fun signOut() {
        showLoading(true)
        lifecycleScope.launch {
            val credentialManager = CredentialManager.create(this@SettingActivity)
            auth.signOut()
            Toast.makeText(
                this@SettingActivity,
                getString(R.string.signed_out_success), Toast.LENGTH_SHORT
            )
                .show()
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

    private fun changeTheme() {
        settingViewModel.getThemeSetting().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding?.apply {
                    switchTheme.isChecked = true
                    switchTheme.text = getString(R.string.dark)
                }
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding?.apply {
                    switchTheme.isChecked = false
                    switchTheme.text = getString(R.string.light)
                }
            }
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