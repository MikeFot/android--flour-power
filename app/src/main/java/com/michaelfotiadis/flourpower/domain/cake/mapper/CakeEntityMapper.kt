package com.michaelfotiadis.flourpower.domain.cake.mapper

import com.michaelfotiadis.flourpower.domain.cake.model.CakeEntity
import com.michaelfotiadis.flourpower.net.model.CakeItem
import javax.inject.Inject

class CakeEntityMapper @Inject constructor() {

    fun convert(apiCakes: List<CakeItem>): List<CakeEntity> {

        val entities = ArrayList<CakeEntity>()
        apiCakes.forEach {
            entities.add(convert(it))
        }
        return entities
    }

    fun convert(apiCake: CakeItem): CakeEntity = CakeEntity(
        title = apiCake.title,
        description = apiCake.desc,
        image = apiCake.image
    )
}