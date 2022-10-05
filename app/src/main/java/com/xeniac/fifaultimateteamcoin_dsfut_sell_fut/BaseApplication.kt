package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut

import android.app.Application
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.SettingsHelper
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class BaseApplication : Application() {

    @set:Inject
    var currentAppThemeIndex = 0

    override fun onCreate() {
        super.onCreate()

        setupTimber()
        setAppTheme()
    }

    private fun setupTimber() = Timber.plant(Timber.DebugTree())

    private fun setAppTheme() = SettingsHelper.setAppTheme(currentAppThemeIndex)
}