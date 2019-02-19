package com.michaelfotiadis.flourpower.ui.main.view

import android.widget.ViewFlipper
import timber.log.Timber

class ViewFlipperController(private val viewFlipper: ViewFlipper) {

    companion object {
        private const val INDEX_CONTENT = 0
        private const val INDEX_PROGRESS = 1
        private const val INDEX_ERROR = 2
    }

    fun showProgress() {
        Timber.d("Showing progress")
        viewFlipper.displayedChild = INDEX_PROGRESS
    }

    fun showContent() {
        Timber.d("Showing content")
        viewFlipper.displayedChild = INDEX_CONTENT
    }

    fun showError() {
        Timber.d("Showing error")
        viewFlipper.displayedChild = INDEX_ERROR
    }
}