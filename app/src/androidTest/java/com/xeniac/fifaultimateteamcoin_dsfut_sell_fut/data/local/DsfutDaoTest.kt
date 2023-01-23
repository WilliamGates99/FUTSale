package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.data.local.models.PickedUpPlayer
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.getOrAwaitValue
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.DateHelper.getCurrentTimeInMillis
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named

@ExperimentalCoroutinesApi
@SmallTest
@HiltAndroidTest
class DsfutDaoTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_db")
    lateinit var database: DsfutDatabase

    @Inject
    @Named("test_dao")
    lateinit var dao: DsfutDao

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertPickedUpPlayer() = runTest {
        val pickedUpPlayer = PickedUpPlayer(
            name = "name",
            position = "FW",
            rating = 69,
            priceStart = 420,
            priceNow = 500,
            pickUpTimeInMillis = getCurrentTimeInMillis()
        )
        dao.insertPickedUpPlayer(pickedUpPlayer)

        val allPickedUpPlayers = dao.observeAllPickedUpPlayers().getOrAwaitValue()
        assertThat(allPickedUpPlayers).contains(pickedUpPlayer)
    }

    @Test
    fun deletePickedUpPlayer() = runTest {
        val pickedUpPlayer = PickedUpPlayer(
            name = "name",
            position = "FW",
            rating = 69,
            priceStart = 420,
            priceNow = 500,
            pickUpTimeInMillis = getCurrentTimeInMillis(),
        )
        dao.insertPickedUpPlayer(pickedUpPlayer)
        dao.deletePickedUpPlayer(pickedUpPlayer)

        val allPickedUpPlayers = dao.observeAllPickedUpPlayers().getOrAwaitValue()
        assertThat(allPickedUpPlayers).doesNotContain(pickedUpPlayer)
    }
}