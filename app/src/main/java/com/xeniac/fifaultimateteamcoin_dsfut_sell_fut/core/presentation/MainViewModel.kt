package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.states.MainActivityState
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.use_case.MainUseCases
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainUseCases: MainUseCases,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val mainState = savedStateHandle.getStateFlow(
        key = "mainState",
        initialValue = MainActivityState()
    )

    init {
        getMainState()
    }

    private fun getMainState() = viewModelScope.launch {
        savedStateHandle["mainState"] = mainState.value.copy(
            currentAppLocale = mainUseCases.getCurrentAppLocaleUseCase.get()(),
            postSplashDestination = getPostSplashDestination()
        )

        savedStateHandle["mainState"] = mainState.value.copy(
            isSplashScreenLoading = false
        )
    }

    private suspend fun getPostSplashDestination(): Screen {
        val isOnboardingCompleted = mainUseCases.getIsOnboardingCompletedUseCase.get()()

        return if (isOnboardingCompleted) Screen.HomeScreen
        else Screen.OnboardingScreen
    }
}