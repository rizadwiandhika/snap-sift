package com.rizadwi.snapsift.datasource.repository

import com.rizadwi.snapsift.common.wrapper.Result
import com.rizadwi.snapsift.model.Source

interface SourceRepository {

    suspend fun getByCategory(category: String): Result<List<Source>>

    suspend fun getAll(): Result<List<Source>>

    suspend fun getCategories(): Result<List<String>>

}