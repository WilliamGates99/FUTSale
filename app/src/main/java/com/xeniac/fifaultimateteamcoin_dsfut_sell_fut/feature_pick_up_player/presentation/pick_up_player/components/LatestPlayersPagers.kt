package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.presentation.pick_up_player.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.unit.sp
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.Player
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.theme.Neutral40
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.presentation.pick_up_player.utils.calculateCurrentPageOffset

@Composable
fun LatestPlayersPagers(
    latestPickedPlayers: List<Player>,
    timerText: String,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(
        start = 24.dp,
        end = 24.dp,
        bottom = 20.dp
    ),
    beyondViewportPageCount: Int = 1,
    pageSpacing: Dp = 12.dp,
    onPlayerCardClick: (player: Player) -> Unit,
    onCountdownStart: (expiryTimeInMillis: Long) -> Unit
) {
    AnimatedVisibility(
        visible = latestPickedPlayers.isNotEmpty(),
        enter = expandVertically(),
        exit = shrinkVertically(),
        modifier = modifier
    ) {
        val pagerState = rememberPagerState(pageCount = { latestPickedPlayers.size })

        LaunchedEffect(key1 = pagerState.settledPage) {
            onCountdownStart(latestPickedPlayers[pagerState.currentPage].expiryTimeInMillis)
        }

        Box(
            modifier = modifier
        ) {
            HorizontalPager(
                state = pagerState,
                contentPadding = contentPadding,
                beyondViewportPageCount = beyondViewportPageCount,
                pageSpacing = pageSpacing,
                key = { index -> latestPickedPlayers[index] }
            ) { scrollPosition ->
                val player = latestPickedPlayers[scrollPosition]

                PlayerCard(
                    player = player,
                    timerText = timerText,
                    onClick = { onPlayerCardClick(player) },
                    modifier = Modifier
                        .graphicsLayer {
                            /*
                            When the page is settled, keep the scaleX and scaleY to 100%,
                            and when the page is scrolling, keep the scaleX value at 100%,
                            and animate the scaleY value between 90% and 100%
                             */
                            lerp(
                                start = DpOffset(x = 1.dp, y = 1.dp),
                                stop = DpOffset(x = 1.dp, y = 0.9.dp),
                                fraction = pagerState.calculateCurrentPageOffset(scrollPosition)
                            ).also { scale ->
                                scaleX = scale.x.value
                                scaleY = scale.y.value
                            }
                        }
                        .fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun PlayerCard(
    player: Player,
    timerText: String,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(12.dp),
    colors: CardColors = CardDefaults.elevatedCardColors(
        containerColor = MaterialTheme.colorScheme.surfaceContainerLow
    ),
    onClick: () -> Unit
) {
    ElevatedCard(
        onClick = onClick,
        shape = shape,
        colors = colors,
        modifier = modifier
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(space = 24.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                RatingAndPosition(
                    rating = player.rating,
                    position = player.position
                )

                PlayerName(name = player.name)

                ExpiryTimer(timerText = timerText)
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(space = 12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                PlayerTextInfo(
                    title = stringResource(id = R.string.pick_up_player_latest_player_title_chemistry_style),
                    value = player.chemistryStyle,
                    modifier = Modifier.fillMaxWidth()
                )

                PlayerTextInfo(
                    title = stringResource(id = R.string.pick_up_player_latest_player_title_start_price),
                    value = player.startPrice,
                    modifier = Modifier.fillMaxWidth()
                )

                PlayerTextInfo(
                    title = stringResource(id = R.string.pick_up_player_latest_player_title_buy_now_price),
                    value = player.buyNowPrice,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun RatingAndPosition(
    rating: String,
    position: String,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(4.dp),
    background: Color = MaterialTheme.colorScheme.secondaryContainer,
    ratingFontSize: TextUnit = 16.sp,
    ratingLineHeight: TextUnit = 16.sp,
    ratingFontWeight: FontWeight = FontWeight.ExtraBold,
    ratingTextAlign: TextAlign = TextAlign.Center,
    ratingMaxLines: Int = 1,
    ratingColor: Color = MaterialTheme.colorScheme.onSecondaryContainer,
    positionFontSize: TextUnit = 16.sp,
    positionLineHeight: TextUnit = 16.sp,
    positionFontWeight: FontWeight = FontWeight.ExtraBold,
    positionTextAlign: TextAlign = TextAlign.Center,
    positionMaxLines: Int = 1,
    positionColor: Color = MaterialTheme.colorScheme.onSecondaryContainer
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(space = 4.dp),
        modifier = modifier
            .clip(shape)
            .background(background)
            .padding(
                horizontal = 8.dp,
                vertical = 4.dp
            )
    ) {
        Text(
            text = rating,
            fontSize = ratingFontSize,
            lineHeight = ratingLineHeight,
            fontWeight = ratingFontWeight,
            textAlign = ratingTextAlign,
            maxLines = ratingMaxLines,
            color = ratingColor
        )

        Text(
            text = position,
            fontSize = positionFontSize,
            lineHeight = positionLineHeight,
            fontWeight = positionFontWeight,
            textAlign = positionTextAlign,
            maxLines = positionMaxLines,
            color = positionColor
        )
    }
}

@Composable
fun PlayerName(
    name: String,
    modifier: Modifier = Modifier,
    nameFontSize: TextUnit = 16.sp,
    nameLineHeight: TextUnit = 16.sp,
    nameFontWeight: FontWeight = FontWeight.ExtraBold,
    nameTextAlign: TextAlign = TextAlign.Center,
    nameMaxLines: Int = 1,
    nameColor: Color = MaterialTheme.colorScheme.onSurface
) {
    Text(
        text = name,
        fontSize = nameFontSize,
        lineHeight = nameLineHeight,
        fontWeight = nameFontWeight,
        textAlign = nameTextAlign,
        maxLines = nameMaxLines,
        color = nameColor,
        modifier = modifier.basicMarquee()
    )
}

@Composable
fun ExpiryTimer(
    timerText: String,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(4.dp),
    background: Color = MaterialTheme.colorScheme.errorContainer,
    timerFontSize: TextUnit = 10.sp,
    timerLineHeight: TextUnit = 10.sp,
    timerFontWeight: FontWeight = FontWeight.Black,
    timerTextAlign: TextAlign = TextAlign.Center,
    timerMaxLines: Int = 1,
    timerColor: Color = MaterialTheme.colorScheme.onErrorContainer
) {
    Text(
        text = timerText,
        fontSize = timerFontSize,
        lineHeight = timerLineHeight,
        fontWeight = timerFontWeight,
        textAlign = timerTextAlign,
        maxLines = timerMaxLines,
        color = timerColor,
        modifier = modifier
            .clip(shape)
            .background(background)
            .padding(
                horizontal = 8.dp,
                vertical = 4.dp
            )
            .animateContentSize()
    )
}

@Composable
fun PlayerTextInfo(
    title: String,
    value: String,
    modifier: Modifier = Modifier,
    titleFontSize: TextUnit = 16.sp,
    titleLineHeight: TextUnit = 16.sp,
    titleFontWeight: FontWeight = FontWeight.Light,
    titleMaxLines: Int = 1,
    titleColor: Color = Neutral40,
    valueFontSize: TextUnit = 16.sp,
    valueLineHeight: TextUnit = 16.sp,
    valueFontWeight: FontWeight = FontWeight.Bold,
    valueMaxLines: Int = 1,
    valueColor: Color = MaterialTheme.colorScheme.onSurfaceVariant
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Text(
            text = title,
            fontSize = titleFontSize,
            lineHeight = titleLineHeight,
            fontWeight = titleFontWeight,
            maxLines = titleMaxLines,
            color = titleColor
        )

        Text(
            text = value,
            fontSize = valueFontSize,
            lineHeight = valueLineHeight,
            fontWeight = valueFontWeight,
            maxLines = valueMaxLines,
            color = valueColor
        )
    }
}