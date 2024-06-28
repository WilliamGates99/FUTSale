package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.domain.use_cases

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.MainCoroutineRule
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.repositories.FakePreferencesRepositoryImpl
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.utils.Result
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.domain.utils.PartnerIdError
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.domain.validation.ValidatePartnerId
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(JUnit4::class)
class UpdatePartnerIdUseCaseTest {

    @get:Rule
    var instanceTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private val validatePartnerId: ValidatePartnerId = ValidatePartnerId()
    private lateinit var fakePreferencesRepository: FakePreferencesRepositoryImpl
    private lateinit var updatePartnerIdUseCase: UpdatePartnerIdUseCase

    @Before
    fun setUp() {
        fakePreferencesRepository = FakePreferencesRepositoryImpl()
        updatePartnerIdUseCase = UpdatePartnerIdUseCase(
            preferencesRepository = fakePreferencesRepository,
            validatePartnerId = validatePartnerId
        )
    }

    @Test
    fun updatePartnerIdWithEmptyCharacters_returnsSuccess() = runTest {
        val newPartnerId = ""
        val updatePartnerIdResult = updatePartnerIdUseCase(newPartnerId)

        assertThat(updatePartnerIdResult.result).isInstanceOf(Result.Success::class.java)
    }

    @Test
    fun updatePartnerIdWithNonDigitsCharacters_returnsError() = runTest {
        val newPartnerId = "abc"
        val updatePartnerIdResult = updatePartnerIdUseCase(newPartnerId)

        assertThat(updatePartnerIdResult.partnerIdError).isInstanceOf(PartnerIdError.InvalidPartnerId::class.java)
    }

    @Test
    fun updatePartnerIdWithValidCharacters_returnsSuccess() = runTest {
        val newPartnerId = "123"
        val updatePartnerIdResult = updatePartnerIdUseCase(newPartnerId)

        assertThat(updatePartnerIdResult.result).isInstanceOf(Result.Success::class.java)
    }
}