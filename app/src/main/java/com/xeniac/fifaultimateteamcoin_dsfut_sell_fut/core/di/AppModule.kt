package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.di

import android.app.NotificationManager
import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import android.os.Vibrator
import android.os.VibratorManager
import androidx.annotation.RequiresApi
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.dataStoreFile
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.local.FutSaleDatabase
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.local.PlayersDao
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.local.migrations.MIGRATION_2_TO_3
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.local.migrations.MIGRATION_3_TO_4
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.AppTheme
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.DsfutPreferences
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.DsfutPreferencesSerializer
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.MiscellaneousPreferences
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.MiscellaneousPreferencesSerializer
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.SettingsPreferences
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.SettingsPreferencesSerializer
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.SettingsDataStoreRepository
import dagger.Lazy
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.cache.storage.FileStorage
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.internal.SynchronizedObject
import kotlinx.serialization.json.Json
import java.security.MessageDigest
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object AppModule {

    @Provides
    @Singleton
    fun provideNotificationManager(
        @ApplicationContext context: Context
    ): NotificationManager = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        context.getSystemService(NotificationManager::class.java)
    } else context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    @RequiresApi(Build.VERSION_CODES.S)
    @Provides
    @Singleton
    fun provideVibratorManager(
        @ApplicationContext context: Context
    ): VibratorManager = context.getSystemService(VibratorManager::class.java)

    @Provides
    @Singleton
    fun provideVibrator(
        @ApplicationContext context: Context,
        vibratorManager: Lazy<VibratorManager>
    ): Vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        vibratorManager.get().defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            context.getSystemService(Vibrator::class.java)
        } else context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }

    @Provides
    @Singleton
    fun provideConnectivityManager(
        @ApplicationContext context: Context
    ): ConnectivityManager = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        context.getSystemService(ConnectivityManager::class.java)
    } else context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        prettyPrint = true
        coerceInputValues = true
    }

    @Provides
    @Singleton
    fun provideHttpClient(
        @ApplicationContext context: Context,
        json: Json
    ): HttpClient = HttpClient(engineFactory = OkHttp) {
        expectSuccess = true

        install(Logging) {
            logger = Logger.ANDROID
            level = LogLevel.INFO
            sanitizeHeader { header -> header == HttpHeaders.Authorization }
        }
        install(HttpCache) {
            val cacheDir = context.cacheDir.resolve(relative = "ktor_cache")
            privateStorage(FileStorage(cacheDir))
        }
        install(ContentNegotiation) {
            json(json)
        }
        install(HttpRequestRetry) {
            retryOnExceptionOrServerErrors(maxRetries = 3)
            exponentialDelay()
        }
        install(HttpTimeout) {
            connectTimeoutMillis = 20_000 // 20 seconds
            requestTimeoutMillis = 20_000 // 20 seconds
            socketTimeoutMillis = 20_000 // 20 seconds
        }
        install(DefaultRequest) {
            contentType(ContentType.Application.Json)
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
    ).apply {
        addMigrations(
            MIGRATION_2_TO_3,
            MIGRATION_3_TO_4
        )
    }.build()

    @Provides
    @Singleton
    fun providePlayersDao(
        database: FutSaleDatabase
    ): PlayersDao = database.playersDao()

    @OptIn(InternalCoroutinesApi::class)
    @Provides
    @Singleton
    @SettingsDataStoreQualifier
    fun provideSettingsDataStore(
        @ApplicationContext context: Context
    ): DataStore<SettingsPreferences> = synchronized(lock = SynchronizedObject()) {
        DataStoreFactory.create(
            serializer = SettingsPreferencesSerializer,
            corruptionHandler = ReplaceFileCorruptionHandler { SettingsPreferences() },
            scope = CoroutineScope(context = Dispatchers.IO + SupervisorJob()),
            produceFile = { context.preferencesDataStoreFile(name = "Settings.pb") }
        )
    }

    @OptIn(InternalCoroutinesApi::class)
    @Provides
    @Singleton
    @DsfutDataStoreQualifier
    fun provideDsfutDataStore(
        @ApplicationContext context: Context
    ): DataStore<DsfutPreferences> = synchronized(lock = SynchronizedObject()) {
        DataStoreFactory.create(
            serializer = DsfutPreferencesSerializer,
            corruptionHandler = ReplaceFileCorruptionHandler { DsfutPreferences() },
            scope = CoroutineScope(context = Dispatchers.IO + SupervisorJob()),
            produceFile = { context.dataStoreFile(fileName = "Dsfut.pb") }
        )
    }

    @OptIn(InternalCoroutinesApi::class)
    @Provides
    @Singleton
    @MiscellaneousDataStoreQualifier
    fun provideMiscellaneousDataStore(
        @ApplicationContext context: Context
    ): DataStore<MiscellaneousPreferences> = synchronized(lock = SynchronizedObject()) {
        DataStoreFactory.create(
            serializer = MiscellaneousPreferencesSerializer,
            corruptionHandler = ReplaceFileCorruptionHandler { MiscellaneousPreferences() },
            scope = CoroutineScope(context = Dispatchers.IO + SupervisorJob()),
            produceFile = { context.preferencesDataStoreFile(name = "Miscellaneous.pb") }
        )
    }

    @Provides
    fun provideAppTheme(
        settingsDataStoreRepository: SettingsDataStoreRepository
    ): AppTheme = settingsDataStoreRepository.getCurrentAppThemeSynchronously()

    @Provides
    @Singleton
    fun provideDecimalFormat(): DecimalFormat = DecimalFormat(
        /* pattern = */ "00",
        /* symbols = */ DecimalFormatSymbols(Locale.US)
    )

    @Provides
    @Singleton
    fun provideMD5MessageDigest(): MessageDigest = MessageDigest.getInstance(
        /* algorithm = */ "MD5"
    )
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class SettingsDataStoreQualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DsfutDataStoreQualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MiscellaneousDataStoreQualifier