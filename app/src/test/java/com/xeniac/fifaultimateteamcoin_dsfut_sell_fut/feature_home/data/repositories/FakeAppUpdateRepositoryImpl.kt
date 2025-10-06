package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.data.repositories

import com.google.android.play.core.appupdate.AppUpdateInfo
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.BuildConfig
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.utils.createKtorTestClient
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.Result
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.MiscellaneousDataStoreRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.data.remote.GetLatestAppVersionResponseDto
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.errors.GetLatestAppVersionError
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.models.LatestAppUpdateInfo
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.repositories.AppUpdateRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.repositories.IsUpdateDownloaded
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json

class FakeAppUpdateRepositoryImpl(
    private val miscellaneousDataStoreRepository: MiscellaneousDataStoreRepository
) : AppUpdateRepository {

    private var isFlexibleUpdateDownloaded = false
    private var isFlexibleUpdateStalled = false
    private var isImmediateUpdateStalled = false
    private var isAppUpdateAvailable = false

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

    fun isNetworkAvailable(isAvailable: Boolean) {
        isNetworkAvailable = isAvailable
    }

    fun setGetLatestAppVersionHttpStatusCode(httpStatusCode: HttpStatusCode) {
        getLatestAppVersionHttpStatusCode = httpStatusCode
    }

    fun setLatestAppVersion(latestAppUpdateInfo: LatestAppUpdateInfo) {
        latestAppVersionCode = latestAppUpdateInfo.versionCode
        latestAppVersionName = latestAppUpdateInfo.versionName
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


    override suspend fun getLatestAppVersion(): Result<LatestAppUpdateInfo?, GetLatestAppVersionError> {
        if (!isNetworkAvailable) {
            return Result.Error(GetLatestAppVersionError.Network.Offline)
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

        val httpResponse = createKtorTestClient(mockEngine = mockEngine).get(
            urlString = AppUpdateRepository.EndPoints.GetLatestAppVersion.url
        )

        return when (httpResponse.status) {
            HttpStatusCode.OK -> { // Code: 200
                val responseDto = Json.decodeFromString<GetLatestAppVersionResponseDto>(
                    httpResponse.bodyAsText()
                )

                val currentVersionCode = BuildConfig.VERSION_CODE
                val latestVersionCode = responseDto.versionCode

                val isAppUpdated = currentVersionCode >= latestVersionCode

                if (isAppUpdated) {
                    with(miscellaneousDataStoreRepository) {
                        storeAppUpdateDialogShowCount(count = 0)
                        removeAppUpdateDialogShowDateTime()
                    }

                    return Result.Success(null)
                }

                with(miscellaneousDataStoreRepository) {
                    val updateDialogShowCount = getAppUpdateDialogShowCount().first()
                    val isAppUpdateDialogShownToday = isAppUpdateDialogShownToday().first()

                    val shouldShowAppUpdateDialog = when {
                        isAppUpdateDialogShownToday && updateDialogShowCount < 2 -> true
                        !isAppUpdateDialogShownToday -> true
                        else -> false
                    }

                    if (shouldShowAppUpdateDialog) {
                        storeAppUpdateDialogShowCount(
                            count = when {
                                isAppUpdateDialogShownToday -> updateDialogShowCount + 1
                                else -> 0
                            }
                        )
                        storeAppUpdateDialogShowDateTime()

                        return@with com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.Result.Success(
                            LatestAppUpdateInfo(
                                versionCode = latestVersionCode,
                                versionName = responseDto.versionName
                            )
                        )
                    }

                    com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.Result.Success(
                        null
                    )
                }
            }
            else -> Result.Error(GetLatestAppVersionError.Network.SomethingWentWrong)
        }
    }
}