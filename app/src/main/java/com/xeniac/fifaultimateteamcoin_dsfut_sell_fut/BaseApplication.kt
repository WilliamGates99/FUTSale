package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut

import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.core.content.ContextCompat
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.services.PickUpPlayerNotificationService
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.SettingsHelper
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class BaseApplication : Application() {

    @set:Inject
    var currentAppThemeIndex = 0

    @Inject
    lateinit var notificationManager: NotificationManager

    override fun onCreate() {
        super.onCreate()

        setupTimber()
        setAppTheme()

        createMutedPickUpPlayerNotificationChannel()
        createSoundedPickUpPlayerNotificationChannel()
        // initAppLovin()
        // initTapsell()
    }

    private fun setupTimber() = Timber.plant(Timber.DebugTree())

    private fun setAppTheme() = SettingsHelper.setAppTheme(currentAppThemeIndex)

    private fun createMutedPickUpPlayerNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val pickUpPlayerNotificationChannel = NotificationChannel(
                PickUpPlayerNotificationService.MUTED_PICK_UP_NOTIFICATION_CHANNEL_ID,
                getString(R.string.notification_channel_name_pick_up_player),
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = getString(R.string.notification_channel_description_pick_up_player)
                lockscreenVisibility = Notification.VISIBILITY_PUBLIC
                lightColor = ContextCompat.getColor(this@BaseApplication, R.color.green)
                enableLights(true)
                enableVibration(false)
                setSound(null, null)
            }

            notificationManager.createNotificationChannel(pickUpPlayerNotificationChannel)
        }
    }

    private fun createSoundedPickUpPlayerNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val pickUpPlayerNotificationChannel = NotificationChannel(
                PickUpPlayerNotificationService.SOUNDED_PICK_UP_NOTIFICATION_CHANNEL_ID,
                getString(R.string.notification_channel_name_pick_up_player),
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = getString(R.string.notification_channel_description_pick_up_player)
                lockscreenVisibility = Notification.VISIBILITY_PUBLIC
                lightColor = ContextCompat.getColor(this@BaseApplication, R.color.green)
                enableLights(true)
                enableVibration(false)
            }

            notificationManager.createNotificationChannel(pickUpPlayerNotificationChannel)
        }
    }

    /*
    private fun initAppLovin() {
        AppLovinSdk.getInstance(this).mediationProvider = "max"
        AppLovinSdk.getInstance(this).initializeSdk {}
        AppLovinPrivacySettings.setHasUserConsent(true, this)
    }

    private fun initTapsell() {
        TapsellPlus.setDebugMode(Log.DEBUG)
        TapsellPlus.initialize(this, BuildConfig.TAPSELL_KEY, object : TapsellPlusInitListener {
            override fun onInitializeSuccess(adNetworks: AdNetworks?) {
                Timber.i("onInitializeSuccess: ${adNetworks?.name}")
            }

            override fun onInitializeFailed(adNetworks: AdNetworks?, error: AdNetworkError?) {
                Timber.e("onInitializeFailed: ${adNetworks?.name}, error: ${error?.errorMessage}")
            }
        })
        TapsellPlus.setGDPRConsent(this, true)
    }
     */
}