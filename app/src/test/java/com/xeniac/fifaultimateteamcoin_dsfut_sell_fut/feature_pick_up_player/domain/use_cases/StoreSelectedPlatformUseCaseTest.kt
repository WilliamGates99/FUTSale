package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.use_cases

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.MainCoroutineRule
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.repositories.FakeDsfutDataStoreRepositoryImpl
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.Platform
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.Result
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
class StoreSelectedPlatformUseCaseTest {

    @get:Rule
    var instanceTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var fakeDsfutDataStoreRepositoryImpl: FakeDsfutDataStoreRepositoryImpl
    private lateinit var storeSelectedPlatformUseCase: StoreSelectedPlatformUseCase

    @Before
    fun setUp() {
        fakeDsfutDataStoreRepositoryImpl = FakeDsfutDataStoreRepositoryImpl()
        storeSelectedPlatformUseCase = StoreSelectedPlatformUseCase(
            dsfutDataStoreRepository = fakeDsfutDataStoreRepositoryImpl
        )
    }

    @Test
    fun storeSelectedPlatform_returnsSuccess() = runTest {
        val newPlatform = Platform.PC
        storeSelectedPlatformUseCase(
            platform = newPlatform
        ).onEach { result ->
            assertThat(result).isInstanceOf(Result.Success::class.java)
        }
    }
}