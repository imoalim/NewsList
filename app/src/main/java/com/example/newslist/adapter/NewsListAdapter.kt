package com.example.newslist.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.newslist.R
import com.example.newslist.data.NewsList

internal class NewsListAdapter(private val newsLists: List<NewsList>, context: Context?) :
    RecyclerView.Adapter<NewsListAdapter.ViewHolder>() {

        var newsList = newsLists
            set(value) {
                field = value
                notifyDataSetChanged()
            }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.news_list_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (_, title) = newsLists[position]
        holder.textViewTitle.text = title
    }

    override fun getItemCount(): Int {
        return newsLists.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textViewTitle: TextView

        init {
            textViewTitle = itemView.findViewById<View>(R.id.tvTitle) as TextView
        }
    }
}
