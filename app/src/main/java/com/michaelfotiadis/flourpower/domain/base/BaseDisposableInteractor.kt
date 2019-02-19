package com.michaelfotiadis.flourpower.domain.base

abstract class BaseDisposableInteractor {

    interface Callback<T> {

        fun onNext(repoResult: RepoResult<T>)
    }

    abstract fun cancelAll()
}