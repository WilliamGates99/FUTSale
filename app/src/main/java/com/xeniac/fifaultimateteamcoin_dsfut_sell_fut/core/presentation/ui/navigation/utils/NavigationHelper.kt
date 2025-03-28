package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.ui.navigation.utils

import android.net.Uri
import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.NavType
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

inline fun <reified T : Any> serializableNavType(
    isNullableAllowed: Boolean = false,
    json: Json = Json
) = object : NavType<T>(isNullableAllowed = isNullableAllowed) {
    override fun serializeAsValue(value: T): String = Uri.encode(json.encodeToString(value))

    override fun parseValue(value: String): T = json.decodeFromString(Uri.decode(value))

    override fun put(bundle: Bundle, key: String, value: T) = bundle.putString(
        key, Uri.encode(json.encodeToString(value))
    )

    override fun get(bundle: Bundle, key: String): T? = bundle.getString(key)?.let { value ->
        json.decodeFromString(Uri.decode(value))
    }
}