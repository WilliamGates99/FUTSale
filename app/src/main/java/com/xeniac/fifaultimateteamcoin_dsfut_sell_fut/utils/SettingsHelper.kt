package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils

import androidx.appcompat.app.AppCompatDelegate
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.Constants.THEME_INDEX_DARK
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.Constants.THEME_INDEX_DEFAULT
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.Constants.THEME_INDEX_LIGHT

object SettingsHelper {

    fun setAppTheme(themeIndex: Int) {
        when (themeIndex) {
            THEME_INDEX_DEFAULT -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            THEME_INDEX_LIGHT -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            THEME_INDEX_DARK -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }
}