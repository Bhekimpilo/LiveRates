package com.legacy.liverates

import android.app.Application
import com.blongho.country_data.World

import com.legacy.liverates.dependencies.AppComponent
import com.legacy.liverates.dependencies.DaggerAppComponent
import com.legacy.liverates.model.RepositoryModule
import com.legacy.liverates.network.RetrofitCall


class App : Application() {

    override fun onCreate() {
        super.onCreate()

        component = buildComponent()

        World.init(this)

    }

    private fun buildComponent(): AppComponent {
        return DaggerAppComponent.builder()
                .repositoryModule(RepositoryModule(applicationContext, RetrofitCall()))
                .build()

    }

    companion object {

        var component: AppComponent? = null
            private set
    }
}
