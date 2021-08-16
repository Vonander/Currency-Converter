package com.vonander.currency_converter.di

import com.vonander.currency_converter.interactors.SearchExchange
import com.vonander.currency_converter.network.responses.CurrencyLayerService
import com.vonander.currency_converter.network.util.ExchangeResponseDtoMapper
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
    fun provideSearchExchange(
        currencyLayerService: CurrencyLayerService,
        exchangeResponseDtoMapper: ExchangeResponseDtoMapper,
        @Named("accessKey") accessKey: String
    ): SearchExchange {
        return SearchExchange(
            service = currencyLayerService,
            dtoMapper = exchangeResponseDtoMapper,
            accessKey = accessKey
        )
    }
}