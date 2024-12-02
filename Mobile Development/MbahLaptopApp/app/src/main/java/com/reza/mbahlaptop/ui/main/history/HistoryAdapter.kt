package com.reza.mbahlaptop.ui.main.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.reza.mbahlaptop.R
import com.reza.mbahlaptop.data.local.ResultEntity
import com.reza.mbahlaptop.databinding.HistoryRowBinding

class HistoryAdapter(private val onDeleteClick: (ResultEntity) -> Unit) :
    ListAdapter<ResultEntity, HistoryAdapter.MyViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = HistoryRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val result = getItem(position)
        holder.bind(result)

        holder.itemView.setOnClickListener {
            AlertDialog.Builder(holder.itemView.context).apply {
                setTitle(context.getString(R.string.delete_history))
                setMessage(context.getString(R.string.are_you_sure_you_want_to_delete_this_item))
                setPositiveButton(context.getString(R.string.yes)) { _, _ ->
                    onDeleteClick(result)
                }
                setNegativeButton(context.getString(R.string.no), null)
                create().show()
            }
        }
    }

    class MyViewHolder(private val binding: HistoryRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(result: ResultEntity) {
            binding.tvOsValue.text = result.os
            binding.tvProcessorValue.text = result.cpu
            binding.tvRamValue.text = result.ram
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