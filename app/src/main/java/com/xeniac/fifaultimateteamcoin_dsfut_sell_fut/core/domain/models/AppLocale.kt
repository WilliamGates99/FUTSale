package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models

import androidx.annotation.StringRes
import androidx.compose.ui.unit.LayoutDirection
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R

enum class AppLocale(
    val index: Int,
    val languageTag: String?,
    val localeString: String?,
    val layoutDirectionCompose: LayoutDirection,
    val layoutDirection: Int,
    @StringRes val titleId: Int,
    @StringRes val fullTitleId: Int
) {
    DEFAULT(
        index = 0,
        languageTag = null,
        localeString = null,
        layoutDirectionCompose = LayoutDirection.Ltr,
        layoutDirection = -1, // LayoutDirection.UNDEFINED
        titleId = R.string.core_locale_title_default,
        fullTitleId = R.string.core_locale_title_full_default
    ),
    ENGLISH_US(
        index = 1,
        languageTag = "en-US",
        localeString = "en_US",
        layoutDirectionCompose = LayoutDirection.Ltr,
        layoutDirection = android.util.LayoutDirection.LTR,
        titleId = R.string.core_locale_title_english_us,
        fullTitleId = R.string.core_locale_title_full_english_us
    ),
    ENGLISH_GB(
        index = 2,
        languageTag = "en-GB",
        localeString = "en_GB",
        layoutDirectionCompose = LayoutDirection.Ltr,
        layoutDirection = android.util.LayoutDirection.LTR,
        titleId = R.string.core_locale_title_english_gb,
        fullTitleId = R.string.core_locale_title_full_english_gb
    ),
    FARSI_IR(
        index = 3,
        languageTag = "fa-IR",
        localeString = "fa_IR",
        layoutDirectionCompose = LayoutDirection.Rtl,
        layoutDirection = android.util.LayoutDirection.RTL,
        titleId = R.string.core_locale_title_farsi_ir,
        fullTitleId = R.string.core_locale_title_full_farsi_ir
    )
}