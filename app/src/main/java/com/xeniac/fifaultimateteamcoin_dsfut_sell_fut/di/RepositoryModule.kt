package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.di

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.local.PreferencesRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.local.PreferencesRepositoryImp
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.data.repository.DsfutRepositoryImp
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.domain.repository.DsfutRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelScoped::class)
abstract class RepositoryModule {

    @Binds
    @ViewModelScoped
    abstract fun bindPreferencesRepository(
        preferencesRepositoryImp: PreferencesRepositoryImp
    ): PreferencesRepository

    @Binds
    @ViewModelScoped
    abstract fun bindDsfutRepository(
        dsfutRepositoryImp: DsfutRepositoryImp
    ): DsfutRepository
}