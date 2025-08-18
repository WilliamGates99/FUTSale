package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_history.presentation.player_info

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_history.domain.use_cases.ObserverPickedUpPlayerUseCase
import dagger.Lazy
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class HistoryPlayerInfoViewModel @Inject constructor(
    observerPickedUpPlayerUseCase: Lazy<ObserverPickedUpPlayerUseCase>,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    val player = observerPickedUpPlayerUseCase.get()(
        playerId = savedStateHandle.toRoute<com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.common.ui.navigation.HistoryPlayerInfoScreen>().playerId
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeout = 5.seconds),
        initialValue = null
    )
}