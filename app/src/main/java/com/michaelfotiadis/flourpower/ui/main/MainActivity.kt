package com.michaelfotiadis.flourpower.ui.main

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.ViewFlipper
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
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
import com.michaelfotiadis.flourpower.ui.main.view.ViewFlipperController
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
    private lateinit var viewFlipperController: ViewFlipperController
    private lateinit var adapter: CakeRecyclerAdapter

    companion object {

        private const val NUMBER_OF_COLUMNS = 2

        @JvmStatic
        fun newInstance(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)
        setContentView(R.layout.activity_main)

        viewHolder = MainViewHolder(this)

        adapter = CakeRecyclerAdapter(object : CakeRecyclerAdapter.Callback {
            override fun onCakeClicked(uiCakeItem: UiCakeItem) {
                // TODO would normally use a details screen with shared element transition
                Toasty.info(this@MainActivity, uiCakeItem.description).show()
            }
        })

        setUpViews()

        viewModel.loadingStateData.observe(
            this,
            Observer { state -> state?.let { onLoadingStateChanged(it) } })
        viewModel.resultsData.observe(
            this,
            Observer { results -> results?.let { onResultsChanged(it) } })
        viewModel.errorData.observe(
            this,
            Observer { error -> error?.let { onErrorChanged(it) } })

        if (savedInstanceState == null) {
            Timber.d("Loading Cakes")
            // trigger full screen progress on first load
            viewFlipperController.showProgress()
            viewModel.loadCakes()
        } else {
            Timber.d("Skipping cake load")
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.cancelAllJobs()
    }

    private fun setUpViews() {

        viewHolder.toolbar.apply {
            setTitle(R.string.app_name)
            this@MainActivity.setSupportActionBar(this)
        }

        viewFlipperController = ViewFlipperController(viewHolder.viewFlipper)

        viewHolder.recyclerView.apply {
            if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                layoutManager = GridLayoutManager(context, NUMBER_OF_COLUMNS)
            } else {
                layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            }
            layoutAnimation =
                AnimationUtils.loadLayoutAnimation(
                    context,
                    R.anim.layout_animation_slide_from_right
                )
            adapter = this@MainActivity.adapter
        }

        viewHolder.swipeRefreshLayout.apply {
            setColorSchemeColors(
                ContextCompat.getColor(context, R.color.accent),
                ContextCompat.getColor(context, R.color.primary)
            )
            setOnRefreshListener { viewModel.loadCakes() }
        }
    }

    private fun onErrorChanged(uiError: UiError) {
        Timber.e("Received error")
        if (adapter.itemCount == 0) {
            viewFlipperController.showError()
        }

        Toasty.error(this, getString(uiError.messageResId)).show()
        // finally, clear viewmodel error after handling it so it does not get re-posted later on
        viewModel.clearError()
    }

    private fun onResultsChanged(results: List<UiCakeItem>) {
        Timber.d("Got ${results.size} results")
        viewFlipperController.showContent()
        adapter.submitList(results)
        adapter.notifyDataSetChanged()
    }

    private fun onLoadingStateChanged(loadingState: LoadingState) {
        Timber.d("Loading state changed to ${loadingState.name}")
        when (loadingState) {
            LoadingState.LOADING -> {
                viewHolder.swipeRefreshLayout.post {
                    viewHolder.swipeRefreshLayout.isEnabled = false
                }
            }
            LoadingState.IDLE -> {
                viewHolder.swipeRefreshLayout.post {
                    viewHolder.swipeRefreshLayout.isEnabled = true
                    viewHolder.swipeRefreshLayout.isRefreshing = false
                }
            }
        }
    }

    internal class MainViewHolder(activity: MainActivity) {
        val toolbar = activity.findViewById<Toolbar>(R.id.toolbar)!!
        val recyclerView = activity.findViewById<RecyclerView>(R.id.recycler_view)!!
        val swipeRefreshLayout =
            activity.findViewById<SwipeRefreshLayout>(R.id.swipe_refresh_layout)!!
        val viewFlipper = activity.findViewById<ViewFlipper>(R.id.view_flipper)!!
    }
}
