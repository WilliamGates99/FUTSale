package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.domain.use_case

import dagger.Lazy

data class SettingsUseCases(
    val getCurrentAppLocaleUseCase: Lazy<GetCurrentAppLocaleUseCase>,
    val getCurrentAppThemeUseCase: Lazy<GetCurrentAppThemeUseCase>,
    val getIsNotificationSoundEnabledUseCase: Lazy<GetIsNotificationSoundEnabledUseCase>,
    val getIsNotificationVibrateEnabledUseCase: Lazy<GetIsNotificationVibrateEnabledUseCase>,
    val setCurrentAppLocaleUseCase: Lazy<SetCurrentAppLocaleUseCase>,
    val setCurrentAppThemeUseCase: Lazy<SetCurrentAppThemeUseCase>,
    val setIsNotificationSoundEnabledUseCase: Lazy<SetIsNotificationSoundEnabledUseCase>,
    val setIsNotificationVibrateEnabledUseCase: Lazy<SetIsNotificationVibrateEnabledUseCase>
)