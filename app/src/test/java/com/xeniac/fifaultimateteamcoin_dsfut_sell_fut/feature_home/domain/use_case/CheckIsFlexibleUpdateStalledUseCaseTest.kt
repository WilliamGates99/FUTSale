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
class CheckIsFlexibleUpdateStalledUseCaseTest {

    @get:Rule
    var instanceTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var fakePreferencesRepositoryImpl: FakePreferencesRepositoryImpl
    private lateinit var fakeHomeRepository: FakeHomeRepositoryImpl
    private lateinit var checkIsFlexibleUpdateStalledUseCase: CheckIsFlexibleUpdateStalledUseCase

    @Before
    fun setUp() {
        fakePreferencesRepositoryImpl = FakePreferencesRepositoryImpl()
        fakeHomeRepository = FakeHomeRepositoryImpl(
            preferencesRepository = { fakePreferencesRepositoryImpl }
        )

        checkIsFlexibleUpdateStalledUseCase = CheckIsFlexibleUpdateStalledUseCase(
            homeRepository = fakeHomeRepository
        )
    }

    @Test
    fun checkIsFlexibleUpdateStalledWithNoUpdateDownloaded_returnsFalse() = runTest {
        fakeHomeRepository.isFlexibleUpdateStalled(isStalled = false)
        val isUpdateDownloaded = checkIsFlexibleUpdateStalledUseCase().first()
        assertThat(isUpdateDownloaded).isFalse()
    }

    @Test
    fun checkIsFlexibleUpdateStalledWithUpdateDownloaded_returnsTrue() = runTest {
        fakeHomeRepository.isFlexibleUpdateStalled(isStalled = true)
        val isUpdateDownloaded = checkIsFlexibleUpdateStalledUseCase().first()
        assertThat(isUpdateDownloaded).isTrue()
    }
}