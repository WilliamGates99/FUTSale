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
import kotlinx.coroutines.flow.onEach
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
        fakeDsfutDataStoreRepositoryImpl.storePartnerId(partnerId = null)

        pickUpPlayerUseCase().onEach { pickUpPlayerResult ->
            assertThat(pickUpPlayerResult.partnerIdError).isInstanceOf(
                PickUpPlayerError.BlankPartnerId::class.java
            )
        }
    }

    @Test
    fun pickUpPlayerWithBlankPartnerId_returnsBlankPartnerIdError() = runTest {
        fakeDsfutDataStoreRepositoryImpl.storePartnerId(partnerId = "")

        pickUpPlayerUseCase().onEach { pickUpPlayerResult ->
            assertThat(pickUpPlayerResult.partnerIdError).isInstanceOf(
                PickUpPlayerError.BlankPartnerId::class.java
            )
        }
    }

    @Test
    fun pickUpPlayerWithNonDigitPartnerId_returnsInvalidPartnerIdError() = runTest {
        fakeDsfutDataStoreRepositoryImpl.storePartnerId(partnerId = "abc123")

        pickUpPlayerUseCase().onEach { pickUpPlayerResult ->
            assertThat(pickUpPlayerResult.partnerIdError).isInstanceOf(
                PickUpPlayerError.InvalidPartnerId::class.java
            )
        }
    }

    @Test
    fun pickUpPlayerWithNullSecretKey_returnsBlankSecretKeyError() = runTest {
        fakeDsfutDataStoreRepositoryImpl.storeSecretKey(secretKey = null)

        pickUpPlayerUseCase().onEach { pickUpPlayerResult ->
            assertThat(pickUpPlayerResult.secretKeyError).isInstanceOf(
                PickUpPlayerError.BlankSecretKey::class.java
            )
        }
    }

    @Test
    fun pickUpPlayerWithBlankSecretKey_returnsBlankSecretKeyError() = runTest {
        fakeDsfutDataStoreRepositoryImpl.storeSecretKey(secretKey = "")

        pickUpPlayerUseCase().onEach { pickUpPlayerResult ->
            assertThat(pickUpPlayerResult.secretKeyError).isInstanceOf(
                PickUpPlayerError.BlankSecretKey::class.java
            )
        }
    }

    @Test
    fun pickUpPlayerWithNonDigitMinPrice_returnsInvalidMinPriceError() = runTest {
        pickUpPlayerUseCase(
            minPrice = "abc123"
        ).onEach { pickUpPlayerResult ->
            assertThat(pickUpPlayerResult.minPriceError).isInstanceOf(
                PickUpPlayerError.InvalidMinPrice::class.java
            )
        }
    }

    @Test
    fun pickUpPlayerWithNonDigitMaxPrice_returnsInvalidMaxPriceError() = runTest {
        pickUpPlayerUseCase(
            maxPrice = "abc123"
        ).onEach { pickUpPlayerResult ->
            assertThat(pickUpPlayerResult.maxPriceError).isInstanceOf(
                PickUpPlayerError.InvalidMaxPrice::class.java
            )
        }
    }

    @Test
    fun pickUpPlayerWithValidInputsAndPlayersInQueue_returnsPickedUpPlayer() = runTest {
        fakePickUpPlayerRepositoryImpl.setPickUpPlayerHttpStatusCode(
            httpStatusCode = HttpStatusCode.OK
        )
        fakePickUpPlayerRepositoryImpl.setIsPlayersQueueEmpty(isEmpty = false)

        fakeDsfutDataStoreRepositoryImpl.storePartnerId(partnerId = "123")
        fakeDsfutDataStoreRepositoryImpl.storeSecretKey(secretKey = "secretKey")

        pickUpPlayerUseCase(
            minPrice = "10000",
            maxPrice = "200000",
            takeAfterDelayInSeconds = 5
        ).onEach { pickUpPlayerResult ->
            assertThat(pickUpPlayerResult.result).isInstanceOf(Result.Success::class.java)
            assertThat((pickUpPlayerResult.result as Result.Success).data).isInstanceOf(Player::class.java)
        }
    }

    @Test
    fun pickUpPlayerWithValidInputsAndEmptyQueue_returnsError() = runTest {
        fakePickUpPlayerRepositoryImpl.setPickUpPlayerHttpStatusCode(
            httpStatusCode = HttpStatusCode.OK
        )
        fakePickUpPlayerRepositoryImpl.setIsPlayersQueueEmpty(isEmpty = true)

        fakeDsfutDataStoreRepositoryImpl.storePartnerId(partnerId = "123")
        fakeDsfutDataStoreRepositoryImpl.storeSecretKey(secretKey = "abc123")

        pickUpPlayerUseCase().onEach { pickUpPlayerResult ->
            assertThat(pickUpPlayerResult.result).isInstanceOf(Result.Error::class.java)
            assertThat((pickUpPlayerResult.result as Result.Error).error).isInstanceOf(
                PickUpPlayerError::class.java
            )
        }
    }

    @Test
    fun pickUpPlayerWithValidInputsAndUnavailableNetwork_returnsError() = runTest {
        fakePickUpPlayerRepositoryImpl.isNetworkAvailable(isAvailable = false)

        fakeDsfutDataStoreRepositoryImpl.storePartnerId(partnerId = "123")
        fakeDsfutDataStoreRepositoryImpl.storeSecretKey(secretKey = "abc123")

        pickUpPlayerUseCase().onEach { pickUpPlayerResult ->
            assertThat(pickUpPlayerResult.result).isInstanceOf(Result.Error::class.java)
        }
    }

    @Test
    fun pickUpPlayerWithValidInputsAndBadNetwork_returnsError() = runTest {
        fakePickUpPlayerRepositoryImpl.setPickUpPlayerHttpStatusCode(
            httpStatusCode = HttpStatusCode.RequestTimeout
        )

        fakeDsfutDataStoreRepositoryImpl.storePartnerId(partnerId = "123")
        fakeDsfutDataStoreRepositoryImpl.storeSecretKey(secretKey = "abc123")

        pickUpPlayerUseCase().onEach { pickUpPlayerResult ->
            assertThat(pickUpPlayerResult.result).isInstanceOf(Result.Error::class.java)
        }
    }
}