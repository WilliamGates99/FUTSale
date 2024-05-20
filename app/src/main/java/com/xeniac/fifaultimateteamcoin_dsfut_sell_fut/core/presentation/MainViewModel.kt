package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.use_case.GetIsOnboardingCompletedUseCase
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.navigation.Screen
import dagger.Lazy
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getIsOnboardingCompletedUseCase: Lazy<GetIsOnboardingCompletedUseCase>
) : ViewModel() {

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    private val _postSplashDestination = MutableStateFlow<Screen?>(null)
    val postSplashDestination = _postSplashDestination.asStateFlow()

    init {
        getPostSplashDestination()
    }

    private fun getPostSplashDestination() = viewModelScope.launch {
        val isOnboardingCompleted = getIsOnboardingCompletedUseCase.get()()

        if (isOnboardingCompleted) {
            _postSplashDestination.value = Screen.HomeScreen
        } else {
            _postSplashDestination.value = Screen.OnboardingScreen
        }

        delay(1.seconds) // 1 second delay to solve the blank screen after showing splash screen
        _isLoading.value = false
    }
}