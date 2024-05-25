package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.domain.states

import android.os.Parcelable
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.AppLocale
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.AppTheme
import kotlinx.parcelize.Parcelize

@Parcelize
data class SettingsState(
    val appLocale: AppLocale = AppLocale.DEFAULT,
    val appTheme: AppTheme = AppTheme.DEFAULT,
    val isNotificationSoundEnabled: Boolean = true,
    val isNotificationVibrateEnabled: Boolean = true
) : Parcelable