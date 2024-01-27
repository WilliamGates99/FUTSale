package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.model

import android.os.Parcelable
import android.util.LayoutDirection
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.util.UiText
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
enum class AppLocale(
    val index: Int,
    val tag: String?,
    val localeString: String?,
    val direction: Int,
    val text: @RawValue UiText
) : Parcelable {
    DEFAULT(
        index = 0,
        tag = null,
        localeString = null,
        direction = -1, // LayoutDirection.UNDEFINED
        text = UiText.StringResource(R.string.settings_text_settings_language_default)
    ),
    ENGLISH_US(
        index = 1,
        tag = "en-US",
        localeString = "en_US",
        direction = LayoutDirection.LTR,
        text = UiText.StringResource(R.string.settings_text_settings_language_english_us)
    ),
    ENGLISH_GB(
        index = 2,
        tag = "en-GB",
        localeString = "en_GB",
        direction = LayoutDirection.LTR,
        text = UiText.StringResource(R.string.settings_text_settings_language_english_gb)
    ),
    FARSI_IR(
        index = 3,
        tag = "fa-IR",
        localeString = "fa_IR",
        direction = LayoutDirection.RTL,
        text = UiText.StringResource(R.string.settings_text_settings_language_farsi_ir)
    )
}