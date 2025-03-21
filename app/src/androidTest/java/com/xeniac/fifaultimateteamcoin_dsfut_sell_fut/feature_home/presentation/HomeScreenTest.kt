package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.presentation

import android.content.Context
import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.rule.GrantPermissionRule
import com.google.common.truth.Truth.assertThat
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.main_activity.MainActivity
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.TestTags
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.navigation.HomeScreen
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.navigation.nav_graph.SetupRootNavGraph
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.theme.FutSaleTheme
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.presentation.util.TestTags.NAVIGATION_BAR
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
@HiltAndroidTest
class HomeScreenTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(/* testInstance = */ this)

    @get:Rule(order = 1)
    var permissionRule: GrantPermissionRule = GrantPermissionRule.grant(
        android.Manifest.permission.POST_NOTIFICATIONS
    )

    @get:Rule(order = 2)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private val context: Context = ApplicationProvider.getApplicationContext()

    private lateinit var testNavController: NavHostController

    @Before
    fun setUp() {
        hiltRule.inject()

        composeTestRule.activity.setContent {
            FutSaleTheme {
                testNavController = rememberNavController()

                SetupRootNavGraph(
                    rootNavController = testNavController,
                    startDestination = HomeScreen
                )
            }
        }
    }

    @Test
    fun launchingHomeScreen_showsNavigationBarAndPickUpPlayerScreen() {
        composeTestRule.apply {
            onNodeWithTag(testTag = NAVIGATION_BAR).assertExists()

            onNodeWithText(
                text = context.getString(R.string.pick_up_player_app_bar_title)
            ).assertIsDisplayed()
        }
    }

    @Test
    fun clickOnPickUpPlayerNavigationBarItem_navigateToPickUpPlayerScreen() {
        composeTestRule.apply {
            onNodeWithTag(testTag = TestTags.NAVIGATION_BAR_ITEM_PICK_UP_PLAYER).apply {
                assertExists()
                performClick()
            }

            onNodeWithTag(testTag = TestTags.TEST_TAG_SCREEN_PICK_UP_PLAYER).assertIsDisplayed()
        }
    }

    @Test
    fun clickOnProfileNavigationBarItem_navigateToProfileScreen() {
        composeTestRule.apply {
            onNodeWithTag(testTag = TestTags.NAVIGATION_BAR_ITEM_PROFILE).apply {
                assertExists()
                performClick()
            }

            onNodeWithTag(testTag = TestTags.TEST_TAG_SCREEN_PROFILE).assertIsDisplayed()
        }
    }

    @Test
    fun clickOnHistoryNavigationBarItem_navigateToHistoryScreen() {
        composeTestRule.apply {
            onNodeWithTag(testTag = TestTags.NAVIGATION_BAR_ITEM_HISTORY).apply {
                assertExists()
                performClick()
            }

            onNodeWithTag(testTag = TestTags.TEST_TAG_SCREEN_HISTORY).assertIsDisplayed()
        }
    }

    @Test
    fun clickOnSettingsNavigationBarItem_navigateToSettingsScreen() {
        composeTestRule.apply {
            onNodeWithTag(testTag = TestTags.NAVIGATION_BAR_ITEM_SETTINGS).apply {
                assertExists()
                performClick()
            }

            onNodeWithTag(testTag = TestTags.TEST_TAG_SCREEN_SETTINGS).assertIsDisplayed()
        }
    }

    @Test
    fun pressBackInPickUpPlayerScreen_closesTheApp() {
        Espresso.pressBackUnconditionally()
        assertThat(composeTestRule.activityRule.scenario.state).isEqualTo(Lifecycle.State.DESTROYED)
    }

    @Test
    fun pressBackInProfileScreen_navigateToPickUpPlayerScreen() {
        composeTestRule.apply {
            onNodeWithTag(testTag = TestTags.NAVIGATION_BAR_ITEM_PROFILE).apply {
                assertExists()
                performClick()
            }

            Espresso.pressBackUnconditionally()

            onNodeWithTag(testTag = TestTags.TEST_TAG_SCREEN_PICK_UP_PLAYER).assertIsDisplayed()
        }
    }

    @Test
    fun pressBackInHistoryScreen_navigateToPickUpPlayerScreen() {
        composeTestRule.apply {
            onNodeWithTag(testTag = TestTags.NAVIGATION_BAR_ITEM_HISTORY).apply {
                assertExists()
                performClick()
            }

            Espresso.pressBackUnconditionally()

            onNodeWithTag(testTag = TestTags.TEST_TAG_SCREEN_PICK_UP_PLAYER).assertIsDisplayed()
        }
    }

    @Test
    fun pressBackInSettingsScreen_navigateToPickUpPlayerScreen() {
        composeTestRule.apply {
            onNodeWithTag(testTag = TestTags.NAVIGATION_BAR_ITEM_SETTINGS).apply {
                assertExists()
                performClick()
            }

            Espresso.pressBackUnconditionally()

            onNodeWithTag(testTag = TestTags.TEST_TAG_SCREEN_PICK_UP_PLAYER).assertIsDisplayed()
        }
    }

    @Test
    fun pressBackTwiceInProfileScreen_closesTheApp() {
        composeTestRule.apply {
            onNodeWithTag(testTag = TestTags.NAVIGATION_BAR_ITEM_PROFILE).apply {
                assertExists()
                performClick()
            }

            Espresso.pressBackUnconditionally()
            Espresso.pressBackUnconditionally()

            assertThat(activityRule.scenario.state).isEqualTo(Lifecycle.State.DESTROYED)
        }
    }

    @Test
    fun pressBackTwiceInHistoryScreen_closesTheApp() {
        composeTestRule.apply {
            onNodeWithTag(testTag = TestTags.NAVIGATION_BAR_ITEM_HISTORY).apply {
                assertExists()
                performClick()
            }

            Espresso.pressBackUnconditionally()
            Espresso.pressBackUnconditionally()

            assertThat(activityRule.scenario.state).isEqualTo(Lifecycle.State.DESTROYED)
        }
    }

    @Test
    fun pressBackTwiceISettingsScreen_closesTheApp() {
        composeTestRule.apply {
            onNodeWithTag(testTag = TestTags.NAVIGATION_BAR_ITEM_SETTINGS).apply {
                assertExists()
                performClick()
            }

            Espresso.pressBackUnconditionally()
            Espresso.pressBackUnconditionally()

            assertThat(activityRule.scenario.state).isEqualTo(Lifecycle.State.DESTROYED)
        }
    }
}