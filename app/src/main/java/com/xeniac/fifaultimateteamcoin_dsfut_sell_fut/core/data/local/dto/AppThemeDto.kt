package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.local.dto

import androidx.appcompat.app.AppCompatDelegate
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.AppTheme

sealed class AppThemeDto(
    val index: Int,
    val setAppTheme: () -> Unit
) {
    data object Default : AppThemeDto(
        index = 0,
        setAppTheme = { AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM) }
    )

    data object Light : AppThemeDto(
        index = 1,
        setAppTheme = { AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO) }
    )

    data object Dark : AppThemeDto(
        index = 2,
        setAppTheme = { AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES) }
    )

    fun toAppTheme(): AppTheme = when (this) {
        Default -> AppTheme.Default
        Light -> AppTheme.Light
        Dark -> AppTheme.Dark
    }
}