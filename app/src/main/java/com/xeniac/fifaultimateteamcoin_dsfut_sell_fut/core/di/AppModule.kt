package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.di

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.db.FutSaleDatabase
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.AppTheme
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.PreferencesRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.services.PickUpPlayerNotificationService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.serialization.json.Json
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object AppModule {

    @Provides
    @Singleton
    fun provideConnectivityManager(
        @ApplicationContext context: Context
    ): ConnectivityManager = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        context.getSystemService(ConnectivityManager::class.java)
    } else context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    @Provides
    @Singleton
    fun provideNotificationManager(
        @ApplicationContext context: Context
    ): NotificationManager = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        context.getSystemService(NotificationManager::class.java)
    } else context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient = HttpClient(engineFactory = OkHttp) {
        expectSuccess = true

        install(Logging) {
            level = LogLevel.INFO
            sanitizeHeader { header -> header == HttpHeaders.Authorization }
        }
        install(HttpCache)
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
                coerceInputValues = true
            })
        }
        install(HttpRequestRetry) {
            retryOnExceptionOrServerErrors(maxRetries = 3)
            exponentialDelay()
        }
        install(HttpTimeout) {
            connectTimeoutMillis = 20000 // 20 seconds
            requestTimeoutMillis = 20000 // 20 seconds
            socketTimeoutMillis = 20000 // 20 seconds
        }
    }

    @Provides
    @Singleton
    fun provideFUTSaleDatabase(
        @ApplicationContext context: Context
    ): FutSaleDatabase = Room.databaseBuilder(
        context = context,
        klass = FutSaleDatabase::class.java,
        name = "FUTSale.db"
    ).build()

    @Provides
    @Singleton
    fun providePlayersDao(
        database: FutSaleDatabase
    ) = database.playersDao()

    @Provides
    @Singleton
    fun provideSettingsDataStore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> = PreferenceDataStoreFactory.create(
        corruptionHandler = ReplaceFileCorruptionHandler { emptyPreferences() },
        scope = CoroutineScope(context = Dispatchers.IO + SupervisorJob()),
        produceFile = { context.preferencesDataStoreFile(name = "settings") }
    )

    @Provides
    fun provideAppThemeIndex(
        preferencesRepository: PreferencesRepository
    ): AppTheme = preferencesRepository.getCurrentAppThemeSynchronously()

    @Provides
    @Named("notification_sound")
    fun provideIsNotificationSoundActive(
        preferencesRepository: PreferencesRepository
    ): Boolean = preferencesRepository.isNotificationSoundActiveSynchronously()

    @Provides
    @Named("notification_vibrate")
    fun provideIsNotificationVibrateActive(
        preferencesRepository: PreferencesRepository
    ): Boolean = preferencesRepository.isNotificationVibrateActiveSynchronously()

    @Provides
    @Singleton
    fun provideDecimalFormat(): DecimalFormat = DecimalFormat(
        /* pattern = */ "00",
        /* symbols = */ DecimalFormatSymbols(Locale.US)
    )

    @Provides
    @Singleton
    fun provideCancelNotificationPendingIntent(
        @ApplicationContext context: Context
    ): PendingIntent = PendingIntent.getActivity(
        /* context = */ context,
        /* requestCode = */ 0,
        /* intent = */ Intent(""),
        /* flags = */ if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            PendingIntent.FLAG_IMMUTABLE else 0
    )

    @Provides
    @Singleton
    @Named("muted_pick_up_notification")
    fun provideMutedBasePickUpNotificationBuilder(
        @ApplicationContext context: Context,
        cancelNotificationPendingIntent: PendingIntent
    ) = NotificationCompat.Builder(
        context, PickUpPlayerNotificationService.MUTED_PICK_UP_NOTIFICATION_CHANNEL_ID
    ).apply {
        setAutoCancel(true)
        setSmallIcon(R.drawable.ic_notification)
        setContentIntent(cancelNotificationPendingIntent)

        /**
         * On Android 8.0 and above these values are ignored
         * in favor of the values set on the notification's channel.
         * On older platforms, these values are still used,
         * so it is still required for apps supporting those platforms.
         */
        setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
        setLights(ContextCompat.getColor(context, R.color.green), 1000, 1000)
        setSound(null)
        setVibrate(null)
    }

    @Provides
    @Singleton
    @Named("sounded_pick_up_notification")
    fun provideSoundedBasePickUpNotificationBuilder(
        @ApplicationContext context: Context,
        cancelNotificationPendingIntent: PendingIntent
    ) = NotificationCompat.Builder(
        context, PickUpPlayerNotificationService.SOUNDED_PICK_UP_NOTIFICATION_CHANNEL_ID
    ).apply {
        setAutoCancel(true)
        setSmallIcon(R.drawable.ic_notification)
        setContentIntent(cancelNotificationPendingIntent)

        /**
         * On Android 8.0 and above these values are ignored
         * in favor of the values set on the notification's channel.
         * On older platforms, these values are still used,
         * so it is still required for apps supporting those platforms.
         */
        setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
        setLights(ContextCompat.getColor(context, R.color.green), 1000, 1000)
        setVibrate(null)
    }
}