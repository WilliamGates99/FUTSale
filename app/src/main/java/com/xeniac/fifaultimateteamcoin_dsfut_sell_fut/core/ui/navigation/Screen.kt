package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.navigation

import android.os.Parcelable
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.Player
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
sealed class Screen : Parcelable {

    @Serializable
    data object OnboardingScreen : Screen()

    @Serializable
    data object HomeScreen : Screen()

    @Serializable
    data object PickUpPlayerScreen : Screen()

    @Serializable
    data class PickedUpPlayerInfoScreen(val player: Player) : Screen()

    @Serializable
    data object ProfileScreen : Screen()

    @Serializable
    data object HistoryScreen : Screen()

    @Serializable
    data class HistoryPlayerInfoScreen(val player: Player) : Screen()

    @Serializable
    data object SettingsScreen : Screen()
}