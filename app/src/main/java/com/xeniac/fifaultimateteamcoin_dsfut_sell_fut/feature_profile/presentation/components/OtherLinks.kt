package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.presentation.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.common.utils.openLinkInInAppBrowser
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.presentation.utils.Constants

enum class OtherLinkRowItems(
    @DrawableRes val iconId: Int,
    @StringRes val titleId: Int,
    val url: String?
) {
    DsfutNews(
        iconId = R.drawable.ic_profile_dsfut_news,
        titleId = R.string.profile_text_other_links_dsfut_news,
        url = Constants.URL_DSFUT_DSFUT_NEWS
    ),
    ConsoleNotifications(
        iconId = R.drawable.ic_profile_notifications_channel,
        titleId = R.string.profile_text_other_links_console_notifications,
        url = Constants.URL_DSFUT_CONSOLE_NOTIFICATIONS
    ),
    PcNotifications(
        iconId = R.drawable.ic_profile_notifications_channel,
        titleId = R.string.profile_text_other_links_pc_notifications,
        url = Constants.URL_DSFUT_PC_NOTIFICATIONS
    ),
    DsfutWebsite(
        iconId = R.drawable.ic_profile_dsfut_website,
        titleId = R.string.profile_text_other_links_dsfut_website,
        url = Constants.URL_DSFUT_DSFUT_WEBSITE
    )
}

@Composable
fun OtherLinks(
    modifier: Modifier = Modifier,
    titlePadding: PaddingValues = PaddingValues(horizontal = 8.dp),
    title: String = stringResource(id = R.string.profile_title_other_links),
    titleFontSize: TextUnit = 16.sp,
    titleFontWeight: FontWeight = FontWeight.ExtraBold,
    titleColor: Color = MaterialTheme.colorScheme.onBackground,
    cardShape: Shape = RoundedCornerShape(12.dp)
) {
    val context = LocalContext.current

    Column(
        verticalArrangement = Arrangement.spacedBy(space = 8.dp),
        modifier = modifier.fillMaxWidth()
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
            OtherLinkRowItems.entries.forEachIndexed { index, otherLinkRowItem ->
                CardClickableLinkRowItem(
                    icon = painterResource(id = otherLinkRowItem.iconId),
                    title = stringResource(id = otherLinkRowItem.titleId),
                    onClick = {
                        otherLinkRowItem.url?.let { url ->
                            context.openLinkInInAppBrowser(urlString = url)
                        }
                    }
                )

                val isNotLastItem = index != OtherLinkRowItems.entries.size - 1
                if (isNotLastItem) {
                    HorizontalDivider()
                }
            }
        }
    }
}