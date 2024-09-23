package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.Platform
import kotlinx.coroutines.flow.Flow

interface DsfutDataStoreRepository {

    fun getPartnerId(): Flow<String?>

    fun getSecretKey(): Flow<String?>

    fun getSelectedPlatform(): Flow<Platform>

    suspend fun storePartnerId(partnerId: String?)

    suspend fun storeSecretKey(secretKey: String?)

    suspend fun storeSelectedPlatform(platform: Platform)
}