package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.presentation.picked_up_player_info

import android.content.Context
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.rule.GrantPermissionRule
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.Platform
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.Player
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.MainActivity
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.TestTags
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.navigation.PickedUpPlayerInfoScreen
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.navigation.nav_graph.SetupHomeNavGraph
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.theme.FutSaleTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
@HiltAndroidTest
class PickedUpPlayerInfoScreenTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(/* testInstance = */ this)

    @get:Rule(order = 1)
    var permissionRule: GrantPermissionRule = GrantPermissionRule.grant(
        android.Manifest.permission.POST_NOTIFICATIONS
    )

    @get:Rule(order = 2)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private val context: Context = ApplicationProvider.getApplicationContext()

    private val testPlayer = Player(
        id = 1,
        tradeID = 1,
        assetID = 1,
        resourceID = 1,
        transactionID = 1,
        name = "Test Player",
        rating = "88",
        position = "ST",
        startPrice = "50000",
        buyNowPrice = "200000",
        owners = "5",
        contracts = "4",
        chemistryStyle = "Basic",
        chemistryStyleID = 1,
        platform = Platform.PC,
        pickUpTimeInMs = 1720521991000, // Jul 9, 2024 - 10:45:00
        expiryTimeInMs = 1720522080000 // Jul 9, 2024 - 10:48:00
    )

    @Before
    fun setUp() {
        hiltRule.inject()

        composeTestRule.activity.setContent {
            FutSaleTheme {
                val testNavController = rememberNavController()

                SetupHomeNavGraph(
                    homeNavController = testNavController,
                    bottomPadding = 0.dp
                )

                LaunchedEffect(key1 = Unit) {
                    testNavController.navigate(
                        PickedUpPlayerInfoScreen(playerId = testPlayer.id!!)
                    )
                }
            }
        }
    }

    @Test
    fun navigatingToPickedUpPlayerInfoScreen_showsPlayerInfoAndInstruction() {
        composeTestRule.apply {
            onNodeWithText(testPlayer.name).apply {
                assertExists()
                assertIsDisplayed()
            }

            onNodeWithText(
                context.getString(
                    R.string.picked_up_player_info_message,
                    testPlayer.name
                )
            ).apply {
                assertExists()
                performScrollTo()
                assertIsDisplayed()
            }

            onNodeWithText(testPlayer.rating).apply {
                assertExists()
                performScrollTo()
                assertIsDisplayed()
            }

            onNodeWithText(testPlayer.position).apply {
                assertExists()
                performScrollTo()
                assertIsDisplayed()
            }

            onNodeWithText(testPlayer.chemistryStyle).apply {
                assertExists()
                performScrollTo()
                assertIsDisplayed()
            }

            onNodeWithText(context.getString(R.string.picked_up_player_info_instruction_title)).apply {
                assertExists()
                performScrollTo()
                assertIsDisplayed()
            }
        }
    }

    @Test
    fun clickOnBackBtn_navigatesToPickUpPlayerScreen() {
        composeTestRule.apply {
            onNodeWithContentDescription(context.getString(R.string.core_content_description_back)).apply {
                assertExists()
                assertIsDisplayed()
                performClick()
            }

            onNodeWithTag(testTag = TestTags.TEST_TAG_SCREEN_PICK_UP_PLAYER).assertIsDisplayed()
        }
    }

    @Test
    fun pressBack_navigatesToPickUpPlayerScreen() {
        Espresso.pressBackUnconditionally()

        composeTestRule.onNodeWithTag(
            testTag = TestTags.TEST_TAG_SCREEN_PICK_UP_PLAYER
        ).assertIsDisplayed()
    }
}