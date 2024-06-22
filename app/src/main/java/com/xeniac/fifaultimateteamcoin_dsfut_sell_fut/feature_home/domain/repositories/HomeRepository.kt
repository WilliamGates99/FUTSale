package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.repositories

import com.google.android.play.core.review.ReviewInfo
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    fun requestInAppReviews(): Flow<ReviewInfo?>
}