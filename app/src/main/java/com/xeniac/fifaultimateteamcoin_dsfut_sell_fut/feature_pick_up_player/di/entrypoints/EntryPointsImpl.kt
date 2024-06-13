package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.di.entrypoints

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.services.PickUpPlayerNotificationService
import dagger.hilt.EntryPoints

private lateinit var pickUpPlayerNotificationService: PickUpPlayerNotificationServiceEntryPoint

@Composable
fun requirePickUpPlayerNotificationService(): PickUpPlayerNotificationService {
    if (!::pickUpPlayerNotificationService.isInitialized) {
        pickUpPlayerNotificationService = EntryPoints.get(
            LocalContext.current.applicationContext,
            PickUpPlayerNotificationServiceEntryPoint::class.java
        )
    }
    return pickUpPlayerNotificationService.notificationService
}