package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.data.repositories

import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.review.ReviewInfo
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.repositories.HomeRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.repositories.isUpdateDownloaded
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FakeHomeRepositoryImpl @Inject constructor() : HomeRepository {

    private var isFlexibleUpdateStalled = false
    private var isImmediateUpdateStalled = false
    private var isAppUpdateAvailable = false
    private var isInAppReviewsAvailable = false

    fun isFlexibleUpdateStalled(isStalled: Boolean) {
        isFlexibleUpdateStalled = isStalled
    }

    fun isImmediateUpdateStalled(isStalled: Boolean) {
        isImmediateUpdateStalled = isStalled
    }

    fun isAppUpdateAvailable(isAvailable: Boolean) {
        isAppUpdateAvailable = isAvailable
    }

    fun isInAppReviewsAvailable(isAvailable: Boolean) {
        isInAppReviewsAvailable = isAvailable
    }

    override fun checkIsFlexibleUpdateStalled(): Flow<isUpdateDownloaded> = flow {
        emit(isFlexibleUpdateStalled)
    }

    override fun checkIsImmediateUpdateStalled(): Flow<AppUpdateInfo?> = flow {
        if (isImmediateUpdateStalled) {
            val appUpdateInfo: AppUpdateInfo = AppUpdateInfo.zzb(
                "", 1, 1, 1, null, 1, 1L, 1L, 1L, 1L, null, null, null, null, mapOf(Pair("", ""))
            )
            emit(appUpdateInfo)
        } else emit(null)
    }

    override fun checkForAppUpdates(): Flow<AppUpdateInfo?> = flow {
        if (isAppUpdateAvailable) {
            val appUpdateInfo: AppUpdateInfo = AppUpdateInfo.zzb(
                "", 1, 1, 1, null, 1, 1L, 1L, 1L, 1L, null, null, null, null, mapOf(Pair("", ""))
            )
            emit(appUpdateInfo)
        } else emit(null)
    }

    override fun requestInAppReviews(): Flow<ReviewInfo?> = flow {
        if (isInAppReviewsAvailable) {
            emit(null)
        } else emit(null)
    }
}