package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.di

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.PreferencesRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.repositories.HomeRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.repositories.UpdateType
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.use_case.CheckFlexibleUpdateDownloadStateUseCase
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.use_case.CheckForAppUpdatesUseCase
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.use_case.CheckIsFlexibleUpdateStalledUseCase
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.use_case.CheckIsImmediateUpdateStalledUseCase
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.use_case.GetLatestAppVersionUseCase
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.use_case.GetNotificationPermissionCountUseCase
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.use_case.GetPreviousRateAppRequestTimeInMsUseCase
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.use_case.GetSelectedRateAppOptionUseCase
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.use_case.HomeUseCases
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.use_case.RequestInAppReviewsUseCase
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.use_case.StoreNotificationPermissionCountUseCase
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.use_case.StorePreviousRateAppRequestTimeInMsUseCase
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.use_case.StoreSelectedRateAppOptionUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped

typealias FirstInstallTimeInMs = Long

@Module
@InstallIn(ViewModelComponent::class)
internal object HomeModule {

    @Provides
    @ViewModelScoped
    fun provideAppUpdateType(): UpdateType = AppUpdateType.FLEXIBLE

    @Provides
    @ViewModelScoped
    fun provideAppUpdateManager(
        @ApplicationContext context: Context
    ): AppUpdateManager = AppUpdateManagerFactory.create(context)

    @Provides
    @ViewModelScoped
    fun provide(
        appUpdateType: UpdateType
    ): AppUpdateOptions = AppUpdateOptions.newBuilder(appUpdateType).apply {
        setAllowAssetPackDeletion(true)
    }.build()

    @Provides
    @ViewModelScoped
    fun provideReviewManager(
        @ApplicationContext context: Context
    ): ReviewManager = ReviewManagerFactory.create(context)

    @Provides
    @ViewModelScoped
    fun provideFirstInstallTimeInMs(
        @ApplicationContext context: Context
    ): FirstInstallTimeInMs = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        context.packageManager.getPackageInfo(
            /* packageName = */ context.packageName,
            /* flags = */ PackageManager.PackageInfoFlags.of(0)
        ).firstInstallTime
    } else {
        context.packageManager.getPackageInfo(
            /* packageName = */ context.packageName,
            /* flags = */ 0
        ).firstInstallTime
    }

    @Provides
    @ViewModelScoped
    fun provideCheckFlexibleUpdateDownloadStateUseCase(
        homeRepository: HomeRepository
    ): CheckFlexibleUpdateDownloadStateUseCase =
        CheckFlexibleUpdateDownloadStateUseCase(homeRepository)

    @Provides
    @ViewModelScoped
    fun provideCheckIsFlexibleUpdateStalledUseCase(
        homeRepository: HomeRepository
    ): CheckIsFlexibleUpdateStalledUseCase = CheckIsFlexibleUpdateStalledUseCase(homeRepository)

    @Provides
    @ViewModelScoped
    fun provideCheckIsImmediateUpdateStalledUseCase(
        homeRepository: HomeRepository
    ): CheckIsImmediateUpdateStalledUseCase = CheckIsImmediateUpdateStalledUseCase(homeRepository)

    @Provides
    @ViewModelScoped
    fun provideCheckForAppUpdatesUseCase(
        homeRepository: HomeRepository
    ): CheckForAppUpdatesUseCase = CheckForAppUpdatesUseCase(homeRepository)

    @Provides
    @ViewModelScoped
    fun provideRequestInAppReviewsUseCase(
        homeRepository: HomeRepository
    ): RequestInAppReviewsUseCase = RequestInAppReviewsUseCase(homeRepository)

    @Provides
    @ViewModelScoped
    fun provideGetLatestAppVersionUseCase(
        homeRepository: HomeRepository
    ): GetLatestAppVersionUseCase = GetLatestAppVersionUseCase(homeRepository)

    @Provides
    @ViewModelScoped
    fun provideGetNotificationPermissionCountUseCase(
        preferencesRepository: PreferencesRepository
    ): GetNotificationPermissionCountUseCase =
        GetNotificationPermissionCountUseCase(preferencesRepository)

    @Provides
    @ViewModelScoped
    fun provideStoreNotificationPermissionCountUseCase(
        preferencesRepository: PreferencesRepository
    ): StoreNotificationPermissionCountUseCase =
        StoreNotificationPermissionCountUseCase(preferencesRepository)

    @Provides
    @ViewModelScoped
    fun provideGetSelectedRateAppOptionUseCase(
        preferencesRepository: PreferencesRepository
    ): GetSelectedRateAppOptionUseCase = GetSelectedRateAppOptionUseCase(preferencesRepository)

    @Provides
    @ViewModelScoped
    fun provideStoreSelectedRateAppOptionUseCase(
        preferencesRepository: PreferencesRepository
    ): StoreSelectedRateAppOptionUseCase = StoreSelectedRateAppOptionUseCase(preferencesRepository)

    @Provides
    @ViewModelScoped
    fun provideGetPreviousRateAppRequestTimeInMsUseCase(
        preferencesRepository: PreferencesRepository
    ): GetPreviousRateAppRequestTimeInMsUseCase = GetPreviousRateAppRequestTimeInMsUseCase(
        preferencesRepository
    )

    @Provides
    @ViewModelScoped
    fun provideStorePreviousRateAppRequestTimeInMsUseCase(
        preferencesRepository: PreferencesRepository
    ): StorePreviousRateAppRequestTimeInMsUseCase = StorePreviousRateAppRequestTimeInMsUseCase(
        preferencesRepository
    )

    @Provides
    @ViewModelScoped
    fun provideHomeUseCases(
        checkFlexibleUpdateDownloadStateUseCase: CheckFlexibleUpdateDownloadStateUseCase,
        checkIsFlexibleUpdateStalledUseCase: CheckIsFlexibleUpdateStalledUseCase,
        checkIsImmediateUpdateStalledUseCase: CheckIsImmediateUpdateStalledUseCase,
        checkForAppUpdatesUseCase: CheckForAppUpdatesUseCase,
        requestInAppReviewsUseCase: RequestInAppReviewsUseCase,
        getLatestAppVersionUseCase: GetLatestAppVersionUseCase,
        getNotificationPermissionCountUseCase: GetNotificationPermissionCountUseCase,
        storeNotificationPermissionCountUseCase: StoreNotificationPermissionCountUseCase,
        getSelectedRateAppOptionUseCase: GetSelectedRateAppOptionUseCase,
        storeSelectedRateAppOptionUseCase: StoreSelectedRateAppOptionUseCase,
        getPreviousRateAppRequestTimeInMsUseCase: GetPreviousRateAppRequestTimeInMsUseCase,
        storePreviousRateAppRequestTimeInMsUseCase: StorePreviousRateAppRequestTimeInMsUseCase
    ): HomeUseCases = HomeUseCases(
        { checkFlexibleUpdateDownloadStateUseCase },
        { checkIsFlexibleUpdateStalledUseCase },
        { checkIsImmediateUpdateStalledUseCase },
        { checkForAppUpdatesUseCase },
        { requestInAppReviewsUseCase },
        { getLatestAppVersionUseCase },
        { getNotificationPermissionCountUseCase },
        { storeNotificationPermissionCountUseCase },
        { getSelectedRateAppOptionUseCase },
        { storeSelectedRateAppOptionUseCase },
        { getPreviousRateAppRequestTimeInMsUseCase },
        { storePreviousRateAppRequestTimeInMsUseCase }
    )
}