package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.common.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
data object OnboardingScreen

@Serializable
data object HomeScreen

@Serializable
data object PickUpPlayerScreen

@Serializable
data class PickedUpPlayerInfoScreen(val playerId: Long)

@Serializable
data object ProfileScreen

@Serializable
data object HistoryScreen

@Serializable
data class HistoryPlayerInfoScreen(val playerId: Long)

@Serializable
data object SettingsScreen