package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.di

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.PreferencesRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.domain.use_case.GetCurrentAppLocaleUseCase
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.domain.use_case.GetCurrentAppThemeUseCase
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.domain.use_case.GetIsNotificationSoundEnabledUseCase
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.domain.use_case.GetIsNotificationVibrateEnabledUseCase
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.domain.use_case.SetCurrentAppLocaleUseCase
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.domain.use_case.SetCurrentAppThemeUseCase
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.domain.use_case.SetIsNotificationSoundEnabledUseCase
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.domain.use_case.SetIsNotificationVibrateEnabledUseCase
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.domain.use_case.SettingsUseCases
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
    ): GetIsNotificationSoundEnabledUseCase = GetIsNotificationSoundEnabledUseCase(
        preferencesRepository
    )

    @Provides
    @ViewModelScoped
    fun provideGetIsNotificationVibrateEnabledUseCase(
        preferencesRepository: PreferencesRepository
    ): GetIsNotificationVibrateEnabledUseCase = GetIsNotificationVibrateEnabledUseCase(
        preferencesRepository
    )

    @Provides
    @ViewModelScoped
    fun provideSetCurrentAppLocaleUseCase(
        preferencesRepository: PreferencesRepository
    ): SetCurrentAppLocaleUseCase = SetCurrentAppLocaleUseCase(preferencesRepository)

    @Provides
    @ViewModelScoped
    fun provideSetCurrentAppThemeUseCase(
        preferencesRepository: PreferencesRepository
    ): SetCurrentAppThemeUseCase = SetCurrentAppThemeUseCase(preferencesRepository)

    @Provides
    @ViewModelScoped
    fun provideSetIsNotificationSoundEnabledUseCase(
        preferencesRepository: PreferencesRepository
    ): SetIsNotificationSoundEnabledUseCase = SetIsNotificationSoundEnabledUseCase(
        preferencesRepository
    )

    @Provides
    @ViewModelScoped
    fun provideSetIsNotificationVibrateEnabledUseCase(
        preferencesRepository: PreferencesRepository
    ): SetIsNotificationVibrateEnabledUseCase = SetIsNotificationVibrateEnabledUseCase(
        preferencesRepository
    )

    @Provides
    @ViewModelScoped
    fun provideSettingsUseCases(
        getCurrentAppLocaleUseCase: GetCurrentAppLocaleUseCase,
        getCurrentAppThemeUseCase: GetCurrentAppThemeUseCase,
        getIsNotificationSoundEnabledUseCase: GetIsNotificationSoundEnabledUseCase,
        getIsNotificationVibrateEnabledUseCase: GetIsNotificationVibrateEnabledUseCase,
        setCurrentAppLocaleUseCase: SetCurrentAppLocaleUseCase,
        setCurrentAppThemeUseCase: SetCurrentAppThemeUseCase,
        setIsNotificationSoundEnabledUseCase: SetIsNotificationSoundEnabledUseCase,
        setIsNotificationVibrateEnabledUseCase: SetIsNotificationVibrateEnabledUseCase
    ): SettingsUseCases = SettingsUseCases(
        { getCurrentAppLocaleUseCase },
        { getCurrentAppThemeUseCase },
        { getIsNotificationSoundEnabledUseCase },
        { getIsNotificationVibrateEnabledUseCase },
        { setCurrentAppLocaleUseCase },
        { setCurrentAppThemeUseCase },
        { setIsNotificationSoundEnabledUseCase },
        { setIsNotificationVibrateEnabledUseCase }
    )
}