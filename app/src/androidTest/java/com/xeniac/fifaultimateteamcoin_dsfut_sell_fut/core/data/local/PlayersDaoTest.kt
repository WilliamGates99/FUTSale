package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.testing.TestPager
import com.google.common.truth.Truth.assertThat
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.local.entities.PlayerEntity
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.utils.DateHelper
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.di.AppModule
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.Platform
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltAndroidTest
@UninstallModules(AppModule::class)
class PlayersDaoTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(/* testInstance = */ this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    lateinit var database: FutSaleDatabase

    @Inject
    lateinit var dao: PlayersDao

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertPlayer() = runTest {
        val playerEntity = PlayerEntity(
            id = 1,
            tradeID = "1",
            assetID = 1,
            resourceID = 1,
            transactionID = 1,
            name = "Test",
            rating = 88,
            position = "FW",
            startPrice = 10000,
            buyNowPrice = 50000,
            owners = 1,
            contracts = 1,
            chemistryStyle = "Basic",
            chemistryStyleID = 1,
            platform = Platform.CONSOLE
        )
        dao.insertPlayer(playerEntity)

        val pickedPlayers = dao.getPlayers()
        assertThat(pickedPlayers).contains(playerEntity)
    }

    @Test
    fun clearPlayers() = runTest {
        val dummyPlayers = mutableListOf<PlayerEntity>()
        repeat(times = 10) { index ->
            val playerEntity = PlayerEntity(
                id = index.toLong(),
                tradeID = index.toString(),
                assetID = index,
                resourceID = index,
                transactionID = index,
                name = "Test $index",
                rating = 88,
                position = "FW",
                startPrice = 10000,
                buyNowPrice = 50000,
                owners = index,
                contracts = index,
                chemistryStyle = "Basic",
                chemistryStyleID = index,
                platform = Platform.CONSOLE
            )

            dummyPlayers.add(playerEntity)
            dao.insertPlayer(playerEntity)
        }

        val pickedPlayers = dao.getPlayers()
        assertThat(pickedPlayers).isNotEmpty()
        assertThat(pickedPlayers).containsExactlyElementsIn(dummyPlayers)

        dao.clearPlayers()

        val pickedPlayersAfterClear = dao.getPlayers()
        assertThat(pickedPlayersAfterClear).isEmpty()
    }

    @Test
    fun deletePlayer() = runTest {
        val playerEntity = PlayerEntity(
            id = 1,
            tradeID = "1",
            assetID = 1,
            resourceID = 1,
            transactionID = 1,
            name = "Test",
            rating = 88,
            position = "FW",
            startPrice = 10000,
            buyNowPrice = 50000,
            owners = 1,
            contracts = 1,
            chemistryStyle = "Basic",
            chemistryStyleID = 1,
            platform = Platform.CONSOLE
        )
        dao.insertPlayer(playerEntity)

        val pickedPlayers = dao.getPlayers()
        assertThat(pickedPlayers).contains(playerEntity)

        dao.deletePlayer(playerEntity)

        val pickedPlayersAfterDelete = dao.getPlayers()
        assertThat(pickedPlayersAfterDelete).doesNotContain(playerEntity)
    }

    @Test
    fun getPlayers() = runTest {
        val dummyPlayers = mutableListOf<PlayerEntity>()
        repeat(times = 10) { index ->
            val playerEntity = PlayerEntity(
                id = index.toLong(),
                tradeID = index.toString(),
                assetID = index,
                resourceID = index,
                transactionID = index,
                name = "Test $index",
                rating = 88,
                position = "FW",
                startPrice = 10000,
                buyNowPrice = 50000,
                owners = index,
                contracts = index,
                chemistryStyle = "Basic",
                chemistryStyleID = index,
                platform = Platform.CONSOLE
            )

            dummyPlayers.add(playerEntity)
            dao.insertPlayer(playerEntity)
        }

        val latestPickedPlayers = dao.getPlayers()

        assertThat(latestPickedPlayers).isNotEmpty()
        assertThat(latestPickedPlayers).containsExactlyElementsIn(dummyPlayers)
    }

    @Test
    fun observeLatestPickedPlayers() = runTest {
        val dummyPlayers = mutableListOf<PlayerEntity>()
        repeat(times = 10) { index ->
            val playerEntity = PlayerEntity(
                id = index.toLong(),
                tradeID = index.toString(),
                assetID = index,
                resourceID = index,
                transactionID = index,
                name = "Test $index",
                rating = 88,
                position = "FW",
                startPrice = 10000,
                buyNowPrice = 50000,
                owners = index,
                contracts = index,
                chemistryStyle = "Basic",
                chemistryStyleID = index,
                platform = Platform.CONSOLE
            )

            dummyPlayers.add(playerEntity)
            dao.insertPlayer(playerEntity)
        }

        val latestPickedPlayers = dao.observeLatestPickedPlayers(
            currentTimeInSeconds = DateHelper.getCurrentTimeInSeconds()
        ).first()

        assertThat(latestPickedPlayers).isNotEmpty()
        assertThat(latestPickedPlayers).containsExactlyElementsIn(dummyPlayers)

        for (i in 0..latestPickedPlayers.size - 2) {
            assertThat(latestPickedPlayers[i].pickUpTimeInSeconds)
                .isAtLeast(latestPickedPlayers[i + 1].pickUpTimeInSeconds)
        }
    }

    @Test
    fun observerPlayer() = runTest {
        val playerEntity = PlayerEntity(
            id = 1,
            tradeID = "1",
            assetID = 1,
            resourceID = 1,
            transactionID = 1,
            name = "Test",
            rating = 88,
            position = "FW",
            startPrice = 10000,
            buyNowPrice = 50000,
            owners = 1,
            contracts = 1,
            chemistryStyle = "Basic",
            chemistryStyleID = 1,
            platform = Platform.CONSOLE
        )
        dao.insertPlayer(playerEntity)

        val player = dao.observerPlayer(playerEntity.id!!).first()
        assertThat(player).isEqualTo(playerEntity)
    }

    @Test
    fun refreshPager_returnsEntireList() = runTest {
        val dummyPlayers = mutableListOf<PlayerEntity>()
        repeat(times = 50) { index ->
            val playerEntity = PlayerEntity(
                id = index.toLong(),
                tradeID = index.toString(),
                assetID = index,
                resourceID = index,
                transactionID = index,
                name = "Test $index",
                rating = 88,
                position = "FW",
                startPrice = 10000,
                buyNowPrice = 50000,
                owners = index,
                contracts = index,
                chemistryStyle = "Basic",
                chemistryStyleID = index,
                platform = Platform.CONSOLE
            )

            dummyPlayers.add(playerEntity)
            dao.insertPlayer(playerEntity)
        }

        val pager = TestPager(
            config = PagingConfig(pageSize = 20),
            pagingSource = dao.pagingSource()
        )

        val result = pager.refresh() as PagingSource.LoadResult.Page

        assertThat(result.data).isNotEmpty()
        assertThat(result.data).containsExactlyElementsIn(dummyPlayers)
    }
}