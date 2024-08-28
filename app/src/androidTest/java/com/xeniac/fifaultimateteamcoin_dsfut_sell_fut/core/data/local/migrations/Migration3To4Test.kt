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
class Migration3To4Test {

    @get:Rule
    val migrationHelper = MigrationTestHelper(
        instrumentation = InstrumentationRegistry.getInstrumentation(),
        databaseClass = FutSaleDatabase::class.java,
        specs = listOf(Migration1To2()),
        openFactory = FrameworkSQLiteOpenHelperFactory()
    )

    @Test
    fun migration3To4_containsCorrectData() = runTest {
        val currentTime = DateHelper.getCurrentTimeInSeconds()

        migrationHelper.createDatabase(
            name = Constants.MIGRATION_DB_NAME,
            version = 3
        ).apply {
            execSQL(
                sql = """
                INSERT INTO players VALUES(
                '1', -- trade_id
                1, -- asset_id
                1, -- resource_id
                1, -- transaction_id
                'Test Player', -- name
                80, -- rating
                'FW', -- position
                1500, -- start_price
                2500, -- buy_now_price
                1, -- owners
                1, -- contracts
                'Basic', -- chemistry_style
                1, -- chemistry_style_id
                'pc', -- platform
                $currentTime, -- pick_up_time_in_seconds
                1 -- id
                )
                """.trimIndent()
            )

            close()
        }

        val db = migrationHelper.runMigrationsAndValidate(
            name = Constants.MIGRATION_DB_NAME,
            version = 4,
            validateDroppedTables = true,
            MIGRATION_3_TO_4
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
            assertThat(getInt(getColumnIndex("pick_up_time_in_seconds"))).isEqualTo(currentTime)
            assertThat(getInt(getColumnIndex("expiry_time_in_seconds"))).isEqualTo(currentTime + 180)
        }
    }
}