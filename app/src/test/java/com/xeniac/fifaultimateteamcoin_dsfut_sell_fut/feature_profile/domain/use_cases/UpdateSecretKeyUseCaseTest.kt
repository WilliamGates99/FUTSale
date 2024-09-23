package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.domain.use_cases

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.MainCoroutineRule
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.repositories.FakeDsfutDataStoreRepositoryImpl
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.utils.Result
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.domain.validation.ValidateSecretKey
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(JUnit4::class)
class UpdateSecretKeyUseCaseTest {

    @get:Rule
    var instanceTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private val validateSecretKey: ValidateSecretKey = ValidateSecretKey()
    private lateinit var fakeDsfutDataStoreRepositoryImpl: FakeDsfutDataStoreRepositoryImpl
    private lateinit var updateSecretKeyUseCase: UpdateSecretKeyUseCase

    @Before
    fun setUp() {
        fakeDsfutDataStoreRepositoryImpl = FakeDsfutDataStoreRepositoryImpl()
        updateSecretKeyUseCase = UpdateSecretKeyUseCase(
            dsfutDataStoreRepository = fakeDsfutDataStoreRepositoryImpl,
            validateSecretKey = validateSecretKey
        )
    }

    @Test
    fun updateSecretKeyWithEmptyCharacters_returnsSuccess() = runTest {
        val newSecretKey = ""
        val updateSecretKeyResult = updateSecretKeyUseCase(newSecretKey)

        assertThat(updateSecretKeyResult.result).isInstanceOf(Result.Success::class.java)
    }

    @Test
    fun updateSecretKeyWithValidCharacters_returnsSuccess() = runTest {
        val newSecretKey = "abc123"
        val updateSecretKeyResult = updateSecretKeyUseCase(newSecretKey)

        assertThat(updateSecretKeyResult.result).isInstanceOf(Result.Success::class.java)
    }
}