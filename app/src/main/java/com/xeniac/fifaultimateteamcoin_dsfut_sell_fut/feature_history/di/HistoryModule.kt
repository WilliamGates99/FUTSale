package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_history.di

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.local.PlayersDao
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.local.entities.PlayerEntity
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_history.domain.repositories.HistoryRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_history.domain.use_cases.ObservePickedPlayersHistoryUseCase
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_history.domain.use_cases.ObservePlayerUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object HistoryModule {

    @Provides
    @ViewModelScoped
    fun providePickedPlayersHistoryPager(
        playerDao: PlayersDao
    ): Pager<Int, PlayerEntity> = Pager(
        config = PagingConfig(pageSize = 20),
        pagingSourceFactory = { playerDao.pagingSource() }
    )

    @Provides
    @ViewModelScoped
    fun provideObservePickedPlayersHistoryUseCase(
        historyRepository: HistoryRepository
    ): ObservePickedPlayersHistoryUseCase = ObservePickedPlayersHistoryUseCase(historyRepository)

    @Provides
    @ViewModelScoped
    fun provideObservePlayerUseCase(
        historyRepository: HistoryRepository
    ): ObservePlayerUseCase = ObservePlayerUseCase(historyRepository)
}