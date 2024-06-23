package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.use_case

import dagger.Lazy

data class HomeUseCases(
    val checkForAppUpdatesUseCase: Lazy<CheckForAppUpdatesUseCase>,
    val requestInAppReviewsUseCase: Lazy<RequestInAppReviewsUseCase>,
    val getNotificationPermissionCountUseCase: Lazy<GetNotificationPermissionCountUseCase>,
    val setNotificationPermissionCountUseCase: Lazy<SetNotificationPermissionCountUseCase>,
    val getSelectedRateAppOptionUseCase: Lazy<GetSelectedRateAppOptionUseCase>,
    val setSelectedRateAppOptionUseCase: Lazy<SetSelectedRateAppOptionUseCase>,
    val getPreviousRateAppRequestTimeInMsUseCase: Lazy<GetPreviousRateAppRequestTimeInMsUseCase>,
    val setPreviousRateAppRequestTimeInMsUseCase: Lazy<SetPreviousRateAppRequestTimeInMsUseCase>
)