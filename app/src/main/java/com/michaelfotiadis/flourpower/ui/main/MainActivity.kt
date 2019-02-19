package com.michaelfotiadis.flourpower.ui.main

import android.content.res.Configuration
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.michaelfotiadis.flourpower.R
import com.michaelfotiadis.flourpower.domain.base.LoadingState
import com.michaelfotiadis.flourpower.ui.base.BaseActivity
import com.michaelfotiadis.flourpower.ui.error.UiError
import com.michaelfotiadis.flourpower.ui.main.adapter.CakeRecyclerAdapter
import com.michaelfotiadis.flourpower.ui.main.model.UiCakeItem
import com.michaelfotiadis.flourpower.ui.main.viewmodel.MainViewModel
import com.michaelfotiadis.flourpower.ui.main.viewmodel.MainViewModelFactory
import es.dmoral.toasty.Toasty
import timber.log.Timber
import javax.inject.Inject

class MainActivity : BaseActivity() {

    @Inject
    lateinit var viewModelFactory: MainViewModelFactory

    private lateinit var viewModel: MainViewModel
    private lateinit var viewHolder: MainViewHolder
    private lateinit var adapter: CakeRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)
        setContentView(R.layout.activity_main)

        viewHolder = MainViewHolder(this)

        viewModel.loadingStateData.observe(this, Observer { onLoadingStateChanged(it) })
        viewModel.resultsData.observe(this, Observer { onResultsChanged(it) })
        viewModel.errorData.observe(this, Observer { onErrorChanged(it) })

        adapter = CakeRecyclerAdapter(object : CakeRecyclerAdapter.Callback {
            override fun onCakeClicked(uiCakeItem: UiCakeItem) {
                Toasty.info(this@MainActivity, uiCakeItem.title).show()
            }
        })

        viewHolder.toolbar.apply {
            setTitle(R.string.app_name)
            this@MainActivity.setSupportActionBar(this)
        }

        viewHolder.recyclerView.apply {
            if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                layoutManager = GridLayoutManager(context, 2)
            } else {
                layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            }
            layoutAnimation =
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_slide_from_right)
            adapter = this@MainActivity.adapter
        }

        viewHolder.swipeRefreshLayout.apply {
            setOnRefreshListener { viewModel.loadCakes() }
        }

        if (savedInstanceState == null) {
            Timber.d("Loading Cakes")
            viewModel.loadCakes()
        } else {
            Timber.d("Skipping cake load")
        }
    }

    private fun onErrorChanged(uiError: UiError?) {
        uiError?.let {
            Toasty.error(this, getString(uiError.messageResId)).show()
            // finally, clear viewmodel error after handling it so it does not get re-posted later on
            viewModel.clearError()
        }
    }

    private fun onResultsChanged(results: List<UiCakeItem>?) {

        results?.let {
            Timber.d("Got ${results.size} results")
            adapter.submitList(results)
            adapter.notifyDataSetChanged()
        }
    }

    private fun onLoadingStateChanged(loadingState: LoadingState?) {

        when (loadingState) {
            LoadingState.LOADING -> viewHolder.swipeRefreshLayout.isEnabled = false
            LoadingState.IDLE -> {
                viewHolder.swipeRefreshLayout.isEnabled = true
                viewHolder.swipeRefreshLayout.isRefreshing = false
            }
        }
    }

    inner class MainViewHolder(activity: AppCompatActivity) {
        val toolbar = activity.findViewById<Toolbar>(R.id.toolbar)
        val recyclerView = activity.findViewById<RecyclerView>(R.id.recycler_view)
        val swipeRefreshLayout =
            activity.findViewById<SwipeRefreshLayout>(R.id.swipe_refresh_layout)
    }
}
