package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.repositories

import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.Platform
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.DsfutDataStoreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FakeDsfutDataStoreRepositoryImpl @Inject constructor() : DsfutDataStoreRepository {

    var storedPartnerId: String? = null
    var storedSecretKey: String? = null
    var selectedPlatform = SnapshotStateList<Platform>().apply { add(Platform.CONSOLE) }

    fun changePartnerId(newPartnerId: String?) {
        storedPartnerId = newPartnerId
    }

    fun changeSecretKey(newSecretKey: String?) {
        storedSecretKey = newSecretKey
    }

    override fun getPartnerId(): Flow<String?> = flow { emit(storedPartnerId) }

    override fun getSecretKey(): Flow<String?> = flow { emit(storedSecretKey) }

    override fun getSelectedPlatform(): Flow<Platform> = snapshotFlow { selectedPlatform.first() }

    override suspend fun storePartnerId(partnerId: String?) {
        storedPartnerId = partnerId
    }

    override suspend fun storeSecretKey(secretKey: String?) {
        storedSecretKey = secretKey
    }

    override suspend fun storeSelectedPlatform(platform: Platform) {
        selectedPlatform.apply {
            clear()
            add(platform)
        }
    }
}