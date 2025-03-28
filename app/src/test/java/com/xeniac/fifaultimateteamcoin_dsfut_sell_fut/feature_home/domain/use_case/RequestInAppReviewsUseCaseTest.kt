package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.use_case

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.MainCoroutineRule
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.data.repositories.FakeAppReviewRepositoryImpl
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

    private lateinit var fakeAppReviewRepositoryImpl: FakeAppReviewRepositoryImpl
    private lateinit var requestInAppReviewsUseCase: RequestInAppReviewsUseCase

    @Before
    fun setUp() {
        fakeAppReviewRepositoryImpl = FakeAppReviewRepositoryImpl()

        requestInAppReviewsUseCase = RequestInAppReviewsUseCase(
            appReviewRepository = fakeAppReviewRepositoryImpl
        )
    }

    @Test
    fun requestInAppReviewsWithNoReviewAvailable_returnsNull() = runTest {
        fakeAppReviewRepositoryImpl.isInAppReviewsAvailable(isAvailable = false)
        val reviewInfo = requestInAppReviewsUseCase().first()
        assertThat(reviewInfo).isNull()
    }
}