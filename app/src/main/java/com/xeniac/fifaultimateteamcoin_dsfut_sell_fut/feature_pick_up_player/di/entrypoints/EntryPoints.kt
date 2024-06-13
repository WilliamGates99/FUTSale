package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.di.entrypoints

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.services.PickUpPlayerNotificationService
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface PickUpPlayerNotificationServiceEntryPoint {
    val notificationService: PickUpPlayerNotificationService
}