package com.example.newslist.magicCards.data

data class MagicCard(
    val name: String,
    val type: String,
    val rarity: String,
    val colors: List<String>
) {
    override fun toString(): String {
        return "$name, $type, $rarity, $colors"
    }
}