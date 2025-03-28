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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.ui.navigation.HistoryScreen
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.ui.navigation.PickUpPlayerScreen
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.ui.navigation.ProfileScreen
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.ui.navigation.SettingsScreen
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.TestTags
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.presentation.util.TestTags.NAVIGATION_BAR

enum class NavigationBarItems(
    val screen: Any,
    @StringRes val title: Int,
    @DrawableRes val inactiveIconId: Int,
    @DrawableRes val activeIconId: Int,
    val testTag: String
) {
    PickUpPlayer(
        screen = PickUpPlayerScreen,
        title = R.string.home_nav_title_pick_up_player,
        inactiveIconId = R.drawable.ic_home_nav_pick_up_player_outlined,
        activeIconId = R.drawable.ic_home_nav_pick_up_player_filled,
        testTag = TestTags.NAVIGATION_BAR_ITEM_PICK_UP_PLAYER
    ),
    Profile(
        screen = ProfileScreen,
        title = R.string.home_nav_title_profile,
        inactiveIconId = R.drawable.ic_home_nav_profile_outlined,
        activeIconId = R.drawable.ic_home_nav_profile_filled,
        testTag = TestTags.NAVIGATION_BAR_ITEM_PROFILE
    ),
    History(
        screen = HistoryScreen,
        title = R.string.home_nav_title_history,
        inactiveIconId = R.drawable.ic_home_nav_history_outlined,
        activeIconId = R.drawable.ic_home_nav_history_filled,
        testTag = TestTags.NAVIGATION_BAR_ITEM_HISTORY
    ),
    Settings(
        screen = SettingsScreen,
        title = R.string.home_nav_title_settings,
        inactiveIconId = R.drawable.ic_home_nav_settings_outlined,
        activeIconId = R.drawable.ic_home_nav_settings_filled,
        testTag = TestTags.NAVIGATION_BAR_ITEM_SETTINGS
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CustomNavigationBar(
    currentDestination: NavDestination?,
    modifier: Modifier = Modifier,
    alwaysShowLabel: Boolean = true,
    iconSize: Dp = 24.dp,
    onItemClick: (screen: Any) -> Unit
) {
    NavigationBar(
        modifier = modifier
            .testTag(NAVIGATION_BAR)
            .semantics {
                testTagsAsResourceId = true
            }
    ) {
        NavigationBarItems.entries.forEach { navItem ->
            val isSelected = currentDestination?.hierarchy?.any {
                it.hasRoute(route = navItem.screen::class)
            } ?: false

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
                            painter = painterResource(
                                id = if (isSelected) navItem.activeIconId else navItem.inactiveIconId
                            ),
                            contentDescription = stringResource(id = navItem.title)
                        )
                    }

                },
                label = {
                    Text(
                        text = stringResource(id = navItem.title),
                        fontSize = 12.sp,
                        lineHeight = 16.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                    )
                },
                onClick = {
                    onItemClick(navItem.screen)
                },
                modifier = Modifier.testTag(navItem.testTag)
            )
        }
    }
}