package com.rizadwi.snapsift.datasource.repository.impl

import com.rizadwi.snapsift.common.wrapper.Result
import com.rizadwi.snapsift.datasource.repository.ArticleRepository
import com.rizadwi.snapsift.datasource.service.NewsService
import com.rizadwi.snapsift.model.Article
import com.rizadwi.snapsift.util.extension.getResult
import java.net.URLEncoder
import javax.inject.Inject

class ArticleRepositoryImpl @Inject constructor(private val newsService: NewsService) :
    ArticleRepository {
    override suspend fun getHeadlineArticles(
        q: String,
        sources: String,
        pageSize: Int,
        page: Int
    ): Result<List<Article>> = getHeadlineArticles(
        mapOf(
            "q" to URLEncoder.encode(q, Charsets.UTF_8.name()),
            "searchIn" to SEARCH_IN,
            "sources" to sources,
            "pageSize" to pageSize.toString(),
            "page" to page.toString(),
        )
    )

    override suspend fun getHeadlineArticles(
        sources: String,
        pageSize: Int,
        page: Int
    ): Result<List<Article>> = getHeadlineArticles(
        mapOf(
            "searchIn" to SEARCH_IN,
            "sources" to sources,
            "pageSize" to pageSize.toString(),
            "page" to page.toString(),
        )
    )

    private suspend fun getHeadlineArticles(map: Map<String, String>) = try {
        val response =
            newsService.getHeadlineArticles(map)
        when (val it = response.getResult()) {
            is Result.Failure -> it
            is Result.Success -> Result.Success(it.payload.articles)
        }
    } catch (t: Throwable) {
        Result.Failure(t)
    }

    companion object {
        const val SEARCH_IN = "title"
    }
}