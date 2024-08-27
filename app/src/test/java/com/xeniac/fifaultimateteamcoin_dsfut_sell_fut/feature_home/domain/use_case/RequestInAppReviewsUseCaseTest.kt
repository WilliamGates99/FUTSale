package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.use_case

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.MainCoroutineRule
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.repositories.FakePreferencesRepositoryImpl
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.data.repositories.FakeHomeRepositoryImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(JUnit4::class)
class RequestInAppReviewsUseCaseTest {

    @get:Rule
    var instanceTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var fakePreferencesRepositoryImpl: FakePreferencesRepositoryImpl
    private lateinit var fakeHomeRepository: FakeHomeRepositoryImpl
    private lateinit var requestInAppReviewsUseCase: RequestInAppReviewsUseCase

    @Before
    fun setUp() {
        fakePreferencesRepositoryImpl = FakePreferencesRepositoryImpl()
        fakeHomeRepository = FakeHomeRepositoryImpl(
            preferencesRepository = { fakePreferencesRepositoryImpl }
        )

        requestInAppReviewsUseCase = RequestInAppReviewsUseCase(
            homeRepository = fakeHomeRepository
        )
    }

    @Test
    fun requestInAppReviewsWithNoReviewAvailable_returnsNull() = runTest {
        fakeHomeRepository.isInAppReviewsAvailable(isAvailable = false)
        val reviewInfo = requestInAppReviewsUseCase().first()
        assertThat(reviewInfo).isNull()
    }
}