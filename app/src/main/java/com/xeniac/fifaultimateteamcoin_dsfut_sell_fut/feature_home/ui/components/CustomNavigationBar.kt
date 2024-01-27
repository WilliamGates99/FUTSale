package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.ui.components

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.navigation.Screen

enum class NavigationBarItems(
    val route: String,
    @StringRes val title: Int,
    @DrawableRes val inactiveIconId: Int,
    @DrawableRes val activeIconId: Int
) {
    PickUp(
        route = Screen.PickUpScreen.route,
        title = R.string.home_nav_title_pick_up,
        inactiveIconId = R.drawable.ic_home_nav_pick_up_outlined,
        activeIconId = R.drawable.ic_home_nav_pick_up_filled
    ),
    Profile(
        route = Screen.ProfileScreen.route,
        title = R.string.home_nav_title_profile,
        inactiveIconId = R.drawable.ic_home_nav_profile_outlined,
        activeIconId = R.drawable.ic_home_nav_profile_filled
    ),
    History(
        route = Screen.HistoryScreen.route,
        title = R.string.home_nav_title_history,
        inactiveIconId = R.drawable.ic_home_nav_history_outlined,
        activeIconId = R.drawable.ic_home_nav_history_filled
    ),
    Settings(
        route = Screen.SettingsScreen.route,
        title = R.string.home_nav_title_settings,
        inactiveIconId = R.drawable.ic_home_nav_settings_outlined,
        activeIconId = R.drawable.ic_home_nav_settings_filled
    )
}

@Composable
fun CustomNavigationBar(
    currentRoute: String,
    modifier: Modifier = Modifier,
    alwaysShowLabel: Boolean = true,
    iconSize: Dp = 24.dp,
    onItemClick: (route: String) -> Unit
) {
    NavigationBar(modifier = modifier) {
        NavigationBarItems.entries.forEach { navigationBarItem ->
            val isSelected = currentRoute == navigationBarItem.route

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
                    onItemClick(navigationBarItem.route)
                }
            )
        }
    }
}