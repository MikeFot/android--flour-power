package com.michaelfotiadis.flourpower.di

import android.app.Application
import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.michaelfotiadis.flourpower.BuildConfig
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import javax.inject.Named
import javax.inject.Singleton

@Module
class AppModule {

    companion object {
        const val KEY_BASE_URL = "key.base.url"
    }

    @Provides
    @Singleton
    internal fun providesContext(application: Application): Context {
        return application
    }

    @Provides
    internal fun providesDebugFlag(): Boolean {
        return BuildConfig.DEBUG
    }

    @Provides
    @Named(KEY_BASE_URL)
    internal fun providesBaseUrl(): String {
        return BuildConfig.BASE_URL
    }

    @Provides
    internal fun providesGson(): Gson {
        return GsonBuilder().setPrettyPrinting().create()
    }

    @Provides
    internal fun providesExecutionScheduler() = Schedulers.io()
}