package com.michaelfotiadis.flourpower.domain.cake

import com.michaelfotiadis.flourpower.domain.base.BaseDisposableInteractor
import com.michaelfotiadis.flourpower.domain.base.LoadingState
import com.michaelfotiadis.flourpower.domain.base.RepoResult
import com.michaelfotiadis.flourpower.domain.cake.mapper.CakeDuplicateRemover
import com.michaelfotiadis.flourpower.domain.cake.mapper.CakeEntityMapper
import com.michaelfotiadis.flourpower.domain.cake.model.CakeEntity
import com.michaelfotiadis.flourpower.domain.error.mapper.RetrofitErrorMapper
import com.michaelfotiadis.flourpower.domain.error.model.DataSourceError
import com.michaelfotiadis.flourpower.domain.error.model.DataSourceErrorKind
import com.michaelfotiadis.flourpower.net.api.CakeApi
import com.michaelfotiadis.flourpower.net.resolver.NetworkResolver
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable

class GetAllCakesInteractor(
    private val cakeApi: CakeApi,
    private val cakeEntityMapper: CakeEntityMapper,
    private val cakeDuplicateRemover: CakeDuplicateRemover,
    private val networkResolver: NetworkResolver,
    private val errorMapper: RetrofitErrorMapper,
    private val scheduler: Scheduler
) : BaseDisposableInteractor() {

    private val compositeDisposable = CompositeDisposable()

    fun getCakes(callback: Callback<List<CakeEntity>>) {

        if (networkResolver.isConnected()) {

            compositeDisposable.add(
                cakeApi.getAll()
                    .subscribeOn(scheduler)
                    .doOnSubscribe { callback.onNext(RepoResult(loadingState = LoadingState.LOADING)) }
                    .map { cakeDuplicateRemover.cleanup(it) }
                    .map { cakeEntityMapper.convert(it) }
                    .subscribe(
                        {
                            callback.onNext(
                                RepoResult(
                                    payload = it,
                                    loadingState = LoadingState.IDLE
                                )
                            )
                        },
                        {
                            callback.onNext(
                                RepoResult(
                                    dataSourceError = errorMapper.convert(it),
                                    loadingState = LoadingState.IDLE
                                )
                            )
                        }
                    )
            )
        } else {
            callback.onNext(
                RepoResult(
                    dataSourceError = DataSourceError(null, DataSourceErrorKind.NO_NETWORK),
                    loadingState = LoadingState.IDLE
                )
            )
        }
    }

    override fun cancelAll() {
        compositeDisposable.clear()
    }
}