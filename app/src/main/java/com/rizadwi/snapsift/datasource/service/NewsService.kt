package com.rizadwi.snapsift.datasource.service

import com.rizadwi.snapsift.datasource.service.dto.response.ArticleResponse
import com.rizadwi.snapsift.datasource.service.dto.response.SourceResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsService {

    @GET("top-headlines/sources")
    suspend fun getNewsSourcesByCategory(
        @Query("category") category: String
    ): Response<SourceResponse>

    @GET("top-headlines/sources")
    suspend fun getNewsSources(): Response<SourceResponse>

    @GET("everything")
    suspend fun getHeadlineArticles(
        @Query("q") q: String,
        @Query("sources") sources: String,
        @Query("pageSize") pageSize: Int,
        @Query("page") page: Int,
    ): Response<ArticleResponse>

    @GET("everything")
    suspend fun getHeadlineArticles(
        @Query("sources") sources: String,
        @Query("pageSize") pageSize: Int,
        @Query("page") page: Int,
    ): Response<ArticleResponse>
}