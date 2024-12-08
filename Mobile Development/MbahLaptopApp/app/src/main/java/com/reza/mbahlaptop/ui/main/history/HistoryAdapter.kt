package com.reza.mbahlaptop.ui.main.history

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.reza.mbahlaptop.R
import com.reza.mbahlaptop.data.local.entity.ResultEntity
import com.reza.mbahlaptop.databinding.HeaderRowBinding
import com.reza.mbahlaptop.databinding.HistoryRowBinding
import com.reza.mbahlaptop.ui.main.history.HistoryFragment.ListItem
import com.reza.mbahlaptop.ui.result.ResultActivity
import com.reza.mbahlaptop.utils.DateUtils
import com.reza.mbahlaptop.utils.formatGBValue
import com.reza.mbahlaptop.utils.withCurrencyFormat

class HistoryAdapter :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items = mutableListOf<ListItem>()

    fun submitList(list: List<ListItem>) {
        items.clear()
        items.addAll(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_HEADER -> {
                val binding =
                    HeaderRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                HeaderViewHolder(binding)
            }

            TYPE_CONTENT -> {
                val binding =
                    HistoryRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ContentViewHolder(binding)
            }

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HeaderViewHolder -> holder.bind((items[position] as ListItem.Header).date)
            is ContentViewHolder -> holder.bind((items[position] as ListItem.Content).result)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is ListItem.Header -> TYPE_HEADER
            is ListItem.Content -> TYPE_CONTENT
        }
    }

    override fun getItemCount(): Int = items.size

    class HeaderViewHolder(private val binding: HeaderRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(date: String) {
            binding.tvHeaderDate.text = DateUtils.formatDate(date)
        }
    }

    class ContentViewHolder(private val binding: HistoryRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(result: ResultEntity) {
            val lowestPrice = result.lowestPrice.withCurrencyFormat().substringBefore(",")
            val ram = formatGBValue(result.ram)
            val storage = formatGBValue(result.storage)
            binding.apply {
                tvProcessorValue.text = result.processor
                tvPriceValue.text =
                    itemView.context.getString(R.string.predicted_price_format, lowestPrice)
                tvSpecsValue.text =
                    itemView.context.getString(R.string.specs_format, ram, storage, result.os)
            }

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, ResultActivity::class.java)
                intent.putExtra("RESULT", result)
                itemView.context.startActivity(intent)
            }
        }
    }

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_CONTENT = 1
    }
}