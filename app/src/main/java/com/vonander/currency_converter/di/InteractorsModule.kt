package com.vonander.currency_converter.di

import com.vonander.currency_converter.cache.LiveResponseDao
import com.vonander.currency_converter.cache.util.LiveResponseEntityMapper
import com.vonander.currency_converter.interactors.GetCurrencyConversion
import com.vonander.currency_converter.interactors.GetSupportedCurrencies
import com.vonander.currency_converter.interactors.SearchLiveRates
import com.vonander.currency_converter.network.responses.CurrencyLayerService
import com.vonander.currency_converter.network.util.ConvertResponseDtoMapper
import com.vonander.currency_converter.network.util.ListResponseDtoMapper
import com.vonander.currency_converter.network.util.LiveResponseDtoMapper
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
        liveResponseDao: LiveResponseDao,
        currencyLayerService: CurrencyLayerService,
        entityMapper: LiveResponseEntityMapper,
        liveResponseDtoMapper: LiveResponseDtoMapper,
        @Named("accessKey") accessKey: String
    ): SearchLiveRates {
        return SearchLiveRates(
            liveResponseDao = liveResponseDao,
            service = currencyLayerService,
            entityMapper = entityMapper,
            dtoMapper = liveResponseDtoMapper,
            accessKey = accessKey
        )
    }

    @ViewModelScoped
    @Provides
    fun provideGetSupportedCurrencies(
        currencyLayerService: CurrencyLayerService,
        listResponseDtoMapper: ListResponseDtoMapper,
        @Named("accessKey") accessKey: String
    ): GetSupportedCurrencies {
        return GetSupportedCurrencies(
            service = currencyLayerService,
            dtoMapper = listResponseDtoMapper,
            accessKey = accessKey
        )
    }

    @ViewModelScoped
    @Provides
    fun provideGetCurrencyConversion(
        currencyLayerService: CurrencyLayerService,
        convertResponseDtoMapper: ConvertResponseDtoMapper,
        @Named("accessKey") accessKey: String
    ): GetCurrencyConversion {
        return GetCurrencyConversion(
            service = currencyLayerService,
            dtoMapper = convertResponseDtoMapper,
            accessKey = accessKey
        )
    }
}