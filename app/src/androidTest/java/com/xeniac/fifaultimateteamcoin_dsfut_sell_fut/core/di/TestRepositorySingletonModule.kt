package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.di

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.repositories.FakeConnectivityObserverImpl
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.repositories.FakeDsfutDataStoreRepositoryImpl
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.repositories.FakeMiscellaneousDataStoreRepositoryImpl
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.repositories.FakeSettingsDataStoreRepositoryImpl
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.ConnectivityObserver
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.DsfutDataStoreRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.MiscellaneousDataStoreRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.SettingsDataStoreRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [RepositorySingletonModule::class]
)
abstract class TestRepositorySingletonModule {

    @Binds
    @Singleton
    abstract fun bindConnectivityObserver(
        fakeConnectivityObserverImpl: FakeConnectivityObserverImpl
    ): ConnectivityObserver

    @Binds
    @Singleton
    abstract fun bindSettingsDataStoreRepository(
        fakeSettingsDataStoreRepositoryImpl: FakeSettingsDataStoreRepositoryImpl
    ): SettingsDataStoreRepository

    @Binds
    @Singleton
    abstract fun bindDsfutDataStoreRepository(
        fakeDsfutDataStoreRepositoryImpl: FakeDsfutDataStoreRepositoryImpl
    ): DsfutDataStoreRepository

    @Binds
    @Singleton
    abstract fun bindMiscellaneousDataStoreRepository(
        fakeMiscellaneousDataStoreRepositoryImpl: FakeMiscellaneousDataStoreRepositoryImpl
    ): MiscellaneousDataStoreRepository
}