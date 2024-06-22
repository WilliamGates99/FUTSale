package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.di

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.PreferencesRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.use_case.GetNotificationPermissionCountUseCase
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.use_case.GetPreviousRateAppRequestTimeInMsUseCase
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.use_case.GetSelectedRateAppOptionUseCase
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.use_case.HomeUseCases
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.use_case.SetNotificationPermissionCountUseCase
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.use_case.SetPreviousRateAppRequestTimeInMsUseCase
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.use_case.SetSelectedRateAppOptionUseCase
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
    fun provideFirstInstallTimeInMs(
        @ApplicationContext context: Context
    ): FirstInstallTimeInMs = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        context.packageManager.getPackageInfo(
            /* packageName = */ context.packageName,
            /* flags = */ PackageManager.PackageInfoFlags.of(0)
        ).firstInstallTime
    } else {
        context.packageManager.getPackageInfo(
            /* p0 = */ context.packageName,
            /* p1 = */ 0
        ).firstInstallTime
    }

    @Provides
    @ViewModelScoped
    fun provideReviewManager(
        @ApplicationContext context: Context
    ): ReviewManager = ReviewManagerFactory.create(context)

    @Provides
    @ViewModelScoped
    fun provideGetNotificationPermissionCountUseCase(
        preferencesRepository: PreferencesRepository
    ): GetNotificationPermissionCountUseCase =
        GetNotificationPermissionCountUseCase(preferencesRepository)

    @Provides
    @ViewModelScoped
    fun provideSetNotificationPermissionCountUseCase(
        preferencesRepository: PreferencesRepository
    ): SetNotificationPermissionCountUseCase =
        SetNotificationPermissionCountUseCase(preferencesRepository)

    @Provides
    @ViewModelScoped
    fun provideGetSelectedRateAppOptionUseCase(
        preferencesRepository: PreferencesRepository
    ): GetSelectedRateAppOptionUseCase = GetSelectedRateAppOptionUseCase(preferencesRepository)

    @Provides
    @ViewModelScoped
    fun provideSetSelectedRateAppOptionUseCase(
        preferencesRepository: PreferencesRepository
    ): SetSelectedRateAppOptionUseCase = SetSelectedRateAppOptionUseCase(preferencesRepository)

    @Provides
    @ViewModelScoped
    fun provideGetPreviousRateAppRequestTimeInMsUseCase(
        preferencesRepository: PreferencesRepository
    ): GetPreviousRateAppRequestTimeInMsUseCase = GetPreviousRateAppRequestTimeInMsUseCase(
        preferencesRepository
    )

    @Provides
    @ViewModelScoped
    fun provideSetPreviousRateAppRequestTimeInMsUseCase(
        preferencesRepository: PreferencesRepository
    ): SetPreviousRateAppRequestTimeInMsUseCase = SetPreviousRateAppRequestTimeInMsUseCase(
        preferencesRepository
    )

    @Provides
    @ViewModelScoped
    fun provideHomeUseCases(
        getNotificationPermissionCountUseCase: GetNotificationPermissionCountUseCase,
        setNotificationPermissionCountUseCase: SetNotificationPermissionCountUseCase,
        getSelectedRateAppOptionUseCase: GetSelectedRateAppOptionUseCase,
        setSelectedRateAppOptionUseCase: SetSelectedRateAppOptionUseCase,
        getPreviousRateAppRequestTimeInMsUseCase: GetPreviousRateAppRequestTimeInMsUseCase,
        setPreviousRateAppRequestTimeInMsUseCase: SetPreviousRateAppRequestTimeInMsUseCase
    ): HomeUseCases = HomeUseCases(
        { getNotificationPermissionCountUseCase },
        { setNotificationPermissionCountUseCase },
        { getSelectedRateAppOptionUseCase },
        { setSelectedRateAppOptionUseCase },
        { getPreviousRateAppRequestTimeInMsUseCase },
        { setPreviousRateAppRequestTimeInMsUseCase }
    )
}