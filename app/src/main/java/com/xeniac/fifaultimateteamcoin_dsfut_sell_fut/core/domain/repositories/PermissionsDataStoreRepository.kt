package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories

import kotlinx.coroutines.flow.Flow

interface PermissionsDataStoreRepository {

    fun getNotificationPermissionCount(): Flow<Int>

    suspend fun storeNotificationPermissionCount(count: Int)
}