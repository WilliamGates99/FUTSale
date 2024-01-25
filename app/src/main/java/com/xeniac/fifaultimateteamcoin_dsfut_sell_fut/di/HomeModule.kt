package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.di

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.local.PreferencesRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.use_case.GetNotificationPermissionCountUseCase
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.use_case.HomeUseCases
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.use_case.SetNotificationPermissionCountUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
internal object HomeModule {

    @Provides
    @ViewModelScoped
    fun provideGetNotificationPermissionCountUseCase(
        preferencesRepository: PreferencesRepository
    ): GetNotificationPermissionCountUseCase = GetNotificationPermissionCountUseCase(
        preferencesRepository
    )

    @Provides
    @ViewModelScoped
    fun provideSetNotificationPermissionCountUseCase(
        preferencesRepository: PreferencesRepository
    ): SetNotificationPermissionCountUseCase = SetNotificationPermissionCountUseCase(
        preferencesRepository
    )

    @Provides
    @ViewModelScoped
    fun provideHomeUseCases(
        getNotificationPermissionCountUseCase: GetNotificationPermissionCountUseCase,
        setNotificationPermissionCountUseCase: SetNotificationPermissionCountUseCase
    ): HomeUseCases = HomeUseCases(
        { getNotificationPermissionCountUseCase },
        { setNotificationPermissionCountUseCase }
    )
}