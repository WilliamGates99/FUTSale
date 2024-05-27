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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.presentation.utils.Constants

enum class AccountLinkRowItems(
    @DrawableRes val icon: Int,
    @StringRes val title: Int,
    val url: String?
) {
    Wallet(
        icon = R.drawable.ic_profile_wallet,
        title = R.string.profile_text_account_links_wallet,
        url = Constants.URL_ACCOUNT_WALLET
    ),
    PurchasedPlayers(
        icon = R.drawable.ic_profile_purchased_players,
        title = R.string.profile_text_account_links_purchased_players,
        url = Constants.URL_ACCOUNT_PURCHASED_PLAYERS
    ),
    Rules(
        icon = R.drawable.ic_profile_rules,
        title = R.string.profile_text_account_links_rules,
        url = Constants.URL_ACCOUNT_RULES
    ),
    Statistics(
        icon = R.drawable.ic_profile_statistics,
        title = R.string.profile_text_account_links_statistics,
        url = Constants.URL_ACCOUNT_STATISTICS
    )
}

@Composable
fun AccountLinks(
    modifier: Modifier = Modifier,
    titlePadding: PaddingValues = PaddingValues(horizontal = 8.dp),
    title: String = stringResource(id = R.string.profile_title_account_links),
    titleFontSize: TextUnit = 16.sp,
    titleFontWeight: FontWeight = FontWeight.ExtraBold,
    titleColor: Color = MaterialTheme.colorScheme.onBackground,
    cardShape: Shape = RoundedCornerShape(12.dp),
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
            AccountLinkRowItems.entries.forEachIndexed { index, accountLinkRowItem ->
                CardClickableLinkRowItem(
                    icon = painterResource(id = accountLinkRowItem.icon),
                    title = stringResource(id = accountLinkRowItem.title),
                    onClick = {
                        openUrlInBrowser(accountLinkRowItem.url)
                    }
                )

                val isNotLastItem = index != AccountLinkRowItems.entries.size - 1
                if (isNotLastItem) {
                    HorizontalDivider()
                }
            }
        }
    }
}