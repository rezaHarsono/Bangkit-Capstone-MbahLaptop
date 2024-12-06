package com.reza.mbahlaptop.ui.about

import android.os.Bundle
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.reza.mbahlaptop.databinding.ActivityAboutBinding
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
        val teamAdapter = TeamAdapter()
        val teamRv = binding?.rvTeamList

        val teamList = listOf(
            TeamAdapter.TeamMember("Adam Noor Falah", "Machine Learning"),
            TeamAdapter.TeamMember("Juan Graciano", "Machine Learning"),
            TeamAdapter.TeamMember("German Mindo Simamarta", "Machine Learning"),
            TeamAdapter.TeamMember("Muhammad Reza Harsono", "Mobile Development"),
            TeamAdapter.TeamMember("Muhammad Rizky Ramadhani", "Mobile Development"),
            TeamAdapter.TeamMember("Zuwita S", "Cloud Computing"),
            TeamAdapter.TeamMember("Muhammad Adnan Fadillah", "Cloud Computing")
        )

        teamRv?.layoutManager = LinearLayoutManager(this)
        teamRv?.setOnTouchListener { _, _ -> true }
        teamRv?.adapter = teamAdapter
        teamAdapter.submitList(teamList)
    }
}