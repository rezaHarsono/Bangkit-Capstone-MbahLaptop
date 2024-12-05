package com.reza.mbahlaptop.ui.result

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.reza.mbahlaptop.R
import com.reza.mbahlaptop.databinding.ActivityResultBinding
import com.reza.mbahlaptop.utils.withCurrencyFormat

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.tvResultTime.text = intent.getStringExtra("date")
        binding.tvResultOs.text = intent.getStringExtra("os")
        binding.tvResultProcessor.text = intent.getStringExtra("processor")
        binding.tvResultRam.text = intent.getStringExtra("ram")
        binding.tvResultGpu.text = intent.getStringExtra("gpu")
        binding.tvResultStorageType.text = intent.getStringExtra("storageType")
        binding.tvResultStorageSize.text = intent.getStringExtra("storageSize")
        binding.tvResultResolution.text = intent.getStringExtra("resolution")

        binding.tvResultLowestPrice.text =
            intent.getStringExtra("lowPrice")?.withCurrencyFormat()?.substringBefore(",")
        binding.tvResultHighestPrice.text =
            intent.getStringExtra("highPrice")?.withCurrencyFormat()?.substringBefore(",")
    }
}