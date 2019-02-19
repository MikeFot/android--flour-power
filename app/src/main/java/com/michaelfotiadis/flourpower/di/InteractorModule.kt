package com.michaelfotiadis.flourpower.di

import com.michaelfotiadis.flourpower.domain.cake.GetAllCakesInteractor
import com.michaelfotiadis.flourpower.domain.cake.mapper.CakeDuplicateRemover
import com.michaelfotiadis.flourpower.domain.cake.mapper.CakeEntityMapper
import com.michaelfotiadis.flourpower.domain.error.mapper.RetrofitErrorMapper
import com.michaelfotiadis.flourpower.net.api.CakeApi
import com.michaelfotiadis.flourpower.net.resolver.NetworkResolver
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler

@Module
class InteractorModule {

    @Provides
    internal fun providesGetAllCakesInteractor(
        cakeApi: CakeApi,
        cakeEntityMapper: CakeEntityMapper,
        cakeDuplicateRemover: CakeDuplicateRemover,
        networkResolver: NetworkResolver,
        errorMapper: RetrofitErrorMapper,
        scheduler: Scheduler
    ) = GetAllCakesInteractor(
        cakeApi,
        cakeEntityMapper,
        cakeDuplicateRemover,
        networkResolver,
        errorMapper,
        scheduler
    )
}