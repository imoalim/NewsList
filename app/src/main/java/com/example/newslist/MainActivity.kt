package com.example.newslist


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import com.example.newslist.adapter.NewsListAdapter
import com.example.newslist.databinding.ActivityMainBinding
import com.example.newslist.viewModel.NewsListViewModel

class MainActivity : AppCompatActivity() {


    companion object {
        const val LOG_TAG = "MainActivity"
    }

    private lateinit var binding: ActivityMainBinding
    private val newsListViewModel: NewsListViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Initialize the binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root) // Use the root property to set the content view

        val newsListAdapter = NewsListAdapter(emptyList(), this)
        binding.rvTitle.adapter = newsListAdapter

        // observing livedata is the way to retrieve it
        // by observing livedata we are notified whenever it changes
        // the first observe call gives us the latest data
        newsListViewModel.newsList.observe(this) {
            Log.e(LOG_TAG, "Observe magicCardList is called: ${it.take(2)}")
            newsListAdapter.newsList = it
        }

        newsListViewModel.hasError.observe(this) { hasError ->
            if (hasError) {
                Toast.makeText(
                    this@MainActivity,
                    R.string.error_loading,
                    Toast.LENGTH_LONG
                )
                    .show()
            }
        }
        binding.btnLoadCards.setOnClickListener {
            binding.btnLoadCards.isEnabled = false
            newsListAdapter.newsList = emptyList()
            newsListViewModel.loadData()
            binding.btnLoadCards.isEnabled = true
        }

        newsListAdapter.onItemClickListener ={
            Log.e(LOG_TAG, "MagicCard clicked: $it")
            val intent = Intent(this@MainActivity, NewsListDetailActivity::class.java)
            intent.putExtra("title", it.title)
            intent.putExtra("description", it.description)
            intent.putExtra("id", it.id)
            intent.putExtra("articleLink", it.articleLink)
            intent.putExtra("author", it.author)
            intent.putExtra("imageUrl", it.imageUrl)
            intent.putExtra("keywords", it.keywords)
            intent.putExtra("pubDate", it.pubDate)

            // Add other extras as needed
            startActivity(intent)
        }
    }

}