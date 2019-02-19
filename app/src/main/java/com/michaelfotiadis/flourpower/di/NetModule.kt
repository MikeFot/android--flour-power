package com.michaelfotiadis.flourpower.di

import android.content.Context
import android.net.ConnectivityManager
import com.google.gson.Gson
import com.michaelfotiadis.flourpower.net.api.CakeApi
import com.michaelfotiadis.flourpower.net.error.RxErrorHandlingCallAdapterFactory
import com.michaelfotiadis.flourpower.net.resolver.NetworkResolver
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
class NetModule {

    @Provides
    @Singleton
    internal fun providesCache(context: Context): Cache {
        return Cache(context.cacheDir, 10 * 1024 * 1024)
    }

    @Provides
    @Singleton
    internal fun providesOkHttpClient(
        cache: Cache,
        isDebugEnabled: Boolean
    ): OkHttpClient {
        val okHttpBuilder = OkHttpClient().newBuilder()
        val loggingInterceptor = HttpLoggingInterceptor()
        val level = when {
            isDebugEnabled -> HttpLoggingInterceptor.Level.BODY
            else -> HttpLoggingInterceptor.Level.NONE
        }
        loggingInterceptor.level = level
        okHttpBuilder.addInterceptor(loggingInterceptor)
        okHttpBuilder.cache(cache)
        return okHttpBuilder.build()
    }

    @Provides
    internal fun providesRetrofit(
        @Named(AppModule.KEY_BASE_URL) baseUrl: String,
        gson: Gson,
        okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addCallAdapterFactory(RxErrorHandlingCallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    internal fun providesNetworkResolver(context: Context): NetworkResolver {

        return object : NetworkResolver {
            override fun isConnected(): Boolean {
                val connectivityManager =
                    context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
                val activeNetworkInfo = connectivityManager!!.activeNetworkInfo
                return activeNetworkInfo != null && activeNetworkInfo.isConnected
            }
        }
    }

    @Provides
    internal fun providesCakeApi(retrofit: Retrofit) = CakeApi(retrofit)
}