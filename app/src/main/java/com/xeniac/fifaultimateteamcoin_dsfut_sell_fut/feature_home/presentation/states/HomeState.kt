package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.presentation.states

import android.os.Parcelable
import com.google.android.play.core.review.ReviewInfo
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.RateAppOption
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.PreviousRateAppRequestTimeInMs
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.models.LatestAppUpdateInfo
import kotlinx.parcelize.Parcelize

@Parcelize
data class HomeState(
    val notificationPermissionCount: Int = 0,
    val permissionDialogQueue: List<String> = emptyList(),
    val isPermissionDialogVisible: Boolean = false,
    val selectedRateAppOption: RateAppOption? = null,
    val previousRateAppRequestTimeInMs: PreviousRateAppRequestTimeInMs? = null,
    val inAppReviewInfo: ReviewInfo? = null,
    val latestAppUpdateInfo: LatestAppUpdateInfo? = null
) : Parcelable