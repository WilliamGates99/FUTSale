package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.states.MainActivityState
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.use_case.MainUseCases
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

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
        getCurrentAppLocale()
        getPostSplashDestination()
    }

    private fun getCurrentAppLocale() = viewModelScope.launch {
        savedStateHandle["mainState"] = mainState.value.copy(
            currentAppLocale = mainUseCases.getCurrentAppLocaleUseCase.get()()
        )
    }

    private fun getPostSplashDestination() = viewModelScope.launch {
        val isOnboardingCompleted = mainUseCases.getIsOnboardingCompletedUseCase.get()()

        if (isOnboardingCompleted) {
            savedStateHandle["mainState"] = mainState.value.copy(
                postSplashDestination = Screen.HomeScreen
            )
        } else {
            savedStateHandle["mainState"] = mainState.value.copy(
                postSplashDestination = Screen.OnboardingScreen
            )
        }

        delay(1.seconds) // 1 second delay to solve the blank screen after showing splash screen
        savedStateHandle["mainState"] = mainState.value.copy(
            isSplashScreenLoading = false
        )
    }
}