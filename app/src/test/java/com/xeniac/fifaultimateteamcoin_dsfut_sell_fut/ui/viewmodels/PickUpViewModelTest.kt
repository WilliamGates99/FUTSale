package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.ui.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.google.common.truth.Truth.assertThat
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.MainCoroutineRule
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.data.remote.models.Player
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.data.repository.FakeDsfutRepositoryImp
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.data.repository.FakePreferencesRepositoryImp
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.getOrAwaitValue
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.util.Constants.DELAY_TIME_AUTO_PICK_UP
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.util.Constants.SELECTED_PLATFORM_CONSOLE
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.util.Constants.SELECTED_PLATFORM_PC
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.util.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class PickUpViewModelTest {

    @get:Rule
    var instanceTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var fakePreferencesRepository: FakePreferencesRepositoryImp
    private lateinit var fakeDsfutRepository: FakeDsfutRepositoryImp
    private lateinit var savedStateHandle: SavedStateHandle

    private lateinit var testViewModel: PickUpViewModel

    @Before
    fun setUp() {
        fakePreferencesRepository = FakePreferencesRepositoryImp()
        fakeDsfutRepository = FakeDsfutRepositoryImp()
        savedStateHandle = SavedStateHandle()

        testViewModel = PickUpViewModel(
            fakePreferencesRepository, { fakeDsfutRepository }, savedStateHandle
        )
    }

    @Test
    fun getSelectedPlatform_returnsDefaultSelectedPlatform() {
        val defaultSelectedPlatform = SELECTED_PLATFORM_CONSOLE

        testViewModel.getSelectedPlatform()

        val responseEvent = testViewModel.selectedPlatformIndexLiveData.getOrAwaitValue()

        assertThat(responseEvent.getContentIfNotHandled()).isEqualTo(defaultSelectedPlatform)
    }

    @Test
    fun setSelectedPlatform_returnsNewSelectedPlatform() {
        val newSelectedPlatform = SELECTED_PLATFORM_PC

        testViewModel.setSelectedPlatform(newSelectedPlatform)

        val responseEvent = testViewModel.selectedPlatformIndexLiveData.getOrAwaitValue()

        assertThat(responseEvent.getContentIfNotHandled()).isEqualTo(newSelectedPlatform)
    }

    @Test
    fun validatePickOnceInputsWithBlankPartnerId_returnsError() = runTest {
        fakePreferencesRepository.setPartnerId("")

        testViewModel.validatePickOnceInputs()

        val responseEvent = testViewModel.pickPlayerOnceLiveData.getOrAwaitValue()

        assertThat(responseEvent.getContentIfNotHandled()).isInstanceOf(Resource.Error::class.java)
    }

    @Test
    fun validatePickOnceInputsWithBlankSecretKey_returnsError() = runTest {
        fakePreferencesRepository.setSecretKey("")

        testViewModel.validatePickOnceInputs()

        val responseEvent = testViewModel.pickPlayerOnceLiveData.getOrAwaitValue()

        assertThat(responseEvent.getContentIfNotHandled()).isInstanceOf(Resource.Error::class.java)
    }

    @Test
    fun validatePickOnceInputsWithNoInternet_returnsError() {
        fakeDsfutRepository.setShouldReturnNetworkError(true)

        testViewModel.validatePickOnceInputs()

        val responseEvent = testViewModel.pickPlayerOnceLiveData.getOrAwaitValue()

        assertThat(responseEvent.getContentIfNotHandled()).isInstanceOf(Resource.Error::class.java)
    }

    @Test
    fun validatePickOnceInputsWithValidInputs_returnsSuccess() = runTest {
        fakePreferencesRepository.setPartnerId("123")
        fakePreferencesRepository.setSecretKey("abc")

        testViewModel.validatePickOnceInputs()

        val responseEvent = testViewModel.pickPlayerOnceLiveData.getOrAwaitValue()

        assertThat(responseEvent.getContentIfNotHandled()).isInstanceOf(Resource.Success::class.java)
    }

    @Test
    fun validateAutoPickPlayerInputsWithBlankPartnerId_returnsError() = runTest {
        fakePreferencesRepository.setPartnerId("")

        testViewModel.validateAutoPickPlayerInputs()

        val responseEvent = testViewModel.autoPickPlayerLiveData.getOrAwaitValue()

        assertThat(responseEvent.getContentIfNotHandled()).isInstanceOf(Resource.Error::class.java)
    }

    @Test
    fun validateAutoPickPlayerInputsWithBlankSecretKey_returnsError() = runTest {
        fakePreferencesRepository.setSecretKey("")

        testViewModel.validateAutoPickPlayerInputs()

        val responseEvent = testViewModel.autoPickPlayerLiveData.getOrAwaitValue()

        assertThat(responseEvent.getContentIfNotHandled()).isInstanceOf(Resource.Error::class.java)
    }

    @Test
    fun validateAutoPickPlayerInputsWithNoInternet_returnsError() {
        fakeDsfutRepository.setShouldReturnNetworkError(true)

        testViewModel.validateAutoPickPlayerInputs()

        val responseEvent = testViewModel.autoPickPlayerLiveData.getOrAwaitValue()

        assertThat(responseEvent.getContentIfNotHandled()).isInstanceOf(Resource.Error::class.java)
    }

    @Test
    fun validateAutoPickPlayerInputsWithValidInputs_returnsSuccess() = runTest {
        fakePreferencesRepository.setPartnerId("123")
        fakePreferencesRepository.setSecretKey("abc")

        testViewModel.validateAutoPickPlayerInputs()
        delay(DELAY_TIME_AUTO_PICK_UP)

        val responseEvent = testViewModel.autoPickPlayerLiveData.getOrAwaitValue()

        assertThat(responseEvent.getContentIfNotHandled()).isInstanceOf(Resource.Success::class.java)
    }

    @Test
    fun insertPickedUpPlayerIntoDbWithValidInput_returnsSuccess() {
        val testPlayer = Player(
            assetID = 1,
            buyNowPrice = 100,
            chemistryStyle = "Basic",
            chemistryStyleID = 2,
            contracts = 7,
            expires = 100,
            name = "Test Player",
            owners = 1,
            position = "CDM",
            rating = 89,
            resourceID = 1,
            startPrice = 50,
            tradeID = 100L,
            transactionID = 10
        )
        testViewModel.insertPickedUpPlayerIntoDb(testPlayer)

        val responseEvent = testViewModel.insertPickedUpPlayerLiveData.getOrAwaitValue()

        assertThat(responseEvent.getContentIfNotHandled()).isInstanceOf(Resource.Success::class.java)
    }
}