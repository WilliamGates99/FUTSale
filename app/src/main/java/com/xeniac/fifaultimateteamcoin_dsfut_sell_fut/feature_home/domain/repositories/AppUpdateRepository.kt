package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.repositories

import com.google.android.play.core.appupdate.AppUpdateInfo
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.BuildConfig
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.Result
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.errors.GetLatestAppVersionError
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.models.LatestAppUpdateInfo
import kotlinx.coroutines.flow.Flow

typealias UpdateType = Int
typealias IsUpdateDownloaded = Boolean

interface AppUpdateRepository {

    fun checkFlexibleUpdateDownloadState(): Flow<IsUpdateDownloaded>

    fun checkIsFlexibleUpdateStalled(): Flow<IsUpdateDownloaded>

    fun checkIsImmediateUpdateStalled(): Flow<AppUpdateInfo?>

    fun checkForAppUpdates(): Flow<AppUpdateInfo?>

    suspend fun getLatestAppVersion(): Result<LatestAppUpdateInfo?, GetLatestAppVersionError>

    sealed class EndPoints(val url: String) {
        data object GetLatestAppVersion : EndPoints(
            url = "${BuildConfig.FUTSALE_HTTP_BASE_URL}/app_version.json"
        )
    }
}