package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.presentation.pick_up_player.utils

import androidx.compose.foundation.pager.PagerState
import kotlin.math.absoluteValue

/**
 * Calculate the absolute offset for the current page from the scroll position.
 * We use the absolute value which allows us to mirror any effects for both directions
 */
fun PagerState.calculateCurrentPageOffset(scrollPosition: Int): Float {
    return ((currentPage - scrollPosition) + currentPageOffsetFraction).absoluteValue
}