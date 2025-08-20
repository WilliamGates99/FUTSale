package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.presentation.picked_up_player_info

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
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.repositories.FakeDsfutDataStoreRepositoryImpl
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.repositories.FakeSettingsDataStoreRepositoryImpl
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.di.MD5HashGeneratorQualifier
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.ConnectivityObserver
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.HashGenerator
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.common.ui.navigation.screens.PickUpPlayerScreen
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.common.ui.navigation.screens.PickedUpPlayerInfoScreen
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.common.ui.navigation.screens.ProfileScreen
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.common.ui.theme.FutSaleTheme
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.common.utils.NetworkObserverHelper
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.common.utils.TestTags
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.main_activity.MainActivity
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.data.mappers.toPlayer
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.data.repositories.FakeCountDownTimerRepositoryImpl
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.data.repositories.FakePickUpPlayerRepositoryImpl
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.data.repositories.FakePickedUpPlayersRepositoryImpl
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.data.utils.DummyPlayersHelper
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.use_cases.GetIsNotificationSoundEnabledUseCase
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.use_cases.GetIsNotificationVibrateEnabledUseCase
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.use_cases.GetSelectedPlatformUseCase
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.use_cases.ObserveLatestPickedUpPlayersUseCase
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.use_cases.ObservePickedUpPlayerUseCase
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.use_cases.PickUpPlayerUseCase
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.use_cases.PickUpPlayerUseCases
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.use_cases.PickedUpPlayerInfoUseCases
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.use_cases.StartCountDownTimerUseCase
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.use_cases.StoreSelectedPlatformUseCase
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.validation.ValidateMaxPrice
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.validation.ValidateMinPrice
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.validation.ValidatePartnerId
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.validation.ValidateSecretKey
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.validation.ValidateTakeAfter
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.presentation.pick_up_player.PickUpPlayerScreen
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.presentation.pick_up_player.PickUpPlayerViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.text.DecimalFormat
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltAndroidTest
class PickedUpPlayerInfoScreenTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(/* testInstance = */ this)

    @get:Rule(order = 1)
    var permissionRule: GrantPermissionRule = GrantPermissionRule.grant(
        android.Manifest.permission.POST_NOTIFICATIONS
    )

    @get:Rule(order = 2)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Inject
    lateinit var connectivityObserver: ConnectivityObserver

    @Inject
    lateinit var decimalFormat: DecimalFormat

    @Inject
    @MD5HashGeneratorQualifier
    lateinit var md5HashGenerator: HashGenerator

    private val context: Context = ApplicationProvider.getApplicationContext()

    private val fakeSettingsDataStoreRepositoryImpl = FakeSettingsDataStoreRepositoryImpl()
    private val fakeDsfutDataStoreRepositoryImpl = FakeDsfutDataStoreRepositoryImpl()
    private val fakePickUpPlayerRepository = FakePickUpPlayerRepositoryImpl(
        md5HashGenerator = { md5HashGenerator }
    )
    private val fakePickedUpPlayersRepository = FakePickedUpPlayersRepositoryImpl()
    private val fakeCountDownTimerRepository = FakeCountDownTimerRepositoryImpl()

    private val testPlayer = DummyPlayersHelper.dummyPlayerDto.toPlayer()

    @Before
    fun setUp() {
        hiltRule.inject()

        DummyPlayersHelper.resetLatestPlayers()
        NetworkObserverHelper.observeNetworkConnection(connectivityObserver)

        val observeLatestPickedUpPlayersUseCaseUseCase = ObserveLatestPickedUpPlayersUseCase(
            pickedUpPlayersRepository = fakePickedUpPlayersRepository
        )
        val observePickedUpPlayerUseCase = ObservePickedUpPlayerUseCase(
            pickedUpPlayersRepository = fakePickedUpPlayersRepository
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
            countDownTimerRepository = fakeCountDownTimerRepository
        )

        val pickUpPlayerUseCases = PickUpPlayerUseCases(
            { observeLatestPickedUpPlayersUseCaseUseCase },
            { getIsNotificationSoundEnabledUseCase },
            { getIsNotificationVibrateEnabledUseCase },
            { getSelectedPlatformUseCase },
            { storeSelectedPlatformUseCase },
            { pickUpPlayerUseCase },
            { startCountDownTimerUseCase }
        )

        val pickedUpPlayerInfoUseCases = PickedUpPlayerInfoUseCases(
            { observePickedUpPlayerUseCase },
            { startCountDownTimerUseCase }
        )

        runBlocking {
            fakeDsfutDataStoreRepositoryImpl.storePartnerId(partnerId = "123")
            fakeDsfutDataStoreRepositoryImpl.storeSecretKey(secretKey = "abc123")
        }

        composeTestRule.apply {
            activity.setContent {
                FutSaleTheme {
                    val testNavController = rememberNavController()

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
                                    pickedUpPlayerInfoUseCases = pickedUpPlayerInfoUseCases,
                                    decimalFormat = decimalFormat,
                                    savedStateHandle = backStackEntry.savedStateHandle
                                ),
                                onNavigateUp = testNavController::navigateUp
                            )
                        }
                    }
                }
            }

            onNodeWithText(context.getString(R.string.pick_up_player_btn_pick_once)).apply {
                performScrollTo()
                performClick()
            }
        }
    }

    @Test
    fun navigatingToPickedUpPlayerInfoScreen_showsPlayerInfoAndInstruction() {
        composeTestRule.apply {
            onNodeWithText(text = testPlayer.name).apply {
                assertExists()
                assertIsDisplayed()
            }

            onNodeWithText(
                text = context.getString(
                    R.string.picked_up_player_info_message,
                    testPlayer.name
                )
            ).apply {
                assertExists()
                performScrollTo()
                assertIsDisplayed()
            }

            onNodeWithText(text = testPlayer.rating).apply {
                assertExists()
                performScrollTo()
                assertIsDisplayed()
            }

            onNodeWithText(text = testPlayer.position).apply {
                assertExists()
                performScrollTo()
                assertIsDisplayed()
            }

            onNodeWithText(text = testPlayer.chemistryStyle).apply {
                assertExists()
                performScrollTo()
                assertIsDisplayed()
            }

            onNodeWithText(
                text = context.getString(R.string.picked_up_player_info_instruction_title)
            ).apply {
                assertExists()
                performScrollTo()
                assertIsDisplayed()
            }
        }
    }

    @Test
    fun clickOnBackBtn_navigatesToPickUpPlayerScreen() {
        composeTestRule.apply {
            onNodeWithContentDescription(
                label = context.getString(R.string.core_content_description_back)
            ).apply {
                assertExists()
                assertIsDisplayed()
                performClick()
            }

            onNodeWithTag(testTag = TestTags.TEST_TAG_SCREEN_PICK_UP_PLAYER).assertIsDisplayed()
        }
    }

    @Test
    fun pressBack_navigatesToPickUpPlayerScreen() {
        Espresso.pressBackUnconditionally()

        composeTestRule.onNodeWithTag(
            testTag = TestTags.TEST_TAG_SCREEN_PICK_UP_PLAYER
        ).assertIsDisplayed()
    }
}