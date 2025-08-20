package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.repositories

import androidx.datastore.core.DataStore
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.DsfutPreferences
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.Platform
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.DsfutDataStoreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

class DsfutDataStoreRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<DsfutPreferences>
) : DsfutDataStoreRepository {

    override suspend fun isOnboardingCompleted(): Boolean = try {
        dataStore.data.first().isOnboardingCompleted
    } catch (e: Exception) {
        Timber.e("Get is onboarding completed failed:")
        e.printStackTrace()
        false
    }

    override suspend fun isOnboardingCompleted(isCompleted: Boolean) {
        try {
            dataStore.updateData { it.copy(isOnboardingCompleted = isCompleted) }
            Timber.i("Is onboarding completed edited to $isCompleted")
        } catch (e: Exception) {
            Timber.e("Store is onboarding completed failed:")
            e.printStackTrace()
        }
    }

    override suspend fun getPartnerId(): String? = try {
        dataStore.data.first().partnerId
    } catch (e: Exception) {
        Timber.e("Get partner id failed:")
        e.printStackTrace()
        null
    }

    override suspend fun getSecretKey(): String? = try {
        dataStore.data.first().secretKey
    } catch (e: Exception) {
        Timber.e("Get secret key failed:")
        e.printStackTrace()
        null
    }

    override fun getSelectedPlatform(): Flow<Platform> = dataStore.data.map {
        it.selectedPlatform
    }.catch { e ->
        Timber.e("Get selected platform failed:")
        e.printStackTrace()
    }

    override suspend fun storePartnerId(partnerId: String?) {
        try {
            dataStore.updateData {
                if (partnerId.isNullOrBlank()) {
                    it.copy(partnerId = null)
                } else {
                    it.copy(partnerId = partnerId)
                }
            }
            Timber.i("Partner id edited to $partnerId")
        } catch (e: Exception) {
            Timber.e("Store partner id failed:")
            e.printStackTrace()
        }
    }

    override suspend fun storeSecretKey(secretKey: String?) {
        try {
            dataStore.updateData {
                if (secretKey.isNullOrBlank()) {
                    it.copy(secretKey = null)
                } else {
                    it.copy(secretKey = secretKey)
                }
            }
            Timber.i("Secret key edited to $secretKey")
        } catch (e: Exception) {
            Timber.e("Store secret key failed:")
            e.printStackTrace()
        }
    }

    override suspend fun storeSelectedPlatform(platform: Platform) {
        try {
            dataStore.updateData { it.copy(selectedPlatform = platform) }
            Timber.i("Selected platform edited to ${platform.value}")
        } catch (e: Exception) {
            Timber.e("Store selected platform failed:")
            e.printStackTrace()
        }
    }
}