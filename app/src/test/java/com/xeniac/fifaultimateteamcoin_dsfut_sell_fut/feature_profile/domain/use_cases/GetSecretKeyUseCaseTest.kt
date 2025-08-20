package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.domain.use_cases

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.MainCoroutineRule
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.repositories.FakeDsfutDataStoreRepositoryImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(JUnit4::class)
class GetSecretKeyUseCaseTest {

    @get:Rule
    var instanceTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var fakeDsfutDataStoreRepositoryImpl: FakeDsfutDataStoreRepositoryImpl
    private lateinit var getSecretKeyUseCase: GetSecretKeyUseCase

    @Before
    fun setUp() {
        fakeDsfutDataStoreRepositoryImpl = FakeDsfutDataStoreRepositoryImpl()
        getSecretKeyUseCase = GetSecretKeyUseCase(
            dsfutDataStoreRepository = fakeDsfutDataStoreRepositoryImpl
        )
    }

    @Test
    fun getSecretKeyWithNullValue_returnsEmptySecretKey() = runTest {
        fakeDsfutDataStoreRepositoryImpl.changeSecretKey(newSecretKey = null)

        getSecretKeyUseCase().onEach { currentSecretKey ->
            assertThat(currentSecretKey).isNull()
        }
    }

    @Test
    fun getSecretKeyWithNonNullValue_returnsSecretKey() = runTest {
        val testValue = "abc123"
        fakeDsfutDataStoreRepositoryImpl.changeSecretKey(newSecretKey = testValue)

        getSecretKeyUseCase().onEach { currentSecretKey ->
            assertThat(currentSecretKey).isNotNull()
            assertThat(currentSecretKey).isEqualTo(testValue)
        }
    }
}