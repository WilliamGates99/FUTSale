package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.use_cases

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.MainCoroutineRule
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.repositories.FakeDsfutDataStoreRepositoryImpl
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.Player
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.Result
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.data.repositories.FakePickUpPlayerRepositoryImpl
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.errors.PickUpPlayerError
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.validation.ValidateMaxPrice
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.validation.ValidateMinPrice
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.validation.ValidatePartnerId
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.validation.ValidateSecretKey
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.validation.ValidateTakeAfter
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(JUnit4::class)
class PickUpPlayerUseCaseTest {

    @get:Rule
    var instanceTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var fakeDsfutDataStoreRepositoryImpl: FakeDsfutDataStoreRepositoryImpl
    private lateinit var fakePickUpPlayerRepositoryImpl: FakePickUpPlayerRepositoryImpl
    private lateinit var pickUpPlayerUseCase: PickUpPlayerUseCase
    private val validatePartnerId: ValidatePartnerId = ValidatePartnerId()
    private val validateSecretKey: ValidateSecretKey = ValidateSecretKey()
    private val validateMinPrice: ValidateMinPrice = ValidateMinPrice()
    private val validateMaxPrice: ValidateMaxPrice = ValidateMaxPrice()
    private val validateTakeAfter: ValidateTakeAfter = ValidateTakeAfter()

    @Before
    fun setUp() {
        fakeDsfutDataStoreRepositoryImpl = FakeDsfutDataStoreRepositoryImpl()
        fakePickUpPlayerRepositoryImpl = FakePickUpPlayerRepositoryImpl()

        pickUpPlayerUseCase = PickUpPlayerUseCase(
            dsfutDataStoreRepository = fakeDsfutDataStoreRepositoryImpl,
            pickUpPlayerRepository = fakePickUpPlayerRepositoryImpl,
            validatePartnerId = validatePartnerId,
            validateSecretKey = validateSecretKey,
            validateMinPrice = validateMinPrice,
            validateMaxPrice = validateMaxPrice,
            validateTakeAfter = validateTakeAfter
        )
    }

    @Test
    fun pickUpPlayerWithNullPartnerId_returnsBlankPartnerIdError() = runTest {
        fakeDsfutDataStoreRepositoryImpl.storePartnerId(null)

        val pickUpPlayerResult = pickUpPlayerUseCase()
        assertThat(pickUpPlayerResult.partnerIdError).isInstanceOf(PickUpPlayerError.BlankPartnerId::class.java)
    }

    @Test
    fun pickUpPlayerWithBlankPartnerId_returnsBlankPartnerIdError() = runTest {
        fakeDsfutDataStoreRepositoryImpl.storePartnerId("")

        val pickUpPlayerResult = pickUpPlayerUseCase()
        assertThat(pickUpPlayerResult.partnerIdError).isInstanceOf(PickUpPlayerError.BlankPartnerId::class.java)
    }

    @Test
    fun pickUpPlayerWithNonDigitPartnerId_returnsInvalidPartnerIdError() = runTest {
        fakeDsfutDataStoreRepositoryImpl.storePartnerId("abc123")

        val pickUpPlayerResult = pickUpPlayerUseCase()
        assertThat(pickUpPlayerResult.partnerIdError).isInstanceOf(PickUpPlayerError.InvalidPartnerId::class.java)
    }

    @Test
    fun pickUpPlayerWithNullSecretKey_returnsBlankSecretKeyError() = runTest {
        fakeDsfutDataStoreRepositoryImpl.storeSecretKey(null)

        val pickUpPlayerResult = pickUpPlayerUseCase()
        assertThat(pickUpPlayerResult.secretKeyError).isInstanceOf(PickUpPlayerError.BlankSecretKey::class.java)
    }

