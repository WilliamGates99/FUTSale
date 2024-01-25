package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.use_case.HomeUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeUseCases: HomeUseCases
) : ViewModel() {

    private val notificationPermissionCount = MutableStateFlow(0)

    private val _permissionDialogQueue = MutableStateFlow<MutableList<String>>(mutableListOf())
    val permissionDialogQueue = _permissionDialogQueue.asStateFlow()

    private val _isPermissionDialogVisible = MutableStateFlow(false)
    val isPermissionDialogVisible = _isPermissionDialogVisible.asStateFlow()

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.OnPermissionResult -> onPermissionResult(
                permission = event.permission,
                isGranted = event.isGranted
            )
            is HomeEvent.DismissDialog -> dismissDialog(permission = event.permission)
        }
    }

    private fun onPermissionResult(
        permission: String,
        isGranted: Boolean
    ) = viewModelScope.launch {
        notificationPermissionCount.value = homeUseCases
            .getNotificationPermissionCountUseCase.get()()

        val shouldAskForNotificationPermission = notificationPermissionCount.value < 1 &&
                !isGranted && !permissionDialogQueue.value.contains(permission)

        if (shouldAskForNotificationPermission) {
            _permissionDialogQueue.value.add(permission)
            _isPermissionDialogVisible.value = true
        }
    }

    private fun dismissDialog(permission: String) = viewModelScope.launch {
        homeUseCases.setNotificationPermissionCountUseCase.get()(
            count = notificationPermissionCount.value.plus(1)
        )
        _permissionDialogQueue.value.remove(permission)
        _isPermissionDialogVisible.value = false
    }
}