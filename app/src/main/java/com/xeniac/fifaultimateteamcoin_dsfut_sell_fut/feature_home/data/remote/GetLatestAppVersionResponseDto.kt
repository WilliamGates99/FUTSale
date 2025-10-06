package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetLatestAppVersionResponseDto(
    @SerialName("version_code")
    val versionCode: Int,
    @SerialName("version_name")
    val versionName: String
)