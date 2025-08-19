package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.di

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.repositories.ConnectivityObserverImpl
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.repositories.DsfutDataStoreRepositoryImpl
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.repositories.MD5HashGenerator
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.repositories.MiscellaneousDataStoreRepositoryImpl
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.repositories.PermissionsDataStoreRepositoryImpl
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.repositories.SettingsDataStoreRepositoryImpl
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.ConnectivityObserver
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.DsfutDataStoreRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.HashGenerator
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.MiscellaneousDataStoreRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.PermissionsDataStoreRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.SettingsDataStoreRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositorySingletonModule {

    @Binds
    @Singleton
    abstract fun bindConnectivityObserver(
        connectivityObserverImpl: ConnectivityObserverImpl
    ): ConnectivityObserver

    @Binds
    @Singleton
    abstract fun bindPermissionsDataStoreRepository(
        permissionsDataStoreRepositoryImpl: PermissionsDataStoreRepositoryImpl
    ): PermissionsDataStoreRepository

    @Binds
    @Singleton
    abstract fun bindSettingsDataStoreRepository(
        settingsDataStoreRepositoryImpl: SettingsDataStoreRepositoryImpl
    ): SettingsDataStoreRepository

    @Binds
    @Singleton
    abstract fun bindMiscellaneousDataStoreRepository(
        miscellaneousDataStoreRepositoryImpl: MiscellaneousDataStoreRepositoryImpl
    ): MiscellaneousDataStoreRepository

    @Binds
    @Singleton
    abstract fun bindDsfutDataStoreRepository(
        dsfutDataStoreRepositoryImpl: DsfutDataStoreRepositoryImpl
    ): DsfutDataStoreRepository

    @Binds
    @Singleton
    @MD5HashGeneratorQualifier
    abstract fun bindMD5HashGenerator(
        md5HashGenerator: MD5HashGenerator
    ): HashGenerator
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MD5HashGeneratorQualifier