package com.michaelfotiadis.flourpower.domain.cake.mapper

import com.michaelfotiadis.flourpower.net.model.CakeItem
import javax.inject.Inject

class CakeDuplicateRemover @Inject constructor() {

    fun cleanup(apiCakes: List<CakeItem>): List<CakeItem> {

        return apiCakes.distinctBy { it.title }
    }
}