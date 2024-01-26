package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.local.dto

import androidx.appcompat.app.AppCompatDelegate
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.util.UiText

enum class AppThemeDto(
    val index: Int,
    val text: UiText,
    val setAppTheme: Unit
) {
    DEFAULT(
        index = 0,
        text = UiText.StringResource(R.string.settings_text_settings_theme_default),
        setAppTheme = AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
    ),
    LIGHT(
        index = 1,
        text = UiText.StringResource(R.string.settings_text_settings_theme_light),
        setAppTheme = AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    ),
    DARK(
        index = 2,
        text = UiText.StringResource(R.string.settings_text_settings_theme_dark),
        setAppTheme = AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    )
}