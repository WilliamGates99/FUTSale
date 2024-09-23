package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.domain.use_cases

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.MainCoroutineRule
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.repositories.FakeDsfutDataStoreRepositoryImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(JUnit4::class)
class GetProfileUseCaseTest {

    @get:Rule
    var instanceTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var fakeDsfutDataStoreRepositoryImpl: FakeDsfutDataStoreRepositoryImpl
    private lateinit var getProfileUseCase: GetProfileUseCase

    @Before
    fun setUp() {
        fakeDsfutDataStoreRepositoryImpl = FakeDsfutDataStoreRepositoryImpl()
        getProfileUseCase = GetProfileUseCase(
            dsfutDataStoreRepository = fakeDsfutDataStoreRepositoryImpl
        )
    }

    @Test
    fun getProfileWithNullValues_returnsEmptyPartnerIdSecretKey() = runTest {
        fakeDsfutDataStoreRepositoryImpl.changePartnerId(newPartnerId = null)
        fakeDsfutDataStoreRepositoryImpl.changeSecretKey(newSecretKey = null)

        val currentProfileState = getProfileUseCase()

        assertThat(currentProfileState.partnerId).isEmpty()
        assertThat(currentProfileState.secretKey).isEmpty()
        assertThat(currentProfileState.isPartnerIdSaved).isFalse()
        assertThat(currentProfileState.isSecretKeySaved).isFalse()
    }

    @Test
    fun getProfileWithNonNullPartnerId_returnsPartnerIdAndTrueIsPartnerIdSaved() = runTest {
        fakeDsfutDataStoreRepositoryImpl.changePartnerId(newPartnerId = "123")

        val currentProfileState = getProfileUseCase()

        assertThat(currentProfileState.partnerId).isEqualTo(
            fakeDsfutDataStoreRepositoryImpl.storedPartnerId
        )
        assertThat(currentProfileState.isPartnerIdSaved).isTrue()
    }

    @Test
    fun getProfileWithNonNullSecretKey_returnsPartnerIdAndTrueIsSecretKeySaved() = runTest {
        fakeDsfutDataStoreRepositoryImpl.changeSecretKey(newSecretKey = "abc123")

        val currentProfileState = getProfileUseCase()

        assertThat(currentProfileState.secretKey).isEqualTo(
            fakeDsfutDataStoreRepositoryImpl.storedSecretKey
        )
        assertThat(currentProfileState.isSecretKeySaved).isTrue()
    }

    @Test
    fun getProfileWithNonNullValues_returnsPartnerIdAndSecretKey() = runTest {
        fakeDsfutDataStoreRepositoryImpl.changePartnerId(newPartnerId = "123")
        fakeDsfutDataStoreRepositoryImpl.changeSecretKey(newSecretKey = "abc123")

        val currentProfileState = getProfileUseCase()

        assertThat(currentProfileState.partnerId).isEqualTo(
            fakeDsfutDataStoreRepositoryImpl.storedPartnerId
        )
        assertThat(currentProfileState.secretKey).isEqualTo(
            fakeDsfutDataStoreRepositoryImpl.storedSecretKey
        )
        assertThat(currentProfileState.isPartnerIdSaved).isTrue()
        assertThat(currentProfileState.isSecretKeySaved).isTrue()
    }
}