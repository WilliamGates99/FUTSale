package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.repositories

import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.review.ReviewInfo
import kotlinx.coroutines.flow.Flow

typealias UpdateType = Int
typealias IsUpdateDownloaded = Boolean

interface HomeRepository {

    fun checkFlexibleUpdateDownloadState(): Flow<IsUpdateDownloaded>

    fun checkIsFlexibleUpdateStalled(): Flow<IsUpdateDownloaded>

    fun checkIsImmediateUpdateStalled(): Flow<AppUpdateInfo?>

    fun checkForAppUpdates(): Flow<AppUpdateInfo?>

    fun requestInAppReviews(): Flow<ReviewInfo?>
}