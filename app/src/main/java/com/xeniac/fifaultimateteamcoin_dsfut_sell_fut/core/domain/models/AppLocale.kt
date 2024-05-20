package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models

import android.os.Parcelable
import androidx.compose.ui.unit.LayoutDirection
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.utils.UiText
import kotlinx.parcelize.Parcelize

@Parcelize
enum class AppLocale(
    val index: Int,
    val languageTag: String?,
    val localeString: String?,
    val layoutDirectionCompose: LayoutDirection,
    val layoutDirection: Int,
    val text: UiText
) : Parcelable {
    DEFAULT(
        index = 0,
        languageTag = null,
        localeString = null,
        layoutDirectionCompose = LayoutDirection.Ltr,
        layoutDirection = -1, // LayoutDirection.UNDEFINED
        text = UiText.StringResource(R.string.settings_text_settings_language_default)
    ),
    ENGLISH_US(
        index = 1,
        languageTag = "en-US",
        localeString = "en_US",
        layoutDirectionCompose = LayoutDirection.Ltr,
        layoutDirection = android.util.LayoutDirection.LTR,
        text = UiText.StringResource(R.string.settings_text_settings_language_english_us)
    ),
    ENGLISH_GB(
        index = 2,
        languageTag = "en-GB",
        localeString = "en_GB",
        layoutDirectionCompose = LayoutDirection.Ltr,
        layoutDirection = android.util.LayoutDirection.LTR,
        text = UiText.StringResource(R.string.settings_text_settings_language_english_gb)
    ),
    FARSI_IR(
        index = 3,
        languageTag = "fa-IR",
        localeString = "fa_IR",
        layoutDirectionCompose = LayoutDirection.Rtl,
        layoutDirection = android.util.LayoutDirection.RTL,
        text = UiText.StringResource(R.string.settings_text_settings_language_farsi_ir)
    )
}