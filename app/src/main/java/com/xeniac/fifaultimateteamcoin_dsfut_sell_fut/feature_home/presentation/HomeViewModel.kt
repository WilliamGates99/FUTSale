package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.states.HomeState
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.use_case.HomeUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeUseCases: HomeUseCases,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val homeState = savedStateHandle.getStateFlow(
        key = "homeState",
        initialValue = HomeState()
    )

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.OnPermissionResult -> onPermissionResult(
                permission = event.permission,
                isGranted = event.isGranted
            )
            is HomeEvent.DismissPermissionDialog -> dismissPermissionDialog(permission = event.permission)
        }
    }

    private fun onPermissionResult(
        permission: String,
        isGranted: Boolean
    ) = viewModelScope.launch {
        savedStateHandle["homeState"] = homeState.value.copy(
            notificationPermissionCount = homeUseCases.getNotificationPermissionCountUseCase.get()()
        )

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
        homeUseCases.setNotificationPermissionCountUseCase.get()(
            count = homeState.value.notificationPermissionCount.plus(1)
        )

        savedStateHandle["homeState"] = homeState.value.copy(
            permissionDialogQueue = emptyList(),
            isPermissionDialogVisible = false
        )
    }
}