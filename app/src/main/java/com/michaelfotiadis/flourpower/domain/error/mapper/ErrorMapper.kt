package com.michaelfotiadis.flourpower.domain.error.mapper

import com.michaelfotiadis.flourpower.domain.error.model.DataSourceError

interface ErrorMapper<in T> {
    fun convert(error: T?): DataSourceError
}
