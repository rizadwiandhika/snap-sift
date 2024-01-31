package com.rizadwi.snapsift.usecase

import com.rizadwi.snapsift.common.wrapper.Result
import com.rizadwi.snapsift.datasource.repository.SourceRepository
import com.rizadwi.snapsift.model.Source
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class GetAllSourcesUseCase @Inject constructor(private val sourceRepository: SourceRepository) {
    suspend fun invoke(): Result<List<Source>> {
        return sourceRepository.getAll()
    }
}