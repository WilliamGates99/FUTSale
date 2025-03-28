package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.presentation.pick_up_player.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.states.CustomTextFieldState
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.ui.components.CustomOutlinedTextField
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.ui.components.NumberSeparatorVisualTransformation
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.presentation.pick_up_player.events.PickUpPlayerAction
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.presentation.pick_up_player.utils.TestTags

@Composable
fun PriceTextFields(
    isAutoPickUpLoading: Boolean,
    isPickUpOnceLoading: Boolean,
    minPriceState: CustomTextFieldState,
    maxPriceState: CustomTextFieldState,
    modifier: Modifier = Modifier,
    isLoading: Boolean = isAutoPickUpLoading || isPickUpOnceLoading,
    supportingText: String = stringResource(id = R.string.pick_up_player_helper_optional),
    onAction: (action: PickUpPlayerAction) -> Unit
) {
    val focusManager = LocalFocusManager.current

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
            value = minPriceState.value,
            onValueChange = { newValue ->
                onAction(PickUpPlayerAction.MinPriceChanged(newValue))
            },
            title = stringResource(id = R.string.pick_up_player_title_min_price),
            placeholder = stringResource(id = R.string.pick_up_player_hint_min_price),
            leadingIcon = painterResource(id = R.drawable.ic_pick_up_player_min_price),
            supportingText = supportingText,
            errorText = minPriceState.errorText?.asString(),
            visualTransformation = NumberSeparatorVisualTransformation(),
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Next,
            testTag = TestTags.MIN_PRICE_TEXT_FIELD,
            modifier = Modifier.weight(1f)
        )

        CustomOutlinedTextField(
            isLoading = isLoading,
            value = maxPriceState.value,
            onValueChange = { newValue ->
                onAction(PickUpPlayerAction.MaxPriceChanged(newValue))
            },
            title = stringResource(id = R.string.pick_up_player_title_max_price),
            placeholder = stringResource(id = R.string.pick_up_player_hint_max_price),
            leadingIcon = painterResource(id = R.drawable.ic_pick_up_player_max_price),
            supportingText = supportingText,
            errorText = maxPriceState.errorText?.asString(),
            visualTransformation = NumberSeparatorVisualTransformation(),
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done,
            keyboardAction = { focusManager.clearFocus() },
            testTag = TestTags.MAX_PRICE_TEXT_FIELD,
            modifier = Modifier.weight(1f)
        )
    }
}