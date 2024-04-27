package com.example.newslist.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newslist.MainActivity
import com.example.newslist.data.NewsListLoader
import com.example.newslist.data.NewsListParser
import com.example.newslist.data.Failed
import com.example.newslist.data.NewsList
import com.example.newslist.data.Success
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayInputStream
import java.io.IOException
import java.nio.charset.StandardCharsets

class NewsListViewModel : ViewModel() {

    private val newsListLoader = NewsListLoader()
    private val newsListParser = NewsListParser()

    private var currentPage = 1

    // common pattern
    // private Mutable LiveData, public immutable Livedata
    private val _newsList = MutableLiveData<List<NewsList>>(emptyList())
    val newsList: LiveData<List<NewsList>> = _newsList

    private val _hasError = MutableLiveData(false)
    val hasError: LiveData<Boolean> = _hasError


    fun loadData() {
        _hasError.postValue(false)

        viewModelScope.launch {
            var magicCardResultList = fetchAndParseMagicCardsResult() ?: return@launch

            if (magicCardResultList.isEmpty()) {
                currentPage = 1
                magicCardResultList = fetchAndParseMagicCardsResult() ?: return@launch // if an error occurred and the result is null, we stop the coroutine
            } else {
                currentPage++
            }
            _newsList.postValue(magicCardResultList)
        }
    }

    private suspend fun fetchAndParseMagicCardsResult(): List<NewsList>? {
        //error
        val webResult = try {
            withContext(Dispatchers.IO) {
                newsListLoader.fetchNewsList()
            }
        } catch (e: IOException) {
            Log.e(MainActivity.LOG_TAG, "Network error", e)
            _hasError.postValue(true)
            return null
        }

        return when (webResult) {
            is Success -> withContext(Dispatchers.Default) {
                try {
                    val inputStream = ByteArrayInputStream(webResult.result.toByteArray(StandardCharsets.UTF_8))
                    val parsedNewsList = newsListParser.parse(inputStream)  // Parse the XML into a list
                    parsedNewsList.sortedBy { it.title }  // Return the parsed list.
                } catch (e: Exception) { // Generic exception handling for parsing
                    Log.e(MainActivity.LOG_TAG, "Parsing error", e)
                    _hasError.postValue(true)
                    null
                }
            }
            is Failed -> {
                Log.e(MainActivity.LOG_TAG, "Failed loading: ${webResult.text}", webResult.throwable)
                _hasError.postValue(true)
                null
            }
            else -> {
                Log.e(MainActivity.LOG_TAG, "Unhandled result type")
                _hasError.postValue(true)
                null
            }
        }
    }

}
