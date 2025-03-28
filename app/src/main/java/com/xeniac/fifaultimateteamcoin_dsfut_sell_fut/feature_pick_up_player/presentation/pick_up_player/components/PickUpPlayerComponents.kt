package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.presentation.pick_up_player.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.ui.components.GradientButton
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.presentation.pick_up_player.events.PickUpPlayerAction

@Composable
fun AutoPickUpButton(
    isAutoPickUpLoading: Boolean,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = !isAutoPickUpLoading,
    contentPadding: PaddingValues = PaddingValues(vertical = 14.dp),
    textFontSize: TextUnit = 16.sp,
    textLineHeight: TextUnit = 20.sp,
    textFontWeight: FontWeight = FontWeight.Bold,
    onAction: (action: PickUpPlayerAction) -> Unit
) {
    if (isAutoPickUpLoading) {
        GradientButton(
            onClick = {
                onAction(PickUpPlayerAction.CancelAutoPickUpPlayer)
            },
            gradientColors = listOf(
                MaterialTheme.colorScheme.secondaryContainer,
                MaterialTheme.colorScheme.secondaryContainer,
                MaterialTheme.colorScheme.primary
            ),
            contentPadding = contentPadding,
            modifier = modifier
        ) {
            Text(
                text = stringResource(id = R.string.pick_up_player_btn_pick_auto_cancel),
                fontSize = textFontSize,
                lineHeight = textLineHeight,
                fontWeight = textFontWeight,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    } else {
        Button(
            onClick = {
                onAction(PickUpPlayerAction.AutoPickUpPlayer)
            },
            enabled = isEnabled,
            contentPadding = contentPadding,
            modifier = modifier
        ) {
            Text(
                text = stringResource(id = R.string.pick_up_player_btn_pick_auto),
                fontSize = textFontSize,
                lineHeight = textLineHeight,
                fontWeight = textFontWeight
            )
        }
    }
}

@Composable
fun PickUpOnceButton(
    isPickUpOnceLoading: Boolean,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(vertical = 12.dp),
    textFontSize: TextUnit = 14.sp,
    textLineHeight: TextUnit = 20.sp,
    textFontWeight: FontWeight = FontWeight.Medium,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        enabled = !isPickUpOnceLoading,
        contentPadding = contentPadding,
        modifier = modifier
    ) {
        AnimatedContent(
            targetState = isPickUpOnceLoading,
            transitionSpec = { scaleIn().togetherWith(scaleOut()) },
            label = "PickUpOnceButton"
        ) { isLoading ->
            if (isLoading) {
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
}