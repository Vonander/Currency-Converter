package com.vonander.currency_converter.di

import com.vonander.currency_converter.interactors.GetCurrencyConversion
import com.vonander.currency_converter.interactors.GetSupportedCurrencies
import com.vonander.currency_converter.interactors.SearchLiveRates
import com.vonander.currency_converter.network.responses.CurrencyLayerService
import com.vonander.currency_converter.network.util.ExchangeConvertResponseDtoMapper
import com.vonander.currency_converter.network.util.ExchangeListResponseDtoMapper
import com.vonander.currency_converter.network.util.ExchangeLiveResponseDtoMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Named

@Module
@InstallIn(ViewModelComponent::class)
class InteractorsModule {

    @ViewModelScoped
    @Provides
    fun provideSearchLiveRates(
        currencyLayerService: CurrencyLayerService,
        exchangeLiveResponseDtoMapper: ExchangeLiveResponseDtoMapper,
        @Named("accessKey") accessKey: String
    ): SearchLiveRates {
        return SearchLiveRates(
            service = currencyLayerService,
            dtoMapper = exchangeLiveResponseDtoMapper,
            accessKey = accessKey
        )
    }

    @ViewModelScoped
    @Provides
    fun provideGetSupportedCurrencies(
        currencyLayerService: CurrencyLayerService,
        exchangeListResponseDtoMapper: ExchangeListResponseDtoMapper,
        @Named("accessKey") accessKey: String
    ): GetSupportedCurrencies {
        return GetSupportedCurrencies(
            service = currencyLayerService,
            dtoMapper = exchangeListResponseDtoMapper,
            accessKey = accessKey
        )
    }

    @ViewModelScoped
    @Provides
    fun provideGetCurrencyConversion(
        currencyLayerService: CurrencyLayerService,
        exchangeConvertResponseDtoMapper: ExchangeConvertResponseDtoMapper,
        @Named("accessKey") accessKey: String
    ): GetCurrencyConversion {
        return GetCurrencyConversion(
            service = currencyLayerService,
            dtoMapper = exchangeConvertResponseDtoMapper,
            accessKey = accessKey
        )
    }
}