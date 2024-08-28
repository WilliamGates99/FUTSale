package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.di

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.PreferencesRepository
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
        preferencesRepository: PreferencesRepository
    ): GetCurrentAppLocaleUseCase = GetCurrentAppLocaleUseCase(preferencesRepository)

    @Provides
    @ViewModelScoped
    fun provideGetCurrentAppThemeUseCase(
        preferencesRepository: PreferencesRepository
    ): GetCurrentAppThemeUseCase = GetCurrentAppThemeUseCase(preferencesRepository)

    @Provides
    @ViewModelScoped
    fun provideGetIsNotificationSoundEnabledUseCase(
        preferencesRepository: PreferencesRepository
    ): GetIsNotificationSoundEnabledUseCase =
        GetIsNotificationSoundEnabledUseCase(preferencesRepository)

    @Provides
    @ViewModelScoped
    fun provideGetIsNotificationVibrateEnabledUseCase(
        preferencesRepository: PreferencesRepository
    ): GetIsNotificationVibrateEnabledUseCase =
        GetIsNotificationVibrateEnabledUseCase(preferencesRepository)

    @Provides
    @ViewModelScoped
    fun provideStoreCurrentAppLocaleUseCase(
        preferencesRepository: PreferencesRepository
    ): StoreCurrentAppLocaleUseCase = StoreCurrentAppLocaleUseCase(preferencesRepository)

    @Provides
    @ViewModelScoped
    fun provideStoreCurrentAppThemeUseCase(
        preferencesRepository: PreferencesRepository
    ): StoreCurrentAppThemeUseCase = StoreCurrentAppThemeUseCase(preferencesRepository)

    @Provides
    @ViewModelScoped
    fun provideStoreIsNotificationSoundEnabledUseCase(
        preferencesRepository: PreferencesRepository
    ): StoreIsNotificationSoundEnabledUseCase = StoreIsNotificationSoundEnabledUseCase(
        preferencesRepository
    )

    @Provides
    @ViewModelScoped
    fun provideStoreIsNotificationVibrateEnabledUseCase(
        preferencesRepository: PreferencesRepository
    ): StoreIsNotificationVibrateEnabledUseCase = StoreIsNotificationVibrateEnabledUseCase(
        preferencesRepository
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