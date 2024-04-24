package com.example.newslist.data



import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class NewsListLoader {
    fun fetchNewsList(): FetchNewsListResult {
        val urlConnection = URL("https://www.engadget.com/rss.xml")
            .openConnection() as HttpURLConnection
        return try {
            urlConnection.run {
                requestMethod = "GET"
                connectTimeout = 5000
                readTimeout = 5000
                String(inputStream.readBytes())
            }.let { Success(it) }
        } catch (ioException: IOException) {
            Failed("There was an error fetching the data", throwable = ioException)
        } finally {
            urlConnection.disconnect()
        }
    }

}
sealed interface FetchNewsListResult
class Success(val result: String) : FetchNewsListResult
class Failed(val text: String, val throwable: Throwable) : FetchNewsListResult
