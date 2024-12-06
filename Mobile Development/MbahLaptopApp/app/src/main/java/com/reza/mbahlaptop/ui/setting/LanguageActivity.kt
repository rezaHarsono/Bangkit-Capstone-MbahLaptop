package com.reza.mbahlaptop.ui.setting

import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.reza.mbahlaptop.R
import com.reza.mbahlaptop.databinding.ActivityLanguageBinding
import com.reza.mbahlaptop.utils.TemplateActivity

class LanguageActivity : TemplateActivity() {
    private lateinit var binding: ActivityLanguageBinding

    private lateinit var preferences: SettingsPreferences
    private lateinit var factory: SettingViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLanguageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.myToolbar)
        supportActionBar?.title = getString(R.string.title_language_setting)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        preferences = SettingsPreferences.getInstance(this.dataStore)
        factory = SettingViewModelFactory(preferences)


        binding.buttonEng.setOnClickListener {
            updateLocale("en")
        }

        binding.buttonIn.setOnClickListener {
            updateLocale("in")
        }
    }

    private fun updateLocale(languageCode: String) {
        val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(languageCode)
        AppCompatDelegate.setApplicationLocales(appLocale)
    }
}