package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.main_activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.use_case.MainUseCases
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.main_activity.states.MainActivityState
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.navigation.HomeScreen
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.navigation.OnboardingScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainUseCases: MainUseCases
) : ViewModel() {

    private val _mainState = MutableStateFlow(MainActivityState())
    val mainState = _mainState.onStart {
        getMainState()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeout = 5.seconds),
        initialValue = MainActivityState()
    )

    private fun getMainState() = viewModelScope.launch {
        _mainState.update { state ->
            state.copy(
                currentAppLocale = mainUseCases.getCurrentAppLocaleUseCase.get()(),
                postSplashDestination = getPostSplashDestination(),
                isSplashScreenLoading = false
            )
        }
    }

    private suspend fun getPostSplashDestination(): Any {
        val isOnboardingCompleted = mainUseCases.getIsOnboardingCompletedUseCase.get()()

        return if (isOnboardingCompleted) HomeScreen
        else OnboardingScreen
    }
}