package com.reza.mbahlaptop.ui.intro

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import com.reza.mbahlaptop.databinding.ActivityIntroBinding
import com.reza.mbahlaptop.ui.auth.login.LoginActivity
import com.reza.mbahlaptop.ui.main.MainActivity
import com.reza.mbahlaptop.ui.auth.register.RegisterActivity

class IntroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityIntroBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.btnLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(
                intent,
                ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle()
            )
        }

        binding.btnRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(
                intent,
                ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle()
            )
        }

        binding.btnGuest.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(
                intent,
                ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle()
            )
        }

        playAnimation()
    }

    private fun playAnimation() {
        val introTitle =
            ObjectAnimator.ofFloat(binding.introWelcome, View.ALPHA, 1f).setDuration(500)
        val introInfo = ObjectAnimator.ofFloat(binding.introInfo, View.ALPHA, 1f).setDuration(500)
        val introImage = ObjectAnimator.ofFloat(binding.introImage, View.ALPHA, 1f).setDuration(500)
        val introLogin = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(500)
        val introRegister =
            ObjectAnimator.ofFloat(binding.btnRegister, View.ALPHA, 1f).setDuration(500)
        val introGuest = ObjectAnimator.ofFloat(binding.btnGuest, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(
                introTitle,
                introInfo,
                introImage,
                introLogin,
                introRegister,
                introGuest
            )
            start()
        }
    }
}