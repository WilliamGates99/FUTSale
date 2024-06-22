package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.use_case

import com.google.android.play.core.review.ReviewInfo
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.repositories.HomeRepository
import kotlinx.coroutines.flow.Flow

class RequestInAppReviewsUseCase(
    private val homeRepository: HomeRepository
) {
    operator fun invoke(): Flow<ReviewInfo?> = homeRepository.requestInAppReviews()
}