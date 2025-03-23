package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.presentation

import android.content.Context
import androidx.activity.compose.setContent
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextReplacement
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.rule.GrantPermissionRule
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.repositories.FakeDsfutDataStoreRepositoryImpl
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.repositories.FakeSettingsDataStoreRepositoryImpl
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.di.AppModule
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.main_activity.MainActivity
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.ui.navigation.HomeScreen
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.ui.navigation.PickUpPlayerScreen
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.ui.navigation.PickedUpPlayerInfoScreen
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.ui.navigation.ProfileScreen
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.ui.navigation.SettingsScreen
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.ui.navigation.nav_graph.historyNavGraph
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.ui.theme.FutSaleTheme
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.data.repositories.FakePickUpPlayerRepositoryImpl
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.use_cases.GetIsNotificationSoundEnabledUseCase
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.use_cases.GetIsNotificationVibrateEnabledUseCase
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.use_cases.GetSelectedPlatformUseCase
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.use_cases.ObserveLatestPickedPlayersUseCase
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
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.presentation.pick_up_player.PickUpPlayerScreen
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.presentation.pick_up_player.PickUpPlayerViewModel
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.presentation.picked_up_player_info.PickedUpPlayerInfoScreen
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.presentation.picked_up_player_info.PickedUpPlayerInfoViewModel
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.domain.use_cases.GetProfileUseCase
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.domain.use_cases.ProfileUseCases
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.domain.use_cases.UpdatePartnerIdUseCase
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.domain.use_cases.UpdateSecretKeyUseCase
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.presentation.ProfileScreen
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.presentation.ProfileViewModel
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.presentation.utils.TestTags.PARTNER_ID_TEXT_FIELD
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.presentation.utils.TestTags.SECRET_KEY_TEXT_FIELD
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.presentation.SettingsScreen
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.text.DecimalFormat
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltAndroidTest
@UninstallModules(AppModule::class)
class PickUpPlayerEndToEndTest {

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

    private val testPartnerId = "123"
    private val testSecretKey = "abc123"
    private val testPlayer = FakePickUpPlayerRepositoryImpl.dummyPlayerDto.toPlayer()

    private lateinit var testNavController: NavHostController

