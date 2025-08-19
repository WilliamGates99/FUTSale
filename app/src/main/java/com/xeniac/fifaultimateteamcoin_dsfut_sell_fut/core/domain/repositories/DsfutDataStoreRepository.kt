package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.Platform
import kotlinx.coroutines.flow.Flow

interface DsfutDataStoreRepository {

    suspend fun isOnboardingCompleted(): Boolean

    suspend fun isOnboardingCompleted(isCompleted: Boolean)

    suspend fun getPartnerId(): String?

    suspend fun storePartnerId(partnerId: String?)

    suspend fun getSecretKey(): String?

    suspend fun storeSecretKey(secretKey: String?)

    fun getSelectedPlatform(): Flow<Platform>

    suspend fun storeSelectedPlatform(platform: Platform)
}