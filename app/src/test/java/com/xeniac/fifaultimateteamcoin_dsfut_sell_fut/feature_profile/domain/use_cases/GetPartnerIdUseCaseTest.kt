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
class GetPartnerIdUseCaseTest {

    @get:Rule
    var instanceTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var fakeDsfutDataStoreRepositoryImpl: FakeDsfutDataStoreRepositoryImpl
    private lateinit var getPartnerIdUseCase: GetPartnerIdUseCase

    @Before
    fun setUp() {
        fakeDsfutDataStoreRepositoryImpl = FakeDsfutDataStoreRepositoryImpl()
        getPartnerIdUseCase = GetPartnerIdUseCase(
            dsfutDataStoreRepository = fakeDsfutDataStoreRepositoryImpl
        )
    }

    @Test
    fun getPartnerIdWithNullValue_returnsEmptyPartnerId() = runTest {
        fakeDsfutDataStoreRepositoryImpl.changePartnerId(newPartnerId = null)

        getPartnerIdUseCase().onEach { currentPartnerId ->
            assertThat(currentPartnerId).isNull()
        }
    }

    @Test
    fun getSecretKeyWithNonNullValue_returnsSecretKey() = runTest {
        val testValue = "123"
        fakeDsfutDataStoreRepositoryImpl.changePartnerId(newPartnerId = testValue)

        getPartnerIdUseCase().onEach { currentPartnerId ->
            assertThat(currentPartnerId).isNotNull()
            assertThat(currentPartnerId).isEqualTo(testValue)
        }
    }
}