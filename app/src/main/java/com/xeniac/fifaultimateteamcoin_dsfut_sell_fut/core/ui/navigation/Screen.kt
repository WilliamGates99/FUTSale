package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.navigation

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.Player
import kotlinx.serialization.Serializable

@Serializable
data object OnboardingScreen

@Serializable
data object HomeScreen

@Serializable
data object PickUpPlayerScreen

@Serializable
data class PickedUpPlayerInfoScreen(val player: Player)

@Serializable
data object ProfileScreen

@Serializable
data object HistoryScreen

@Serializable
data class HistoryPlayerInfoScreen(val player: Player)

@Serializable
data object SettingsScreen