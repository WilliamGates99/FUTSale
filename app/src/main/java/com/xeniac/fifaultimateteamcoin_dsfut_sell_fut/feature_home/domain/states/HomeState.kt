package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.states

import android.os.Parcelable
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.RateAppOption
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.PreviousRateAppRequestTimeInMs
import kotlinx.parcelize.Parcelize

@Parcelize
data class HomeState(
    val notificationPermissionCount: Int = 0,
    val permissionDialogQueue: List<String> = emptyList(),
    val isPermissionDialogVisible: Boolean = false,
    val selectedRateAppOption: RateAppOption? = null,
    val previousRateAppRequestTimeInMs: PreviousRateAppRequestTimeInMs? = null
) : Parcelable