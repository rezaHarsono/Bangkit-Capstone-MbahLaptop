package com.reza.mbahlaptop.ui.main.home.welcome

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.reza.mbahlaptop.data.local.entity.ResultEntity
import com.reza.mbahlaptop.databinding.RecentHistoryRowBinding
import com.reza.mbahlaptop.ui.result.ResultActivity
import com.reza.mbahlaptop.utils.DateUtils
import com.reza.mbahlaptop.utils.withCurrencyFormat

class RecentHistoryAdapter :
    ListAdapter<ResultEntity, RecentHistoryAdapter.MyViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            RecentHistoryRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val result = getItem(position)
        holder.bind(result)

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, ResultActivity::class.java)
            intent.putExtra("RESULT", result)
            holder.itemView.context.startActivity(intent)
        }
    }

    class MyViewHolder(private val binding: RecentHistoryRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(result: ResultEntity) {
            binding.apply {
                tvCpuValue.text = result.processor
                tvPriceValue.text = result.lowestPrice.withCurrencyFormat().substringBefore(",")
                tvDateValue.text = DateUtils.formatLocalDateToRelativeTime(result.date)
            }

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, ResultActivity::class.java)
                intent.putExtra("RESULT", result)
                itemView.context.startActivity(intent)
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