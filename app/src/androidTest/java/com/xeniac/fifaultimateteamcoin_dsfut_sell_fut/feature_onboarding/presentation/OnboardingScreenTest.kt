package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_onboarding.presentation

import android.content.Context
import android.content.Intent
import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeLeft
import androidx.compose.ui.test.swipeRight
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import com.google.common.truth.Truth.assertThat
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.di.AppModule
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.MainActivity
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.Constants
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.EspressoUtils
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.navigation.Screen
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.navigation.nav_graph.SetupRootNavGraph
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.theme.FutSaleTheme
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_onboarding.presentation.utils.TestTags
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
class OnboardingScreenTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(/* testInstance = */ this)

    @get:Rule(order = 1)
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
                    startDestination = Screen.OnboardingScreen
                )
            }
        }
    }

    @Test
    fun launchingOnboardingScreen_showsOnboardingDotIndicatorAndPagerAndPageOne() {
        composeTestRule.apply {
            onNodeWithTag(testTag = TestTags.DOT_INDICATOR).assertExists()
            onNodeWithTag(testTag = TestTags.HORIZONTAL_PAGER).assertExists()

            onNodeWithText(
                text = context.getString(R.string.onboarding_first_title)
            ).assertIsDisplayed()
        }
    }

    @Test
    fun swipeLeftOnceWhileOnPageOne_showsPageTwo() {
        composeTestRule.apply {
            onNodeWithTag(testTag = TestTags.HORIZONTAL_PAGER).performTouchInput {
                swipeLeft()
            }

            EspressoUtils.solveIdlingResourceTimeout()

            onNodeWithText(
                text = context.getString(R.string.onboarding_second_title)
            ).assertIsDisplayed()
        }
    }

    @Test
    fun swipeLeftTwiceWhileOnPageOne_showsPageTwo() {
        composeTestRule.apply {
            onNodeWithTag(testTag = TestTags.HORIZONTAL_PAGER).performTouchInput {
                swipeLeft()
                swipeLeft()
            }

            EspressoUtils.solveIdlingResourceTimeout()

            onNodeWithText(
                text = context.getString(R.string.onboarding_third_title)
            ).assertIsDisplayed()
        }
    }

    @Test
    fun swipeLeftThreeTimesWhileOnPageOne_showsPageThree() {
        composeTestRule.apply {
            onNodeWithTag(testTag = TestTags.HORIZONTAL_PAGER).performTouchInput {
                swipeLeft()
                swipeLeft()
                swipeLeft()
            }

            EspressoUtils.solveIdlingResourceTimeout()

            onNodeWithText(
                text = context.getString(R.string.onboarding_fourth_title)
            ).assertIsDisplayed()
        }
    }

    @Test
    fun swipeRightOnceWhileOnPageFour_showsPageOne() {
        composeTestRule.apply {
            onNodeWithTag(testTag = TestTags.HORIZONTAL_PAGER).apply {
                performScrollToIndex(index = 3)

                EspressoUtils.solveIdlingResourceTimeout()

                performTouchInput {
                    swipeRight()
                }
            }

            EspressoUtils.solveIdlingResourceTimeout()

            onNodeWithText(
                text = context.getString(R.string.onboarding_first_title)
            ).assertIsDisplayed()
        }
    }

    @Test
    fun swipeRightTwiceWhileOnPageFour_showsPageOne() {
        composeTestRule.apply {
            onNodeWithTag(testTag = TestTags.HORIZONTAL_PAGER).apply {
                performScrollToIndex(index = 3)

                EspressoUtils.solveIdlingResourceTimeout()

                performTouchInput {
                    swipeRight()
                    swipeRight()
                }
            }

            EspressoUtils.solveIdlingResourceTimeout()

            onNodeWithText(
                text = context.getString(R.string.onboarding_second_title)
            ).assertIsDisplayed()
        }
    }

    @Test
    fun swipeRightThreeTimesWhileOnPageFour_showsPageOne() {
        composeTestRule.apply {
            onNodeWithTag(testTag = TestTags.HORIZONTAL_PAGER).apply {
                performScrollToIndex(index = 3)

                EspressoUtils.solveIdlingResourceTimeout()

                performTouchInput {
                    swipeRight()
                    swipeRight()
                    swipeRight()
                }
            }

            EspressoUtils.solveIdlingResourceTimeout()

            onNodeWithText(
                text = context.getString(R.string.onboarding_first_title)
            ).assertIsDisplayed()
        }
    }

    @Test
    fun clickOnPageOneSkipBtn_showsPageFour() {
        composeTestRule.apply {
            onNodeWithText(text = context.getString(R.string.onboarding_first_btn_skip)).apply {
                assertExists()
                performClick()
            }

            EspressoUtils.solveIdlingResourceTimeout()

            onNodeWithText(
                text = context.getString(R.string.onboarding_fourth_title)
            ).assertIsDisplayed()
        }
    }

    @Test
    fun clickOnPageOneNextBtn_showsPageTwo() {
        composeTestRule.apply {
            onNodeWithTag(testTag = TestTags.PAGE_ONE_NEXT_BTN).apply {
                assertExists()
                performClick()
            }

            EspressoUtils.solveIdlingResourceTimeout()

            onNodeWithText(
                text = context.getString(R.string.onboarding_second_title)
            ).assertIsDisplayed()
        }
    }

    @Test
    fun clickOnPageTwoBackBtn_showsPageOne() {
        composeTestRule.apply {
            onNodeWithTag(testTag = TestTags.HORIZONTAL_PAGER).performScrollToIndex(index = 1)

            EspressoUtils.solveIdlingResourceTimeout()

            onNodeWithTag(testTag = TestTags.PAGE_TWO_BACK_BTN).apply {
                assertExists()
                performClick()
            }

            EspressoUtils.solveIdlingResourceTimeout()

            onNodeWithText(
                text = context.getString(R.string.onboarding_first_title)
            ).assertIsDisplayed()
        }
    }

    @Test
    fun clickOnPageTwoNextBtn_showsPageThree() {
        composeTestRule.apply {
            onNodeWithTag(testTag = TestTags.HORIZONTAL_PAGER).performScrollToIndex(index = 1)

            EspressoUtils.solveIdlingResourceTimeout()

            onNodeWithTag(testTag = TestTags.PAGE_TWO_NEXT_BTN).apply {
                assertExists()
                performClick()
            }

            EspressoUtils.solveIdlingResourceTimeout()

            onNodeWithText(
                text = context.getString(R.string.onboarding_third_title)
            ).assertIsDisplayed()
        }
    }

    @Test
    fun clickOnPageThreeBackBtn_showsPageTwo() {
        composeTestRule.apply {
            onNodeWithTag(testTag = TestTags.HORIZONTAL_PAGER).performScrollToIndex(index = 2)

            EspressoUtils.solveIdlingResourceTimeout()

            onNodeWithTag(testTag = TestTags.PAGE_THREE_BACK_BTN).apply {
                assertExists()
                performClick()
            }

            EspressoUtils.solveIdlingResourceTimeout()

            onNodeWithText(
                text = context.getString(R.string.onboarding_second_title)
            ).assertIsDisplayed()
        }
    }

    @Test
    fun clickOnPageThreeNextBtn_showsPageTwo() {
        composeTestRule.apply {
            onNodeWithTag(testTag = TestTags.HORIZONTAL_PAGER).performScrollToIndex(index = 2)

            EspressoUtils.solveIdlingResourceTimeout()

            onNodeWithTag(testTag = TestTags.PAGE_THREE_NEXT_BTN).apply {
                assertExists()
                performClick()
            }

            EspressoUtils.solveIdlingResourceTimeout()

            onNodeWithText(
                text = context.getString(R.string.onboarding_fourth_title)
            ).assertIsDisplayed()
        }
    }

    @Test
    fun clickOnPageFourStartBtn_navigatesToHomeScreen() {
        composeTestRule.apply {
            onNodeWithTag(testTag = TestTags.HORIZONTAL_PAGER).performScrollToIndex(index = 3)

            EspressoUtils.solveIdlingResourceTimeout()

            onNodeWithText(
                text = context.getString(R.string.onboarding_fourth_btn_start)
            ).performClick()

            val backStackEntry = testNavController.currentBackStackEntry
            val currentRoute = backStackEntry?.destination?.route
            assertThat(currentRoute).isEqualTo(Screen.HomeScreen::class.qualifiedName)
        }
    }

    @Test
    fun clickRegisterBtnOnPageFour_opensDsfutUrlInBrowser() {
        Intents.init()

        composeTestRule.apply {
            onNodeWithTag(testTag = TestTags.HORIZONTAL_PAGER).performScrollToIndex(index = 3)

            EspressoUtils.solveIdlingResourceTimeout()

            onNodeWithTag(testTag = TestTags.PAGE_FOUR_REGISTER_BTN).performClick()

            EspressoUtils.solveIdlingResourceTimeout()
        }

        Intents.intended(
            CoreMatchers.allOf(
                IntentMatchers.hasAction(Intent.ACTION_VIEW),
                IntentMatchers.hasData(Constants.URL_DSFUT)
            )
        )

        Intents.release()
    }

    @Test
    fun clickAgreementBtnOnPageFour_opensPrivacyPolicyUrlInBrowser() {
        Intents.init()

        composeTestRule.apply {
            onNodeWithTag(testTag = TestTags.HORIZONTAL_PAGER).performScrollToIndex(index = 3)

            EspressoUtils.solveIdlingResourceTimeout()

            onNodeWithTag(testTag = TestTags.PAGE_FOUR_AGREEMENT_BTN).performClick()

            EspressoUtils.solveIdlingResourceTimeout()
        }

        Intents.intended(
            CoreMatchers.allOf(
                IntentMatchers.hasAction(Intent.ACTION_VIEW),
                IntentMatchers.hasData(Constants.URL_PRIVACY_POLICY)
            )
        )

        Intents.release()
    }
}