    @Before
    fun setUp() {
        hiltRule.inject()

        val observeLatestPickedPlayersUseCaseUseCase = ObserveLatestPickedPlayersUseCase(
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
            { observeLatestPickedPlayersUseCaseUseCase },
            { observePickedUpPlayerUseCase },
            { getIsNotificationSoundEnabledUseCase },
            { getIsNotificationVibrateEnabledUseCase },
            { getSelectedPlatformUseCase },
            { storeSelectedPlatformUseCase },
            { pickUpPlayerUseCase },
            { startCountDownTimerUseCase }
        )

        val getProfileUseCase = GetProfileUseCase(
            dsfutDataStoreRepository = fakeDsfutDataStoreRepositoryImpl
        )
        val updatePartnerIdUseCase = UpdatePartnerIdUseCase(
            dsfutDataStoreRepository = fakeDsfutDataStoreRepositoryImpl,
            validatePartnerId = com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.domain.validation.ValidatePartnerId()
        )
        val updateSecretKeyUseCase = UpdateSecretKeyUseCase(
            dsfutDataStoreRepository = fakeDsfutDataStoreRepositoryImpl,
            validateSecretKey = com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.domain.validation.ValidateSecretKey()
        )

        val profileUseCases = ProfileUseCases(
            { getProfileUseCase },
            { updatePartnerIdUseCase },
            { updateSecretKeyUseCase }
        )

        composeTestRule.activity.setContent {
            FutSaleTheme {
                testNavController = rememberNavController()

                NavHost(
                    navController = rememberNavController(),
                    startDestination = HomeScreen
                ) {
                    composable<HomeScreen> {
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
                                            PickedUpPlayerInfoScreen(playerId)
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

                            composable<ProfileScreen> { backStackEntry ->
                                ProfileScreen(
                                    viewModel = ProfileViewModel(
                                        profileUseCases = profileUseCases,
                                        savedStateHandle = backStackEntry.savedStateHandle
                                    ),
                                    bottomPadding = 0.dp
                                )
                            }

                            historyNavGraph(
                                homeNavController = testNavController,
                                bottomPadding = 0.dp
                            )

                            composable<SettingsScreen> {
                                SettingsScreen(bottomPadding = 0.dp)
                            }
                        }
                    }
                }
            }
        }
    }

    @Test
    fun openProfileScreen_setPartnerIdAndSecretKey_returnToPickUpPlayerScreen_pickUpPlayerSuccessfully() {
        composeTestRule.apply {
            // Navigate to Profile screen via open profile snackbar action button
            onNodeWithText(context.getString(R.string.pick_up_player_btn_pick_once)).apply {
                assertExists()
                performScrollTo()
                assertIsDisplayed()
                performClick()
            }

            onNode(hasText(text = context.getString(R.string.pick_up_player_error_btn_open_profile))).apply {
                assertExists()
                assertIsDisplayed()
                performClick()
            }

            // Enter Partner ID
            onNodeWithTag(testTag = PARTNER_ID_TEXT_FIELD).apply {
                assertExists()
                performScrollTo()
                assertIsDisplayed()
                performTextReplacement(text = testPartnerId)

                Thread.sleep(500) // Sleep due to saving delay

                assertTextEquals(testPartnerId)
            }

            // Enter Secret Key
            onNodeWithTag(testTag = SECRET_KEY_TEXT_FIELD).apply {
                assertExists()
                performScrollTo()
                assertIsDisplayed()
                performTextReplacement(text = testSecretKey)

                Thread.sleep(500) // Sleep due to saving delay

                assertTextEquals(testSecretKey)
            }

            // Navigate back to Pick Up Player screen
            Espresso.pressBackUnconditionally() // Clear textfield focus
            Espresso.pressBackUnconditionally() // Navigate back

            // Pick up player once
            onNodeWithText(context.getString(R.string.pick_up_player_btn_pick_once)).apply {
                assertExists()
                performScrollTo()
                assertIsDisplayed()
                performClick()
            }

            // Check if successfully navigated to Picked Up Player Info screen
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
    }

    @Test
    fun openProfileScreen_setPartnerIdAndSecretKey_returnToPickUpPlayerScreen_pickUpPlayerWithUnavailableNetwork_showsNetworkUnavailableSnackbar() {
        fakePickUpPlayerRepository.isNetworkAvailable(isAvailable = false)

        composeTestRule.apply {
            // Navigate to Profile screen via open profile snackbar action button
            onNodeWithText(context.getString(R.string.pick_up_player_btn_pick_once)).apply {
                assertExists()
                performScrollTo()
                assertIsDisplayed()
                performClick()
            }

            onNode(hasText(text = context.getString(R.string.pick_up_player_error_btn_open_profile))).apply {
                assertExists()
                assertIsDisplayed()
                performClick()
            }

            // Enter Partner ID
            onNodeWithTag(testTag = PARTNER_ID_TEXT_FIELD).apply {
                assertExists()
                performScrollTo()
                assertIsDisplayed()
                performTextReplacement(text = testPartnerId)

                Thread.sleep(500) // Sleep due to saving delay

                assertTextEquals(testPartnerId)
            }

            // Enter Secret Key
            onNodeWithTag(testTag = SECRET_KEY_TEXT_FIELD).apply {
                assertExists()
                performScrollTo()
                assertIsDisplayed()
                performTextReplacement(text = testSecretKey)

                Thread.sleep(500) // Sleep due to saving delay

                assertTextEquals(testSecretKey)
            }

            Espresso.pressBackUnconditionally() // Clear textfield focus
            Espresso.pressBackUnconditionally() // Navigate back

            // Pick up player once
            onNodeWithText(context.getString(R.string.pick_up_player_btn_pick_once)).apply {
                assertExists()
                performScrollTo()
                assertIsDisplayed()
                performClick()
            }

            // Check unavailable network snackbar
            onNode(hasText(text = context.getString(R.string.error_network_connection_unavailable))).apply {
                assertExists()
                assertIsDisplayed()
            }
        }
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun openProfileScreen_setPartnerIdAndSecretKey_returnToPickUpPlayerScreen_autoPickUpPlayerSuccessfully() {
        composeTestRule.apply {
            // Navigate to Profile screen via open profile snackbar action button
            onNodeWithText(context.getString(R.string.pick_up_player_btn_pick_once)).apply {
                assertExists()
                performScrollTo()
                assertIsDisplayed()
                performClick()
            }

            onNode(hasText(text = context.getString(R.string.pick_up_player_error_btn_open_profile))).apply {
                assertExists()
                assertIsDisplayed()
                performClick()
            }

            // Enter Partner ID
            onNodeWithTag(testTag = PARTNER_ID_TEXT_FIELD).apply {
                assertExists()
                performScrollTo()
                assertIsDisplayed()
                performTextReplacement(text = testPartnerId)

                Thread.sleep(500) // Sleep due to saving delay

                assertTextEquals(testPartnerId)
            }

            // Enter Secret Key
            onNodeWithTag(testTag = SECRET_KEY_TEXT_FIELD).apply {
                assertExists()
                performScrollTo()
                assertIsDisplayed()
                performTextReplacement(text = testSecretKey)

                Thread.sleep(500) // Sleep due to saving delay

                assertTextEquals(testSecretKey)
            }

            // Navigate back to Pick Up Player screen
            Espresso.pressBackUnconditionally() // Clear textfield focus
            Espresso.pressBackUnconditionally() // Navigate back

            // Auto pick up player
            fakePickUpPlayerRepository.setIsPlayersQueueEmpty(isEmpty = true)
            onNodeWithText(context.getString(R.string.pick_up_player_btn_pick_auto)).apply {
                assertExists()
                performScrollTo()
                assertIsDisplayed()
                performClick()
            }
            fakePickUpPlayerRepository.setIsPlayersQueueEmpty(isEmpty = false)

            // Check if successfully navigated to Picked Up Player Info screen
            waitUntilAtLeastOneExists(
                timeoutMillis = 10000,
                matcher = hasText(
                    text = context.getString(
                        R.string.picked_up_player_info_message,
                        testPlayer.name
                    )
                )
            )

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
    }

    @Test
    fun openProfileScreen_setPartnerIdAndSecretKey_returnToPickUpPlayerScreen_autoPickUpPlayerWithUnavailableNetwork_showsNetworkUnavailableSnackbar() {
        fakePickUpPlayerRepository.isNetworkAvailable(isAvailable = false)

        composeTestRule.apply {
            // Navigate to Profile screen via open profile snackbar action button
            onNodeWithText(context.getString(R.string.pick_up_player_btn_pick_once)).apply {
                assertExists()
                performScrollTo()
                assertIsDisplayed()
                performClick()
            }

            onNode(hasText(text = context.getString(R.string.pick_up_player_error_btn_open_profile))).apply {
                assertExists()
                assertIsDisplayed()
                performClick()
            }

            // Enter Partner ID
            onNodeWithTag(testTag = PARTNER_ID_TEXT_FIELD).apply {
                assertExists()
                performScrollTo()
                assertIsDisplayed()
                performTextReplacement(text = testPartnerId)

                Thread.sleep(500) // Sleep due to saving delay

                assertTextEquals(testPartnerId)
            }

            // Enter Secret Key
            onNodeWithTag(testTag = SECRET_KEY_TEXT_FIELD).apply {
                assertExists()
                performScrollTo()
                assertIsDisplayed()
                performTextReplacement(text = testSecretKey)

                Thread.sleep(500) // Sleep due to saving delay

                assertTextEquals(testSecretKey)
            }

            // Navigate back to Pick Up Player screen
            Espresso.pressBackUnconditionally() // Clear textfield focus
            Espresso.pressBackUnconditionally() // Navigate back

            // Auto pick up player
            onNodeWithText(context.getString(R.string.pick_up_player_btn_pick_auto)).apply {
                assertExists()
                performScrollTo()
                assertIsDisplayed()
                performClick()
            }

            // Check unavailable network snackbar
            onNode(hasText(text = context.getString(R.string.error_network_connection_unavailable))).apply {
                assertExists()
                assertIsDisplayed()
            }
        }
    }
}