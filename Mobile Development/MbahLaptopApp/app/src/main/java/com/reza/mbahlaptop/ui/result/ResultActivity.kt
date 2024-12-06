package com.reza.mbahlaptop.ui.result

import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.reza.mbahlaptop.R
import com.reza.mbahlaptop.data.local.entity.ResultEntity
import com.reza.mbahlaptop.databinding.ActivityResultBinding
import com.reza.mbahlaptop.utils.TemplateActivity
import com.reza.mbahlaptop.utils.formatNumber
import com.reza.mbahlaptop.utils.withCurrencyFormat

class ResultActivity : TemplateActivity() {
    private lateinit var binding: ActivityResultBinding

    private var result: ResultEntity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setSupportActionBar(binding.myToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Prediction Result"

        result = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("RESULT", ResultEntity::class.java)
        } else {
            intent.getParcelableExtra("RESULT")
        }

        setupView()
    }

    private fun setupView() {
        result?.let {
            val lowestPrice = it.lowestPrice.withCurrencyFormat().substringBefore(",")
            val highestPrice = it.highestPrice.withCurrencyFormat().substringBefore(",")
            binding.apply {
                tvResultPrice.text = getString(R.string.price_range, lowestPrice, highestPrice)
                tvResultTime.text = it.date
                tvResultOs.text = it.os
                tvResultProcessor.text = it.processor
                tvResultGpu.text = it.gpu
                tvResultRam.text = formatNumber(it.ram)
                tvResultStorageType.text = it.storageType
                tvResultStorageSize.text = formatNumber(it.storage)
                tvResultResolution.text = it.displayRes
                tvResultLowestPrice.text = lowestPrice
                tvResultHighestPrice.text = highestPrice
            }
        }
    }
}