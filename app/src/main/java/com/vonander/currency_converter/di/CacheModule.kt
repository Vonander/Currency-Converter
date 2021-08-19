package com.vonander.currency_converter.di

import androidx.room.Room
import com.vonander.currency_converter.BaseApplication
import com.vonander.currency_converter.cache.LiveResponseDao
import com.vonander.currency_converter.cache.dataBase.LiveResponseDatabase
import com.vonander.currency_converter.cache.util.LiveResponseEntityMapper
import com.vonander.currency_converter.util.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CacheModule {

    @Singleton
    @Provides
    fun provideDb(app: BaseApplication): LiveResponseDatabase {
        return Room
            .databaseBuilder(app, LiveResponseDatabase::class.java, DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideLiveResponseDao(app: LiveResponseDatabase): LiveResponseDao {
        return app.LiveResponseDao()
    }

    @Singleton
    @Provides
    fun provideLiveResponseEntityMapper(): LiveResponseEntityMapper {
        return LiveResponseEntityMapper()
    }
}