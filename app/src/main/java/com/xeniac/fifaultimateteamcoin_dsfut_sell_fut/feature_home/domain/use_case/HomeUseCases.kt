package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.use_case

import dagger.Lazy

data class HomeUseCases(
    val checkFlexibleUpdateDownloadStateUseCase: Lazy<CheckFlexibleUpdateDownloadStateUseCase>,
    val checkIsFlexibleUpdateStalledUseCase: Lazy<CheckIsFlexibleUpdateStalledUseCase>,
    val checkIsImmediateUpdateStalledUseCase: Lazy<CheckIsImmediateUpdateStalledUseCase>,
    val checkForAppUpdatesUseCase: Lazy<CheckForAppUpdatesUseCase>,
    val requestInAppReviewsUseCase: Lazy<RequestInAppReviewsUseCase>,
    val getLatestAppVersionUseCase: Lazy<GetLatestAppVersionUseCase>,
    val getNotificationPermissionCountUseCase: Lazy<GetNotificationPermissionCountUseCase>,
    val storeNotificationPermissionCountUseCase: Lazy<StoreNotificationPermissionCountUseCase>,
    val getSelectedRateAppOptionUseCase: Lazy<GetSelectedRateAppOptionUseCase>,
    val storeSelectedRateAppOptionUseCase: Lazy<StoreSelectedRateAppOptionUseCase>,
    val getPreviousRateAppRequestTimeInMsUseCase: Lazy<GetPreviousRateAppRequestTimeInMsUseCase>,
    val storePreviousRateAppRequestTimeInMsUseCase: Lazy<StorePreviousRateAppRequestTimeInMsUseCase>
)