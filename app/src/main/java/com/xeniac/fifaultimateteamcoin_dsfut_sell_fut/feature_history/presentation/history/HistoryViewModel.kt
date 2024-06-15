package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_history.presentation.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_history.domain.use_cases.ObservePickedPlayersHistoryUseCase
import dagger.Lazy
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val observePickedPlayersHistoryUseCase: Lazy<ObservePickedPlayersHistoryUseCase>
) : ViewModel() {

    val pickedPlayersHistory = observePickedPlayersHistoryUseCase.get()().stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = emptyList()
    )
}