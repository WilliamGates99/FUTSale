package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.presentation.pick_up_player.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.formatNumber
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.components.CustomOutlinedTextField
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.presentation.pick_up_player.states.PickUpPlayerState
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.presentation.pick_up_player.utils.TestTags

@Composable
fun PriceTextFields(
    pickUpPlayerState: PickUpPlayerState,
    modifier: Modifier = Modifier,
    isLoading: Boolean = pickUpPlayerState.isAutoPickUpLoading || pickUpPlayerState.isPickUpOnceLoading,
    supportingText: String = stringResource(id = R.string.pick_up_player_helper_optional),
    onMinPriceChange: (newPrice: String) -> Unit,
    onMaxPriceChange: (newPrice: String) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(
            space = 16.dp,
            alignment = Alignment.CenterHorizontally
        ),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        CustomOutlinedTextField(
            isLoading = isLoading,
            value = pickUpPlayerState.minPrice,
            onValueChange = onMinPriceChange,
            title = stringResource(id = R.string.pick_up_player_title_min_price),
            placeholder = formatNumber(stringResource(id = R.string.pick_up_player_hint_min_price).toInt()),
            leadingIcon = painterResource(id = R.drawable.ic_pick_up_player_min_price),
            supportingText = supportingText,
            errorText = pickUpPlayerState.minPriceErrorText?.asString(),
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Next,
            testTag = TestTags.MIN_PRICE_TEXT_FIELD,
            modifier = Modifier.weight(1f)
        )

        CustomOutlinedTextField(
            isLoading = isLoading,
            value = pickUpPlayerState.maxPrice,
            onValueChange = onMaxPriceChange,
            title = stringResource(id = R.string.pick_up_player_title_max_price),
            placeholder = formatNumber(stringResource(id = R.string.pick_up_player_hint_max_price).toInt()),
            leadingIcon = painterResource(id = R.drawable.ic_pick_up_player_max_price),
            supportingText = supportingText,
            errorText = pickUpPlayerState.maxPriceErrorText?.asString(),
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done,
            testTag = TestTags.MAX_PRICE_TEXT_FIELD,
            modifier = Modifier.weight(1f)
        )
    }
}