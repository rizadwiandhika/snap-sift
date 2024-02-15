package com.rizadwi.snapsift.datasource.service

import com.rizadwi.snapsift.datasource.service.dto.response.ArticleResponse
import com.rizadwi.snapsift.datasource.service.dto.response.SourceResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface NewsService {

    @GET("top-headlines/sources")
    suspend fun getNewsSourcesByCategory(
        @Query("category") category: String
    ): Response<SourceResponse>

    @GET("top-headlines/sources")
    suspend fun getNewsSources(): Response<SourceResponse>

    @GET("everything")
    suspend fun getHeadlineArticles(@QueryMap(encoded = false) map: Map<String, String>): Response<ArticleResponse>

}