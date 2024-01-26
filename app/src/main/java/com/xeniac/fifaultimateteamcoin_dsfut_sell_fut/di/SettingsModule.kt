package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.di

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.local.PreferencesRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.domain.use_case.GetCurrentAppLocaleUseCase
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.domain.use_case.GetCurrentAppThemeUseCase
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.domain.use_case.GetIsNotificationSoundActiveUseCase
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.domain.use_case.GetIsNotificationVibrateActiveUseCase
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.domain.use_case.SetCurrentAppLocaleUseCase
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.domain.use_case.SetCurrentAppThemeUseCase
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.domain.use_case.SetIsNotificationSoundActiveUseCase
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.domain.use_case.SetIsNotificationVibrateActiveUseCase
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
    fun provideGetCurrentAppThemeUseCase(
        preferencesRepository: PreferencesRepository
    ): GetCurrentAppThemeUseCase = GetCurrentAppThemeUseCase(preferencesRepository)

    @Provides
    @ViewModelScoped
    fun provideGetCurrentAppLocaleUseCase(
        preferencesRepository: PreferencesRepository
    ): GetCurrentAppLocaleUseCase = GetCurrentAppLocaleUseCase(preferencesRepository)

    @Provides
    @ViewModelScoped
    fun provideGetIsNotificationSoundActiveUseCase(
        preferencesRepository: PreferencesRepository
    ): GetIsNotificationSoundActiveUseCase = GetIsNotificationSoundActiveUseCase(
        preferencesRepository
    )

    @Provides
    @ViewModelScoped
    fun provideGetIsNotificationVibrateActiveUseCase(
        preferencesRepository: PreferencesRepository
    ): GetIsNotificationVibrateActiveUseCase = GetIsNotificationVibrateActiveUseCase(
        preferencesRepository
    )

    @Provides
    @ViewModelScoped
    fun provideSetCurrentAppThemeUseCase(
        preferencesRepository: PreferencesRepository
    ): SetCurrentAppThemeUseCase = SetCurrentAppThemeUseCase(preferencesRepository)

    @Provides
    @ViewModelScoped
    fun provideSetCurrentAppLocaleUseCase(
        preferencesRepository: PreferencesRepository
    ): SetCurrentAppLocaleUseCase = SetCurrentAppLocaleUseCase(preferencesRepository)

    @Provides
    @ViewModelScoped
    fun provideSetIsNotificationSoundActiveUseCase(
        preferencesRepository: PreferencesRepository
    ): SetIsNotificationSoundActiveUseCase = SetIsNotificationSoundActiveUseCase(
        preferencesRepository
    )

    @Provides
    @ViewModelScoped
    fun provideSetIsNotificationVibrateActiveUseCase(
        preferencesRepository: PreferencesRepository
    ): SetIsNotificationVibrateActiveUseCase = SetIsNotificationVibrateActiveUseCase(
        preferencesRepository
    )

    @Provides
    @ViewModelScoped
    fun provideSettingsUseCases(
        getCurrentAppThemeUseCase: GetCurrentAppThemeUseCase,
        getCurrentAppLocaleUseCase: GetCurrentAppLocaleUseCase,
        getIsNotificationSoundActiveUseCase: GetIsNotificationSoundActiveUseCase,
        getIsNotificationVibrateActiveUseCase: GetIsNotificationVibrateActiveUseCase,
        setCurrentAppThemeUseCase: SetCurrentAppThemeUseCase,
        setCurrentAppLocaleUseCase: SetCurrentAppLocaleUseCase,
        setIsNotificationSoundActiveUseCase: SetIsNotificationSoundActiveUseCase,
        setIsNotificationVibrateActiveUseCase: SetIsNotificationVibrateActiveUseCase
    ): SettingsUseCases = SettingsUseCases(
        { getCurrentAppThemeUseCase },
        { getCurrentAppLocaleUseCase },
        { getIsNotificationSoundActiveUseCase },
        { getIsNotificationVibrateActiveUseCase },
        { setCurrentAppThemeUseCase },
        { setCurrentAppLocaleUseCase },
        { setIsNotificationSoundActiveUseCase },
        { setIsNotificationVibrateActiveUseCase }
    )
}