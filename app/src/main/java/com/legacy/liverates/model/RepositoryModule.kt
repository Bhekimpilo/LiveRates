package com.legacy.liverates.model

import android.content.Context

import javax.inject.Singleton

import dagger.Module
import dagger.Provides
import com.legacy.liverates.network.RetrofitCall

@Module
class RepositoryModule(context: Context, retrofitCall: RetrofitCall) {
    val repository: RepoRates

    init {
        repository = RepoRatesImplementer(context, retrofitCall)
    }

    @Provides
    @Singleton
    fun provideRepository(): RepoRates {
        return repository
    }
}
