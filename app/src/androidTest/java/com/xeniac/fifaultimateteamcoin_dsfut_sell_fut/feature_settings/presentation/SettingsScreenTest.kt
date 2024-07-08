package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.presentation

import android.content.Context
import android.content.Intent
import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.isDialog
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onLast
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsRule
import androidx.test.rule.GrantPermissionRule
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.BuildConfig
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.di.AppModule
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.MainActivity
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.Constants.URL_PRIVACY_POLICY
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.TestTags.NAVIGATION_BAR_ITEM_SETTINGS
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.navigation.Screen
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.navigation.nav_graph.SetupRootNavGraph
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.theme.FutSaleTheme
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.presentation.util.Constants
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.presentation.util.TestTags
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
class SettingsScreenTest {

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

            onNodeWithTag(testTag = NAVIGATION_BAR_ITEM_SETTINGS).performClick()
        }
    }

    @Test
    fun launchingSettingsScreen_showsAppSettings() {
        composeTestRule.apply {
            onNodeWithText(context.getString(R.string.settings_text_settings_language)).apply {
                assertExists()
                assertIsDisplayed()
            }

            onNodeWithText(context.getString(R.string.settings_text_settings_theme)).apply {
                assertExists()
                assertIsDisplayed()
            }

            onNodeWithText(context.getString(R.string.settings_text_settings_notification_sound)).apply {
                assertExists()
                assertIsDisplayed()
            }

            onNodeWithText(context.getString(R.string.settings_text_settings_notification_vibrate)).apply {
                assertExists()
                assertIsDisplayed()
            }
        }
    }

    @Test
    fun clickOnLanguageBtn_showsSelectLanguageDialog() {
        composeTestRule.apply {
            onNodeWithText(context.getString(R.string.settings_text_settings_language)).apply {
                assertExists()
                assertIsDisplayed()
                performClick()
            }

            onAllNodes(isDialog()).onFirst().assertIsDisplayed()

            onAllNodesWithContentDescription(context.getString(R.string.settings_dialog_title_language)).apply {
                onLast().apply {
                    assertExists()
                    assertIsDisplayed()
                }
            }
        }
    }

    @Test
    fun clickOnThemeBtn_showsSelectThemeDialog() {
        composeTestRule.apply {
            onNodeWithText(context.getString(R.string.settings_text_settings_theme)).apply {
                assertExists()
                assertIsDisplayed()
                performClick()
            }

            onAllNodes(isDialog()).onFirst().assertIsDisplayed()

            onAllNodesWithContentDescription(context.getString(R.string.settings_dialog_title_theme)).apply {
                onLast().apply {
                    assertExists()
                    assertIsDisplayed()
                }
            }
        }
    }

    @Test
    fun clickOnNotificationSoundSwitch_NegatesTheNotificationSound() {
        composeTestRule.apply {
            onNodeWithTag(testTag = TestTags.NOTIFICATION_SOUND_SWITCH).apply {
                assertExists()
                assertIsDisplayed()
                assertIsOn()

                performClick()

                assertIsOff()
            }
        }
    }

    @Test
    fun clickOnNotificationVibrateSwitch_NegatesTheNotificationVibrate() {
        composeTestRule.apply {
            onNodeWithTag(testTag = TestTags.NOTIFICATION_VIBRATE_SWITCH).apply {
                assertExists()
                assertIsDisplayed()
                assertIsOn()

                performClick()

                assertIsOff()
            }
        }
    }

    @Test
    fun clickOnDonateBtn_opensDonateUrlInBrowser() {
        composeTestRule.apply {
            onNodeWithText(context.getString(R.string.settings_text_miscellaneous_donate)).apply {
                assertExists()
                performScrollTo()
                assertIsDisplayed()
                performClick()
            }
        }

        Intents.intended(
            CoreMatchers.allOf(
                IntentMatchers.hasAction(Intent.ACTION_VIEW),
                IntentMatchers.hasData(Constants.URL_DONATE)
            )
        )
    }

    @Test
    fun clickOnImproveTranslationsBtn_opensCrowdinUrlInBrowser() {
        composeTestRule.apply {
            onNodeWithText(context.getString(R.string.settings_text_miscellaneous_improve_translations)).apply {
                assertExists()
                performScrollTo()
                assertIsDisplayed()
                performClick()
            }
        }

        Intents.intended(
            CoreMatchers.allOf(
                IntentMatchers.hasAction(Intent.ACTION_VIEW),
                IntentMatchers.hasData(Constants.URL_CROWDIN)
            )
        )
    }

    @Test
    fun clickOnRateUsBtn_opensAppStoreUrlInBrowser() {
        composeTestRule.apply {
            onNodeWithText(context.getString(R.string.settings_text_miscellaneous_rate_us)).apply {
                assertExists()
                performScrollTo()
                assertIsDisplayed()
                performClick()
            }
        }

        Intents.intended(
            CoreMatchers.allOf(
                IntentMatchers.hasAction(Intent.ACTION_VIEW),
                IntentMatchers.hasData(BuildConfig.URL_APP_STORE)
            )
        )
    }

    @Test
    fun clickOnPrivacyPolicyBtn_opensPrivacyPolicyUrlInBrowser() {
        composeTestRule.apply {
            onNodeWithText(context.getString(R.string.settings_text_miscellaneous_privacy_policy)).apply {
                assertExists()
                performScrollTo()
                assertIsDisplayed()
                performClick()
            }
        }

        Intents.intended(
            CoreMatchers.allOf(
                IntentMatchers.hasAction(Intent.ACTION_VIEW),
                IntentMatchers.hasData(URL_PRIVACY_POLICY)
            )
        )
    }
}