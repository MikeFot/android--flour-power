package com.michaelfotiadis.flourpower.domain.base

import com.michaelfotiadis.flourpower.domain.error.model.DataSourceError

class RepoResult<T>(
    val payload: T? = null,
    val dataSourceError: DataSourceError? = null,
    val loadingState: LoadingState
) {

    fun hasPayload(): Boolean {
        return payload != null
    }

    fun hasError(): Boolean {
        return dataSourceError != null
    }
}
