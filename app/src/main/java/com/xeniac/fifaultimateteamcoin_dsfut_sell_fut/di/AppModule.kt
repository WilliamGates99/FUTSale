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
import androidx.room.Room
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.BuildConfig
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.data.local.DsfutDatabase
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.data.remote.DsfutApi
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.domain.repository.PreferencesRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.services.PickUpPlayerNotificationService
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.Constants.DATASTORE_NAME_SETTINGS
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.Constants.DSFUT_DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.DecimalFormat
import java.util.*
import javax.inject.Named
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideSettingsDataStore(@ApplicationContext context: Context): DataStore<Preferences> =
        PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler { emptyPreferences() },
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
            produceFile = { context.preferencesDataStoreFile(DATASTORE_NAME_SETTINGS) }
        )

    @Provides
    fun provideAppThemeIndex(preferencesRepository: PreferencesRepository): Int =
        preferencesRepository.getCurrentAppThemeSynchronously()

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

    @Singleton
    @Provides
    fun provideDsfutDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(context, DsfutDatabase::class.java, DSFUT_DATABASE_NAME).build()

    @Singleton
    @Provides
    fun provideDsfutDao(
        database: DsfutDatabase
    ) = database.dsfutDao()

    @Singleton
    @Provides
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

    @Singleton
    @Provides
    fun provideDsfutApi(retrofitInstance: Retrofit): DsfutApi =
        retrofitInstance.create(DsfutApi::class.java)

    @Singleton
    @Provides
    fun provideDecimalFormat() = DecimalFormat("00")

    @Singleton
    @Provides
    fun provideNotificationManager(
        @ApplicationContext context: Context
    ) = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    @Singleton
    @Provides
    fun provideCancelNotificationPendingIntent(
        @ApplicationContext context: Context
    ): PendingIntent = PendingIntent.getActivity(
        context,
        0,
        Intent(""),
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
    )

    @Singleton
    @Provides
    fun provideBasePickUpNotificationBuilder(
        @ApplicationContext context: Context,
        cancelNotificationPendingIntent: PendingIntent
    ) = NotificationCompat.Builder(
        context, PickUpPlayerNotificationService.PICK_UP_NOTIFICATION_CHANNEL_ID
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
}