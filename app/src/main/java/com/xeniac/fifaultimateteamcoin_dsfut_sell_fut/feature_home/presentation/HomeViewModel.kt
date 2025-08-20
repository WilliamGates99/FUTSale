package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.review.ReviewManager
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.RateAppOption
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.Result
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.common.utils.Event
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.common.utils.NetworkObserverHelper.hasNetworkConnection
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.common.utils.isAppInstalledFromPlayStore
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.di.FirstInstallTimeInMs
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.repositories.UpdateType
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.use_case.HomeUseCases
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.presentation.states.HomeState
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.presentation.util.Constants
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.presentation.util.DateHelper
import dagger.Lazy
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeUseCases: HomeUseCases,
    private val appUpdateType: Lazy<UpdateType>,
    val appUpdateManager: Lazy<AppUpdateManager>,
    val appUpdateOptions: Lazy<AppUpdateOptions>,
    val reviewManager: Lazy<ReviewManager>,
    private val firstInstallTimeInMs: Lazy<FirstInstallTimeInMs>
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state = combine(
        flow = _state,
        flow2 = homeUseCases.getNotificationPermissionCountUseCase.get()(),
        flow3 = homeUseCases.getSelectedRateAppOptionUseCase.get()(),
        flow4 = homeUseCases.getPreviousRateAppRequestDateTimeUseCase.get()()
    ) { state, notificationPermissionCount, selectedRateAppOption, previousRateAppRequestDateTime ->
        _state.update {
            state.copy(
                notificationPermissionCount = notificationPermissionCount,
                selectedRateAppOption = selectedRateAppOption,
                previousRateAppRequestDateTime = previousRateAppRequestDateTime
            )
        }
        _state.value
    }.onStart {
        when {
            isAppInstalledFromPlayStore() -> {
                checkIsAppUpdateStalled()
                checkFlexibleUpdateDownloadState()
                checkForAppUpdates()
                requestInAppReviews()
            }
            else -> getLatestAppVersion()
        }
    }.take(count = 1).onEach {
        if (!isAppInstalledFromPlayStore()) {
            checkSelectedRateAppOption(selectedRateAppOption = it.selectedRateAppOption)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeout = 30.seconds),
        initialValue = _state.value
    )

    private val _inAppUpdatesEventChannel = Channel<Event>()
    val inAppUpdatesEventChannel = _inAppUpdatesEventChannel.receiveAsFlow()

    private val _inAppReviewsEventChannel = Channel<Event>()
    val inAppReviewEventChannel = _inAppReviewsEventChannel.receiveAsFlow()

    fun onAction(action: HomeAction) {
        when (action) {
            HomeAction.CheckIsAppUpdateStalled -> checkIsAppUpdateStalled()
            HomeAction.CheckFlexibleUpdateDownloadState -> checkFlexibleUpdateDownloadState()
            HomeAction.CheckForAppUpdates -> checkForAppUpdates()
            HomeAction.GetLatestAppVersion -> getLatestAppVersion()
            HomeAction.DismissAppUpdateSheet -> dismissAppUpdateSheet()
            HomeAction.RequestInAppReviews -> requestInAppReviews()
            is HomeAction.CheckSelectedRateAppOption -> checkSelectedRateAppOption(action.selectedRateAppOption)
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

    private fun checkForAppUpdates() {
        homeUseCases.checkForAppUpdatesUseCase.get()().onEach { appUpdateInfo ->
            appUpdateInfo?.let {
                _inAppUpdatesEventChannel.send(HomeUiEvent.StartAppUpdateFlow(appUpdateInfo))
            }
        }.launchIn(scope = viewModelScope)
    }

    private fun getLatestAppVersion() {
        if (hasNetworkConnection()) {
            homeUseCases.getLatestAppVersionUseCase.get()().onEach { result ->
                when (result) {
                    is Result.Success -> result.data?.let { latestAppUpdateInfo ->
                        _state.update {
                            it.copy(latestAppUpdateInfo = latestAppUpdateInfo)
                        }
                    }
                    is Result.Error -> Unit
                }
            }.launchIn(scope = viewModelScope)
        }
    }

    private fun dismissAppUpdateSheet() = viewModelScope.launch {
        _state.update {
            it.copy(latestAppUpdateInfo = null)
        }
    }

    private fun requestInAppReviews() {
        homeUseCases.requestInAppReviewsUseCase.get()().onEach { reviewInfo ->
            _state.update {
                it.copy(inAppReviewInfo = reviewInfo)
            }
            delay(timeMillis = 100) // Wait for the state to be updated
            checkSelectedRateAppOption(_state.value.selectedRateAppOption)
        }.launchIn(scope = viewModelScope)
    }

    private fun checkSelectedRateAppOption(
        selectedRateAppOption: RateAppOption?
    ) = viewModelScope.launch {
        when (selectedRateAppOption) {
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
            _state.update {
                it.copy(isAppReviewDialogVisible = true)
            }
        }
    }

    private fun checkDaysFromPreviousRequestTime() {
        _state.value.previousRateAppRequestDateTime?.let { previousRateAppRequestDateTime ->
            val daysFromPreviousRequestTime = DateHelper.getDaysFromPreviousRequestTime(
                previousRequestTime = previousRateAppRequestDateTime
            )

            val shouldShowAppReviewDialog =
                daysFromPreviousRequestTime >= Constants.IN_APP_REVIEWS_DAYS_FROM_PREVIOUS_REQUEST_TIME
            if (shouldShowAppReviewDialog) {
                _state.update {
                    it.copy(isAppReviewDialogVisible = true)
                }
            }
        }
    }

    private fun launchInAppReview() = viewModelScope.launch {
        _inAppReviewsEventChannel.send(HomeUiEvent.LaunchInAppReview)
    }

    private fun setSelectedRateAppOptionToNever() {
        homeUseCases.storeSelectedRateAppOptionUseCase.get()(
            rateAppOption = RateAppOption.NEVER
        ).launchIn(scope = viewModelScope)
    }

    private fun setSelectedRateAppOptionToRemindLater() {
        homeUseCases.storeSelectedRateAppOptionUseCase.get()(
            rateAppOption = RateAppOption.REMIND_LATER
        ).zip(
            other = homeUseCases.storePreviousRateAppRequestDateTimeUseCase.get()(),
            transform = { _, _ -> }
        ).launchIn(scope = viewModelScope)
    }

    private fun dismissAppReviewDialog() = viewModelScope.launch {
        _state.update {
            it.copy(isAppReviewDialogVisible = false)
        }
    }

    private fun onPermissionResult(
        permission: String,
        isGranted: Boolean
    ) = viewModelScope.launch {
        val shouldAskForPermission = _state.value.run {
            notificationPermissionCount < 1 && !permissionDialogQueue.contains(permission) && !isGranted
        }

        if (shouldAskForPermission) {
            _state.update {
                it.copy(
                    permissionDialogQueue = listOf(permission),
                    isPermissionDialogVisible = true
                )
            }
        }
    }

    private fun dismissPermissionDialog(
        permission: String
    ) {
        homeUseCases.storeNotificationPermissionCountUseCase.get()(
            count = _state.value.notificationPermissionCount.plus(1)
        ).onCompletion {
            _state.update {
                it.copy(
                    permissionDialogQueue = _state.value.permissionDialogQueue
                        .toMutableList().apply { remove(permission) }.toList(),
                    isPermissionDialogVisible = false
                )
            }
        }.launchIn(scope = viewModelScope)
    }
}