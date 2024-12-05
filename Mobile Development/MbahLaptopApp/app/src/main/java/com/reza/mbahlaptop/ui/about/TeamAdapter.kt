package com.reza.mbahlaptop.ui.about

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.reza.mbahlaptop.databinding.TeamRowBinding

class TeamAdapter :
    ListAdapter<TeamAdapter.TeamMember, TeamAdapter.MyViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = TeamRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val result = getItem(position)
        holder.bind(result)
    }

    class MyViewHolder(private val binding: TeamRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(member: TeamMember) {
            binding.tvMemberName.text = member.name
            binding.tvMemberRole.text = member.role
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<TeamMember>() {
            override fun areItemsTheSame(oldItem: TeamMember, newItem: TeamMember): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: TeamMember, newItem: TeamMember): Boolean {
                return oldItem == newItem
            }
        }
    }

    data class TeamMember(val name: String, val role: String)
}