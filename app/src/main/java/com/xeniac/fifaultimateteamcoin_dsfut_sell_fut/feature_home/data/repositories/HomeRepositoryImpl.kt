package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.data.repositories

import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.ktx.clientVersionStalenessDays
import com.google.android.play.core.ktx.installStatus
import com.google.android.play.core.ktx.isFlexibleUpdateAllowed
import com.google.android.play.core.ktx.isImmediateUpdateAllowed
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManager
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.data.utils.Constants
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.repositories.HomeRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.repositories.UpdateType
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.repositories.isUpdateDownloaded
import dagger.Lazy
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import timber.log.Timber
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val appUpdateType: Lazy<UpdateType>,
    private val appUpdateManager: Lazy<AppUpdateManager>,
    private val reviewManager: Lazy<ReviewManager>
) : HomeRepository {

    override fun checkIsFlexibleUpdateStalled(): Flow<isUpdateDownloaded> = callbackFlow {
        appUpdateManager.get().appUpdateInfo.addOnSuccessListener { updateInfo ->
            val isUpdateDownloadedButNotInstalled =
                updateInfo.installStatus == InstallStatus.DOWNLOADED

            if (isUpdateDownloadedButNotInstalled) trySend(true)
            else trySend(false)
        }

        awaitClose {}
    }

    override fun checkIsImmediateUpdateStalled(): Flow<AppUpdateInfo?> = callbackFlow {
        appUpdateManager.get().appUpdateInfo.addOnSuccessListener { updateInfo ->
            val isUpdateStalled =
                updateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS

            if (isUpdateStalled) trySend(updateInfo)
            else trySend(null)
        }

        awaitClose {}
    }

    override fun checkForAppUpdates(): Flow<AppUpdateInfo?> = callbackFlow {
        appUpdateManager.get().appUpdateInfo.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Timber.i("Check for in-app update was successful.")
                val updateInfo = task.result

                val isUpdateAvailable = updateInfo.updateAvailability() ==
                        UpdateAvailability.UPDATE_AVAILABLE

                val isUpdateAllowed = when (appUpdateType.get()) {
                    AppUpdateType.IMMEDIATE -> updateInfo.isImmediateUpdateAllowed
                    AppUpdateType.FLEXIBLE -> updateInfo.isFlexibleUpdateAllowed
                    else -> false
                }

                val stalenessDays = updateInfo.clientVersionStalenessDays ?: -1
                val hasUpdateStalenessDaysReached = stalenessDays >=
                        Constants.IN_APP_UPDATES_DAYS_FOR_FLEXIBLE_UPDATE

                val shouldStartUpdateFlow = isUpdateAvailable &&
                        isUpdateAllowed && hasUpdateStalenessDaysReached

                if (shouldStartUpdateFlow) trySend(updateInfo)
                else trySend(null)
            } else {
                Timber.e("Check for in-app update was not successful:")
                task.exception?.printStackTrace()
                trySend(null)
            }
        }

        awaitClose { }
    }

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