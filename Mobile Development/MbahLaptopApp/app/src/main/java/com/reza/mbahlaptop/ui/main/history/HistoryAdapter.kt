package com.reza.mbahlaptop.ui.main.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.reza.mbahlaptop.data.local.ResultEntity
import com.reza.mbahlaptop.databinding.HistoryRowBinding

class HistoryAdapter( private val onDetailClick: (ResultEntity) -> Unit) :
    ListAdapter<ResultEntity, HistoryAdapter.MyViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = HistoryRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val result = getItem(position)
        holder.bind(result, onDetailClick)
    }

    class MyViewHolder(private val binding: HistoryRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(result: ResultEntity, onDetailClick: (ResultEntity) -> Unit) {
            binding.tvOsValue.text = result.os
            binding.tvProcessorValue.text = result.cpu
            binding.tvRamValue.text = result.ram

            binding.btnPredictionDetail.setOnClickListener {
                onDetailClick(result)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ResultEntity>() {
            override fun areItemsTheSame(oldItem: ResultEntity, newItem: ResultEntity): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ResultEntity, newItem: ResultEntity): Boolean {
                return oldItem == newItem
            }
        }
    }
}