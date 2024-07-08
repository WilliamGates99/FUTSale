package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.presentation

import android.content.Context
import android.content.Intent
import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextInput
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsRule
import androidx.test.rule.GrantPermissionRule
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.di.AppModule
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.MainActivity
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.TestTags.NAVIGATION_BAR_ITEM_PROFILE
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.navigation.Screen
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.navigation.nav_graph.SetupRootNavGraph
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.theme.FutSaleTheme
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.presentation.utils.Constants
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.presentation.utils.TestTags
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.CoreMatchers
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
@HiltAndroidTest
@UninstallModules(AppModule::class)
class ProfileScreenTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(/* testInstance = */ this)

    @get:Rule(order = 1)
    var permissionRule: GrantPermissionRule = GrantPermissionRule.grant(
        android.Manifest.permission.POST_NOTIFICATIONS
    )

    @get:Rule(order = 2)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @get:Rule(order = 3)
    val intentsTestRule = IntentsRule()

    private val context: Context = ApplicationProvider.getApplicationContext()

    private lateinit var testNavController: NavHostController

    @Before
    fun setUp() {
        hiltRule.inject()

        composeTestRule.apply {
            activity.setContent {
                FutSaleTheme {
                    testNavController = rememberNavController()

                    SetupRootNavGraph(
                        rootNavController = testNavController,
                        startDestination = Screen.HomeScreen
                    )
                }
            }

            onNodeWithTag(testTag = NAVIGATION_BAR_ITEM_PROFILE).performClick()
        }
    }

    @Test
    fun launchingProfileScreen_showsPartnerIdAndSecretKeyTextFields() {
        composeTestRule.apply {
            onNodeWithTag(testTag = TestTags.PARTNER_ID_TEXT_FIELD).apply {
                assertExists()
                assertIsDisplayed()
            }

            onNodeWithTag(testTag = TestTags.SECRET_KEY_TEXT_FIELD).apply {
                assertExists()
                assertIsDisplayed()
            }
        }
    }

    @Test
    fun editPartnerId_showsTheUpdatedPartnerId() {
        composeTestRule.apply {
            onNodeWithTag(testTag = TestTags.PARTNER_ID_TEXT_FIELD).apply {
                assertExists()
                performTextInput(text = "123")
                assertTextEquals("123")
            }
        }
    }

    @Test
    fun editSecretKey_showsTheUpdatedSecretKey() {
        composeTestRule.apply {
            onNodeWithTag(testTag = TestTags.SECRET_KEY_TEXT_FIELD).apply {
                assertExists()
                performTextInput(text = "abc123")
                assertTextEquals("abc123")
            }
        }
    }

    @Test
    fun clickOnWalletBtn_opensWalletUrlInBrowser() {
        composeTestRule.apply {
            onNodeWithText(context.getString(R.string.profile_text_account_links_wallet)).apply {
                assertExists()
                performScrollTo()
                assertIsDisplayed()
                performClick()
            }
        }

        Intents.intended(
            CoreMatchers.allOf(
                IntentMatchers.hasAction(Intent.ACTION_VIEW),
                IntentMatchers.hasData(Constants.URL_ACCOUNT_WALLET)
            )
        )
    }

    @Test
    fun clickOnPurchasedPlayersBtn_opensPurchasedPlayersUrlInBrowser() {
        composeTestRule.apply {
            onNodeWithText(context.getString(R.string.profile_text_account_links_purchased_players)).apply {
                assertExists()
                performScrollTo()
                assertIsDisplayed()
                performClick()
            }
        }

        Intents.intended(
            CoreMatchers.allOf(
                IntentMatchers.hasAction(Intent.ACTION_VIEW),
                IntentMatchers.hasData(Constants.URL_ACCOUNT_PURCHASED_PLAYERS)
            )
        )
    }

    @Test
    fun clickOnRulesBtn_opensRulesUrlInBrowser() {
        composeTestRule.apply {
            onNodeWithText(context.getString(R.string.profile_text_account_links_rules)).apply {
                assertExists()
                performScrollTo()
                assertIsDisplayed()
                performClick()
            }
        }

        Intents.intended(
            CoreMatchers.allOf(
                IntentMatchers.hasAction(Intent.ACTION_VIEW),
                IntentMatchers.hasData(Constants.URL_ACCOUNT_RULES)
            )
        )
    }

    @Test
    fun clickOnStatisticsBtn_opensStatisticsUrlInBrowser() {
        composeTestRule.apply {
            onNodeWithText(context.getString(R.string.profile_text_account_links_statistics)).apply {
                assertExists()
                performScrollTo()
                assertIsDisplayed()
                performClick()
            }
        }

        Intents.intended(
            CoreMatchers.allOf(
                IntentMatchers.hasAction(Intent.ACTION_VIEW),
                IntentMatchers.hasData(Constants.URL_ACCOUNT_STATISTICS)
            )
        )
    }

    @Test
    fun clickOnDsfutNewsBtn_opensDsfutNewsUrlInBrowser() {
        composeTestRule.apply {
            onNodeWithText(context.getString(R.string.profile_text_other_links_dsfut_news)).apply {
                assertExists()
                performScrollTo()
                assertIsDisplayed()
                performClick()
            }
        }

        Intents.intended(
            CoreMatchers.allOf(
                IntentMatchers.hasAction(Intent.ACTION_VIEW),
                IntentMatchers.hasData(Constants.URL_DSFUT_DSFUT_NEWS)
            )
        )
    }

    @Test
    fun clickOnConsoleNotificationsBtn_opensConsoleNotificationsUrlInBrowser() {
        composeTestRule.apply {
            onNodeWithText(context.getString(R.string.profile_text_other_links_console_notifications)).apply {
                assertExists()
                performScrollTo()
                assertIsDisplayed()
                performClick()
            }
        }

        Intents.intended(
            CoreMatchers.allOf(
                IntentMatchers.hasAction(Intent.ACTION_VIEW),
                IntentMatchers.hasData(Constants.URL_DSFUT_CONSOLE_NOTIFICATIONS)
            )
        )
    }

    @Test
    fun clickOnPcNotificationsBtn_opensPcNotificationsUrlInBrowser() {
        composeTestRule.apply {
            onNodeWithText(context.getString(R.string.profile_text_other_links_pc_notifications)).apply {
                assertExists()
                performScrollTo()
                assertIsDisplayed()
                performClick()
            }
        }

        Intents.intended(
            CoreMatchers.allOf(
                IntentMatchers.hasAction(Intent.ACTION_VIEW),
                IntentMatchers.hasData(Constants.URL_DSFUT_PC_NOTIFICATIONS)
            )
        )
    }

    @Test
    fun clickOnDsfutWebsiteBtn_opensDsfutUrlInBrowser() {
        composeTestRule.apply {
            onNodeWithText(context.getString(R.string.profile_text_other_links_dsfut_website)).apply {
                assertExists()
                performScrollTo()
                assertIsDisplayed()
                performClick()
            }
        }

        Intents.intended(
            CoreMatchers.allOf(
                IntentMatchers.hasAction(Intent.ACTION_VIEW),
                IntentMatchers.hasData(Constants.URL_DSFUT_DSFUT_WEBSITE)
            )
        )
    }
}