package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.presentation.pick_up_player.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.states.PickUpPlayerState

@Composable
fun PickUpOnceButton(
    pickUpPlayerState: PickUpPlayerState,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(vertical = 12.dp),
    textFontSize: TextUnit = 14.sp,
    textLineHeight: TextUnit = 20.sp,
    textFontWeight: FontWeight = FontWeight.Medium,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        enabled = !pickUpPlayerState.isPickUpOnceLoading,
        contentPadding = contentPadding,
        modifier = modifier
    ) {
        if (pickUpPlayerState.isPickUpOnceLoading) {
            CircularProgressIndicator(
                strokeWidth = 3.dp,
                strokeCap = StrokeCap.Round,
                modifier = Modifier.size(LocalDensity.current.run { (textFontSize * 1.5).toDp() })
            )
        } else {
            Text(
                text = stringResource(id = R.string.pick_up_player_btn_pick_once),
                fontSize = textFontSize,
                lineHeight = textLineHeight,
                fontWeight = textFontWeight
            )
        }
    }
}