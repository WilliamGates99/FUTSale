package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LatestAppUpdateInfo(
    val versionCode: Int,
    val versionName: String
) : Parcelable