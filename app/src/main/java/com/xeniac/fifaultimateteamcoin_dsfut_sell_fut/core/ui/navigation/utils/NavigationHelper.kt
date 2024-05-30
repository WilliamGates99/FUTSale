package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.navigation.utils

import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.Player
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(
    navController: NavHostController
): T {
    val navGraphRoute = destination.parent?.route ?: return hiltViewModel()
    val parentEntry = remember(key1 = this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return hiltViewModel(parentEntry)
}

inline fun <reified T : Parcelable> customNavType() = object : NavType<T>(
    isNullableAllowed = false
) {
    override fun put(bundle: Bundle, key: String, value: T) {
        bundle.putParcelable(key, value)
    }

    override fun get(bundle: Bundle, key: String): T? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            bundle.getParcelable(key, T::class.java)
        } else {
            @Suppress("DEPRECATION")
            bundle.getParcelable(key)
        }
    }

    override fun serializeAsValue(value: T): String = Json.encodeToString(value)

    override fun parseValue(value: String): T = Json.decodeFromString(value)
}

val PlayerCustomNavType = object : NavType<Player>(isNullableAllowed = false) {
    override fun put(bundle: Bundle, key: String, value: Player) {
        bundle.putParcelable(key, value)
    }

    override fun get(bundle: Bundle, key: String): Player? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            bundle.getParcelable(key, Player::class.java)
        } else {
            @Suppress("DEPRECATION")
            bundle.getParcelable(key)
        }
    }

    override fun serializeAsValue(value: Player): String = Json.encodeToString(value)

    override fun parseValue(value: String): Player = Json.decodeFromString<Player>(value)
}