package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_history.presentation.history

import android.content.Context
import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import androidx.test.core.app.ApplicationProvider
import androidx.test.rule.GrantPermissionRule
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.di.AppModule
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.Player
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.MainActivity
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.navigation.Screen
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.navigation.utils.PlayerCustomNavType
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.theme.FutSaleTheme
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_history.data.repositories.FakeHistoryRepositoryImpl
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_history.domain.use_cases.ObservePickedPlayersHistoryUseCase
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_history.presentation.history.utils.TestTags
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_history.presentation.player_info.HistoryPlayerInfoScreen
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.reflect.typeOf

@ExperimentalCoroutinesApi
@HiltAndroidTest
@UninstallModules(AppModule::class)
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

    private val fakeHistoryRepository = FakeHistoryRepositoryImpl()

    private lateinit var testNavController: NavHostController

    @Before
    fun setUp() {
        hiltRule.inject()

        val observePickedPlayersHistoryUseCase = ObservePickedPlayersHistoryUseCase(
            historyRepository = fakeHistoryRepository
        )

        val fakeHistoryViewModel = HistoryViewModel(
            observePickedPlayersHistoryUseCase = { observePickedPlayersHistoryUseCase }
        )

        composeTestRule.activity.setContent {
            FutSaleTheme {
                testNavController = rememberNavController()

                NavHost(
                    navController = testNavController,
                    startDestination = Screen.HistoryScreen
                ) {
                    composable<Screen.HistoryScreen> {
                        HistoryScreen(
                            viewModel = fakeHistoryViewModel,
                            bottomPadding = 0.dp,
                            onNavigateToPlayerInfoScreen = { player ->
                                testNavController.navigate(
                                    Screen.HistoryPlayerInfoScreen(player = player)
                                )
                            }
                        )
                    }

                    composable<Screen.HistoryPlayerInfoScreen>(
                        typeMap = mapOf(typeOf<Player>() to PlayerCustomNavType)
                    ) { backStackEntry ->
                        val args = backStackEntry.toRoute<Screen.HistoryPlayerInfoScreen>()

                        HistoryPlayerInfoScreen(
                            player = args.player,
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
        fakeHistoryRepository.addDummyPlayersToLatestPlayers()

        composeTestRule.onNodeWithTag(TestTags.HISTORY_LAZY_COLUMN).apply {
            assertExists()
            assertIsDisplayed()
        }
    }
}