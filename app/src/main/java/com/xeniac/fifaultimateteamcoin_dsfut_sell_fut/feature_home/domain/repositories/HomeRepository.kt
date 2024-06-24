package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.repositories

import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.review.ReviewInfo
import kotlinx.coroutines.flow.Flow

typealias UpdateType = Int
typealias isUpdateDownloaded = Boolean

interface HomeRepository {

    fun checkIsFlexibleUpdateStalled(): Flow<isUpdateDownloaded>

    fun checkIsImmediateUpdateStalled(): Flow<AppUpdateInfo?>

    fun checkForAppUpdates(): Flow<AppUpdateInfo?>

    fun requestInAppReviews(): Flow<ReviewInfo?>
}