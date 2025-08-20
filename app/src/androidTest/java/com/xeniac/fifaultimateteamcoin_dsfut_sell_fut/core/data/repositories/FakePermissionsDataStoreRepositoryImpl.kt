package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.repositories

import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.PermissionsDataStoreRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FakePermissionsDataStoreRepositoryImpl @Inject constructor(
) : PermissionsDataStoreRepository {

    var notificationPermissionCount = SnapshotStateList<Int>().apply {
        add(0)
    }

    override fun getNotificationPermissionCount(): Flow<Int> = snapshotFlow {
        notificationPermissionCount.first()
    }

    override suspend fun storeNotificationPermissionCount(count: Int) {
        notificationPermissionCount.apply {
            clear()
            add(count)
        }
    }
}