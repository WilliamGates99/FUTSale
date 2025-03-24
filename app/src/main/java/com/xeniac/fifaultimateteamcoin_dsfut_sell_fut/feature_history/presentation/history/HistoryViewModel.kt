package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_history.presentation.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_history.domain.use_cases.ObservePickedPlayersHistoryUseCase
import dagger.Lazy
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    observePickedPlayersHistoryUseCase: Lazy<ObservePickedPlayersHistoryUseCase>
) : ViewModel() {
    val pickedPlayersHistory = observePickedPlayersHistoryUseCase.get()().cachedIn(
        scope = viewModelScope
    )
}