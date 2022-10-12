package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.ui.viewmodels

import android.text.Editable
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.MainCoroutineRule
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.data.FakePreferencesRepositoryImp
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.getOrAwaitValue
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.Status
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock

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
        val editableFactory = mock(Editable.Factory::class.java)
        val newPartnerId = editableFactory.newEditable("123")
        testViewModel.updatePartnerId(newPartnerId)

        val responseEvent = testViewModel.updatePartnerIdLiveData.getOrAwaitValue()

        assertThat(responseEvent.getContentIfNotHandled()?.status).isEqualTo(Status.SUCCESS)
    }

    @Test
    fun updateSecretKey_returnsSuccess() {
        val editableFactory = mock(Editable.Factory::class.java)
        val newSecretKey = editableFactory.newEditable("abc123")
        testViewModel.updateSecretKey(newSecretKey)

        val responseEvent = testViewModel.updateSecretKeyLiveData.getOrAwaitValue()

        assertThat(responseEvent.getContentIfNotHandled()?.status).isEqualTo(Status.SUCCESS)
    }
}