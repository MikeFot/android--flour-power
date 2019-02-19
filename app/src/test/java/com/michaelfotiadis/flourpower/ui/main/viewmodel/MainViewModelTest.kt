package com.michaelfotiadis.flourpower.ui.main.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import com.michaelfotiadis.flourpower.domain.base.BaseDisposableInteractor
import com.michaelfotiadis.flourpower.domain.base.LoadingState
import com.michaelfotiadis.flourpower.domain.base.RepoResult
import com.michaelfotiadis.flourpower.domain.cake.GetAllCakesInteractor
import com.michaelfotiadis.flourpower.domain.cake.model.CakeEntity
import com.michaelfotiadis.flourpower.domain.error.model.DataSourceError
import com.michaelfotiadis.flourpower.domain.error.model.DataSourceErrorKind
import com.michaelfotiadis.flourpower.ui.error.UiError
import com.michaelfotiadis.flourpower.ui.error.UiErrorMapper
import com.michaelfotiadis.flourpower.ui.main.mapper.UiCakeMapper
import com.michaelfotiadis.flourpower.ui.main.mapper.UiCakeSorter
import com.michaelfotiadis.flourpower.ui.main.model.UiCakeItem
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doAnswer
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.verifyZeroInteractions
import junit.framework.TestCase.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyList
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class MainViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    internal lateinit var lifecycleOwner: LifecycleOwner
    @Mock
    internal lateinit var getAllCakesInteractor: GetAllCakesInteractor
    @Mock
    internal lateinit var uiCakesMapper: UiCakeMapper
    @Mock
    internal lateinit var uiCakeSorter: UiCakeSorter
    @Mock
    internal lateinit var uiErrorMapper: UiErrorMapper
    @Mock
    internal lateinit var loadingStateObserver: Observer<LoadingState>
    @Mock
    internal lateinit var resultsObserver: Observer<List<UiCakeItem>>
    @Mock
    internal lateinit var errorObserver: Observer<UiError>

    private lateinit var viewModel: MainViewModel
    private lateinit var lifecycleRegistry: LifecycleRegistry

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        // setup
        lifecycleRegistry = LifecycleRegistry(lifecycleOwner)
        `when`(lifecycleOwner.lifecycle).thenReturn(lifecycleRegistry)

        viewModel = MainViewModel(getAllCakesInteractor, uiCakesMapper, uiCakeSorter, uiErrorMapper)

        // act
        viewModel.loadingStateData.observe(lifecycleOwner, loadingStateObserver)
        viewModel.resultsData.observe(lifecycleOwner, resultsObserver)
        viewModel.errorData.observe(lifecycleOwner, errorObserver)
        // assert
        assertEquals(viewModel.loadingStateData.hasObservers(), true)
        assertEquals(viewModel.loadingStateData.hasActiveObservers(), false)
        assertEquals(viewModel.resultsData.hasObservers(), true)
        assertEquals(viewModel.resultsData.hasActiveObservers(), false)
        assertEquals(viewModel.errorData.hasObservers(), true)
        assertEquals(viewModel.errorData.hasActiveObservers(), false)

        // act
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)
        // assert
        assertEquals(viewModel.loadingStateData.hasObservers(), true)
        assertEquals(viewModel.loadingStateData.hasActiveObservers(), true)
        assertEquals(viewModel.resultsData.hasObservers(), true)
        assertEquals(viewModel.resultsData.hasActiveObservers(), true)
        assertEquals(viewModel.errorData.hasObservers(), true)
        assertEquals(viewModel.errorData.hasActiveObservers(), true)
    }

    @After
    fun tearDown() {

        // act
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
        // assert
        assertEquals(viewModel.loadingStateData.hasObservers(), true)
        assertEquals(viewModel.loadingStateData.hasActiveObservers(), false)
        assertEquals(viewModel.resultsData.hasObservers(), true)
        assertEquals(viewModel.resultsData.hasActiveObservers(), false)
        assertEquals(viewModel.errorData.hasObservers(), true)
        assertEquals(viewModel.errorData.hasActiveObservers(), false)

        // tear down
        viewModel.loadingStateData.removeObserver(loadingStateObserver)
        viewModel.resultsData.removeObserver(resultsObserver)
        viewModel.errorData.removeObserver(errorObserver)
        // assert
        assertEquals(viewModel.loadingStateData.hasObservers(), false)
        assertEquals(viewModel.loadingStateData.hasActiveObservers(), false)
        viewModel.resultsData.removeObserver(resultsObserver)
        assertEquals(viewModel.resultsData.hasObservers(), false)
        assertEquals(viewModel.resultsData.hasActiveObservers(), false)
        viewModel.errorData.removeObserver(errorObserver)
        assertEquals(viewModel.errorData.hasObservers(), false)
        assertEquals(viewModel.errorData.hasActiveObservers(), false)
    }

    @Test
    fun loadCakes_andStartLoading() {

        val loadingResult = RepoResult<List<CakeEntity>>(loadingState = LoadingState.LOADING)

        doAnswer {
            val callback: BaseDisposableInteractor.Callback<List<CakeEntity>> = it.getArgument(0)
            callback.onNext(loadingResult)
        }.`when`(getAllCakesInteractor).getCakes(any())

        // act
        viewModel.loadCakes()

        // assert
        verify(loadingStateObserver).onChanged(LoadingState.LOADING)

        verifyZeroInteractions(resultsObserver)
        verifyZeroInteractions(errorObserver)
    }

    @Test
    fun loadCakes_andShowError() {

        val errorResult = RepoResult<List<CakeEntity>>(
            dataSourceError = DataSourceError("Whoops", DataSourceErrorKind.BAD_REQUEST),
            loadingState = LoadingState.IDLE
        )
        val uiErrorResult = UiError.BAD_REQUEST

        `when`(uiErrorMapper.convert(any())).thenReturn(uiErrorResult)

        doAnswer {
            val callback: BaseDisposableInteractor.Callback<List<CakeEntity>> = it.getArgument(0)
            callback.onNext(errorResult)
        }.`when`(getAllCakesInteractor).getCakes(any())

        // act
        viewModel.loadCakes()

        // assert
        verify(loadingStateObserver).onChanged(LoadingState.IDLE)

        verify(errorObserver).onChanged(uiErrorResult)

        verifyZeroInteractions(resultsObserver)
    }

    @Test
    fun loadCakes_andShowResult() {

        val cakeResponse = mock<List<CakeEntity>>()
        val cakeUiItems = mock<List<UiCakeItem>> {
            on(it.size) doReturn 2
        }

        val cakeResult = RepoResult(
            payload = cakeResponse,
            dataSourceError = null,
            loadingState = LoadingState.IDLE
        )

        `when`(uiErrorMapper.convert(any())).thenReturn(null)
        `when`(uiCakesMapper.convert(anyList())).thenReturn(cakeUiItems)
        `when`(uiCakeSorter.sort(anyList())).thenReturn(cakeUiItems)

        doAnswer {
            val callback: BaseDisposableInteractor.Callback<List<CakeEntity>> = it.getArgument(0)
            callback.onNext(cakeResult)
        }.`when`(getAllCakesInteractor).getCakes(any())

        // act
        viewModel.loadCakes()

        // assert
        verify(uiCakesMapper).convert(cakeResponse)
        verify(loadingStateObserver).onChanged(LoadingState.IDLE)
        verify(resultsObserver).onChanged(cakeUiItems)
        verifyZeroInteractions(errorObserver)
    }

    @Test
    fun clearError() {

        val errorResult = RepoResult<List<CakeEntity>>(
            dataSourceError = DataSourceError("Whoops2", DataSourceErrorKind.COMMUNICATION),
            loadingState = LoadingState.IDLE
        )
        val uiErrorResult = UiError.COMMUNICATION

        `when`(uiErrorMapper.convert(any())).thenReturn(uiErrorResult)

        doAnswer {
            val callback: BaseDisposableInteractor.Callback<List<CakeEntity>> = it.getArgument(0)
            callback.onNext(errorResult)
        }.`when`(getAllCakesInteractor).getCakes(any())

        // act
        viewModel.loadCakes()

        // assert
        verify(loadingStateObserver).onChanged(LoadingState.IDLE)

        verify(errorObserver).onChanged(uiErrorResult)

        verifyZeroInteractions(resultsObserver)

        // act again
        viewModel.clearError()

        // assert again
        verify(errorObserver).onChanged(null)
    }

    @Test
    fun cancelAllJobs() {

        // act
        viewModel.cancelAllJobs()

        verify(getAllCakesInteractor).cancelAll()

        // assert
        verifyZeroInteractions(resultsObserver)
        verifyZeroInteractions(errorObserver)
        verifyZeroInteractions(loadingStateObserver)
    }
}