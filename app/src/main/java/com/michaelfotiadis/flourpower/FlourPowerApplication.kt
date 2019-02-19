package com.michaelfotiadis.flourpower

import androidx.core.content.ContextCompat
import com.michaelfotiadis.flourpower.di.DaggerAppComponent
import com.squareup.leakcanary.LeakCanary
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import es.dmoral.toasty.Toasty
import timber.log.Timber

class FlourPowerApplication : DaggerApplication() {

    override fun onCreate() {
        super.onCreate()

        if (LeakCanary.isInAnalyzerProcess(this)) {
            return
        }

        LeakCanary.install(this)

        initTimber()

        initToasty()
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            Timber.d("Timber initialised")
        } else {
            Timber.d("You should not be seeing this!")
        }
    }

    private fun initToasty() {
        Toasty.Config.getInstance()
            .setErrorColor(ContextCompat.getColor(this, R.color.error))
            .setInfoColor(ContextCompat.getColor(this, R.color.primary_dark))
            .setSuccessColor(ContextCompat.getColor(this, R.color.primary))
            .setWarningColor(ContextCompat.getColor(this, R.color.accent))
            .setTextColor(ContextCompat.getColor(this, R.color.white))
            .tintIcon(true)
            .apply()
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder()
            .application(this)
            .build()
    }
}
