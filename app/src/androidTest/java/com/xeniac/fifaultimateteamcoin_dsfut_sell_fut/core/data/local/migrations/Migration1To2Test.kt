package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.local.migrations

import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth.assertThat
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.local.FutSaleDatabase
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.utils.Constants
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.utils.DateHelper
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class Migration1To2Test {

    @get:Rule
    val migrationHelper = MigrationTestHelper(
        instrumentation = InstrumentationRegistry.getInstrumentation(),
        databaseClass = FutSaleDatabase::class.java,
        specs = listOf(Migration1To2()),
        openFactory = FrameworkSQLiteOpenHelperFactory()
    )

    @Test
    fun migration1To2_containsCorrectData() = runTest {
        val currentTime = DateHelper.getCurrentTimeInSeconds()

        migrationHelper.createDatabase(
            name = Constants.MIGRATION_DB_NAME,
            version = 1
        ).apply {
            execSQL(
                sql = """
                INSERT INTO players VALUES(
                '1', -- tradeID
                1, -- assetID
                1, -- resourceID
                1, -- transactionID
                'Test Player', -- name
                80, -- rating
                'FW', -- position
                1500, -- startPrice
                2500, -- buyNowPrice
                1, -- owners
                1, -- contracts
                'Basic', -- chemistryStyle
                1, -- chemistryStyleID
                'pc', -- platform
                $currentTime, -- pickUpTimeInMillis
                1 -- id
                )
                """.trimIndent()
            )
            close()
        }

        val db = migrationHelper.runMigrationsAndValidate(
            name = Constants.MIGRATION_DB_NAME,
            version = 2,
            validateDroppedTables = true
        )

        db.query(query = "SELECT * FROM players").apply {
            assertThat(moveToFirst()).isTrue()

            assertThat(getInt(getColumnIndex("id"))).isEqualTo(1)
            assertThat(getString(getColumnIndex("trade_id"))).isEqualTo("1")
            assertThat(getInt(getColumnIndex("asset_id"))).isEqualTo(1)
            assertThat(getInt(getColumnIndex("resource_id"))).isEqualTo(1)
            assertThat(getInt(getColumnIndex("transaction_id"))).isEqualTo(1)
            assertThat(getString(getColumnIndex("name"))).isEqualTo("Test Player")
            assertThat(getInt(getColumnIndex("rating"))).isEqualTo(80)
            assertThat(getString(getColumnIndex("position"))).isEqualTo("FW")
            assertThat(getInt(getColumnIndex("start_price"))).isEqualTo(1500)
            assertThat(getInt(getColumnIndex("buy_now_price"))).isEqualTo(2500)
            assertThat(getInt(getColumnIndex("owners"))).isEqualTo(1)
            assertThat(getInt(getColumnIndex("contracts"))).isEqualTo(1)
            assertThat(getString(getColumnIndex("chemistry_style"))).isEqualTo("Basic")
            assertThat(getInt(getColumnIndex("chemistry_style_id"))).isEqualTo(1)
            assertThat(getString(getColumnIndex("platform"))).isEqualTo("pc")
            assertThat(getString(getColumnIndex("pick_up_time_in_seconds"))).isEqualTo(currentTime.toString())
        }
    }
}