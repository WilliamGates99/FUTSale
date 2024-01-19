package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.di

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.local.PreferencesRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.local.PreferencesRepositoryImp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositorySingletonModule {

    @Binds
    @ViewModelScoped
    abstract fun bindPreferencesRepository(
        preferencesRepositoryImp: PreferencesRepositoryImp
    ): PreferencesRepository
}