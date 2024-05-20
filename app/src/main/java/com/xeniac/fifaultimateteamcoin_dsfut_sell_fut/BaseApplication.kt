package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut

import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.core.content.ContextCompat
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.decode.SvgDecoder
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.request.CachePolicy
import coil.util.DebugLogger
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.model.AppTheme
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.services.PickUpPlayerNotificationService
import dagger.hilt.android.HiltAndroidApp
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class BaseApplication : Application(), ImageLoaderFactory {

    @Inject
    lateinit var currentAppTheme: AppTheme

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

    private fun setAppTheme() = currentAppTheme.setAppTheme()

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

    override fun newImageLoader(): ImageLoader = ImageLoader(context = this).newBuilder().apply {
        components {
            add(factory = SvgDecoder.Factory())
            add(
                factory = if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)) {
                    ImageDecoderDecoder.Factory()
                } else {
                    GifDecoder.Factory()
                }
            )
        }
        crossfade(enable = true)
        // Ignore the network cache headers and always read from/write to the disk cache.
        respectCacheHeaders(enable = false)
        memoryCachePolicy(policy = CachePolicy.ENABLED)
        diskCachePolicy(policy = CachePolicy.ENABLED)
        memoryCache {
            MemoryCache.Builder(this@BaseApplication)
                // Set the max size to 25% of the app's available memory.
                .maxSizePercent(percent = 0.25)
                .build()
        }
        diskCache {
            DiskCache.Builder()
                // Set cache directory folder name
                .directory(cacheDir.resolve("image_cache"))
                .maxSizePercent(percent = 0.03)
                .build()
        }
        okHttpClient {
            // Don't limit concurrent network requests by host.
            val dispatcher = Dispatcher().apply { maxRequestsPerHost = maxRequests }

            // Lazily create the OkHttpClient that is used for network operations.
            OkHttpClient.Builder()
                .dispatcher(dispatcher)
                .build()
        }
        if (BuildConfig.DEBUG) logger(logger = DebugLogger())
    }.build()
}