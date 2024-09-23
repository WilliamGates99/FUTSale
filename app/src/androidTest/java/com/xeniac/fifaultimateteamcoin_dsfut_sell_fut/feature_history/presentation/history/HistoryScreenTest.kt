package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_history.presentation.history

import android.content.Context
import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import androidx.test.core.app.ApplicationProvider
import androidx.test.rule.GrantPermissionRule
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.MainActivity
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.navigation.HistoryPlayerInfoScreen
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.navigation.HistoryScreen
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.theme.FutSaleTheme
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_history.data.repositories.FakeHistoryRepositoryImpl
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_history.domain.use_cases.ObservePickedPlayersHistoryUseCase
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_history.domain.use_cases.ObservePlayerUseCase
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_history.presentation.history.utils.TestTags
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_history.presentation.player_info.HistoryPlayerInfoScreen
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_history.presentation.player_info.HistoryPlayerInfoViewModel
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_history.presentation.player_info.utils.TestTags.TEST_TAG_SCREEN_HISTORY_PLAYER_INFO
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
@HiltAndroidTest
class HistoryScreenTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(/* testInstance = */ this)

    @get:Rule(order = 1)
    var permissionRule: GrantPermissionRule = GrantPermissionRule.grant(
        android.Manifest.permission.POST_NOTIFICATIONS
    )

    @get:Rule(order = 2)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private val context: Context = ApplicationProvider.getApplicationContext()

    private lateinit var fakeHistoryRepository: FakeHistoryRepositoryImpl

    private lateinit var testNavController: NavHostController

    @Before
    fun setUp() {
        hiltRule.inject()

        fakeHistoryRepository = FakeHistoryRepositoryImpl()
        val observePickedPlayersHistoryUseCase = ObservePickedPlayersHistoryUseCase(
            historyRepository = fakeHistoryRepository
        )
        val observePlayerUseCase = ObservePlayerUseCase(
            historyRepository = fakeHistoryRepository
        )

        composeTestRule.activity.setContent {
            FutSaleTheme {
                testNavController = rememberNavController()

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
    }

    @Test
    fun launchingHistoryScreenWithEmptyPlayersHistoryList_showsEmptyAnimation() = runTest {
        composeTestRule.apply {
            onNodeWithText(context.getString(R.string.history_empty_list_message)).apply {
                assertExists()
                assertIsDisplayed()
            }
        }
    }

    @Test
    fun launchingHistoryScreenWithPlayersHistoryList_showsPlayersList() = runTest {
        fakeHistoryRepository.addDummyPlayersToHistory()

        composeTestRule.onNodeWithTag(TestTags.HISTORY_LAZY_COLUMN).apply {
            assertExists()
            assertIsDisplayed()
        }
    }

    @Test
    fun clickOnPlayerCard_NavigatesToHistoryPlayerInfoScreen() = runTest {
        fakeHistoryRepository.addDummyPlayerToHistory()

        composeTestRule.apply {
            onNodeWithTag(TestTags.HISTORY_LAZY_COLUMN).apply {
                assertExists()
                assertIsDisplayed()
            }

            onAllNodesWithTag(TestTags.HISTORY_LAZY_COLUMN_ITEM).onFirst().apply {
                assertExists()
                performScrollTo()
                assertIsDisplayed()
                performClick()
            }

            onNodeWithTag(testTag = TEST_TAG_SCREEN_HISTORY_PLAYER_INFO).assertIsDisplayed()
        }
    }
}