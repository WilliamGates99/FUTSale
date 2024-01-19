package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.di

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.local.PreferencesRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.model.AppTheme
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.services.PickUpPlayerNotificationService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object AppModule {

    @Provides
    @Singleton
    fun provideSettingsDataStore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> = PreferenceDataStoreFactory.create(
        corruptionHandler = ReplaceFileCorruptionHandler { emptyPreferences() },
        scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
        produceFile = { context.preferencesDataStoreFile(name = "settings") }
    )

    @Provides
    fun provideAppThemeIndex(
        preferencesRepository: PreferencesRepository
    ): AppTheme = preferencesRepository.getCurrentAppThemeIndexSynchronously()

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

    /*
    @Provides
    @Singleton
    fun provideDsfutDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(context, DsfutDatabase::class.java, DSFUT_DATABASE_NAME).build()

    @Provides
    @Singleton
    fun provideDsfutDao(
        database: DsfutDatabase
    ) = database.dsfutDao()

    @Provides
    @Singleton
    fun provideRetrofitInstance(): Retrofit {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl(BuildConfig.RETROFIT_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    @Provides
    @Singleton
    fun provideDsfutApi(retrofitInstance: Retrofit): DsfutApi =
        retrofitInstance.create(DsfutApi::class.java)
    */

    @Provides
    @Singleton
    fun provideDecimalFormat(): DecimalFormat = DecimalFormat(
        /* pattern = */ "00",
        /* symbols = */ DecimalFormatSymbols(Locale.US)
    )

    @Provides
    @Singleton
    fun provideNotificationManager(
        @ApplicationContext context: Context
    ) = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    @Provides
    @Singleton
    fun provideCancelNotificationPendingIntent(
        @ApplicationContext context: Context
    ): PendingIntent = PendingIntent.getActivity(
        context,
        0,
        Intent(""),
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
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