package com.legacy.liverates.viewmodel

import javax.annotation.Generated
import javax.inject.Provider

import dagger.internal.Factory
import dagger.internal.Preconditions
import com.legacy.liverates.model.RepoRates

@Generated(value = ["dagger.internal.codegen.ComponentProcessor"], comments = "https://google.github.io/dagger")
class ViewModelFactoryProvider(
        private val module: RatesViewModelModule, private val repositoryProvider: Provider<RepoRates>) : Factory<RatesViewModel> {

    override fun get(): RatesViewModel {
        return Preconditions.checkNotNull(
                module.provideViewModel(repositoryProvider.get()),
                "Cannot return null from a non-@Nullable @Provides method")
    }

    companion object {

        fun create(
                module: RatesViewModelModule, repositoryProvider: Provider<RepoRates>): Factory<RatesViewModel> {
            return ViewModelFactoryProvider(module, repositoryProvider)
        }

        fun proxyProvideViewModel(
                instance: RatesViewModelModule, repository: RepoRates): RatesViewModel {
            return instance.provideViewModel(repository)
        }
    }
}
