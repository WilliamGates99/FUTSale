package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.data.repositories

import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.review.ReviewInfo
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.BuildConfig
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.PreferencesRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.utils.Result
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.data.remote.dto.GetLatestAppVersionResponseDto
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.models.LatestAppUpdateInfo
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.repositories.HomeRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.repositories.IsUpdateDownloaded
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.utils.GetLatestAppVersionError
import dagger.Lazy
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

class FakeHomeRepositoryImpl @Inject constructor(
    private val preferencesRepository: Lazy<PreferencesRepository>
) : HomeRepository {

    private var isFlexibleUpdateDownloaded = false
    private var isFlexibleUpdateStalled = false
    private var isImmediateUpdateStalled = false
    private var isAppUpdateAvailable = false
    private var isInAppReviewsAvailable = false

    private var isNetworkAvailable = true
    private var latestAppVersionCode = BuildConfig.VERSION_CODE
    private var latestAppVersionName = BuildConfig.VERSION_NAME
    private var getLatestAppVersionHttpStatusCode = HttpStatusCode.OK

    fun isFlexibleUpdateDownloaded(isDownloaded: Boolean) {
        isFlexibleUpdateDownloaded = isDownloaded
    }

    fun isFlexibleUpdateStalled(isStalled: Boolean) {
        isFlexibleUpdateStalled = isStalled
    }

    fun isImmediateUpdateStalled(isStalled: Boolean) {
        isImmediateUpdateStalled = isStalled
    }

    fun isAppUpdateAvailable(isAvailable: Boolean) {
        isAppUpdateAvailable = isAvailable
    }

    fun isInAppReviewsAvailable(isAvailable: Boolean) {
        isInAppReviewsAvailable = isAvailable
    }

    fun isNetworkAvailable(isAvailable: Boolean) {
        isNetworkAvailable = isAvailable
    }

    fun setGetLatestAppVersionHttpStatusCode(httpStatusCode: HttpStatusCode) {
        getLatestAppVersionHttpStatusCode = httpStatusCode
    }

    fun setLatestAppVersion(
        versionCode: Int,
        versionName: String
    ) {
        latestAppVersionCode = versionCode
        latestAppVersionName = versionName
    }

    override fun checkFlexibleUpdateDownloadState(): Flow<IsUpdateDownloaded> = flow {
        if (isFlexibleUpdateDownloaded) {
            emit(true)
        }
    }

    override fun checkIsFlexibleUpdateStalled(): Flow<IsUpdateDownloaded> = flow {
        emit(isFlexibleUpdateStalled)
    }

    override fun checkIsImmediateUpdateStalled(): Flow<AppUpdateInfo?> = flow {
        if (isImmediateUpdateStalled) {
            val appUpdateInfo: AppUpdateInfo = AppUpdateInfo.zzb(
                "", 1, 1, 1, null, 1, 1L, 1L, 1L, 1L, null, null, null, null, mapOf(Pair("", ""))
            )
            emit(appUpdateInfo)
        } else emit(null)
    }

    override fun checkForAppUpdates(): Flow<AppUpdateInfo?> = flow {
        if (isAppUpdateAvailable) {
            val appUpdateInfo: AppUpdateInfo = AppUpdateInfo.zzb(
                "", 1, 1, 1, null, 1, 1L, 1L, 1L, 1L, null, null, null, null, mapOf(Pair("", ""))
            )
            emit(appUpdateInfo)
        } else emit(null)
    }

    override fun requestInAppReviews(): Flow<ReviewInfo?> = flow {
        if (isInAppReviewsAvailable) {
            emit(null)
        } else emit(null)
    }

    override fun getLatestAppVersion(
    ): Flow<Result<LatestAppUpdateInfo?, GetLatestAppVersionError>> = flow {
        if (!isNetworkAvailable) {
            return@flow emit(Result.Error(GetLatestAppVersionError.Network.Offline))
        }

        val mockEngine = MockEngine {
            val getLatestAppVersionResponseDto = GetLatestAppVersionResponseDto(
                versionCode = latestAppVersionCode,
                versionName = latestAppVersionName
            )

            respond(
                content = Json.encodeToString(getLatestAppVersionResponseDto),
                status = getLatestAppVersionHttpStatusCode,
                headers = headersOf(
                    name = HttpHeaders.ContentType,
                    value = ContentType.Application.Json.toString()
                )
            )
        }

        val testClient = HttpClient(engine = mockEngine) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                    coerceInputValues = true
                })
            }
            install(DefaultRequest) {
                contentType(ContentType.Application.Json)
            }
        }

        val response = testClient.get(urlString = HomeRepository.EndPoints.GetLatestAppVersion.url)

        when (response.status) {
            HttpStatusCode.OK -> {
                val getLatestAppVersionResponse = Json
                    .decodeFromString<GetLatestAppVersionResponseDto>(response.bodyAsText())
                    .toGetLatestAppVersionResponse()

                val currentVersionCode = BuildConfig.VERSION_CODE
                val latestVersionCode = getLatestAppVersionResponse.versionCode

                val isAppOutdated = currentVersionCode < latestVersionCode
                if (isAppOutdated) {
                    val updateDialogShowCount = preferencesRepository.get()
                        .getAppUpdateDialogShowCount().first()
                    val isAppUpdateDialogShownToday = preferencesRepository.get()
                        .isAppUpdateDialogShownToday().first()

                    val shouldShowAppUpdateDialog = when {
                        isAppUpdateDialogShownToday && updateDialogShowCount < 2 -> true
                        !isAppUpdateDialogShownToday -> true
                        else -> false
                    }

                    if (shouldShowAppUpdateDialog) {
                        preferencesRepository.get().apply {
                            storeAppUpdateDialogShowCount(
                                if (isAppUpdateDialogShownToday) updateDialogShowCount + 1
                                else 0
                            )
                            storeAppUpdateDialogShowEpochDays()
                        }

                        emit(
                            Result.Success(
                                LatestAppUpdateInfo(
                                    versionCode = latestAppVersionCode,
                                    versionName = getLatestAppVersionResponse.versionName
                                )
                            )
                        )
                    } else emit(Result.Success(null))
                } else {
                    preferencesRepository.get().apply {
                        storeAppUpdateDialogShowCount(0)
                        removeAppUpdateDialogShowEpochDays()
                    }

                    emit(Result.Success(null))
                }
            }
            else -> emit(Result.Error(GetLatestAppVersionError.Network.SomethingWentWrong))
        }
    }
}