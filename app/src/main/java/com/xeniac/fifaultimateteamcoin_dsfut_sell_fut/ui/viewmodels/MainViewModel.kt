package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.domain.repository.PreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val preferencesRepository: PreferencesRepository
) : ViewModel() {

    fun isOnBoardingCompleted() = preferencesRepository.isOnBoardingCompletedSynchronously()

}