package com.michaelfotiadis.flourpower.ui.main.mapper

import com.michaelfotiadis.flourpower.ui.main.model.UiCakeItem
import javax.inject.Inject

class UiCakeSorter @Inject constructor() {

    fun sort(cakes: List<UiCakeItem>): List<UiCakeItem> {

        return cakes.sortedBy { it.title }
    }
}