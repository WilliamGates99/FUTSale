package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.main_activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.use_case.MainUseCases
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.common.ui.navigation.screens.HomeScreen
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.common.ui.navigation.screens.OnboardingScreen
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.main_activity.states.MainActivityState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.zip
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainUseCases: MainUseCases
) : ViewModel() {

    private val _state = MutableStateFlow(MainActivityState())
    val state = _state.onStart {
        getMainStateData()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeout = 5.seconds),
        initialValue = _state.value
    )

    private fun getMainStateData() {
        mainUseCases.getCurrentAppLocaleUseCase.get()().zip(
            other = mainUseCases.getIsOnboardingCompletedUseCase.get()(),
            transform = { currentAppLocale, isOnboardingCompleted ->
                _state.update {
                    it.copy(
                        currentAppLocale = currentAppLocale,
                        postSplashDestination = getPostSplashDestination(isOnboardingCompleted)
                    )
                }
            }
        ).onCompletion {
            _state.update {
                it.copy(isSplashScreenLoading = false)
            }
        }.launchIn(scope = viewModelScope)
    }

    private fun getPostSplashDestination(
        isOnboardingCompleted: Boolean
    ): Any {
        return if (isOnboardingCompleted) HomeScreen
        else OnboardingScreen
    }
}