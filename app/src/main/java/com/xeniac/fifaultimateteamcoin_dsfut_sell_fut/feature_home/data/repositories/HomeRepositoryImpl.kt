package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.data.repositories

import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManager
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.repositories.HomeRepository
import dagger.Lazy
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import timber.log.Timber
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val reviewManager: Lazy<ReviewManager>
) : HomeRepository {

    override fun requestInAppReviews(): Flow<ReviewInfo?> = callbackFlow {
        reviewManager.get().requestReviewFlow().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Timber.i("In-App Reviews request was successful.")
                trySend(task.result)
            } else {
                Timber.e("In-App Reviews request was not successful:")
                task.exception?.printStackTrace()
                trySend(null)
            }
        }

        awaitClose { }
    }
}