package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.di

import android.content.Context
import androidx.room.Room
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.data.local.DsfutDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object TestAppModule {

    @Provides
    @Named("test_db")
    fun provideInMemoryDb(
        @ApplicationContext context: Context
    ) = Room.inMemoryDatabaseBuilder(context, DsfutDatabase::class.java)
        .allowMainThreadQueries()
        .build()

    @Provides
    @Named("test_dao")
    fun provideTestDao(
        @Named("test_db") database: DsfutDatabase
    ) = database.dsfutDao()
}