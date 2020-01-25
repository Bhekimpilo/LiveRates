package com.legacy.liverates.viewmodel

import javax.inject.Singleton

import dagger.Module
import dagger.Provides
import com.legacy.liverates.model.RepoRates

@Module
class RatesViewModelModule {

    @Provides
    @Singleton
    fun provideViewModel(repository: RepoRates): RatesViewModel {
        return RatesViewModel(repository)
    }

}
