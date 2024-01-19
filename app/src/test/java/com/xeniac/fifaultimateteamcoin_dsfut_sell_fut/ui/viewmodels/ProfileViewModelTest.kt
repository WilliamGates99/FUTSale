package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.ui.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.MainCoroutineRule
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.data.repository.FakePreferencesRepositoryImp
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.getOrAwaitValue
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.util.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ProfileViewModelTest {

    @get:Rule
    var instanceTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var fakePreferencesRepository: FakePreferencesRepositoryImp
    private lateinit var testViewModel: ProfileViewModel

    @Before
    fun setUp() {
        fakePreferencesRepository = FakePreferencesRepositoryImp()
        testViewModel = ProfileViewModel(fakePreferencesRepository)
    }

    @Test
    fun getPartnerId_returnsSavedPartnerId() {
        testViewModel.getPartnerId()

        val responseEvent = testViewModel.partnerIdLiveData.getOrAwaitValue()

        assertThat(responseEvent.getContentIfNotHandled()).isNull()
    }

    @Test
    fun getSecretKey_returnsSavedSecretKey() {
        testViewModel.getSecretKey()

        val responseEvent = testViewModel.secretKeyLiveData.getOrAwaitValue()

        assertThat(responseEvent.getContentIfNotHandled()).isNull()
    }

    @Test
    fun updatePartnerId_returnsSuccess() {
        testViewModel.updatePartnerId("123")

        val responseEvent = testViewModel.updatePartnerIdLiveData.getOrAwaitValue()

        assertThat(responseEvent.getContentIfNotHandled()).isInstanceOf(Resource.Success::class.java)
    }

    @Test
    fun updateSecretKey_returnsSuccess() {
        testViewModel.updateSecretKey("abc123")

        val responseEvent = testViewModel.updateSecretKeyLiveData.getOrAwaitValue()

        assertThat(responseEvent.getContentIfNotHandled()).isInstanceOf(Resource.Success::class.java)
    }

    @Test
    fun updatePartnerId_returnsSavedPartnerId() {
        val newPartnerId = "123"
        testViewModel.updatePartnerId(newPartnerId)
        testViewModel.getPartnerId()

        val responseEvent = testViewModel.partnerIdLiveData.getOrAwaitValue()

        assertThat(responseEvent.getContentIfNotHandled()).isEqualTo(newPartnerId)
    }

    @Test
    fun updateSecretKey_returnsSavedSecretKey() {
        val newSecretKey = "abc123"
        testViewModel.updateSecretKey(newSecretKey)
        testViewModel.getSecretKey()

        val responseEvent = testViewModel.secretKeyLiveData.getOrAwaitValue()

        assertThat(responseEvent.getContentIfNotHandled()).isEqualTo(newSecretKey)
    }
}