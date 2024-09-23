package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_history.presentation.history.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.Platform
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.Player
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.components.dashedBorder
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.theme.NeutralVariant20
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_history.presentation.history.utils.TestTags

@Composable
fun PlayersLazyColumn(
    pickedPlayersHistory: LazyPagingItems<Player>,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
    onClick: (playerId: Long) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = contentPadding,
        modifier = modifier.testTag(tag = TestTags.HISTORY_LAZY_COLUMN)
    ) {
        items(
            count = pickedPlayersHistory.itemCount,
            key = pickedPlayersHistory.itemKey { it },
            contentType = pickedPlayersHistory.itemContentType { it }
        ) { index ->
            val player = pickedPlayersHistory[index]

            player?.let {
                PlayerCard(
                    player = player,
                    onClick = {
                        player.id?.let { playerId ->
                            onClick(playerId)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag(TestTags.HISTORY_LAZY_COLUMN_ITEM)
                )
            }
        }
    }
}

@Composable
fun PlayerCard(
    player: Player,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(12.dp),
    colors: CardColors = CardDefaults.outlinedCardColors(),
    elevation: CardElevation = CardDefaults.outlinedCardElevation(),
    borderColor: Color = MaterialTheme.colorScheme.outlineVariant,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        shape = shape,
        colors = colors,
        elevation = elevation,
        modifier = modifier.dashedBorder(
            strokeWidth = 2.dp,
            color = borderColor,
            cornerRadius = 12.dp,
            dash = 4.dp,
            gap = 4.dp
        )
    ) {
        CompositionLocalProvider(value = LocalLayoutDirection provides LayoutDirection.Ltr) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(space = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 16.dp)
            ) {
                RatingAndPosition(
                    rating = player.rating,
                    position = player.position
                )

                PlayerName(
                    name = player.name,
                    modifier = Modifier.weight(1f)
                )

                PlatformIcon(
                    platform = player.platform,
                    modifier = Modifier.size(16.dp)
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
    fontSize: TextUnit = 16.sp,
    lineHeight: TextUnit = 16.sp,
    fontWeight: FontWeight = FontWeight.ExtraBold,
    color: Color = MaterialTheme.colorScheme.onSurface
) {
    Text(
        text = name,
        fontSize = fontSize,
        lineHeight = lineHeight,
        fontWeight = fontWeight,
        color = color,
        modifier = modifier
    )
}

@Composable
fun PlatformIcon(
    platform: Platform,
    modifier: Modifier = Modifier,
    icon: Painter = painterResource(
        id = when (platform) {
            Platform.CONSOLE -> R.drawable.ic_core_platform_console
            Platform.PC -> R.drawable.ic_core_platform_pc
        }
    ),
    contentDescription: String = stringResource(
        id = when (platform) {
            Platform.CONSOLE -> R.string.core_platform_console
            Platform.PC -> R.string.core_platform_pc
        }
    ),
    color: Color = NeutralVariant20
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        Icon(
            painter = icon,
            contentDescription = contentDescription,
            tint = color,
            modifier = Modifier.fillMaxWidth()
        )
    }
}