package com.rizadwi.snapsift.datasource.repository

import com.rizadwi.snapsift.common.wrapper.Result
import com.rizadwi.snapsift.model.Article


interface ArticleRepository {
    suspend fun getHeadlineArticles(
        q: String,
        sources: String,
        pageSize: Int,
        page: Int,
    ): Result<List<Article>>

    suspend fun getHeadlineArticles(
        sources: String,
        pageSize: Int,
        page: Int,
    ): Result<List<Article>>
}