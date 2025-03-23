package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_history.presentation.player_info.components

import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.Player
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.ui.theme.Neutral40
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.asUiText

@Composable
fun PlayerInfo(
    player: Player?,
    modifier: Modifier = Modifier
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
                title = stringResource(id = R.string.history_player_info_title_rating),
                value = player?.rating,
                modifier = Modifier.fillMaxWidth()
            )

            PlayerInfoText(
                title = stringResource(id = R.string.history_player_info_title_start_price),
                value = player?.buyNowPrice,
                modifier = Modifier.fillMaxWidth()
            )

            PlayerInfoText(
                title = stringResource(id = R.string.history_player_info_title_owners),
                value = player?.owners,
                modifier = Modifier.fillMaxWidth()
            )

            PlayerInfoText(
                title = stringResource(id = R.string.history_player_info_title_chemistry_style),
                value = player?.chemistryStyle,
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
                title = stringResource(id = R.string.history_player_info_title_position),
                value = player?.position,
                modifier = Modifier.fillMaxWidth()
            )

            PlayerInfoText(
                title = stringResource(id = R.string.history_player_info_title_buy_now_price),
                value = player?.buyNowPrice,
                modifier = Modifier.fillMaxWidth()
            )

            PlayerInfoText(
                title = stringResource(id = R.string.history_player_info_title_contracts),
                value = player?.contracts,
                modifier = Modifier.fillMaxWidth()
            )

            PlayerInfoText(
                title = stringResource(id = R.string.history_player_info_title_platform),
                value = player?.platform?.asUiText()?.asString(),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun PlayerInfoText(
    title: String,
    value: String?,
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
            text = value ?: "",
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