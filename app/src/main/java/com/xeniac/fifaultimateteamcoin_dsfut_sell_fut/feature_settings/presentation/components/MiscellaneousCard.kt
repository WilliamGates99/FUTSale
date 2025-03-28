package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.presentation.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.isAppInstalledFromMyket
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.presentation.util.Constants

enum class MiscellaneousRowItems(
    @DrawableRes val icon: Int,
    @StringRes val title: Int,
    val url: String?
) {
    Donate(
        icon = R.drawable.ic_settings_donate,
        title = R.string.settings_text_miscellaneous_donate,
        url = Constants.URL_DONATE
    ),
    ImproveTranslations(
        icon = R.drawable.ic_settings_improve_translations,
        title = R.string.settings_text_miscellaneous_improve_translations,
        url = Constants.URL_CROWDIN
    ),
    RateUs(
        icon = R.drawable.ic_settings_rate_us,
        title = R.string.settings_text_miscellaneous_rate_us,
        url = null
    ),
    PrivacyPolicy(
        icon = R.drawable.ic_settings_privacy_policy,
        title = R.string.settings_text_miscellaneous_privacy_policy,
        url = com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.Constants.URL_PRIVACY_POLICY
    )
}

@Composable
fun MiscellaneousCard(
    modifier: Modifier = Modifier,
    titlePadding: PaddingValues = PaddingValues(horizontal = 8.dp),
    title: String = stringResource(id = R.string.settings_title_miscellaneous),
    titleFontSize: TextUnit = 16.sp,
    titleFontWeight: FontWeight = FontWeight.ExtraBold,
    titleColor: Color = MaterialTheme.colorScheme.onBackground,
    cardShape: Shape = RoundedCornerShape(12.dp),
    openAppPageInStore: () -> Unit,
    openUrlInInAppBrowser: (url: String?) -> Unit,
    openUrlInBrowser: (url: String?) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(space = 8.dp),
        modifier = modifier
    ) {
        Text(
            text = title.uppercase(),
            fontSize = titleFontSize,
            fontWeight = titleFontWeight,
            color = titleColor,
            modifier = Modifier
                .padding(titlePadding)
                .fillMaxWidth()
        )

        OutlinedCard(
            shape = cardShape,
            modifier = Modifier.fillMaxWidth()
        ) {
            MiscellaneousRowItems.entries.forEachIndexed { index, miscellaneousItem ->
                val shouldShowItem = if (isAppInstalledFromMyket()) {
                    miscellaneousItem != MiscellaneousRowItems.Donate
                } else true

                if (shouldShowItem) {
                    CardClickableLinkRowItem(
                        icon = painterResource(id = miscellaneousItem.icon),
                        title = stringResource(id = miscellaneousItem.title),
                        onClick = {
                            when (miscellaneousItem) {
                                MiscellaneousRowItems.RateUs -> openAppPageInStore()
                                MiscellaneousRowItems.PrivacyPolicy -> openUrlInInAppBrowser(
                                    miscellaneousItem.url
                                )
                                else -> openUrlInBrowser(miscellaneousItem.url)
                            }
                        }
                    )
                }

                val isNotLastItem = index != MiscellaneousRowItems.entries.size - 1
                if (isNotLastItem) {
                    HorizontalDivider()
                }
            }
        }
    }
}

@Composable
private fun CardClickableLinkRowItem(
    icon: Painter,
    title: String,
    modifier: Modifier = Modifier,
    rowPadding: PaddingValues = PaddingValues(
        horizontal = 12.dp,
        vertical = 14.dp
    ),
    iconSize: Dp = 28.dp,
    iconShape: Shape = RoundedCornerShape(8.dp),
    iconBackgroundColor: Color = MaterialTheme.colorScheme.surfaceTint.copy(alpha = 0.12f),
    iconColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    titleFontSize: TextUnit = 16.sp,
    titleFontWeight: FontWeight = FontWeight.SemiBold,
    titleColor: Color = MaterialTheme.colorScheme.onSurface,
    linkIcon: Painter = painterResource(id = R.drawable.ic_core_link),
    linkIconSize: Dp = 16.dp,
    linkIconColor: Color = MaterialTheme.colorScheme.onSurface,
    onClick: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(space = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(rowPadding)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(iconSize)
                .clip(iconShape)
                .background(iconBackgroundColor)
                .padding(all = 4.dp)
        ) {
            Icon(
                painter = icon,
                contentDescription = title,
                tint = iconColor
            )
        }

        Text(
            text = title,
            fontSize = titleFontSize,
            fontWeight = titleFontWeight,
            color = titleColor,
            modifier = Modifier.weight(1f)
        )

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(linkIconSize)
        ) {
            Icon(
                painter = linkIcon,
                contentDescription = null,
                tint = linkIconColor
            )
        }
    }
}