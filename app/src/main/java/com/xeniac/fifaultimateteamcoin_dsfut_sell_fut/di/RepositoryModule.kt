package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.di

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.local.PreferencesRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.local.PreferencesRepositoryImp
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
    abstract fun bindPreferencesRepository(
        preferencesRepositoryImp: PreferencesRepositoryImp
    ): PreferencesRepository

    /*
    @Binds
    @ViewModelScoped
    abstract fun bindDsfutRepository(
        dsfutRepositoryImp: DsfutRepositoryImp
    ): DsfutRepository
    */
}