package com.example.newslist.data

import java.util.Date

data class NewsList (
    val id:  String,
    val title: String,
    val description:  String,
    val imageUrl: String,
    val author: String,
    val pubDate: Date,
    val articleLink: String,
    val keywords: String

){
    override fun toString(): String {
        return "$id, $title, $description, $imageUrl, $author, $pubDate, $articleLink, $keywords"
    }

}