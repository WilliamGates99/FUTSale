package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.presentation.pick_up_player.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Label
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RichTooltip
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderColors
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.components.CustomCheckbox
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.states.PickUpPlayerState
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.presentation.pick_up_player.utils.TestTags
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TakeAfterSlider(
    pickUpPlayerState: PickUpPlayerState,
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    colors: SliderColors = SliderDefaults.colors(),
    sliderInitialPosition: Float = pickUpPlayerState.takeAfterDelayInSeconds.toFloat(),
    isLoading: Boolean = pickUpPlayerState.isAutoPickUpLoading || pickUpPlayerState.isPickUpOnceLoading,
    isEnabled: Boolean = pickUpPlayerState.isTakeAfterChecked && !isLoading,
    sliderValueRange: ClosedFloatingPointRange<Float> = 1f..60f,
    sliderSteps: Int = 57, // 57 + 2 ends of the slider = 59 steps
    errorTextFontSize: TextUnit = 12.sp,
    errorTextFontWeight: FontWeight = FontWeight.Normal,
    errorTextColor: Color = MaterialTheme.colorScheme.error,
    onCheckedChange: (isChecked: Boolean) -> Unit,
    onSliderValueChangeFinished: (sliderPosition: Int) -> Unit
) {
    var sliderPosition by remember {
        mutableFloatStateOf(
            sliderInitialPosition.coerceIn(
                minimumValue = sliderValueRange.start,
                maximumValue = sliderValueRange.endInclusive
            )
        )
    }

    LaunchedEffect(key1 = sliderInitialPosition) {
        if (sliderInitialPosition == 0f) {
            sliderPosition = 1f
        }
    }

    Column(modifier = modifier) {
        CustomCheckbox(
            isLoading = isLoading,
            isChecked = pickUpPlayerState.isTakeAfterChecked,
            onCheckedChange = onCheckedChange,
            text = if (!pickUpPlayerState.isTakeAfterChecked) stringResource(id = R.string.pick_up_player_title_take_after)
            else pluralStringResource(
                id = R.plurals.pick_up_player_title_take_after,
                sliderPosition.roundToInt(),
                sliderPosition.roundToInt()
            ),
            testTag = TestTags.TAKE_AFTER_CHECK_BOX
        )

        Slider(
            enabled = isEnabled,
            value = sliderPosition,
            steps = sliderSteps,
            valueRange = sliderValueRange,
            interactionSource = interactionSource,
            colors = colors,
            thumb = {
                Label(
                    interactionSource = interactionSource,
                    label = {
                        val direction = LocalLayoutDirection.current

                        RichTooltip(
                            colors = TooltipDefaults.richTooltipColors().copy(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            ),
                            shape = CircleShape,
                            modifier = Modifier.animateContentSize(
                                alignment = when (direction) {
                                    LayoutDirection.Ltr -> Alignment.CenterStart
                                    LayoutDirection.Rtl -> Alignment.CenterEnd
                                }
                            )
                        ) {
                            Text(
                                text = sliderPosition.roundToInt().toString(),
                                fontSize = 12.sp,
                                lineHeight = 16.sp,
                                fontWeight = FontWeight.Medium,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )
                        }
                    }
                ) {
                    SliderDefaults.Thumb(
                        enabled = !isLoading,
                        interactionSource = interactionSource,
                        colors = if (isEnabled) colors else colors.copy(
                            thumbColor = colors.thumbColor.copy(alpha = 0.38f)
                                .compositeOver(MaterialTheme.colorScheme.surface)
                        )
                    )
                }
            },
            onValueChange = {
                sliderPosition = it
            },
            onValueChangeFinished = { onSliderValueChangeFinished(sliderPosition.roundToInt()) },
            modifier = Modifier
                .fillMaxWidth()
                .testTag(TestTags.TAKE_AFTER_SLIDER)
        )

        AnimatedVisibility(
            visible = pickUpPlayerState.takeAfterErrorText != null,
            enter = slideInVertically() + fadeIn(),
            exit = slideOutVertically() + fadeOut()
        ) {
            Text(
                text = pickUpPlayerState.takeAfterErrorText?.asString() ?: "",
                fontSize = errorTextFontSize,
                fontWeight = errorTextFontWeight,
                color = errorTextColor,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
        }
    }
}