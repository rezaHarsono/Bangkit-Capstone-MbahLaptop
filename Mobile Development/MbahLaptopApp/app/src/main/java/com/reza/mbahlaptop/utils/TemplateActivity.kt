package com.reza.mbahlaptop.utils

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.reza.mbahlaptop.ui.setting.SettingViewModel
import com.reza.mbahlaptop.ui.setting.SettingViewModelFactory
import com.reza.mbahlaptop.ui.setting.SettingsPreferences
import com.reza.mbahlaptop.ui.setting.dataStore

open class TemplateActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val preferences = SettingsPreferences.getInstance(this.dataStore)
        val factory = SettingViewModelFactory(preferences)
        val viewModel: SettingViewModel by viewModels {
            factory
        }

        viewModel.getThemeSetting().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
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