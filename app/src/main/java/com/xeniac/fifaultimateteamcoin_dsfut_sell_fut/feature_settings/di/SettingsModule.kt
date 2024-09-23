package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.di

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.SettingsDataStoreRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.domain.use_case.GetCurrentAppLocaleUseCase
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.domain.use_case.GetCurrentAppThemeUseCase
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.domain.use_case.GetIsNotificationSoundEnabledUseCase
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.domain.use_case.GetIsNotificationVibrateEnabledUseCase
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.domain.use_case.SettingsUseCases
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.domain.use_case.StoreCurrentAppLocaleUseCase
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.domain.use_case.StoreCurrentAppThemeUseCase
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.domain.use_case.StoreIsNotificationSoundEnabledUseCase
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.domain.use_case.StoreIsNotificationVibrateEnabledUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
internal object SettingsModule {

    @Provides
    @ViewModelScoped
    fun provideGetCurrentAppLocaleUseCase(
        settingsDataStoreRepository: SettingsDataStoreRepository
    ): GetCurrentAppLocaleUseCase = GetCurrentAppLocaleUseCase(settingsDataStoreRepository)

    @Provides
    @ViewModelScoped
    fun provideGetCurrentAppThemeUseCase(
        settingsDataStoreRepository: SettingsDataStoreRepository
    ): GetCurrentAppThemeUseCase = GetCurrentAppThemeUseCase(settingsDataStoreRepository)

    @Provides
    @ViewModelScoped
    fun provideGetIsNotificationSoundEnabledUseCase(
        settingsDataStoreRepository: SettingsDataStoreRepository
    ): GetIsNotificationSoundEnabledUseCase =
        GetIsNotificationSoundEnabledUseCase(settingsDataStoreRepository)

    @Provides
    @ViewModelScoped
    fun provideGetIsNotificationVibrateEnabledUseCase(
        settingsDataStoreRepository: SettingsDataStoreRepository
    ): GetIsNotificationVibrateEnabledUseCase =
        GetIsNotificationVibrateEnabledUseCase(settingsDataStoreRepository)

    @Provides
    @ViewModelScoped
    fun provideStoreCurrentAppLocaleUseCase(
        settingsDataStoreRepository: SettingsDataStoreRepository
    ): StoreCurrentAppLocaleUseCase = StoreCurrentAppLocaleUseCase(settingsDataStoreRepository)

    @Provides
    @ViewModelScoped
    fun provideStoreCurrentAppThemeUseCase(
        settingsDataStoreRepository: SettingsDataStoreRepository
    ): StoreCurrentAppThemeUseCase = StoreCurrentAppThemeUseCase(settingsDataStoreRepository)

    @Provides
    @ViewModelScoped
    fun provideStoreIsNotificationSoundEnabledUseCase(
        settingsDataStoreRepository: SettingsDataStoreRepository
    ): StoreIsNotificationSoundEnabledUseCase = StoreIsNotificationSoundEnabledUseCase(
        settingsDataStoreRepository
    )

    @Provides
    @ViewModelScoped
    fun provideStoreIsNotificationVibrateEnabledUseCase(
        settingsDataStoreRepository: SettingsDataStoreRepository
    ): StoreIsNotificationVibrateEnabledUseCase = StoreIsNotificationVibrateEnabledUseCase(
        settingsDataStoreRepository
    )

    @Provides
    @ViewModelScoped
    fun provideSettingsUseCases(
        getCurrentAppLocaleUseCase: GetCurrentAppLocaleUseCase,
        getCurrentAppThemeUseCase: GetCurrentAppThemeUseCase,
        getIsNotificationSoundEnabledUseCase: GetIsNotificationSoundEnabledUseCase,
        getIsNotificationVibrateEnabledUseCase: GetIsNotificationVibrateEnabledUseCase,
        storeCurrentAppLocaleUseCase: StoreCurrentAppLocaleUseCase,
        storeCurrentAppThemeUseCase: StoreCurrentAppThemeUseCase,
        storeIsNotificationSoundEnabledUseCase: StoreIsNotificationSoundEnabledUseCase,
        storeIsNotificationVibrateEnabledUseCase: StoreIsNotificationVibrateEnabledUseCase
    ): SettingsUseCases = SettingsUseCases(
        { getCurrentAppLocaleUseCase },
        { getCurrentAppThemeUseCase },
        { getIsNotificationSoundEnabledUseCase },
        { getIsNotificationVibrateEnabledUseCase },
        { storeCurrentAppLocaleUseCase },
        { storeCurrentAppThemeUseCase },
        { storeIsNotificationSoundEnabledUseCase },
        { storeIsNotificationVibrateEnabledUseCase }
    )
}