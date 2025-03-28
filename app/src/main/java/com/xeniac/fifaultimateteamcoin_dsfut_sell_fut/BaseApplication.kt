package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut

import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationChannelGroup
import android.app.NotificationManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.toArgb
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import coil3.disk.DiskCache
import coil3.disk.directory
import coil3.gif.AnimatedImageDecoder
import coil3.gif.GifDecoder
import coil3.memory.MemoryCache
import coil3.request.CachePolicy
import coil3.request.crossfade
import coil3.svg.SvgDecoder
import coil3.util.DebugLogger
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.AppTheme
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.ConnectivityObserver
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.ui.theme.GreenNotificationLight
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.NetworkObserverHelper
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class BaseApplication : Application(), SingletonImageLoader.Factory {

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

    @Inject
    lateinit var connectivityObserver: ConnectivityObserver

    override fun onCreate() {
        super.onCreate()

        setupTimber()
        setAppTheme()
        observeNetworkConnection()

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

    private fun observeNetworkConnection() {
        NetworkObserverHelper.observeNetworkConnection(connectivityObserver)
    }

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

    override fun newImageLoader(
        context: PlatformContext
    ): ImageLoader = ImageLoader.Builder(context).apply {
        components {
            add(factory = SvgDecoder.Factory()) // SVGs
            if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)) { // GIFs
                add(factory = AnimatedImageDecoder.Factory())
            } else {
                add(factory = GifDecoder.Factory())
            }
        }
        crossfade(enable = true)
        networkCachePolicy(policy = CachePolicy.ENABLED)
        memoryCachePolicy(policy = CachePolicy.ENABLED)
        diskCachePolicy(policy = CachePolicy.ENABLED)
        memoryCache {
            MemoryCache.Builder()
                // Set the max size to 25% of the app's available memory.
                .maxSizePercent(
                    context = context,
                    percent = 0.25
                )
                .build()
        }
        diskCache {
            DiskCache.Builder()
                // Set cache directory folder name
                .directory(cacheDir.resolve(relative = "image_cache"))
                .maxSizePercent(percent = 0.03) // Set the max size to 3% of the device's free disk space.
                .build()
        }
        if (BuildConfig.DEBUG) logger(logger = DebugLogger())
    }.build()
}