package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.domain.use_case

import dagger.Lazy

data class SettingsUseCases(
    val getCurrentAppThemeUseCase: Lazy<GetCurrentAppThemeUseCase>,
    val getCurrentAppLocaleUseCase: Lazy<GetCurrentAppLocaleUseCase>,
    val getIsNotificationSoundActiveUseCase: Lazy<GetIsNotificationSoundActiveUseCase>,
    val getIsNotificationVibrateActiveUseCase: Lazy<GetIsNotificationVibrateActiveUseCase>,
    val setCurrentAppThemeUseCase: Lazy<SetCurrentAppThemeUseCase>,
    val setCurrentAppLocaleUseCase: Lazy<SetCurrentAppLocaleUseCase>,
    val setIsNotificationSoundActiveUseCase: Lazy<SetIsNotificationSoundActiveUseCase>,
    val setIsNotificationVibrateActiveUseCase: Lazy<SetIsNotificationVibrateActiveUseCase>
)