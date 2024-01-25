package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.use_case

import dagger.Lazy

data class HomeUseCases(
    val getNotificationPermissionCountUseCase: Lazy<GetNotificationPermissionCountUseCase>,
    val setNotificationPermissionCountUseCase: Lazy<SetNotificationPermissionCountUseCase>
)