package com.michaelfotiadis.flourpower.domain.error.model

data class DataSourceError(val errorMessage: String?, val kind: DataSourceErrorKind) {
    val creationTime: Long
    val warnings: MutableMap<String, String>

    init {
        this.warnings = HashMap()
        this.creationTime = System.currentTimeMillis()
    }

    fun addWarning(key: String, value: String) {
        warnings[key] = value
    }
}
