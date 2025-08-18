package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.presentation.pick_up_player

import android.content.Context
import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.rule.GrantPermissionRule
import com.google.common.truth.Truth.assertThat
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.repositories.FakeDsfutDataStoreRepositoryImpl
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.repositories.FakeSettingsDataStoreRepositoryImpl
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.main_activity.MainActivity
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.common.ui.navigation.screens.PickUpPlayerScreen
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.common.ui.navigation.screens.PickedUpPlayerInfoScreen
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.common.ui.navigation.screens.ProfileScreen
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.ui.theme.FutSaleTheme
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.data.repositories.FakePickUpPlayerRepositoryImpl
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.use_cases.GetIsNotificationSoundEnabledUseCase
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.use_cases.GetIsNotificationVibrateEnabledUseCase
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.use_cases.GetSelectedPlatformUseCase
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.use_cases.ObserveLatestPickedUpPlayersUseCase
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.use_cases.ObservePickedUpPlayerUseCase
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.use_cases.PickUpPlayerUseCase
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.use_cases.PickUpPlayerUseCases
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.use_cases.StartCountDownTimerUseCase
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.use_cases.StoreSelectedPlatformUseCase
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.validation.ValidateMaxPrice
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.validation.ValidateMinPrice
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.validation.ValidatePartnerId
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.validation.ValidateSecretKey
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.validation.ValidateTakeAfter
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.presentation.pick_up_player.utils.TestTags
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.presentation.picked_up_player_info.PickedUpPlayerInfoScreen
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.presentation.picked_up_player_info.PickedUpPlayerInfoViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.text.DecimalFormat
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltAndroidTest
class PickUpPlayerScreenTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(/* testInstance = */ this)

    @get:Rule(order = 1)
    var permissionRule: GrantPermissionRule = GrantPermissionRule.grant(
        android.Manifest.permission.POST_NOTIFICATIONS
    )

    @get:Rule(order = 2)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Inject
    lateinit var decimalFormat: DecimalFormat

    private val context: Context = ApplicationProvider.getApplicationContext()

    private val fakeSettingsDataStoreRepositoryImpl = FakeSettingsDataStoreRepositoryImpl()
    private val fakeDsfutDataStoreRepositoryImpl = FakeDsfutDataStoreRepositoryImpl()
    private val fakePickUpPlayerRepository = FakePickUpPlayerRepositoryImpl()

    private val testPlayer = FakePickUpPlayerRepositoryImpl.dummyPlayerDto.toPlayer()

    private lateinit var testNavController: NavHostController

    @Before
    fun setUp() {
        hiltRule.inject()

        val observeLatestPickedUpPlayersUseCaseUseCase = ObserveLatestPickedUpPlayersUseCase(
            pickUpPlayerRepository = fakePickUpPlayerRepository
        )
        val observePickedUpPlayerUseCase = ObservePickedUpPlayerUseCase(
            pickUpPlayerRepository = fakePickUpPlayerRepository
        )
        val getIsNotificationSoundEnabledUseCase = GetIsNotificationSoundEnabledUseCase(
            settingsDataStoreRepository = fakeSettingsDataStoreRepositoryImpl
        )
        val getIsNotificationVibrateEnabledUseCase = GetIsNotificationVibrateEnabledUseCase(
            settingsDataStoreRepository = fakeSettingsDataStoreRepositoryImpl
        )
        val getSelectedPlatformUseCase = GetSelectedPlatformUseCase(
            dsfutDataStoreRepository = fakeDsfutDataStoreRepositoryImpl
        )
        val storeSelectedPlatformUseCase = StoreSelectedPlatformUseCase(
            dsfutDataStoreRepository = fakeDsfutDataStoreRepositoryImpl
        )
        val pickUpPlayerUseCase = PickUpPlayerUseCase(
            dsfutDataStoreRepository = fakeDsfutDataStoreRepositoryImpl,
            pickUpPlayerRepository = fakePickUpPlayerRepository,
            validatePartnerId = ValidatePartnerId(),
            validateSecretKey = ValidateSecretKey(),
            validateMinPrice = ValidateMinPrice(),
            validateMaxPrice = ValidateMaxPrice(),
            validateTakeAfter = ValidateTakeAfter()
        )
        val startCountDownTimerUseCase = StartCountDownTimerUseCase(
            pickUpPlayerRepository = fakePickUpPlayerRepository
        )

        val pickUpPlayerUseCases = PickUpPlayerUseCases(
            { observeLatestPickedUpPlayersUseCaseUseCase },
            { observePickedUpPlayerUseCase },
            { getIsNotificationSoundEnabledUseCase },
            { getIsNotificationVibrateEnabledUseCase },
            { getSelectedPlatformUseCase },
            { storeSelectedPlatformUseCase },
            { pickUpPlayerUseCase },
            { startCountDownTimerUseCase }
        )

        composeTestRule.activity.setContent {
            FutSaleTheme {
                testNavController = rememberNavController()

                NavHost(
                    navController = testNavController,
                    startDestination = PickUpPlayerScreen
                ) {
                    composable<PickUpPlayerScreen> { backStackEntry ->
                        PickUpPlayerScreen(
                            viewModel = PickUpPlayerViewModel(
                                pickUpPlayerUseCases = pickUpPlayerUseCases,
                                decimalFormat = decimalFormat,
                                savedStateHandle = backStackEntry.savedStateHandle
                            ),
                            bottomPadding = 0.dp,
                            onNavigateToProfileScreen = {
                                testNavController.navigate(ProfileScreen) {
                                    launchSingleTop = true
                                    popUpTo(testNavController.graph.startDestinationId)
                                }
                            },
                            onNavigateToPickedUpPlayerInfoScreen = { playerId ->
                                testNavController.navigate(
                                    PickedUpPlayerInfoScreen(
                                        playerId
                                    )
                                )
                            }
                        )
                    }

                    composable<PickedUpPlayerInfoScreen> { backStackEntry ->
                        backStackEntry.savedStateHandle["playerId"] = backStackEntry
                            .toRoute<PickedUpPlayerInfoScreen>().playerId

                        PickedUpPlayerInfoScreen(
                            viewModel = PickedUpPlayerInfoViewModel(
                                pickUpPlayerUseCases = pickUpPlayerUseCases,
                                decimalFormat = decimalFormat,
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
    fun launchingPickUpPlayerScreen_showsPickUpPlayerScreenItems() {
        composeTestRule.apply {
            onNodeWithText(context.getString(R.string.pick_up_player_title_instruction)).apply {
                assertExists()
                assertIsDisplayed()
            }

            onNodeWithTag(TestTags.PC_SEGMENTED_BTN).apply {
                assertExists()
                assertIsDisplayed()
            }

            onNodeWithTag(TestTags.CONSOLE_SEGMENTED_BTN).apply {
                assertExists()
                assertIsDisplayed()
            }

            onNodeWithTag(TestTags.MIN_PRICE_TEXT_FIELD).apply {
                assertExists()
                assertIsDisplayed()
            }

            onNodeWithTag(TestTags.MAX_PRICE_TEXT_FIELD).apply {
                assertExists()
                assertIsDisplayed()
            }

            onNodeWithTag(TestTags.TAKE_AFTER_CHECK_BOX).apply {
                assertExists()
                assertIsDisplayed()
            }

            onNodeWithTag(TestTags.TAKE_AFTER_SLIDER).apply {
                assertExists()
                assertIsDisplayed()
            }

            onNodeWithText(context.getString(R.string.pick_up_player_btn_pick_auto)).apply {
                assertExists()
                assertIsDisplayed()
            }

            onNodeWithText(context.getString(R.string.pick_up_player_btn_pick_once)).apply {
                assertExists()
                assertIsDisplayed()
            }
        }
    }

    @Test
    fun clickOnPcSegmentedBtn_selectsPcPlatform() {
        composeTestRule.apply {
            composeTestRule.apply {
                onNodeWithTag(TestTags.CONSOLE_SEGMENTED_BTN).apply {
                    assertExists()
                    assertIsDisplayed()
                    performClick()
                }

                onNodeWithTag(testTag = TestTags.PC_SEGMENTED_BTN).apply {
                    assertExists()
                    assertIsDisplayed()
                    assertIsNotSelected()
                    performClick()
                    assertIsSelected()
                }
            }
        }
    }

    @Test
    fun clickOnConsoleSegmentedBtn_selectsConsolePlatform() {
        composeTestRule.apply {
            onNodeWithTag(TestTags.PC_SEGMENTED_BTN).apply {
                assertExists()
                assertIsDisplayed()
                performClick()
            }

            onNodeWithTag(testTag = TestTags.CONSOLE_SEGMENTED_BTN).apply {
                assertExists()
                assertIsDisplayed()
                assertIsNotSelected()
                performClick()
                assertIsSelected()
            }
        }
    }

    @Test
    fun clickOnTakeAfterDelay_enablesTakeAfterSlider() {
        composeTestRule.apply {
            onNodeWithTag(testTag = TestTags.TAKE_AFTER_SLIDER).apply {
                assertExists()
                assertIsDisplayed()
                assertIsNotEnabled()
            }

            onNodeWithTag(TestTags.TAKE_AFTER_CHECK_BOX).apply {
                assertExists()
                assertIsDisplayed()
                assertIsNotSelected()

                performClick()

                assertIsSelected()
            }

            onNodeWithTag(testTag = TestTags.TAKE_AFTER_SLIDER).apply {
                assertIsEnabled()
            }
        }
    }

    @Test
    fun clickOnPickOnceBtnWithUnavailableNetworkAndEmptyPartnerIdOrSecretKey_showsOpenProfileSnackbar() {
        fakePickUpPlayerRepository.isNetworkAvailable(isAvailable = false)

        composeTestRule.apply {
            onNodeWithText(context.getString(R.string.pick_up_player_btn_pick_once)).apply {
                assertExists()
                performScrollTo()
                assertIsDisplayed()
                performClick()
            }

            onNode(hasText(text = context.getString(R.string.pick_up_player_error_btn_open_profile))).apply {
                assertExists()
                assertIsDisplayed()
            }
        }
    }

    @Test
    fun clickOnPickOnceBtnWithUnavailableNetwork_showsNetworkUnavailableSnackbar() = runTest {
        fakePickUpPlayerRepository.isNetworkAvailable(isAvailable = false)

        fakeDsfutDataStoreRepositoryImpl.storePartnerId("123")
        fakeDsfutDataStoreRepositoryImpl.storeSecretKey("abc123")

        composeTestRule.apply {
            onNodeWithText(context.getString(R.string.pick_up_player_btn_pick_once)).apply {
                assertExists()
                performScrollTo()
                assertIsDisplayed()
                performClick()
            }

            onNode(hasText(text = context.getString(R.string.error_network_connection_unavailable))).apply {
                assertExists()
                assertIsDisplayed()
            }
        }
    }

    @Test
    fun clickOnPickOnceBtnWithAvailableNetwork_navigatesToPickedUpPlayerInfoScreen() = runTest {
        fakeDsfutDataStoreRepositoryImpl.storePartnerId("123")
        fakeDsfutDataStoreRepositoryImpl.storeSecretKey("abc123")

        composeTestRule.apply {
            onNodeWithText(context.getString(R.string.pick_up_player_btn_pick_once)).apply {
                assertExists()
                performScrollTo()
                assertIsDisplayed()
                performClick()
            }

            onNodeWithText(
                text = context.getString(
                    R.string.picked_up_player_info_message,
                    testPlayer.name
                )
            ).apply {
                assertExists()
                assertIsDisplayed()
            }
        }

        val backStackEntry = testNavController.currentBackStackEntry
        val isNavigatedToPickedUpPlayerInfoScreen = backStackEntry?.destination?.hierarchy?.any {
            it.hasRoute(PickedUpPlayerInfoScreen::class)
        } ?: false
        assertThat(isNavigatedToPickedUpPlayerInfoScreen).isTrue()
    }

    @Test
    fun clickOnPickAutoPickUpBtnWithUnavailableNetworkAndEmptyPartnerIdOrSecretKey_showsOpenProfileSnackbar() {
        fakePickUpPlayerRepository.isNetworkAvailable(isAvailable = false)

        composeTestRule.apply {
            onNodeWithText(context.getString(R.string.pick_up_player_btn_pick_auto)).apply {
                assertExists()
                performScrollTo()
                assertIsDisplayed()
                performClick()
            }

            onNode(hasText(text = context.getString(R.string.pick_up_player_error_btn_open_profile))).apply {
                assertExists()
                assertIsDisplayed()
            }
        }
    }

    @Test
    fun clickOnAutoPickUpBtnWithUnavailableNetwork_showsNetworkUnavailableSnackbar() = runTest {
        fakePickUpPlayerRepository.isNetworkAvailable(isAvailable = false)

        fakeDsfutDataStoreRepositoryImpl.storePartnerId("123")
        fakeDsfutDataStoreRepositoryImpl.storeSecretKey("abc123")

        composeTestRule.apply {
            onNodeWithText(context.getString(R.string.pick_up_player_btn_pick_auto)).apply {
                assertExists()
                performScrollTo()
                assertIsDisplayed()
                performClick()
            }

            onNode(hasText(text = context.getString(R.string.error_network_connection_unavailable))).apply {
                assertExists()
                assertIsDisplayed()
            }
        }
    }

    @Test
    fun clickOnAutoPickUpBtnWithAvailableNetwork_navigatesToPickedUpPlayerInfoScreen() = runTest {
        fakeDsfutDataStoreRepositoryImpl.storePartnerId("123")
        fakeDsfutDataStoreRepositoryImpl.storeSecretKey("abc123")

        composeTestRule.apply {
            onNodeWithText(context.getString(R.string.pick_up_player_btn_pick_auto)).apply {
                assertExists()
                performScrollTo()
                assertIsDisplayed()
                performClick()
            }

            onNodeWithText(
                text = context.getString(
                    R.string.picked_up_player_info_message,
                    testPlayer.name
                )
            ).apply {
                assertExists()
                assertIsDisplayed()
            }
        }

        val backStackEntry = testNavController.currentBackStackEntry
        val isNavigatedToPickedUpPlayerInfoScreen = backStackEntry?.destination?.hierarchy?.any {
            it.hasRoute(PickedUpPlayerInfoScreen::class)
        } ?: false
        assertThat(isNavigatedToPickedUpPlayerInfoScreen).isTrue()
    }

    @Test
    fun clickOnPickOnceBtnThenPressBackBtn_showsLatestPickedUpPlayersPager() = runTest {
        fakeDsfutDataStoreRepositoryImpl.storePartnerId("123")
        fakeDsfutDataStoreRepositoryImpl.storeSecretKey("abc123")

        composeTestRule.apply {
            onNodeWithText(context.getString(R.string.pick_up_player_btn_pick_once)).apply {
                assertExists()
                performScrollTo()
                assertIsDisplayed()
                performClick()
            }

            Espresso.pressBackUnconditionally()

            onNodeWithTag(testTag = TestTags.LATEST_PICKED_UP_PLAYER_CARD).apply {
                assertExists()
                assertIsDisplayed()
            }
        }
    }

    @Test
    fun clickOnPickOnceBtnThenPressBackBtnMultipleTimes_showsLatestPickedUpPlayersPager() =
        runTest {
            val repeatTimes = 5

            fakePickUpPlayerRepository.isNetworkAvailable(isAvailable = true)

            fakeDsfutDataStoreRepositoryImpl.storePartnerId("123")
            fakeDsfutDataStoreRepositoryImpl.storeSecretKey("abc123")

            composeTestRule.apply {
                repeat(repeatTimes) {
                    onNodeWithText(context.getString(R.string.pick_up_player_btn_pick_once)).apply {
                        assertExists()
                        performScrollTo()
                        assertIsDisplayed()
                        performClick()
                    }

                    awaitIdle()

                    Espresso.pressBackUnconditionally()
                }

                onAllNodesWithTag(testTag = TestTags.LATEST_PICKED_UP_PLAYER_CARD).apply {
                    for (i in 0..1)
                        this[i].apply {
                            assertExists()
                            assertIsDisplayed()
                        }

                    for (i in 2..4) {
                        this[i].apply {
                            assertIsNotDisplayed()
                        }
                    }
                }
            }
        }
}