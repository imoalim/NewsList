package com.example.newslist

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.newslist.databinding.ActivityNewsListDetailBinding
import com.bumptech.glide.Glide

class NewsListDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewsListDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsListDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Extract data from intent
        val title = intent.getStringExtra("title") ?: "N/A"
        val author = intent.getStringExtra("author") ?: "Unknown"
        val pubDate = intent.getStringExtra("pubDate") ?: "Unknown Date"
        val description = intent.getStringExtra("description") ?: "No description available."
        val imageUrl = intent.getStringExtra("imageUrl") ?: ""
        val articleLink = intent.getStringExtra("articleLink") ?: ""

        // Set data to views
        binding.tvTitle.text = title
        binding.tvAuthor.text = author
        binding.tvPubDate.text = pubDate
        binding.tvDescription.text = description

        // Load image using Glide
        Glide.with(this)
            .load(imageUrl)
            .into(binding.ivImageUrl)

        // Set up button to open article
        binding.btnOpenArticle.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(articleLink))
            startActivity(intent)
        }
    }
}