    @Test
    fun pickUpPlayerWithBlankSecretKey_returnsBlankSecretKeyError() = runTest {
        fakeDsfutDataStoreRepositoryImpl.storeSecretKey("")

        val pickUpPlayerResult = pickUpPlayerUseCase()
        assertThat(pickUpPlayerResult.secretKeyError).isInstanceOf(PickUpPlayerError.BlankSecretKey::class.java)
    }

    @Test
    fun pickUpPlayerWithNonDigitMinPrice_returnsInvalidMinPriceError() = runTest {
        val pickUpPlayerResult = pickUpPlayerUseCase(minPrice = "abc123")
        assertThat(pickUpPlayerResult.minPriceError).isInstanceOf(PickUpPlayerError.InvalidMinPrice::class.java)
    }

    @Test
    fun pickUpPlayerWithNonDigitMaxPrice_returnsInvalidMaxPriceError() = runTest {
        val pickUpPlayerResult = pickUpPlayerUseCase(maxPrice = "abc123")
        assertThat(pickUpPlayerResult.maxPriceError).isInstanceOf(PickUpPlayerError.InvalidMaxPrice::class.java)
    }

    @Test
    fun pickUpPlayerWithValidInputsAndPlayersInQueue_returnsPickedUpPlayer() = runTest {
        fakePickUpPlayerRepositoryImpl.setPickUpPlayerHttpStatusCode(HttpStatusCode.OK)
        fakePickUpPlayerRepositoryImpl.setIsPlayersQueueEmpty(false)

        fakeDsfutDataStoreRepositoryImpl.storePartnerId("123")
        fakeDsfutDataStoreRepositoryImpl.storeSecretKey("secretKey")

        val pickUpPlayerResult = pickUpPlayerUseCase(
            minPrice = "10000",
            maxPrice = "200000",
            takeAfterDelayInSeconds = 5
        )

        assertThat(pickUpPlayerResult.result).isInstanceOf(Result.Success::class.java)
        assertThat((pickUpPlayerResult.result as Result.Success).data).isInstanceOf(Player::class.java)
    }

    @Test
    fun pickUpPlayerWithValidInputsAndEmptyQueue_returnsError() = runTest {
        fakePickUpPlayerRepositoryImpl.setPickUpPlayerHttpStatusCode(HttpStatusCode.OK)
        fakePickUpPlayerRepositoryImpl.setIsPlayersQueueEmpty(true)

        fakeDsfutDataStoreRepositoryImpl.storePartnerId("123")
        fakeDsfutDataStoreRepositoryImpl.storeSecretKey("abc123")

        val pickUpPlayerResult = pickUpPlayerUseCase()

        assertThat(pickUpPlayerResult.result).isInstanceOf(Result.Error::class.java)
        assertThat((pickUpPlayerResult.result as Result.Error).error).isInstanceOf(PickUpPlayerError::class.java)
    }

    @Test
    fun pickUpPlayerWithValidInputsAndUnavailableNetwork_returnsError() = runTest {
        fakePickUpPlayerRepositoryImpl.isNetworkAvailable(isAvailable = false)

        fakeDsfutDataStoreRepositoryImpl.storePartnerId("123")
        fakeDsfutDataStoreRepositoryImpl.storeSecretKey("abc123")

        val pickUpPlayerResult = pickUpPlayerUseCase()

        assertThat(pickUpPlayerResult.result).isInstanceOf(Result.Error::class.java)
    }

    @Test
    fun pickUpPlayerWithValidInputsAndBadNetwork_returnsError() = runTest {
        fakePickUpPlayerRepositoryImpl.setPickUpPlayerHttpStatusCode(HttpStatusCode.RequestTimeout)

        fakeDsfutDataStoreRepositoryImpl.storePartnerId("123")
        fakeDsfutDataStoreRepositoryImpl.storeSecretKey("abc123")

        val pickUpPlayerResult = pickUpPlayerUseCase()

        assertThat(pickUpPlayerResult.result).isInstanceOf(Result.Error::class.java)
    }
}