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
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.MiscellaneousDataStoreRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.PermissionsDataStoreRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.repositories.AppReviewRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.repositories.AppUpdateRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.repositories.UpdateType
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.use_case.CheckFlexibleUpdateDownloadStateUseCase
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.use_case.CheckForAppUpdatesUseCase
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.use_case.CheckIsFlexibleUpdateStalledUseCase
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.use_case.CheckIsImmediateUpdateStalledUseCase
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.use_case.GetLatestAppVersionUseCase
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.use_case.GetNotificationPermissionCountUseCase
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.use_case.GetPreviousRateAppRequestDateTimeUseCase
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.use_case.GetSelectedRateAppOptionUseCase
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.use_case.HomeUseCases
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.use_case.RequestInAppReviewsUseCase
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.use_case.StoreNotificationPermissionCountUseCase
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.use_case.StorePreviousRateAppRequestDateTimeUseCase
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
        appUpdateRepository: AppUpdateRepository
    ): CheckFlexibleUpdateDownloadStateUseCase = CheckFlexibleUpdateDownloadStateUseCase(
        appUpdateRepository
    )

    @Provides
    @ViewModelScoped
    fun provideCheckIsFlexibleUpdateStalledUseCase(
        appUpdateRepository: AppUpdateRepository
    ): CheckIsFlexibleUpdateStalledUseCase = CheckIsFlexibleUpdateStalledUseCase(
        appUpdateRepository
    )

    @Provides
    @ViewModelScoped
    fun provideCheckIsImmediateUpdateStalledUseCase(
        appUpdateRepository: AppUpdateRepository
    ): CheckIsImmediateUpdateStalledUseCase = CheckIsImmediateUpdateStalledUseCase(
        appUpdateRepository
    )

    @Provides
    @ViewModelScoped
    fun provideCheckForAppUpdatesUseCase(
        appUpdateRepository: AppUpdateRepository
    ): CheckForAppUpdatesUseCase = CheckForAppUpdatesUseCase(appUpdateRepository)

    @Provides
    @ViewModelScoped
    fun provideRequestInAppReviewsUseCase(
        appReviewRepository: AppReviewRepository
    ): RequestInAppReviewsUseCase = RequestInAppReviewsUseCase(appReviewRepository)

    @Provides
    @ViewModelScoped
    fun provideGetLatestAppVersionUseCase(
        appUpdateRepository: AppUpdateRepository
    ): GetLatestAppVersionUseCase = GetLatestAppVersionUseCase(appUpdateRepository)

    @Provides
    @ViewModelScoped
    fun provideGetNotificationPermissionCountUseCase(
        permissionsDataStoreRepository: PermissionsDataStoreRepository
    ): GetNotificationPermissionCountUseCase = GetNotificationPermissionCountUseCase(
        permissionsDataStoreRepository
    )

    @Provides
    @ViewModelScoped
    fun provideStoreNotificationPermissionCountUseCase(
        permissionsDataStoreRepository: PermissionsDataStoreRepository
    ): StoreNotificationPermissionCountUseCase = StoreNotificationPermissionCountUseCase(
        permissionsDataStoreRepository
    )

    @Provides
    @ViewModelScoped
    fun provideGetSelectedRateAppOptionUseCase(
        miscellaneousDataStoreRepository: MiscellaneousDataStoreRepository
    ): GetSelectedRateAppOptionUseCase = GetSelectedRateAppOptionUseCase(
        miscellaneousDataStoreRepository
    )

    @Provides
    @ViewModelScoped
    fun provideStoreSelectedRateAppOptionUseCase(
        miscellaneousDataStoreRepository: MiscellaneousDataStoreRepository
    ): StoreSelectedRateAppOptionUseCase = StoreSelectedRateAppOptionUseCase(
        miscellaneousDataStoreRepository
    )

    @Provides
    @ViewModelScoped
    fun provideGetPreviousRateAppRequestDateTimeUseCase(
        miscellaneousDataStoreRepository: MiscellaneousDataStoreRepository
    ): GetPreviousRateAppRequestDateTimeUseCase = GetPreviousRateAppRequestDateTimeUseCase(
        miscellaneousDataStoreRepository
    )

    @Provides
    @ViewModelScoped
    fun provideStorePreviousRateAppRequestDateTimeUseCase(
        miscellaneousDataStoreRepository: MiscellaneousDataStoreRepository
    ): StorePreviousRateAppRequestDateTimeUseCase = StorePreviousRateAppRequestDateTimeUseCase(
        miscellaneousDataStoreRepository
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
        getPreviousRateAppRequestDateTimeUseCase: GetPreviousRateAppRequestDateTimeUseCase,
        storePreviousRateAppRequestDateTimeUseCase: StorePreviousRateAppRequestDateTimeUseCase
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
        { getPreviousRateAppRequestDateTimeUseCase },
        { storePreviousRateAppRequestDateTimeUseCase }
    )
}