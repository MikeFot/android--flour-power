package com.michaelfotiadis.flourpower.di

import com.michaelfotiadis.flourpower.ui.main.MainActivity
import com.michaelfotiadis.flourpower.ui.main.di.MainActivityModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilderModule {

    @ContributesAndroidInjector(modules = [MainActivityModule::class])
    abstract fun mainActivity(): MainActivity
}
