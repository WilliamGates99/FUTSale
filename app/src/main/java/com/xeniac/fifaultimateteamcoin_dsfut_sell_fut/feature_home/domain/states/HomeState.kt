package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.states

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class HomeState(
    val notificationPermissionCount: Int = 0,
    val permissionDialogQueue: List<String> = emptyList(),
    val isPermissionDialogVisible: Boolean = false
) : Parcelable