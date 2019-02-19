package com.michaelfotiadis.flourpower.ui.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.michaelfotiadis.flourpower.domain.cake.GetAllCakesInteractor
import com.michaelfotiadis.flourpower.ui.error.UiErrorMapper
import com.michaelfotiadis.flourpower.ui.main.mapper.UiCakeMapper
import com.michaelfotiadis.flourpower.ui.main.mapper.UiCakeSorter
import javax.inject.Inject

class MainViewModelFactory @Inject constructor(
    private val getAllCakesInteractor: GetAllCakesInteractor,
    private val uiCakesMapper: UiCakeMapper,
    private val uiCakeSorter: UiCakeSorter,
    private val uiErrorMapper: UiErrorMapper
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return MainViewModel(
            getAllCakesInteractor,
            uiCakesMapper,
            uiCakeSorter,
            uiErrorMapper
        ) as T
    }
}