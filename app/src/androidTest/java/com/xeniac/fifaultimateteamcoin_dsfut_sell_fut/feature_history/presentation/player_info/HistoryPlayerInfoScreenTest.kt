package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_history.presentation.player_info

import android.content.Context
import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.rule.GrantPermissionRule
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.MainActivity
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.TestTags.TEST_TAG_SCREEN_HISTORY
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.navigation.HistoryPlayerInfoScreen
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.navigation.HistoryScreen
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.theme.FutSaleTheme
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_history.data.repositories.FakeHistoryRepositoryImpl
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_history.domain.use_cases.ObservePickedPlayersHistoryUseCase
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_history.domain.use_cases.ObservePlayerUseCase
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_history.presentation.history.HistoryScreen
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_history.presentation.history.HistoryViewModel
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_history.presentation.player_info.utils.TestTags
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
@HiltAndroidTest
class HistoryPlayerInfoScreenTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(/* testInstance = */ this)

    @get:Rule(order = 1)
    var permissionRule: GrantPermissionRule = GrantPermissionRule.grant(
        android.Manifest.permission.POST_NOTIFICATIONS
    )

    @get:Rule(order = 2)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private val context: Context = ApplicationProvider.getApplicationContext()

    private val testPlayer = FakeHistoryRepositoryImpl.dummyPlayer.toPlayer()

    @Before
    fun setUp() {
        hiltRule.inject()

        val fakeHistoryRepository = FakeHistoryRepositoryImpl().apply {
            addDummyPlayerToHistory()
        }

        val observePickedPlayersHistoryUseCase = ObservePickedPlayersHistoryUseCase(
            historyRepository = fakeHistoryRepository
        )
        val observePlayerUseCase = ObservePlayerUseCase(
            historyRepository = fakeHistoryRepository
        )

        composeTestRule.apply {
            activity.setContent {
                FutSaleTheme {
                    val testNavController = rememberNavController()

                    NavHost(
                        navController = testNavController,
                        startDestination = HistoryScreen
                    ) {
                        composable<HistoryScreen> {
                            HistoryScreen(
                                viewModel = HistoryViewModel(
                                    observePickedPlayersHistoryUseCase = { observePickedPlayersHistoryUseCase }
                                ),
                                bottomPadding = 0.dp,
                                onNavigateToPlayerInfoScreen = { playerId ->
                                    testNavController.navigate(HistoryPlayerInfoScreen(playerId))
                                }
                            )
                        }

                        composable<HistoryPlayerInfoScreen> { backStackEntry ->
                            backStackEntry.savedStateHandle["playerId"] = backStackEntry
                                .toRoute<HistoryPlayerInfoScreen>().playerId

                            HistoryPlayerInfoScreen(
                                viewModel = HistoryPlayerInfoViewModel(
                                    observePlayerUseCase = { observePlayerUseCase },
                                    savedStateHandle = backStackEntry.savedStateHandle
                                ),
                                onNavigateUp = testNavController::navigateUp
                            )
                        }
                    }
                }
            }

            onNodeWithText(testPlayer.name).apply {
                performClick()
            }
        }
    }

    @Test
    fun navigatingToHistoryPlayerInfoScreen_showsPlayerInfoAndPickUpDate() = runTest {
        composeTestRule.apply {
            onNodeWithText(testPlayer.name).apply {
                assertExists()
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

            onNodeWithTag(TestTags.PLAYER_PICK_UP_DATE).apply {
                assertExists()
                assertIsDisplayed()
            }
        }
    }

    @Test
    fun clickOnBackBtn_navigatesToHistoryScreen() {
        composeTestRule.apply {
            onNodeWithContentDescription(context.getString(R.string.core_content_description_back)).apply {
                assertExists()
                assertIsDisplayed()
                performClick()
            }

            onNodeWithTag(testTag = TEST_TAG_SCREEN_HISTORY).assertIsDisplayed()
        }
    }

    @Test
    fun pressBack_navigatesToHistoryScreen() {
        Espresso.pressBackUnconditionally()
        composeTestRule.onNodeWithTag(testTag = TEST_TAG_SCREEN_HISTORY).assertIsDisplayed()
    }
}