package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_history.presentation.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_history.domain.use_cases.ObservePickedUpPlayersHistoryUseCase
import dagger.Lazy
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    observePickedUpPlayersHistoryUseCase: Lazy<ObservePickedUpPlayersHistoryUseCase>
) : ViewModel() {
    val pickedUpPlayersHistory = observePickedUpPlayersHistoryUseCase.get()().cachedIn(
        scope = viewModelScope
    )
}