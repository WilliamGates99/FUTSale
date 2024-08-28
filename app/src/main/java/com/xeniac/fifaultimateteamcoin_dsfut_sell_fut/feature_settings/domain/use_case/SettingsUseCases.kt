package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.domain.use_case

import dagger.Lazy

data class SettingsUseCases(
    val getCurrentAppLocaleUseCase: Lazy<GetCurrentAppLocaleUseCase>,
    val getCurrentAppThemeUseCase: Lazy<GetCurrentAppThemeUseCase>,
    val getIsNotificationSoundEnabledUseCase: Lazy<GetIsNotificationSoundEnabledUseCase>,
    val getIsNotificationVibrateEnabledUseCase: Lazy<GetIsNotificationVibrateEnabledUseCase>,
    val storeCurrentAppLocaleUseCase: Lazy<StoreCurrentAppLocaleUseCase>,
    val storeCurrentAppThemeUseCase: Lazy<StoreCurrentAppThemeUseCase>,
    val storeIsNotificationSoundEnabledUseCase: Lazy<StoreIsNotificationSoundEnabledUseCase>,
    val storeIsNotificationVibrateEnabledUseCase: Lazy<StoreIsNotificationVibrateEnabledUseCase>
)