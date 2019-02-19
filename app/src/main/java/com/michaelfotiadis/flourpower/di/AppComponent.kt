package com.michaelfotiadis.flourpower.di

import android.app.Application
import com.michaelfotiadis.flourpower.FlourPowerApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        ActivityBuilderModule::class,
        AppModule::class,
        NetModule::class,
        MapperModule::class,
        InteractorModule::class
    ]
)
interface AppComponent : AndroidInjector<FlourPowerApplication> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
}
