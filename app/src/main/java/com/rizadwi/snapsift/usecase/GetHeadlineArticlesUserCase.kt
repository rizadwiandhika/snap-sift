package com.rizadwi.snapsift.usecase

import com.rizadwi.snapsift.common.wrapper.Result
import com.rizadwi.snapsift.datasource.repository.ArticleRepository
import com.rizadwi.snapsift.model.Article
import javax.inject.Inject

class GetHeadlineArticlesUserCase @Inject constructor(private val articleRepository: ArticleRepository) {
    suspend fun invoke(
        q: String,
        sources: String,
        pageSize: Int,
        page: Int,
    ): Result<List<Article>> {
        return articleRepository.getHeadlineArticles(q, sources, pageSize, page)
    }

    suspend fun invoke(
        sources: String,
        pageSize: Int,
        page: Int,
    ): Result<List<Article>> {
        return articleRepository.getHeadlineArticles(sources, pageSize, page)
    }
}