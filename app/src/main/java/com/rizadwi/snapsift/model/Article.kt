package com.rizadwi.snapsift.model

data class Article(
    val source: SourceShort? = null,
    val publishedAt: String? = null,
    val author: String? = null,
    val urlToImage: String? = null,
    val description: String? = null,
    val title: String? = null,
    val url: String? = null,
    val content: String? = null
)

data class SourceShort(
    val id: String?,
    val name: String?,
)