package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.models

import android.os.Parcelable
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.data.remote.dto.GetLatestAppVersionResponseDto
import kotlinx.parcelize.Parcelize

@Parcelize
data class GetLatestAppVersionResponse(
    val versionCode: Int,
    val versionName: String
) : Parcelable {
    fun toGetLatestAppVersionResponseDto(): GetLatestAppVersionResponseDto =
        GetLatestAppVersionResponseDto(
            versionCode = versionCode,
            versionName = versionName
        )
}