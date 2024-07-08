package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.presentation.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.TestTags
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.navigation.Screen
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.presentation.util.TestTags.NAVIGATION_BAR

enum class NavigationBarItems(
    val screen: Screen,
    @StringRes val title: Int,
    @DrawableRes val inactiveIconId: Int,
    @DrawableRes val activeIconId: Int,
    val testTag: String
) {
    PickUpPlayer(
        screen = Screen.PickUpPlayerScreen,
        title = R.string.home_nav_title_pick_up_player,
        inactiveIconId = R.drawable.ic_home_nav_pick_up_player_outlined,
        activeIconId = R.drawable.ic_home_nav_pick_up_player_filled,
        testTag = TestTags.NAVIGATION_BAR_ITEM_PICK_UP_PLAYER
    ),
    Profile(
        screen = Screen.ProfileScreen,
        title = R.string.home_nav_title_profile,
        inactiveIconId = R.drawable.ic_home_nav_profile_outlined,
        activeIconId = R.drawable.ic_home_nav_profile_filled,
        testTag = TestTags.NAVIGATION_BAR_ITEM_PROFILE
    ),
    History(
        screen = Screen.HistoryScreen,
        title = R.string.home_nav_title_history,
        inactiveIconId = R.drawable.ic_home_nav_history_outlined,
        activeIconId = R.drawable.ic_home_nav_history_filled,
        testTag = TestTags.NAVIGATION_BAR_ITEM_HISTORY
    ),
    Settings(
        screen = Screen.SettingsScreen,
        title = R.string.home_nav_title_settings,
        inactiveIconId = R.drawable.ic_home_nav_settings_outlined,
        activeIconId = R.drawable.ic_home_nav_settings_filled,
        testTag = TestTags.NAVIGATION_BAR_ITEM_SETTINGS
    )
}

@Composable
fun CustomNavigationBar(
    currentRoute: String,
    modifier: Modifier = Modifier,
    alwaysShowLabel: Boolean = true,
    iconSize: Dp = 24.dp,
    onItemClick: (screen: Screen) -> Unit
) {
    NavigationBar(modifier = modifier.testTag(NAVIGATION_BAR)) {
        NavigationBarItems.entries.forEach { navigationBarItem ->
            val isSelected = currentRoute.contains(navigationBarItem.screen.toString())

            NavigationBarItem(
                enabled = !isSelected,
                selected = isSelected,
                alwaysShowLabel = alwaysShowLabel,
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = MaterialTheme.colorScheme.secondaryContainer,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    selectedIconColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    disabledIconColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    selectedTextColor = MaterialTheme.colorScheme.onSurface,
                    disabledTextColor = MaterialTheme.colorScheme.onSurface
                ),
                icon = {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.size(iconSize)
                    ) {
                        Icon(
                            painter = painterResource(id = if (isSelected) navigationBarItem.activeIconId else navigationBarItem.inactiveIconId),
                            contentDescription = stringResource(id = navigationBarItem.title)
                        )
                    }

                },
                label = {
                    Text(
                        text = stringResource(id = navigationBarItem.title),
                        style = TextStyle(
                            fontSize = 12.sp,
                            lineHeight = 16.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        )
                    )
                },
                onClick = {
                    onItemClick(navigationBarItem.screen)
                },
                modifier = Modifier.testTag(navigationBarItem.testTag)
            )
        }
    }
}