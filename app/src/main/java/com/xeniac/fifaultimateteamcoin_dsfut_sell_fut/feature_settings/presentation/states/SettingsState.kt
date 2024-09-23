package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.presentation.states

import android.os.Parcelable
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.AppLocale
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.AppTheme
import kotlinx.parcelize.Parcelize

@Parcelize
data class SettingsState(
    val currentAppTheme: AppTheme? = null,
    val currentAppLocale: AppLocale? = null,
    val isNotificationSoundEnabled: Boolean? = null,
    val isNotificationVibrateEnabled: Boolean? = null,
    val isLocaleBottomSheetVisible: Boolean = false,
    val isThemeBottomSheetVisible: Boolean = false
) : Parcelable