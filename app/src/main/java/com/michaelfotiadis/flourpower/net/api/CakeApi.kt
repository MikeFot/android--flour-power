package com.michaelfotiadis.flourpower.net.api

import com.michaelfotiadis.flourpower.net.model.CakeItem
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.http.GET

class CakeApi(retrofit: Retrofit) {

    private val api: Api = retrofit.create(Api::class.java)

    fun getAll() = api.getAll()

    interface Api {

        @GET("739df99e9d96700f17604a3971e701fa/raw/1d4dd9c5a0ec758ff5ae92b7b13fe4d57d34e1dc/waracle_cake-android-client")
        fun getAll(): Single<List<CakeItem>>
    }
}