package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.review.ReviewManager
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.RateAppOption
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.ConnectivityObserver
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.utils.Result
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.Event
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.NetworkObserverHelper
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.isAppInstalledFromPlayStore
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.di.FirstInstallTimeInMs
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.repositories.UpdateType
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.use_case.HomeUseCases
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.presentation.states.HomeState
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.presentation.util.Constants
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.presentation.util.DateHelper
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.presentation.util.HomeUiEvent
import dagger.Lazy
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val appUpdateType: Lazy<UpdateType>,
    val appUpdateManager: Lazy<AppUpdateManager>,
    val appUpdateOptions: Lazy<AppUpdateOptions>,
    val reviewManager: Lazy<ReviewManager>,
    private val firstInstallTimeInMs: Lazy<FirstInstallTimeInMs>,
    private val homeUseCases: HomeUseCases
) : ViewModel() {

    private val _homeState = MutableStateFlow(HomeState())
    val homeState = combine(
        flow = _homeState,
        flow2 = homeUseCases.getNotificationPermissionCountUseCase.get()(),
        flow3 = homeUseCases.getSelectedRateAppOptionUseCase.get()(),
        flow4 = homeUseCases.getPreviousRateAppRequestTimeInMsUseCase.get()()
    ) { homeState, notificationPermissionCount, selectedRateAppOption, previousRateAppRequestTimeInMs ->
        homeState.copy(
            notificationPermissionCount = notificationPermissionCount,
            selectedRateAppOption = selectedRateAppOption,
            previousRateAppRequestTimeInMs = previousRateAppRequestTimeInMs
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeout = 5.seconds),
        initialValue = HomeState()
    )

    private val _inAppUpdatesEventChannel = Channel<Event>()
    val inAppUpdatesEventChannel = _inAppUpdatesEventChannel.receiveAsFlow()

    private val _inAppReviewsEventChannel = Channel<Event>()
    val inAppReviewEventChannel = _inAppReviewsEventChannel.receiveAsFlow()

    init {
        if (isAppInstalledFromPlayStore()) {
            checkIsAppUpdateStalled()
            checkFlexibleUpdateDownloadState()
            checkForAppUpdates()
            requestInAppReviews()
        } else {
            getLatestAppVersion()
            checkSelectedRateAppOption()
        }
    }

    fun onAction(action: HomeAction) {
        when (action) {
            HomeAction.CheckIsAppUpdateStalled -> checkIsAppUpdateStalled()
            HomeAction.CheckFlexibleUpdateDownloadState -> checkFlexibleUpdateDownloadState()
            HomeAction.CheckForAppUpdates -> checkForAppUpdates()
            HomeAction.GetLatestAppVersion -> getLatestAppVersion()
            HomeAction.DismissAppUpdateSheet -> dismissAppUpdateSheet()
            HomeAction.RequestInAppReviews -> requestInAppReviews()
            HomeAction.CheckSelectedRateAppOption -> checkSelectedRateAppOption()
            HomeAction.LaunchInAppReview -> launchInAppReview()
            HomeAction.SetSelectedRateAppOptionToNever -> setSelectedRateAppOptionToNever()
            HomeAction.SetSelectedRateAppOptionToRemindLater -> setSelectedRateAppOptionToRemindLater()
            HomeAction.DismissAppReviewDialog -> dismissAppReviewDialog()
            is HomeAction.OnPermissionResult -> onPermissionResult(
                permission = action.permission,
                isGranted = action.isGranted
            )
            is HomeAction.DismissPermissionDialog -> dismissPermissionDialog(permission = action.permission)
        }
    }

    private fun checkIsAppUpdateStalled() {
        when (appUpdateType.get()) {
            AppUpdateType.IMMEDIATE -> checkIsImmediateUpdateStalled()
            AppUpdateType.FLEXIBLE -> checkIsFlexibleUpdateStalled()
            else -> Unit
        }
    }

    private fun checkIsImmediateUpdateStalled() {
        homeUseCases.checkIsImmediateUpdateStalledUseCase.get()().onEach { appUpdateInfo ->
            appUpdateInfo?.let {
                _inAppUpdatesEventChannel.send(HomeUiEvent.StartAppUpdateFlow(appUpdateInfo))
            }
        }.launchIn(scope = viewModelScope)
    }

    private fun checkIsFlexibleUpdateStalled() {
        homeUseCases.checkIsFlexibleUpdateStalledUseCase.get()().onEach { isUpdateDownloaded ->
            if (isUpdateDownloaded) {
                _inAppUpdatesEventChannel.send(HomeUiEvent.CompleteFlexibleAppUpdate)
            }
        }.launchIn(scope = viewModelScope)
    }

    private fun checkFlexibleUpdateDownloadState() {
        homeUseCases.checkFlexibleUpdateDownloadStateUseCase.get()().onEach { isUpdateDownloaded ->
            if (isUpdateDownloaded) {
                _inAppUpdatesEventChannel.send(HomeUiEvent.ShowCompleteAppUpdateSnackbar)
            }
        }.launchIn(scope = viewModelScope)
    }

    private fun checkForAppUpdates() = viewModelScope.launch {
        homeUseCases.checkForAppUpdatesUseCase.get()().onEach { appUpdateInfo ->
            appUpdateInfo?.let {
                _inAppUpdatesEventChannel.send(HomeUiEvent.StartAppUpdateFlow(appUpdateInfo))
            }
        }.launchIn(scope = viewModelScope)
    }

    private fun getLatestAppVersion() {
        if (NetworkObserverHelper.networkStatus.value == ConnectivityObserver.Status.AVAILABLE) {
            homeUseCases.getLatestAppVersionUseCase.get()().onEach { result ->
                when (result) {
                    is Result.Success -> result.data?.let { latestAppUpdateInfo ->
                        _homeState.update { state ->
                            state.copy(latestAppUpdateInfo = latestAppUpdateInfo)
                        }
                    }
                    is Result.Error -> Unit
                }
            }.launchIn(scope = viewModelScope)
        }
    }

    private fun dismissAppUpdateSheet() = viewModelScope.launch {
        _homeState.update { state ->
            state.copy(latestAppUpdateInfo = null)
        }
    }

    private fun requestInAppReviews() {
        homeUseCases.requestInAppReviewsUseCase.get()().onEach { reviewInfo ->
            _homeState.update { state ->
                state.copy(inAppReviewInfo = reviewInfo)
            }
            checkSelectedRateAppOption()
        }.launchIn(scope = viewModelScope)
    }

    private fun checkSelectedRateAppOption() = viewModelScope.launch {
        delay(timeMillis = 100) // Wait for the state to be updated
        when (homeState.value.selectedRateAppOption) {
            RateAppOption.NOT_SHOWN_YET, RateAppOption.RATE_NOW -> checkDaysFromFirstInstallTime()
            RateAppOption.REMIND_LATER -> checkDaysFromPreviousRequestTime()
            RateAppOption.NEVER, null -> Unit
        }
    }

    private fun checkDaysFromFirstInstallTime() {
        val daysFromFirstInstallTime = DateHelper.getDaysFromFirstInstallTime(
            firstInstallTimeInMs = firstInstallTimeInMs.get()
        )

        val shouldShowAppReviewDialog =
            daysFromFirstInstallTime >= Constants.IN_APP_REVIEWS_DAYS_FROM_FIRST_INSTALL_TIME
        if (shouldShowAppReviewDialog) {
            _homeState.update { state ->
                state.copy(isAppReviewDialogVisible = true)
            }
        }
    }

    private fun checkDaysFromPreviousRequestTime() {
        homeState.value.previousRateAppRequestTimeInMs?.let {
            val daysFromPreviousRequestTime = DateHelper.getDaysFromPreviousRequestTime(it)

            val shouldShowAppReviewDialog =
                daysFromPreviousRequestTime >= Constants.IN_APP_REVIEWS_DAYS_FROM_PREVIOUS_REQUEST_TIME
            if (shouldShowAppReviewDialog) {
                _homeState.update { state ->
                    state.copy(isAppReviewDialogVisible = true)
                }
            }
        }
    }

    private fun launchInAppReview() = viewModelScope.launch {
        _inAppReviewsEventChannel.send(HomeUiEvent.LaunchInAppReview)
    }

    private fun setSelectedRateAppOptionToNever() = viewModelScope.launch {
        homeUseCases.storeSelectedRateAppOptionUseCase.get()(rateAppOption = RateAppOption.NEVER)
    }

    private fun setSelectedRateAppOptionToRemindLater() = viewModelScope.launch {
        homeUseCases.storeSelectedRateAppOptionUseCase.get()(rateAppOption = RateAppOption.REMIND_LATER)
        homeUseCases.storePreviousRateAppRequestTimeInMsUseCase.get()()
    }

    private fun dismissAppReviewDialog() = viewModelScope.launch {
        _homeState.update { state ->
            state.copy(isAppReviewDialogVisible = false)
        }
    }

    private fun onPermissionResult(
        permission: String,
        isGranted: Boolean
    ) = viewModelScope.launch {
        val shouldAskForPermission = homeState.value.run {
            notificationPermissionCount < 1 && !permissionDialogQueue.contains(permission) && !isGranted
        }

        if (shouldAskForPermission) {
            _homeState.update { state ->
                state.copy(
                    permissionDialogQueue = listOf(permission),
                    isPermissionDialogVisible = true
                )
            }
        }
    }

    private fun dismissPermissionDialog(permission: String) = viewModelScope.launch {
        homeUseCases.storeNotificationPermissionCountUseCase.get()(
            count = homeState.value.notificationPermissionCount.plus(1)
        )

        _homeState.update { state ->
            state.copy(
                permissionDialogQueue = homeState.value.permissionDialogQueue
                    .toMutableList().apply { remove(permission) }.toList(),
                isPermissionDialogVisible = false
            )
        }
    }
}