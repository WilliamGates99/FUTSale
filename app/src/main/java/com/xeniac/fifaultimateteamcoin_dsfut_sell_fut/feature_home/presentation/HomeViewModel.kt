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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.combine
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

    private val _notificationPermissionCount =
        homeUseCases.getNotificationPermissionCountUseCase.get()().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = 0
        )

    private val _selectedRateAppOption =
        homeUseCases.getSelectedRateAppOptionUseCase.get()().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = null
        )

    private val _previousRateAppRequestTimeInMs =
        homeUseCases.getPreviousRateAppRequestTimeInMsUseCase.get()().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = null
        )

    private val _homeState = MutableStateFlow(HomeState())
    val homeState = combine(
        flow = _homeState,
        flow2 = _notificationPermissionCount,
        flow3 = _selectedRateAppOption,
        flow4 = _previousRateAppRequestTimeInMs
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
            HomeAction.DismissAppUpdateSheet -> _homeState.update { state ->
                state.copy(latestAppUpdateInfo = null)
            }
            HomeAction.RequestInAppReviews -> requestInAppReviews()
            HomeAction.CheckSelectedRateAppOption -> checkSelectedRateAppOption()
            HomeAction.LaunchInAppReview -> launchInAppReview()
            is HomeAction.SetSelectedRateAppOptionToNever -> setSelectedRateAppOptionToNever()
            is HomeAction.SetSelectedRateAppOptionToRemindLater -> setSelectedRateAppOptionToRemindLater()
            is HomeAction.OnPermissionResult -> onPermissionResult(
                permission = action.permission,
                isGranted = action.isGranted
            )
            is HomeAction.DismissPermissionDialog -> dismissPermissionDialog(permission = action.permission)
        }
    }

    private fun checkIsAppUpdateStalled() = viewModelScope.launch {
        when (appUpdateType.get()) {
            AppUpdateType.IMMEDIATE -> {
                homeUseCases.checkIsImmediateUpdateStalledUseCase.get()().collect { appUpdateInfo ->
                    appUpdateInfo?.let {
                        _inAppUpdatesEventChannel.send(HomeUiEvent.StartAppUpdateFlow(appUpdateInfo))
                    }
                }
            }
            AppUpdateType.FLEXIBLE -> {
                homeUseCases.checkIsFlexibleUpdateStalledUseCase.get()().collect { isUpdateDownloaded ->
                    if (isUpdateDownloaded) {
                        _inAppUpdatesEventChannel.send(HomeUiEvent.CompleteFlexibleAppUpdate)
                    }
                }
            }
            else -> Unit
        }
    }

    private fun checkFlexibleUpdateDownloadState() = viewModelScope.launch {
        homeUseCases.checkFlexibleUpdateDownloadStateUseCase.get()().collect { isUpdateDownloaded ->
            if (isUpdateDownloaded) {
                _inAppUpdatesEventChannel.send(HomeUiEvent.ShowCompleteAppUpdateSnackbar)
            }
        }
    }

    private fun checkForAppUpdates() = viewModelScope.launch {
        homeUseCases.checkForAppUpdatesUseCase.get()().collect { appUpdateInfo ->
            appUpdateInfo?.let {
                _inAppUpdatesEventChannel.send(HomeUiEvent.StartAppUpdateFlow(appUpdateInfo))
            }
        }
    }

    private fun getLatestAppVersion() = viewModelScope.launch {
        if (NetworkObserverHelper.networkStatus.value == ConnectivityObserver.Status.AVAILABLE) {
            when (val result = homeUseCases.getLatestAppVersionUseCase.get()()) {
                is Result.Success -> result.data?.let { latestAppUpdateInfo ->
                    _homeState.update { state ->
                        state.copy(latestAppUpdateInfo = latestAppUpdateInfo)
                    }
                }
                is Result.Error -> Unit
            }
        }
    }

    private fun requestInAppReviews() = viewModelScope.launch {
        homeUseCases.requestInAppReviewsUseCase.get()().collect { reviewInfo ->
            _homeState.update { state ->
                state.copy(inAppReviewInfo = reviewInfo)
            }
            checkSelectedRateAppOption()
        }
    }

    private fun checkSelectedRateAppOption() = viewModelScope.launch {
        when (homeState.value.selectedRateAppOption) {
            RateAppOption.NOT_SHOWN_YET, RateAppOption.RATE_NOW -> checkDaysFromFirstInstallTime()
            RateAppOption.REMIND_LATER -> checkDaysFromPreviousRequestTime()
            RateAppOption.NEVER, null -> Unit
        }
    }

    private suspend fun checkDaysFromFirstInstallTime() {
        val daysFromFirstInstallTime = DateHelper.getDaysFromFirstInstallTime(
            firstInstallTimeInMs = firstInstallTimeInMs.get()
        )

        val shouldShowAppReviewDialog =
            daysFromFirstInstallTime >= Constants.IN_APP_REVIEWS_DAYS_FROM_FIRST_INSTALL_TIME
        if (shouldShowAppReviewDialog) {
            _inAppReviewsEventChannel.send(HomeUiEvent.ShowAppReviewDialog)
        }
    }

    private suspend fun checkDaysFromPreviousRequestTime() {
        homeState.value.previousRateAppRequestTimeInMs?.let {
            val daysFromPreviousRequestTime = DateHelper.getDaysFromPreviousRequestTime(it)

            val shouldShowAppReviewDialog =
                daysFromPreviousRequestTime >= Constants.IN_APP_REVIEWS_DAYS_FROM_PREVIOUS_REQUEST_TIME
            if (shouldShowAppReviewDialog) {
                _inAppReviewsEventChannel.send(HomeUiEvent.ShowAppReviewDialog)
            }
        }
    }

    private fun launchInAppReview() = viewModelScope.launch {
        _inAppReviewsEventChannel.send(HomeUiEvent.LaunchInAppReview)
    }

    private fun setSelectedRateAppOptionToNever() = viewModelScope.launch {
        val rateAppOption = RateAppOption.NEVER
        homeUseCases.storeSelectedRateAppOptionUseCase.get()(rateAppOption)

        _homeState.update { state ->
            state.copy(selectedRateAppOption = rateAppOption)
        }
    }

    private fun setSelectedRateAppOptionToRemindLater() = viewModelScope.launch {
        val rateAppOption = RateAppOption.REMIND_LATER
        homeUseCases.storeSelectedRateAppOptionUseCase.get()(rateAppOption)
        homeUseCases.storePreviousRateAppRequestTimeInMsUseCase.get()()

        _homeState.update { state ->
            state.copy(selectedRateAppOption = rateAppOption)
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
        val newCount = homeState.value.notificationPermissionCount.plus(1)

        homeUseCases.storeNotificationPermissionCountUseCase.get()(count = newCount)

        _homeState.update { state ->
            state.copy(
                permissionDialogQueue = homeState.value.permissionDialogQueue.toMutableList()
                    .apply { remove(permission) }.toList(),
                isPermissionDialogVisible = false
            )
        }
    }
}