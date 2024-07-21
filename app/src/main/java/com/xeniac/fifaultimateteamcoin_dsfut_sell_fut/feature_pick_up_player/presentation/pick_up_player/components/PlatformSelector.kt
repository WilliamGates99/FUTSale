package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.presentation.pick_up_player.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonColors
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.Platform
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.states.PickUpPlayerState
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.presentation.pick_up_player.utils.TestTags

enum class PlatformItems(
    @StringRes val title: Int,
    val platform: Platform,
    val testTag: String
) {
    PC(
        title = R.string.pick_up_player_segmented_btn_platform_pc,
        platform = Platform.PC,
        testTag = TestTags.PC_SEGMENTED_BTN

    ),
    CONSOLE(
        title = R.string.pick_up_player_segmented_btn_platform_console,
        platform = Platform.CONSOLE,
        testTag = TestTags.CONSOLE_SEGMENTED_BTN
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlatformSelector(
    pickUpPlayerState: PickUpPlayerState,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = !(pickUpPlayerState.isAutoPickUpLoading || pickUpPlayerState.isPickUpOnceLoading),
    title: String = stringResource(id = R.string.pick_up_player_title_platform),
    titleFontSize: TextUnit = 14.sp,
    titleLineHeight: TextUnit = 14.sp,
    titleFontWeight: FontWeight = FontWeight.Bold,
    titleColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    segmentedButtonBorderWidth: Dp = SegmentedButtonDefaults.BorderWidth,
    segmentedButtonColors: SegmentedButtonColors = SegmentedButtonDefaults.colors(
        activeContainerColor = MaterialTheme.colorScheme.secondaryContainer,
        activeContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
        activeBorderColor = MaterialTheme.colorScheme.outline,
        inactiveContainerColor = MaterialTheme.colorScheme.surface,
        inactiveContentColor = MaterialTheme.colorScheme.onSurface,
        inactiveBorderColor = MaterialTheme.colorScheme.outline,
        disabledActiveContainerColor = MaterialTheme.colorScheme.secondaryContainer,
        disabledActiveContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
        disabledActiveBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.38f),
        disabledInactiveContainerColor = MaterialTheme.colorScheme.surface,
        disabledInactiveContentColor = MaterialTheme.colorScheme.onSurface,
        disabledInactiveBorderColor = MaterialTheme.colorScheme.outline
    ),
    segmentedButtonLabelFontSize: TextUnit = 14.sp,
    segmentedButtonLabelLineHeight: TextUnit = 20.sp,
    segmentedButtonLabelFontWeight: FontWeight = FontWeight.SemiBold,
    segmentedButtonLabelTextAlignment: TextAlign = TextAlign.Center,
    onPlatformClick: (platform: Platform) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(space = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Text(
            text = title,
            fontSize = titleFontSize,
            lineHeight = titleLineHeight,
            fontWeight = titleFontWeight,
            color = titleColor
        )

        SingleChoiceSegmentedButtonRow(
            space = segmentedButtonBorderWidth,
            modifier = Modifier.weight(1f)
        ) {
            PlatformItems.entries.forEachIndexed { index, platformItem ->
                val isSelected = pickUpPlayerState.selectedPlatform == platformItem.platform

                SegmentedButton(
                    selected = isSelected,
                    enabled = isEnabled,
                    shape = SegmentedButtonDefaults.itemShape(
                        index = index,
                        count = PlatformItems.entries.size
                    ),
                    colors = segmentedButtonColors,
                    border = SegmentedButtonDefaults.borderStroke(
                        segmentedButtonColors.borderColor(
                            enabled = isEnabled,
                            active = isSelected
                        )
                    ),
                    label = {
                        Text(
                            text = stringResource(id = platformItem.title),
                            fontSize = segmentedButtonLabelFontSize,
                            lineHeight = segmentedButtonLabelLineHeight,
                            fontWeight = segmentedButtonLabelFontWeight,
                            textAlign = segmentedButtonLabelTextAlignment
                        )
                    },
                    icon = {},
                    onClick = { onPlatformClick(platformItem.platform) },
                    modifier = Modifier.testTag(platformItem.testTag)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Stable
internal fun SegmentedButtonColors.borderColor(enabled: Boolean, active: Boolean): Color {
    return when {
        enabled && active -> activeBorderColor
        enabled && !active -> inactiveBorderColor
        !enabled && active -> disabledActiveBorderColor
        else -> disabledInactiveBorderColor
    }
}