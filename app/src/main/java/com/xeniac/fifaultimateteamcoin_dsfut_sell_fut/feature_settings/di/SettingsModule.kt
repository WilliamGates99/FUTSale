package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.di

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.PreferencesRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.domain.use_case.GetCurrentSettingsUseCase
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
    fun provideGetCurrentSettingsUseCase(
        preferencesRepository: PreferencesRepository
    ): GetCurrentSettingsUseCase = GetCurrentSettingsUseCase(preferencesRepository)

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
        getCurrentSettingsUseCase: GetCurrentSettingsUseCase,
        setCurrentAppLocaleUseCase: SetCurrentAppLocaleUseCase,
        setCurrentAppThemeUseCase: SetCurrentAppThemeUseCase,
        setIsNotificationSoundEnabledUseCase: SetIsNotificationSoundEnabledUseCase,
        setIsNotificationVibrateEnabledUseCase: SetIsNotificationVibrateEnabledUseCase
    ): SettingsUseCases = SettingsUseCases(
        { getCurrentSettingsUseCase },
        { setCurrentAppLocaleUseCase },
        { setCurrentAppThemeUseCase },
        { setIsNotificationSoundEnabledUseCase },
        { setIsNotificationVibrateEnabledUseCase }
    )
}