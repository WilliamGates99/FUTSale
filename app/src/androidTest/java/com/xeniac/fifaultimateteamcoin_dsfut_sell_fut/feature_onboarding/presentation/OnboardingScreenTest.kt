package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_onboarding.presentation

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
import androidx.compose.ui.test.performScrollToIndex
import androidx.compose.ui.test.performTextReplacement
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeLeft
import androidx.compose.ui.test.swipeRight
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsRule
import com.google.common.truth.Truth.assertThat
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.main_activity.MainActivity
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.Constants
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.EspressoUtils
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.navigation.HomeScreen
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.navigation.OnboardingScreen
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.navigation.nav_graph.SetupRootNavGraph
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.theme.FutSaleTheme
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_onboarding.presentation.utils.TestTags
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.CoreMatchers
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
@HiltAndroidTest
class OnboardingScreenTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(/* testInstance = */ this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @get:Rule(order = 2)
    val intentsTestRule = IntentsRule()

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
                    startDestination = OnboardingScreen
                )
            }
        }
    }

    @Test
    fun launchingOnboardingScreen_showsOnboardingDotIndicatorAndPagerAndPageOne() {
        composeTestRule.apply {
            onNodeWithTag(testTag = TestTags.DOT_INDICATOR).apply {
                assertExists()
                assertIsDisplayed()
            }

            onNodeWithTag(testTag = TestTags.HORIZONTAL_PAGER).apply {
                assertExists()
                assertIsDisplayed()
            }

            onNodeWithText(text = context.getString(R.string.onboarding_first_title)).apply {
                assertExists()
                assertIsDisplayed()
            }
        }
    }

    @Test
    fun pressBackInPickUpPlayerScreen_closesTheApp() {
        Espresso.pressBackUnconditionally()
        assertThat(composeTestRule.activityRule.scenario.state).isEqualTo(Lifecycle.State.DESTROYED)
    }

    @Test
    fun swipeLeftOnceWhileOnPageOne_showsPageTwo() {
        composeTestRule.apply {
            onNodeWithTag(testTag = TestTags.HORIZONTAL_PAGER).performTouchInput {
                swipeLeft()
            }

            EspressoUtils.solveIdlingResourceTimeout()

            onNodeWithText(text = context.getString(R.string.onboarding_second_title)).apply {
                assertExists()
                assertIsDisplayed()
            }
        }
    }

    @Test
    fun swipeLeftTwiceWhileOnPageOne_showsPageThree() {
        composeTestRule.apply {
            onNodeWithTag(testTag = TestTags.HORIZONTAL_PAGER).performTouchInput {
                swipeLeft()
                swipeLeft()
            }

            EspressoUtils.solveIdlingResourceTimeout()

            onNodeWithText(text = context.getString(R.string.onboarding_third_title)).apply {
                assertExists()
                assertIsDisplayed()
            }
        }
    }

    @Test
    fun swipeLeftThreeTimesWhileOnPageOne_showsPageFour() {
        composeTestRule.apply {
            onNodeWithTag(testTag = TestTags.HORIZONTAL_PAGER).performTouchInput {
                swipeLeft()
                swipeLeft()
                swipeLeft()
            }

            EspressoUtils.solveIdlingResourceTimeout()

            onNodeWithText(text = context.getString(R.string.onboarding_fourth_title)).apply {
                assertExists()
                assertIsDisplayed()
            }
        }
    }

    @Test
    fun swipeRightOnceWhileOnPageFour_showsPageThree() {
        composeTestRule.apply {
            onNodeWithTag(testTag = TestTags.HORIZONTAL_PAGER).apply {
                performScrollToIndex(index = 3)

                EspressoUtils.solveIdlingResourceTimeout()

                performTouchInput {
                    swipeRight()
                }
            }

            EspressoUtils.solveIdlingResourceTimeout()

            onNodeWithText(text = context.getString(R.string.onboarding_third_title)).apply {
                assertExists()
                assertIsDisplayed()
            }
        }
    }

    @Test
    fun swipeRightTwiceWhileOnPageFour_showsPageTwo() {
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

            onNodeWithText(text = context.getString(R.string.onboarding_second_title)).apply {
                assertExists()
                assertIsDisplayed()
            }
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

            onNodeWithText(text = context.getString(R.string.onboarding_first_title)).apply {
                assertExists()
                assertIsDisplayed()
            }
        }
    }

    @Test
    fun clickOnPageOneSkipBtn_showsPageFour() {
        composeTestRule.apply {
            onNodeWithText(text = context.getString(R.string.onboarding_first_btn_skip)).apply {
                assertExists()
                assertIsDisplayed()
                performClick()
            }

            EspressoUtils.solveIdlingResourceTimeout()
            mainClock.advanceTimeBy(milliseconds = 500) // Advance clock due to animation delay

            onNodeWithText(text = context.getString(R.string.onboarding_fourth_title)).apply {
                assertExists()
                assertIsDisplayed()
            }
        }
    }

    @Test
    fun clickOnPageOneNextBtn_showsPageTwo() {
        composeTestRule.apply {
            onNodeWithTag(testTag = TestTags.PAGE_ONE_NEXT_BTN).apply {
                assertExists()
                assertIsDisplayed()
                performClick()
            }

            EspressoUtils.solveIdlingResourceTimeout()
            mainClock.advanceTimeBy(milliseconds = 500) // Advance clock due to animation delay

            onNodeWithText(text = context.getString(R.string.onboarding_second_title)).apply {
                assertExists()
                assertIsDisplayed()
            }
        }
    }

    @Test
    fun clickOnPageTwoBackBtn_showsPageOne() {
        composeTestRule.apply {
            onNodeWithTag(testTag = TestTags.HORIZONTAL_PAGER).performScrollToIndex(index = 1)

            EspressoUtils.solveIdlingResourceTimeout()

            onNodeWithTag(testTag = TestTags.PAGE_TWO_BACK_BTN).apply {
                assertExists()
                assertIsDisplayed()
                performClick()
            }

            EspressoUtils.solveIdlingResourceTimeout()
            mainClock.advanceTimeBy(milliseconds = 500) // Advance clock due to animation delay

            onNodeWithText(text = context.getString(R.string.onboarding_first_title)).apply {
                assertExists()
                assertIsDisplayed()
            }
        }
    }

    @Test
    fun clickOnPageTwoNextBtn_showsPageThree() {
        composeTestRule.apply {
            onNodeWithTag(testTag = TestTags.HORIZONTAL_PAGER).performScrollToIndex(index = 1)

            EspressoUtils.solveIdlingResourceTimeout()

            onNodeWithTag(testTag = TestTags.PAGE_TWO_NEXT_BTN).apply {
                assertExists()
                assertIsDisplayed()
                performClick()
            }

            EspressoUtils.solveIdlingResourceTimeout()
            mainClock.advanceTimeBy(milliseconds = 500) // Advance clock due to animation delay

            onNodeWithText(text = context.getString(R.string.onboarding_third_title)).apply {
                assertExists()
                assertIsDisplayed()
            }
        }
    }

    @Test
    fun clickOnPageThreeBackBtn_showsPageTwo() {
        composeTestRule.apply {
            onNodeWithTag(testTag = TestTags.HORIZONTAL_PAGER).performScrollToIndex(index = 2)

            EspressoUtils.solveIdlingResourceTimeout()

            onNodeWithTag(testTag = TestTags.PAGE_THREE_BACK_BTN).apply {
                assertExists()
                assertIsDisplayed()
                performClick()
            }

            EspressoUtils.solveIdlingResourceTimeout()
            mainClock.advanceTimeBy(milliseconds = 500) // Advance clock due to animation delay

            onNodeWithText(text = context.getString(R.string.onboarding_second_title)).apply {
                assertExists()
                assertIsDisplayed()
            }
        }
    }

    @Test
    fun clickOnPageThreeNextBtn_showsPageFour() {
        composeTestRule.apply {
            onNodeWithTag(testTag = TestTags.HORIZONTAL_PAGER).performScrollToIndex(index = 2)

            EspressoUtils.solveIdlingResourceTimeout()

            onNodeWithTag(testTag = TestTags.PAGE_THREE_NEXT_BTN).apply {
                assertExists()
                assertIsDisplayed()
                performClick()
            }

            EspressoUtils.solveIdlingResourceTimeout()
            mainClock.advanceTimeBy(milliseconds = 500) // Advance clock due to animation delay

            onNodeWithText(text = context.getString(R.string.onboarding_fourth_title)).apply {
                assertExists()
                assertIsDisplayed()
            }
        }
    }

    @Test
    fun clickOnPageFourStartBtn_navigatesToHomeScreen() {
        composeTestRule.apply {
            onNodeWithTag(testTag = TestTags.HORIZONTAL_PAGER).performScrollToIndex(index = 3)

            EspressoUtils.solveIdlingResourceTimeout()

            onNodeWithText(text = context.getString(R.string.onboarding_fourth_btn_start)).apply {
                assertExists()
                performScrollTo()
                assertIsDisplayed()
                performClick()
            }
        }

        val backStackEntry = testNavController.currentBackStackEntry
        val isNavigatedToHomeScreen = backStackEntry?.destination?.hierarchy?.any {
            it.hasRoute(HomeScreen::class)
        } ?: false
        assertThat(isNavigatedToHomeScreen).isTrue()
    }

    @Test
    fun editPartnerIdOnPageFour_showsTheUpdatedPartnerId() {
        val testPartnerId = "123"

        composeTestRule.apply {
            onNodeWithTag(testTag = TestTags.HORIZONTAL_PAGER).performScrollToIndex(index = 3)

            EspressoUtils.solveIdlingResourceTimeout()

            onNodeWithTag(testTag = TestTags.PARTNER_ID_TEXT_FIELD).apply {
                assertExists()
                assertIsDisplayed()
                performTextReplacement(text = testPartnerId)

                mainClock.advanceTimeBy(milliseconds = 500) // Advance clock due to typing delay

                assertTextEquals(testPartnerId)
            }
        }
    }

    @Test
    fun editSecretKeyOnPageFour_showsTheUpdatedSecretKey() {
        val testSecretKey = "abc123"

        composeTestRule.apply {
            onNodeWithTag(testTag = TestTags.HORIZONTAL_PAGER).performScrollToIndex(index = 3)

            EspressoUtils.solveIdlingResourceTimeout()

            onNodeWithTag(testTag = TestTags.SECRET_KEY_TEXT_FIELD).apply {
                assertExists()
                assertIsDisplayed()
                performTextReplacement(text = testSecretKey)

                mainClock.advanceTimeBy(milliseconds = 500) // Advance clock due to typing delay

                assertTextEquals(testSecretKey)
            }
        }
    }

    @Test
    fun clickOnRegisterBtnOnPageFour_opensDsfutUrlInBrowser() {
        composeTestRule.apply {
            onNodeWithTag(testTag = TestTags.HORIZONTAL_PAGER).performScrollToIndex(index = 3)

            EspressoUtils.solveIdlingResourceTimeout()

            onNodeWithTag(testTag = TestTags.PAGE_FOUR_REGISTER_BTN).performClick()
        }

        Intents.intended(
            CoreMatchers.allOf(
                IntentMatchers.hasAction(Intent.ACTION_VIEW),
                IntentMatchers.hasData(Constants.URL_DSFUT)
            )
        )
    }

    @Test
    fun clickOnAgreementBtnOnPageFour_opensPrivacyPolicyUrlInBrowser() {
        composeTestRule.apply {
            onNodeWithTag(testTag = TestTags.HORIZONTAL_PAGER).performScrollToIndex(index = 3)

            EspressoUtils.solveIdlingResourceTimeout()

            onNodeWithTag(testTag = TestTags.PAGE_FOUR_AGREEMENT_BTN).performClick()
        }

        Intents.intended(
            CoreMatchers.allOf(
                IntentMatchers.hasAction(Intent.ACTION_VIEW),
                IntentMatchers.hasData(Constants.URL_PRIVACY_POLICY)
            )
        )
    }
}