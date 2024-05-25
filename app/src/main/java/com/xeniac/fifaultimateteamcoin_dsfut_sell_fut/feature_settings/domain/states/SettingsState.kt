package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.domain.states

import android.os.Parcelable
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.AppLocale
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.AppTheme
import kotlinx.parcelize.Parcelize

@Parcelize
data class SettingsState(
    val appLocale: AppLocale? = null,
    val appTheme: AppTheme? = null,
    val isNotificationSoundEnabled: Boolean? = null,
    val isNotificationVibrateEnabled: Boolean? = null
) : Parcelable