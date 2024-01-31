package com.rizadwi.snapsift.usecase

import com.rizadwi.snapsift.common.wrapper.Result
import com.rizadwi.snapsift.datasource.repository.SourceRepository
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class GetCategoriesUseCase @Inject constructor(private val sourceRepository: SourceRepository) {
    suspend fun invoke(): Result<List<String>> {
        return sourceRepository.getCategories()
    }
}