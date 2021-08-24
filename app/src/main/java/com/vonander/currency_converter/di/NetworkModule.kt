package com.vonander.currency_converter.di

import com.google.gson.GsonBuilder
import com.vonander.currency_converter.network.responses.CurrencyLayerService
import com.vonander.currency_converter.network.util.ConvertResponseDtoMapper
import com.vonander.currency_converter.network.util.ListResponseDtoMapper
import com.vonander.currency_converter.network.util.LiveResponseDtoMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Singleton
    @Provides
    fun provideLiveResponseDtoMapper() : LiveResponseDtoMapper {
        return LiveResponseDtoMapper()
    }

    @Singleton
    @Provides
    fun provideExchangeListResponseDtoMapper() : ListResponseDtoMapper {
        return ListResponseDtoMapper()
    }

    @Singleton
    @Provides
    fun provideExchangeConvertResponseDtoMapper() : ConvertResponseDtoMapper {
        return ConvertResponseDtoMapper()
    }

    @Singleton
    @Provides
    @Named("accessKey")
    fun provideAccessKey() : String {
        return "e4dc9f5f2d04e92f220fc2beb045c241" // free account
        //return "8a6c606aa70b3d564527035656fd6d75" //paid account
    }

    @Singleton
    @Provides
    fun provideCurrencyLayerService() : CurrencyLayerService {
        return Retrofit.Builder()
            .baseUrl("http://api.currencylayer.com/")
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(CurrencyLayerService::class.java)
    }
}