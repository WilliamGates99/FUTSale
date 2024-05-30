package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.presentation.picked_up_player_info

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PickedUpPlayerInfoViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    fun onEvent(event: PickedUpPlayerInfoEvent) {
//        when (event) {
//
//        }
    }
}