package com.example.newslist.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.newslist.MainActivity
import com.example.newslist.R
import com.example.newslist.data.NewsList

internal class NewsListAdapter(newsList: List<NewsList>, context: Context?) :
    RecyclerView.Adapter<NewsListAdapter.ViewHolder>() {

        var newsList = newsList
            set(value) {
                field = value
                notifyDataSetChanged()
            }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.i("adapter","TEST")
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.news_list_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = newsList[position]
        Log.i("Adapter2", item.toString())
        Log.i("bindAdapterTitle", item.title)
        holder.textViewTitle.text = item.title
    }

    override fun getItemCount(): Int {
        return newsList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textViewTitle: TextView

        init {
            textViewTitle = itemView.findViewById<View>(R.id.tvTitle) as TextView
        }
    }
}
