package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.review.ReviewManager
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.RateAppOption
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.Event
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
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val appUpdateType: Lazy<UpdateType>,
    val appUpdateManager: Lazy<AppUpdateManager>,
    val appUpdateOptions: Lazy<AppUpdateOptions>,
    val reviewManager: Lazy<ReviewManager>,
    private val firstInstallTimeInMs: Lazy<FirstInstallTimeInMs>,
    private val homeUseCases: HomeUseCases,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val mutex: Mutex = Mutex()

    val homeState = savedStateHandle.getStateFlow(
        key = "homeState",
        initialValue = HomeState()
    )

    private val _inAppUpdatesEventChannel = Channel<Event>()
    val inAppUpdatesEventChannel = _inAppUpdatesEventChannel.receiveAsFlow()

    private val _inAppReviewsEventChannel = Channel<Event>()
    val inAppReviewEventChannel = _inAppReviewsEventChannel.receiveAsFlow()

    init {
        checkIsAppUpdateStalled()
        checkFlexibleUpdateDownloadState()
        getHomeState()
        checkForAppUpdates()
    }

    fun onEvent(event: HomeEvent) {
        when (event) {
            HomeEvent.CheckIsAppUpdateStalled -> checkIsAppUpdateStalled()
            HomeEvent.CheckFlexibleUpdateDownloadState -> checkFlexibleUpdateDownloadState()
            HomeEvent.GetHomeState -> getHomeState()
            HomeEvent.CheckForAppUpdates -> checkForAppUpdates()
            HomeEvent.RequestInAppReviews -> requestInAppReviews()
            HomeEvent.CheckSelectedRateAppOption -> checkSelectedRateAppOption()
            HomeEvent.LaunchInAppReview -> launchInAppReview()
            is HomeEvent.SetSelectedRateAppOptionToNever -> setSelectedRateAppOptionToNever()
            is HomeEvent.SetSelectedRateAppOptionToRemindLater -> setSelectedRateAppOptionToRemindLater()
            is HomeEvent.OnPermissionResult -> onPermissionResult(
                permission = event.permission,
                isGranted = event.isGranted
            )
            is HomeEvent.DismissPermissionDialog -> dismissPermissionDialog(permission = event.permission)
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

    private fun getHomeState() = viewModelScope.launch {
        mutex.withLock {
            savedStateHandle["homeState"] = homeState.value.copy(
                notificationPermissionCount = homeUseCases.getNotificationPermissionCountUseCase.get()(),
                selectedRateAppOption = homeUseCases.getSelectedRateAppOptionUseCase.get()(),
                previousRateAppRequestTimeInMs = homeUseCases.getPreviousRateAppRequestTimeInMsUseCase.get()()
            )
        }
    }

    private fun checkForAppUpdates() = viewModelScope.launch {
        homeUseCases.checkForAppUpdatesUseCase.get()().collect { appUpdateInfo ->
            appUpdateInfo?.let {
                _inAppUpdatesEventChannel.send(HomeUiEvent.StartAppUpdateFlow(appUpdateInfo))
            }
        }
    }

    private fun requestInAppReviews() = viewModelScope.launch {
        mutex.withLock {
            homeUseCases.requestInAppReviewsUseCase.get()().collect { reviewInfo ->
                savedStateHandle["homeState"] = homeState.value.copy(
                    inAppReviewInfo = reviewInfo
                )
                checkSelectedRateAppOption()
            }
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
        homeUseCases.setSelectedRateAppOptionUseCase.get()(rateAppOption)
        savedStateHandle["homeState"] = homeState.value.copy(
            selectedRateAppOption = rateAppOption
        )
    }

    private fun setSelectedRateAppOptionToRemindLater() = viewModelScope.launch {
        val rateAppOption = RateAppOption.REMIND_LATER
        homeUseCases.setSelectedRateAppOptionUseCase.get()(rateAppOption)
        homeUseCases.setPreviousRateAppRequestTimeInMsUseCase.get()()

        savedStateHandle["homeState"] = homeState.value.copy(
            selectedRateAppOption = rateAppOption
        )
    }

    private fun onPermissionResult(
        permission: String,
        isGranted: Boolean
    ) = viewModelScope.launch {
        val shouldAskForPermission = homeState.value.run {
            notificationPermissionCount < 1 && !permissionDialogQueue.contains(permission) && !isGranted
        }

        if (shouldAskForPermission) {
            savedStateHandle["homeState"] = homeState.value.copy(
                permissionDialogQueue = listOf(permission),
                isPermissionDialogVisible = true
            )
        }
    }

    private fun dismissPermissionDialog(permission: String) = viewModelScope.launch {
        val newCount = homeState.value.notificationPermissionCount.plus(1)

        homeUseCases.setNotificationPermissionCountUseCase.get()(count = newCount)

        savedStateHandle["homeState"] = homeState.value.copy(
            notificationPermissionCount = newCount,
            permissionDialogQueue = homeState.value.permissionDialogQueue.toMutableList().apply {
                remove(permission)
            }.toList(),
            isPermissionDialogVisible = false
        )
    }
}