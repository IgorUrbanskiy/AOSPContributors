package com.example.aospcontributors

import android.app.Application
import com.example.aospcontributors.di.ComponentFactory
import com.example.aospcontributors.di.ComponentFactoryImpl
import com.mapbox.mapboxsdk.Mapbox

/**
 * Created by Ihor Urbanskyi on 30.03.2019.
 */

class App : Application() {

    companion object {
        lateinit var instance: App
            private set
    }

    lateinit var componentFactory: ComponentFactory

    override fun onCreate() {
        super.onCreate()
        instance = this
        Mapbox.getInstance(applicationContext, getString(R.string.mapbox_access_token))
        componentFactory = ComponentFactoryImpl()
    }
}