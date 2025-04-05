package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.presentation.pick_up_player.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.systemGestureExclusion
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.ui.components.CustomCheckbox
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.UiText
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.presentation.pick_up_player.events.PickUpPlayerAction
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.presentation.pick_up_player.utils.TestTags
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TakeAfterSlider(
    isAutoPickUpLoading: Boolean,
    isPickUpOnceLoading: Boolean,
    isTakeAfterChecked: Boolean,
    takeAfterDelayInSeconds: Int,
    takeAfterErrorText: UiText?,
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    colors: SliderColors = SliderDefaults.colors(),
    isLoading: Boolean = isAutoPickUpLoading || isPickUpOnceLoading,
    isEnabled: Boolean = isTakeAfterChecked && !isLoading,
    sliderInitialPosition: Float = takeAfterDelayInSeconds.toFloat(),
    sliderValueRange: ClosedFloatingPointRange<Float> = 1f..60f,
    sliderSteps: Int = 57, // 57 + 2 ends of the slider = 59 steps
    errorTextFontSize: TextUnit = 12.sp,
    errorTextFontWeight: FontWeight = FontWeight.Normal,
    errorTextColor: Color = MaterialTheme.colorScheme.error,
    onAction: (action: PickUpPlayerAction) -> Unit
) {
    var startSliderAnimation by remember { mutableStateOf(false) }
    var sliderPosition by remember {
        mutableFloatStateOf(
            sliderInitialPosition.coerceIn(
                minimumValue = sliderValueRange.start,
                maximumValue = sliderValueRange.endInclusive
            )
        )
    }
    val animatedSliderPosition by animateFloatAsState(
        targetValue = if (startSliderAnimation) {
            sliderPosition
        } else 0f,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "animatedSliderPosition"
    )

    LaunchedEffect(key1 = Unit) {
        startSliderAnimation = true
    }

    LaunchedEffect(key1 = sliderInitialPosition) {
        if (sliderInitialPosition == 0f) {
            sliderPosition = 1f
        }
    }

    Column(modifier = modifier) {
        CustomCheckbox(
            isLoading = isLoading,
            isChecked = isTakeAfterChecked,
            onCheckedChange = { isChecked ->
                onAction(PickUpPlayerAction.TakeAfterCheckedChanged(isChecked))
            },
            text = if (!isTakeAfterChecked) stringResource(id = R.string.pick_up_player_title_take_after)
            else pluralStringResource(
                id = R.plurals.pick_up_player_title_take_after,
                sliderPosition.roundToInt(),
                sliderPosition.roundToInt()
            ),
            testTag = TestTags.TAKE_AFTER_CHECK_BOX
        )

        Spacer(Modifier.height(4.dp))

        Slider(
            enabled = isEnabled,
            value = animatedSliderPosition,
            onValueChange = { sliderPosition = it },
            onValueChangeFinished = {
                onAction(PickUpPlayerAction.TakeAfterSliderChanged(sliderPosition.roundToInt()))
            },
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
                            shape = CircleShape,
                            tonalElevation = 0.dp,
                            shadowElevation = 0.dp,
                            colors = TooltipDefaults.richTooltipColors().copy(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            ),
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
                        thumbSize = DpSize(
                            width = 4.dp,
                            height = 40.dp
                        ),
                        colors = if (isEnabled) colors else colors.copy(
                            thumbColor = colors.thumbColor.copy(alpha = 0.38f)
                                .compositeOver(MaterialTheme.colorScheme.surface)
                        )
                    )
                }
            },
            track = { sliderState ->
                SliderDefaults.Track(
                    enabled = !isLoading,
                    sliderState = sliderState,
                    colors = colors,
                    drawTick = { offset, color ->
                        with(receiver = this) {
                            drawCircle(
                                color = color,
                                center = offset,
                                radius = 3.dp.toPx() / 2f
                            )
                        }
                    }
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .systemGestureExclusion { coordinates ->
                    if (isEnabled) {
                        Rect(
                            topLeft = Offset(
                                x = 0f - 100f,
                                y = 0f
                            ),
                            bottomRight = Offset(
                                x = coordinates.size.width + 100f,
                                y = coordinates.size.height.toFloat()
                            )
                        )
                    } else Rect(left = 0f, top = 0f, right = 0f, bottom = 0f)
                }
                .testTag(TestTags.TAKE_AFTER_SLIDER)
        )

        AnimatedVisibility(
            visible = takeAfterErrorText != null,
            enter = fadeIn() + slideInVertically(),
            exit = slideOutVertically() + fadeOut()
        ) {
            Text(
                text = takeAfterErrorText?.asString().orEmpty(),
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