package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.repositories

import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.Platform
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.DsfutDataStoreRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FakeDsfutDataStoreRepositoryImpl @Inject constructor() : DsfutDataStoreRepository {

    var isOnBoardingCompleted = false
    var storedPartnerId: String? = null
    var storedSecretKey: String? = null
    var selectedPlatform = SnapshotStateList<Platform>().apply { add(Platform.CONSOLE) }

    fun changePartnerId(newPartnerId: String?) {
        storedPartnerId = newPartnerId
    }

    fun changeSecretKey(newSecretKey: String?) {
        storedSecretKey = newSecretKey
    }

    override suspend fun isOnboardingCompleted(): Boolean = isOnBoardingCompleted

    override suspend fun isOnboardingCompleted(isCompleted: Boolean) {
        isOnBoardingCompleted = isCompleted
    }

    override suspend fun getPartnerId(): String? = storedPartnerId

    override suspend fun storePartnerId(partnerId: String?) {
        storedPartnerId = partnerId
    }

    override suspend fun getSecretKey(): String? = storedSecretKey

    override suspend fun storeSecretKey(secretKey: String?) {
        storedSecretKey = secretKey
    }

    override fun getSelectedPlatform(): Flow<Platform> = snapshotFlow {
        selectedPlatform.first()
    }

    override suspend fun storeSelectedPlatform(platform: Platform) {
        selectedPlatform.apply {
            clear()
            add(platform)
        }
    }
}