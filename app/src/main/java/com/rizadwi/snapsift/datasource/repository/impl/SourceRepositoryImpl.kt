package com.rizadwi.snapsift.datasource.repository.impl

import com.rizadwi.snapsift.common.wrapper.Result
import com.rizadwi.snapsift.datasource.repository.SourceRepository
import com.rizadwi.snapsift.datasource.service.NewsService
import com.rizadwi.snapsift.model.Source
import com.rizadwi.snapsift.util.extension.getResult
import javax.inject.Inject

class SourceRepositoryImpl @Inject constructor(private val service: NewsService) :
    SourceRepository {
    override suspend fun getByCategory(category: String): Result<List<Source>> = try {
        val response = service.getNewsSourcesByCategory(category)
        when (val it = response.getResult()) {
            is Result.Failure -> it
            is Result.Success -> Result.Success(it.payload.sources)
        }
    } catch (t: Throwable) {
        Result.Failure(t)
    }


    override suspend fun getAll(): Result<List<Source>> = try {
        val response = service.getNewsSources()
        when (val it = response.getResult()) {
            is Result.Failure -> it
            is Result.Success -> Result.Success(it.payload.sources)
        }
    } catch (t: Throwable) {
        Result.Failure(t)
    }


    override suspend fun getCategories(): Result<List<String>> = Result.Success(
        listOf(
            "all",
            "business",
            "entertainment",
            "general",
            "health",
            "science",
            "sports",
            "technology",
        )
    )
}