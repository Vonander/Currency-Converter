package com.vonander.currency_converter.di

import com.google.gson.GsonBuilder
import com.vonander.currency_converter.network.responses.CurrencyLayerService
import com.vonander.currency_converter.network.util.ExchangeConvertResponseDtoMapper
import com.vonander.currency_converter.network.util.ExchangeListResponseDtoMapper
import com.vonander.currency_converter.network.util.ExchangeLiveResponseDtoMapper
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
    fun provideExchangeLiveResponseDtoMapper() : ExchangeLiveResponseDtoMapper {
        return ExchangeLiveResponseDtoMapper()
    }

    @Singleton
    @Provides
    fun provideExchangeListResponseDtoMapper() : ExchangeListResponseDtoMapper {
        return ExchangeListResponseDtoMapper()
    }

    @Singleton
    @Provides
    fun provideExchangeConvertResponseDtoMapper() : ExchangeConvertResponseDtoMapper {
        return ExchangeConvertResponseDtoMapper()
    }

    @Singleton
    @Provides
    @Named("accessKey")
    fun provideAccessKey() : String {
        return "8a6c606aa70b3d564527035656fd6d75"
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