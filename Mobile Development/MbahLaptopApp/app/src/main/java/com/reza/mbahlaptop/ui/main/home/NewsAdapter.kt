package com.reza.mbahlaptop.ui.main.home

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.reza.mbahlaptop.data.remote.response.ArticlesItem
import com.reza.mbahlaptop.databinding.NewsRowBinding
import com.reza.mbahlaptop.ui.webview.WebViewActivity

class NewsAdapter : ListAdapter<ArticlesItem, NewsAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = NewsRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val news = getItem(position)
        holder.bind(news)

//        holder.itemView.setOnClickListener {
//            val context = holder.itemView.context
//            val url = news.url
//
//            val builder = android.app.AlertDialog.Builder(context)
//            builder.setTitle("Open Link")
//            builder.setMessage("Are you sure you want to open this link in your browser?")
//            builder.setPositiveButton("Yes") { _, _ ->
//                val intent = Intent(Intent.ACTION_VIEW)
//                intent.data = Uri.parse(url)
//                context.startActivity(intent)
//            }
//            builder.setNegativeButton("No") { dialog, _ ->
//                dialog.dismiss()
//            }
//            builder.show()
//        }
    }

    class MyViewHolder(private val binding: NewsRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(news: ArticlesItem) {
            Glide.with(binding.root.context)
                .load(news.urlToImage)
                .into(binding.ivNewsImage)
            binding.tvNewsSource.text = news.source!!.name
            binding.tvNewsTitle.text = news.title
            binding.tvNewsAuthor.text = "By ${news.author}"

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, WebViewActivity::class.java)
                intent.putExtra("url", news.url)
                intent.putExtra("title", news.title)
                itemView.context.startActivity(intent)
            }
        }

    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ArticlesItem>() {
            override fun areItemsTheSame(oldItem: ArticlesItem, newItem: ArticlesItem): Boolean {
                return oldItem.url == newItem.url
            }

            override fun areContentsTheSame(oldItem: ArticlesItem, newItem: ArticlesItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}