package com.michaelfotiadis.flourpower.ui.main.mapper

import com.michaelfotiadis.flourpower.domain.cake.model.CakeEntity
import com.michaelfotiadis.flourpower.ui.main.model.UiCakeItem
import javax.inject.Inject

class UiCakeMapper @Inject constructor() {

    fun convert(cakes: List<CakeEntity>): List<UiCakeItem> {

        val uiCakes = ArrayList<UiCakeItem>()
        cakes.forEach {
            uiCakes.add(convert(it))
        }
        return uiCakes
    }

    fun convert(entityCake: CakeEntity): UiCakeItem = UiCakeItem(
        title = entityCake.title.capitalize(),
        description = entityCake.description,
        image = entityCake.image
    )
}