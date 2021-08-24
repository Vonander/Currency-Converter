package com.vonander.currency_converter.di

import androidx.room.Room
import com.vonander.currency_converter.BaseApplication
import com.vonander.currency_converter.cache.ListResponseDao
import com.vonander.currency_converter.cache.LiveResponseDao
import com.vonander.currency_converter.cache.dataBase.ListResponseDatabase
import com.vonander.currency_converter.cache.dataBase.LiveResponseDatabase
import com.vonander.currency_converter.cache.util.ListResponseEntityMapper
import com.vonander.currency_converter.cache.util.LiveResponseEntityMapper
import com.vonander.currency_converter.util.DATABASE_NAME_LIST
import com.vonander.currency_converter.util.DATABASE_NAME_LIVE

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
    fun provideLiveDb(app: BaseApplication): LiveResponseDatabase {
        return Room
            .databaseBuilder(app, LiveResponseDatabase::class.java, DATABASE_NAME_LIVE)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideListDb(app: BaseApplication): ListResponseDatabase {
        return Room
            .databaseBuilder(app, ListResponseDatabase::class.java, DATABASE_NAME_LIST)
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

    @Singleton
    @Provides
    fun provideListResponseDao(app: ListResponseDatabase): ListResponseDao {
        return app.ListResponseDao()
    }

    @Singleton
    @Provides
    fun provideListResponseEntityMapper(): ListResponseEntityMapper {
        return ListResponseEntityMapper()
    }
}