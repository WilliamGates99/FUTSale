package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.presentation.picked_up_player_info.components

import androidx.annotation.RawRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.Player
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.UiText
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.theme.Neutral40
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.theme.Red
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.theme.RedAlpha20
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.presentation.picked_up_player_info.utils.asUiText

@Composable
fun PickedUpPlayerInfo(
    player: Player,
    timerText: String,
    horizontalPadding: Dp,
    modifier: Modifier = Modifier,
    @RawRes lottieAnimation: Int = R.raw.anim_pick_up_player_success,
) {
    val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(lottieAnimation))

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            LottieAnimation(
                composition = composition,
                speed = 0.7f,
                iterations = 1,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(horizontal = 80.dp)
                    .fillMaxWidth()
            )

            ExpiryTimer(
                timerText = timerText,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(end = horizontalPadding)
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        PickUpMessage(
            message = stringResource(
                id = R.string.picked_up_player_info_message,
                player.name
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = horizontalPadding)
        )

        Spacer(modifier = Modifier.height(24.dp))

        PlayerInfo(
            player = player,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun ExpiryTimer(
    timerText: String,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(4.dp),
    background: Color = RedAlpha20,
    fontSize: TextUnit = 10.sp,
    lineHeight: TextUnit = 10.sp,
    fontWeight: FontWeight = FontWeight.Black,
    textAlign: TextAlign = TextAlign.Center,
    maxLines: Int = 1,
    color: Color = Red
) {
    var oldTimerText by remember { mutableStateOf(timerText) }

    SideEffect {
        oldTimerText = timerText
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clip(shape)
            .background(background)
            .padding(
                horizontal = 8.dp,
                vertical = 4.dp
            )
            .animateContentSize()
    ) {
        val isTimerFinished = timerText == UiText.StringResource(
            R.string.picked_up_player_info_timer_expired
        ).asString()

        if (isTimerFinished) {
            Text(
                text = timerText,
                fontSize = fontSize,
                lineHeight = lineHeight,
                fontWeight = fontWeight,
                textAlign = textAlign,
                maxLines = maxLines,
                color = color
            )
        } else {
            timerText.indices.forEach { i ->
                val oldChar = oldTimerText.getOrNull(i)
                val newChar = timerText[i]
                val timerChar = if (oldChar == newChar) {
                    oldTimerText[i]
                } else {
                    timerText[i]
                }

                AnimatedContent(
                    targetState = timerChar,
                    transitionSpec = {
                        slideInVertically { -it } togetherWith slideOutVertically { it }
                    },
                    label = "ExpiryTimer",
                ) { char ->
                    Text(
                        text = char.toString(),
                        fontSize = fontSize,
                        lineHeight = lineHeight,
                        fontWeight = fontWeight,
                        textAlign = textAlign,
                        maxLines = maxLines,
                        color = color,
                        softWrap = false
                    )
                }
            }
        }
    }
}

@Composable
fun PickUpMessage(
    message: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 16.sp,
    lineHeight: TextUnit = 16.sp,
    fontWeight: FontWeight = FontWeight.ExtraBold,
    textAlign: TextAlign = TextAlign.Center,
    color: Color = MaterialTheme.colorScheme.primary
) {
    Text(
        text = message,
        fontSize = fontSize,
        lineHeight = lineHeight,
        fontWeight = fontWeight,
        textAlign = textAlign,
        color = color,
        modifier = modifier
    )
}

@Composable
fun PlayerInfo(
    player: Player,
    modifier: Modifier = Modifier,
) {
    var rowHeight by remember { mutableIntStateOf(IntSize.Zero.height) }
    val rowHeightDp = LocalDensity.current.run { rowHeight.toDp() }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.onSizeChanged { rowHeight = it.height }
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(space = 24.dp),
            modifier = Modifier
                .weight(1f)
                .padding(
                    horizontal = 8.dp,
                    vertical = 8.dp
                )
        ) {
            PlayerInfoText(
                title = stringResource(id = R.string.picked_up_player_info_title_rating),
                value = player.rating,
                modifier = Modifier.fillMaxWidth()
            )

            PlayerInfoText(
                title = stringResource(id = R.string.picked_up_player_info_title_start_price),
                value = player.buyNowPrice,
                modifier = Modifier.fillMaxWidth()
            )

            PlayerInfoText(
                title = stringResource(id = R.string.picked_up_player_info_title_owners),
                value = player.owners.toString(),
                modifier = Modifier.fillMaxWidth()
            )

            PlayerInfoText(
                title = stringResource(id = R.string.picked_up_player_info_title_chemistry_style),
                value = player.chemistryStyle,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Box(
            modifier = Modifier
                .width(1.dp)
                .height(rowHeightDp)
                .clip(RoundedCornerShape(4.dp))
                .background(MaterialTheme.colorScheme.outlineVariant)
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(space = 24.dp),
            modifier = Modifier
                .weight(1f)
                .padding(
                    horizontal = 8.dp,
                    vertical = 8.dp
                )
        ) {
            PlayerInfoText(
                title = stringResource(id = R.string.picked_up_player_info_title_position),
                value = player.position,
                modifier = Modifier.fillMaxWidth()
            )

            PlayerInfoText(
                title = stringResource(id = R.string.picked_up_player_info_title_buy_now_price),
                value = player.buyNowPrice,
                modifier = Modifier.fillMaxWidth()
            )

            PlayerInfoText(
                title = stringResource(id = R.string.picked_up_player_info_title_contracts),
                value = player.contracts.toString(),
                modifier = Modifier.fillMaxWidth()
            )

            PlayerInfoText(
                title = stringResource(id = R.string.picked_up_player_info_title_platform),
                value = player.platform.asUiText().asString(),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun PlayerInfoText(
    title: String,
    value: String,
    modifier: Modifier = Modifier,
    titleFontSize: TextUnit = 12.sp,
    titleLineHeight: TextUnit = 12.sp,
    titleFontWeight: FontWeight = FontWeight.SemiBold,
    titleTextAlign: TextAlign = TextAlign.Center,
    titleMaxLines: Int = 1,
    titleColor: Color = Neutral40,
    valueFontSize: TextUnit = 14.sp,
    valueLineHeight: TextUnit = 14.sp,
    valueFontWeight: FontWeight = FontWeight.ExtraBold,
    valueTextAlign: TextAlign = TextAlign.Center,
    valueMaxLines: Int = 1,
    valueColor: Color = MaterialTheme.colorScheme.onSurfaceVariant
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(space = 4.dp),
        modifier = modifier
    ) {
        Text(
            text = title,
            fontSize = titleFontSize,
            lineHeight = titleLineHeight,
            fontWeight = titleFontWeight,
            textAlign = titleTextAlign,
            maxLines = titleMaxLines,
            color = titleColor,
            modifier = Modifier
                .fillMaxSize()
                .basicMarquee()
        )

        Text(
            text = value,
            fontSize = valueFontSize,
            lineHeight = valueLineHeight,
            fontWeight = valueFontWeight,
            textAlign = valueTextAlign,
            maxLines = valueMaxLines,
            color = valueColor,
            modifier = Modifier
                .fillMaxSize()
                .basicMarquee()
        )
    }
}