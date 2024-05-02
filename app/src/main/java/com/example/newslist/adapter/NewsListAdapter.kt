package com.example.newslist.adapter

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.example.newslist.R
import com.example.newslist.data.NewsList
import com.example.newslist.databinding.NewsListItemBinding

internal class NewsListAdapter(newsList: List<NewsList>, val context: Context?) :
    RecyclerView.Adapter<NewsListAdapter.NewsListViewHolder>() {

        var newsList = newsList
            set(value) {
                field = value
                notifyDataSetChanged()
            }
    var onItemClickListener: ((NewsList) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsListViewHolder {
        Log.i("adapter","TEST")
        val binding = NewsListItemBinding.inflate(
                LayoutInflater.from(context),parent,false
            )
        return NewsListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsListViewHolder, position: Int) {
        val newsList = newsList[position]
        Log.i("Adapter2", newsList.toString())
        Log.i("bindAdapterTitle", newsList.title)
        //holder.textViewTitle.text = newsList.title
        holder.bindToNewsList(newsList, position == 0)
        holder.itemView.setOnClickListener { onItemClickListener?.invoke(newsList) }
    }

    override fun getItemCount(): Int {
        return newsList.size
    }

    class NewsListViewHolder(private val binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindToNewsList(newsList: NewsList, b: Boolean) {
            when(binding){
                is NewsListItemBinding -> {
                    binding.tvTitle.text = newsList.title
                    binding.tvPubDate.text = newsList.pubDate.toString()
                    binding.tvAuthor.text = newsList.author
                    Glide.with(binding.root)
                        .load(newsList.imageUrl)
                        .placeholder(R.drawable.ic_launcher_background)
                        .into(binding.imageView)

                    if (b) {
                        // Customize this item as the highlighted one
                        binding.tvTitle.textSize = 24f
                        itemView.setBackgroundColor(Color.LTGRAY)
                    }
                }

        }
        }
    }
}
