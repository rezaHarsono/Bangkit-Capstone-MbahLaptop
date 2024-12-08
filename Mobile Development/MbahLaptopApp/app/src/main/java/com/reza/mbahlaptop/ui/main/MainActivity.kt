package com.reza.mbahlaptop.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.github.kwasow.bottomnavigationcircles.BottomNavigationCircles
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.jakewharton.threetenabp.AndroidThreeTen
import com.reza.mbahlaptop.R
import com.reza.mbahlaptop.databinding.ActivityMainBinding
import com.reza.mbahlaptop.ui.setting.SettingActivity
import com.reza.mbahlaptop.utils.TemplateActivity

class MainActivity : TemplateActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidThreeTen.init(this)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.myToolbar)

        val navView: BottomNavigationCircles = binding.bottomCircle

        navView.isItemActiveIndicatorEnabled = false

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        navController = navHostFragment.navController
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_predict, R.id.navigation_history
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        navView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    navController.popBackStack(R.id.navigation_home, true)
                    navController.navigate(R.id.navigation_home)
                    true
                }

                R.id.navigation_predict -> {
                    navController.navigate(R.id.navigation_predict)
                    true
                }

                R.id.navigation_history -> {
                    navController.navigate(R.id.navigation_history)
                    true
                }
                else -> false
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.more_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_setting -> {
                startActivity(Intent(this, SettingActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}