package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.domain.use_case

import dagger.Lazy

data class SettingsUseCases(
    val getCurrentAppThemeUseCase: Lazy<GetCurrentAppThemeUseCase>,
    val getCurrentAppLocaleUseCase: Lazy<GetCurrentAppLocaleUseCase>,
    val getIsNotificationSoundEnabledUseCase: Lazy<GetIsNotificationSoundEnabledUseCase>,
    val getIsNotificationVibrateEnabledUseCase: Lazy<GetIsNotificationVibrateEnabledUseCase>,
    val setCurrentAppThemeUseCase: Lazy<SetCurrentAppThemeUseCase>,
    val setCurrentAppLocaleUseCase: Lazy<SetCurrentAppLocaleUseCase>,
    val setIsNotificationSoundEnabledUseCase: Lazy<SetIsNotificationSoundEnabledUseCase>,
    val setIsNotificationVibrateEnabledUseCase: Lazy<SetIsNotificationVibrateEnabledUseCase>
)