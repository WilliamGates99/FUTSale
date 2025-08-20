package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.data.utils

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.local.entities.PlayerEntity
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.Platform
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.utils.DateHelper
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.data.dto.PlayerDto
import kotlin.random.Random

object DummyPlayersHelper {

    var latestPlayerEntities = SnapshotStateList<PlayerEntity>()

    val dummyPlayerDto = PlayerDto(
        tradeID = 1,
        assetID = 1,
        resourceID = 1,
        transactionID = 1,
        name = "Test Player",
        rating = 88,
        position = "GK",
        startPrice = 10000,
        buyNowPrice = 15000,
        owners = 1,
        contracts = 1,
        chemistryStyle = "Basic",
        chemistryStyleID = 1,
        expires = 0
    )

    fun resetLatestPlayers() {
        latestPlayerEntities = SnapshotStateList()
    }

    fun addDummyPlayersToLatestPlayers() {
        val playersToInsert = ('a'..'z').mapIndexed { index, char ->
            PlayerEntity(
                id = index.toLong(),
                tradeID = index.toString(),
                assetID = index,
                resourceID = index,
                transactionID = index,
                name = char.toString(),
                rating = Random.nextInt(from = 10, until = 99),
                position = "CDM",
                startPrice = 1000,
                buyNowPrice = 2000,
                owners = 1,
                contracts = 1,
                chemistryStyle = "Basic",
                chemistryStyleID = index,
                platform = when (Random.nextBoolean()) {
                    true -> Platform.CONSOLE
                    false -> Platform.PC
                },
                pickUpTimeInSeconds = DateHelper.getCurrentTimeInSeconds().plus(
                    Random.nextLong(
                        from = -600, // 10 minutes ago
                        until = 0 // Now
                    )
                )
            )
        }

        playersToInsert.shuffled().forEach { latestPlayerEntities.add(it) }
    }
}