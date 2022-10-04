package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.di

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.data.repository.MainRepositoryImp
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.data.repository.PreferencesRepositoryImp
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.domain.repository.MainRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.domain.repository.PreferencesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindMainRepository(
        mainRepositoryImp: MainRepositoryImp
    ): MainRepository

    @Binds
    @Singleton
    abstract fun bindPreferencesRepository(
        preferencesRepositoryImp: PreferencesRepositoryImp
    ): PreferencesRepository
}