package com.michaelfotiadis.flourpower.net.model

import com.google.gson.annotations.SerializedName

data class CakeItem(
    @SerializedName("desc")
    val desc: String,
    @SerializedName("image")
    val image: String,
    @SerializedName("title")
    val title: String
) : ApiModel