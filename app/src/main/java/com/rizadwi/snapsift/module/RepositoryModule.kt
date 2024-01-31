package com.rizadwi.snapsift.module

import com.rizadwi.snapsift.datasource.repository.SourceRepository
import com.rizadwi.snapsift.datasource.repository.impl.SourceRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent


@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindSourceRepository(impl: SourceRepositoryImpl): SourceRepository
}