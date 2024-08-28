package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.local.migrations

import androidx.room.Room
import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.local.FutSaleDatabase
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.utils.Constants
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AllMigrationsTest {

    @get:Rule
    val migrationHelper = MigrationTestHelper(
        instrumentation = InstrumentationRegistry.getInstrumentation(),
        databaseClass = FutSaleDatabase::class.java,
        specs = listOf(Migration1To2()),
        openFactory = FrameworkSQLiteOpenHelperFactory()
    )

    @Test
    fun testAllMigrations() = runTest {
        migrationHelper.createDatabase(
            name = Constants.MIGRATION_DB_NAME,
            version = 1
        ).apply {
            close()
        }

        Room.databaseBuilder(
            InstrumentationRegistry.getInstrumentation().targetContext,
            FutSaleDatabase::class.java,
            Constants.MIGRATION_DB_NAME
        ).addMigrations(
            MIGRATION_2_TO_3,
            MIGRATION_3_TO_4
        ).build().apply {
            openHelper.writableDatabase.close()
        }
    }
}