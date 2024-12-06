package com.reza.mbahlaptop.ui.about

import android.os.Bundle
import com.reza.mbahlaptop.R
import com.reza.mbahlaptop.databinding.ActivityAboutBinding
import com.reza.mbahlaptop.databinding.TeamRowBinding
import com.reza.mbahlaptop.utils.TemplateActivity

class AboutActivity : TemplateActivity() {
    private var _binding: ActivityAboutBinding? = null
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.myToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupView()
    }

    private fun setupView() {
        val teamContainer = binding?.teamContainer

        val teamList = listOf(
            TeamMember("Adam Noor Falah", getString(R.string.machine_learning)),
            TeamMember("Juan Graciano", getString(R.string.machine_learning)),
            TeamMember("German Mindo Simamarta", getString(R.string.machine_learning)),
            TeamMember("Muhammad Reza Harsono", getString(R.string.mobile_development)),
            TeamMember("Muhammad Rizky Ramadhani", getString(R.string.mobile_development)),
            TeamMember("Zuwita S", getString(R.string.cloud_computing)),
            TeamMember("Muhammad Adnan Fadillah", getString(R.string.cloud_computing))
        )

        for (member in teamList) {
            val binding = TeamRowBinding.inflate(layoutInflater)
            binding.apply {
                tvMemberName.text = member.name
                tvMemberRole.text = member.role
            }
            teamContainer?.addView(binding.root)
        }
    }

    data class TeamMember(val name: String, val role: String)
}