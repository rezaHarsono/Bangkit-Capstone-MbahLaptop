package com.reza.mbahlaptop.ui.intro

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import com.reza.mbahlaptop.databinding.ActivityIntroBinding
import com.reza.mbahlaptop.ui.auth.AuthActivity

class IntroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityIntroBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.btnStart.setOnClickListener {
            val intent = Intent(this, AuthActivity::class.java)
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
        val introStart = ObjectAnimator.ofFloat(binding.btnStart, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(
                introTitle,
                introInfo,
                introImage,
                introStart
            )
            start()
        }
    }
}