package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut

import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationChannelGroup
import android.app.NotificationManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.toArgb
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.decode.SvgDecoder
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.request.CachePolicy
import coil.util.DebugLogger
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.AppTheme
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.theme.GreenNotificationLight
import dagger.hilt.android.HiltAndroidApp
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class BaseApplication : Application(), ImageLoaderFactory {

    companion object {
        const val NOTIFICATION_CHANNEL_GROUP_ID_FCM = "group_fcm"
        const val NOTIFICATION_CHANNEL_ID_FCM_MISCELLANEOUS = "channel_fcm_miscellaneous"

        const val NOTIFICATION_CHANNEL_GROUP_ID_PICK_UP_PLAYER = "group_pick_up_player"
        const val NOTIFICATION_CHANNEL_ID_PICK_UP_PLAYER_DEFAULT = "channel_pick_up_player_default"
        const val NOTIFICATION_CHANNEL_ID_PICK_UP_PLAYER_SILENT = "channel_pick_up_player_silent"
    }

    @Inject
    lateinit var currentAppTheme: AppTheme

    @Inject
    lateinit var notificationManager: NotificationManager

    override fun onCreate() {
        super.onCreate()

        setupTimber()
        setAppTheme()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createFcmNotificationChannelGroup()
            createMiscellaneousFcmNotificationChannel()

            createPickUpPlayerNotificationChannelGroup()
            createDefaultPickUpPlayerNotificationChannel()
            createSilentPickUpPlayerNotificationChannel()
        }
    }

    private fun setupTimber() = Timber.plant(Timber.DebugTree())

    private fun setAppTheme() = currentAppTheme.setAppTheme()

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createFcmNotificationChannelGroup() {
        val notificationChannelGroup = NotificationChannelGroup(
            /* id = */ NOTIFICATION_CHANNEL_GROUP_ID_FCM,
            /* name = */ getString(R.string.notification_fcm_channel_group_name)
        )

        notificationManager.createNotificationChannelGroup(notificationChannelGroup)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createMiscellaneousFcmNotificationChannel() {
        val miscellaneousNotificationChannel = NotificationChannel(
            /* id = */ NOTIFICATION_CHANNEL_ID_FCM_MISCELLANEOUS,
            /* name = */ getString(R.string.notification_fcm_channel_name_miscellaneous),
            /* importance = */ NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            group = NOTIFICATION_CHANNEL_GROUP_ID_FCM
            lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            lightColor = GreenNotificationLight.toArgb()
            enableLights(true)
        }

        notificationManager.createNotificationChannel(miscellaneousNotificationChannel)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createPickUpPlayerNotificationChannelGroup() {
        val notificationChannelGroup = NotificationChannelGroup(
            /* id = */ NOTIFICATION_CHANNEL_GROUP_ID_PICK_UP_PLAYER,
            /* name = */ getString(R.string.notification_pick_up_player_channel_group_name)
        )

        notificationManager.createNotificationChannelGroup(notificationChannelGroup)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createDefaultPickUpPlayerNotificationChannel() {
        val pickUpPlayerNotificationChannel = NotificationChannel(
            /* id = */ NOTIFICATION_CHANNEL_ID_PICK_UP_PLAYER_DEFAULT,
            /* name = */ getString(R.string.notification_pick_up_player_channel_name_default),
            /* importance = */ NotificationManager.IMPORTANCE_HIGH
        ).apply {
            group = NOTIFICATION_CHANNEL_GROUP_ID_PICK_UP_PLAYER
            description = getString(
                R.string.notification_pick_up_player_channel_description_pick_up_player
            )
            lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            lightColor = GreenNotificationLight.toArgb()
            enableLights(true)
            enableVibration(false)
        }

        notificationManager.createNotificationChannel(pickUpPlayerNotificationChannel)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createSilentPickUpPlayerNotificationChannel() {
        val pickUpPlayerNotificationChannel = NotificationChannel(
            /* id = */ NOTIFICATION_CHANNEL_ID_PICK_UP_PLAYER_SILENT,
            /* name = */ getString(R.string.notification_pick_up_player_channel_name_silent),
            /* importance = */ NotificationManager.IMPORTANCE_HIGH
        ).apply {
            group = NOTIFICATION_CHANNEL_GROUP_ID_PICK_UP_PLAYER
            description = getString(
                R.string.notification_pick_up_player_channel_description_pick_up_player
            )
            lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            lightColor = GreenNotificationLight.toArgb()
            enableLights(true)
            enableVibration(false)
            setSound(
                /* sound = */ null,
                /* audioAttributes = */ null
            )
        }

        notificationManager.createNotificationChannel(pickUpPlayerNotificationChannel)
    }

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