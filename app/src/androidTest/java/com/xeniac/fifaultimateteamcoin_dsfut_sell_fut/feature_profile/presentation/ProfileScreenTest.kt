package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.presentation

import android.content.Context
import android.content.Intent
import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
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
import androidx.test.rule.GrantPermissionRule
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.di.AppModule
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.MainActivity
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.navigation.Screen
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.navigation.nav_graph.SetupRootNavGraph
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.theme.FutSaleTheme
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.presentation.util.TestTags.NAVIGATION_BAR_ITEM_PROFILE
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.presentation.utils.Constants
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
            onNodeWithText(context.getString(R.string.profile_textfield_hint_partner_id)).apply {
                assertExists()
                assertIsDisplayed()
            }

            onNodeWithText(context.getString(R.string.profile_textfield_hint_secret_key)).apply {
                assertExists()
                assertIsDisplayed()
            }
        }
    }

    @Test
    fun editPartnerId_showsTheUpdatedPartnerId() {
        composeTestRule.apply {
            onNodeWithText(context.getString(R.string.profile_textfield_hint_partner_id)).apply {
                assertExists()
                performTextInput(text = "123")
                assertIsNotDisplayed()
            }

            onNodeWithText(text = "123").apply {
                assertExists()
                assertIsDisplayed()
            }
        }
    }

    @Test
    fun editSecretKey_showsTheUpdatedSecretKey() {
        composeTestRule.apply {
            onNodeWithText(context.getString(R.string.profile_textfield_hint_secret_key)).apply {
                assertExists()
                performTextInput(text = "abc123")
                assertIsNotDisplayed()
            }

            onNodeWithText(text = "abc123").apply {
                assertExists()
                assertIsDisplayed()
            }
        }
    }

    @Test
    fun clickOnWalletButton_opensWalletUrlInBrowser() {
        Intents.init()

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

        Intents.release()
    }

    @Test
    fun clickOnPurchasedPlayersButton_opensPurchasedPlayersUrlInBrowser() {
        Intents.init()

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

        Intents.release()
    }

    @Test
    fun clickOnRulesButton_opensRulesUrlInBrowser() {
        Intents.init()

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

        Intents.release()
    }

    @Test
    fun clickOnStatisticsButton_opensStatisticsUrlInBrowser() {
        Intents.init()

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

        Intents.release()
    }

    @Test
    fun clickOnDsfutNewsButton_opensDsfutNewsUrlInBrowser() {
        Intents.init()

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

        Intents.release()
    }

    @Test
    fun clickOnConsoleNotificationsButton_opensConsoleNotificationsUrlInBrowser() {
        Intents.init()

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

        Intents.release()
    }

    @Test
    fun clickOnPcNotificationsButton_opensPcNotificationsUrlInBrowser() {
        Intents.init()

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

        Intents.release()
    }

    @Test
    fun clickOnDsfutWebsiteBtn_opensDsfutUrlInBrowser() {
        Intents.init()

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

        Intents.release()
    }
}