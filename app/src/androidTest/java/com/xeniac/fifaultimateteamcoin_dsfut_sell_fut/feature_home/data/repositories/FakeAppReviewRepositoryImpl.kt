package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.data.repositories

import com.google.android.play.core.review.ReviewInfo
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.repositories.AppReviewRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FakeAppReviewRepositoryImpl @Inject constructor() : AppReviewRepository {

    private var isInAppReviewsAvailable = false

    fun isInAppReviewsAvailable(isAvailable: Boolean) {
        isInAppReviewsAvailable = isAvailable
    }

    override fun requestInAppReviews(): Flow<ReviewInfo?> = flow {
        if (isInAppReviewsAvailable) {
            emit(null)
        } else emit(null)
    }
}