package com.rizadwi.snapsift.datasource.service.dto.response

import com.google.gson.annotations.SerializedName
import com.rizadwi.snapsift.model.Article

data class ArticleResponse(
    @SerializedName("status")
    val status: String,

    @SerializedName("totalResults")
    val totalResults: Int,

    @SerializedName("articles")
    val articles: List<Article>
)
