package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_history.presentation.player_info

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.Player
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.navigation.HistoryPlayerInfoScreen
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.navigation.utils.serializableNavType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.reflect.typeOf

@HiltViewModel
class HistoryPlayerInfoViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    // TODO: GET PLAYER ID FROM NAVIGATION
    val player = savedStateHandle.toRoute<HistoryPlayerInfoScreen>(
        typeMap = mapOf(typeOf<Player>() to serializableNavType<Player>())
    ).player
}