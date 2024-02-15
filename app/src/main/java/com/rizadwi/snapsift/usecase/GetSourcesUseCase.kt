package com.rizadwi.snapsift.usecase

import com.rizadwi.snapsift.common.wrapper.Result
import com.rizadwi.snapsift.datasource.repository.SourceRepository
import com.rizadwi.snapsift.model.Source
import javax.inject.Inject

class GetSourcesUseCase @Inject constructor(private val sourceRepository: SourceRepository) {
    suspend fun invoke(): Result<List<Source>> {
        return sourceRepository.getAll()
    }

    suspend fun invoke(category: String): Result<List<Source>> {
        return sourceRepository.getByCategory(category)
    }
}