package com.michaelfotiadis.flourpower.ui.main.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.michaelfotiadis.flourpower.domain.base.BaseDisposableInteractor
import com.michaelfotiadis.flourpower.domain.base.LoadingState
import com.michaelfotiadis.flourpower.domain.base.RepoResult
import com.michaelfotiadis.flourpower.domain.cake.GetAllCakesInteractor
import com.michaelfotiadis.flourpower.domain.cake.model.CakeEntity
import com.michaelfotiadis.flourpower.ui.error.UiError
import com.michaelfotiadis.flourpower.ui.error.UiErrorMapper
import com.michaelfotiadis.flourpower.ui.main.mapper.UiCakeMapper
import com.michaelfotiadis.flourpower.ui.main.mapper.UiCakeSorter
import com.michaelfotiadis.flourpower.ui.main.model.UiCakeItem
import timber.log.Timber

class MainViewModel(
    private val getAllCakesInteractor: GetAllCakesInteractor,
    private val uiCakesMapper: UiCakeMapper,
    private val uiCakeSorter: UiCakeSorter,
    private val uiErrorMapper: UiErrorMapper
) : ViewModel() {

    val resultsData = MutableLiveData<List<UiCakeItem>>()
    val errorData = MutableLiveData<UiError>()
    val loadingStateData = MutableLiveData<LoadingState>()

    fun loadCakes() {

        val callback = object : BaseDisposableInteractor.Callback<List<CakeEntity>> {
            override fun onNext(repoResult: RepoResult<List<CakeEntity>>) {
                loadingStateData.postValue(repoResult.loadingState)
                if (repoResult.hasPayload()) {
                    val uiSortedCakes =
                        uiCakeSorter.sort(uiCakesMapper.convert(repoResult.payload!!))
                    resultsData.postValue(uiSortedCakes)
                } else if (repoResult.hasError()) {
                    errorData.postValue(uiErrorMapper.convert(repoResult.dataSourceError!!))
                }
            }
        }

        getAllCakesInteractor.getCakes(callback)
    }

    fun clearError() {
        errorData.value = null
        Timber.d("Error cleared")
    }

    fun cancelAllJobs() {
        getAllCakesInteractor.cancelAll()
    }

    override fun onCleared() {
        cancelAllJobs()
        Timber.d("Clearing ViewModel")
        super.onCleared()
    }
}