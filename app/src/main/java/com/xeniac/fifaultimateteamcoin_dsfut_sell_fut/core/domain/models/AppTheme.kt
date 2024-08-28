package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models

import android.os.Parcelable
import androidx.appcompat.app.AppCompatDelegate
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.local.dto.AppThemeDto
import kotlinx.parcelize.Parcelize

@Parcelize
sealed class AppTheme(
    val index: Int,
    val setAppTheme: () -> Unit
) : Parcelable {
    data object Default : AppTheme(
        index = 0,
        setAppTheme = { AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM) }
    )

    data object Light : AppTheme(
        index = 1,
        setAppTheme = { AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO) }
    )

    data object Dark : AppTheme(
        index = 2,
        setAppTheme = { AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES) }
    )

    fun toAppThemeDto(): AppThemeDto = when (this) {
        Default -> AppThemeDto.Default
        Light -> AppThemeDto.Light
        Dark -> AppThemeDto.Dark
    }
}