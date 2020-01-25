package com.legacy.liverates.dependencies

import com.legacy.liverates.model.RepoFactoryProvider
import javax.annotation.Generated
import javax.inject.Provider

import dagger.internal.DoubleCheck
import dagger.internal.Preconditions
import com.legacy.liverates.model.RepoRates
import com.legacy.liverates.model.RepositoryModule
import com.legacy.liverates.view.RatesActivity
import com.legacy.liverates.view.RatesViewModelInjection
import com.legacy.liverates.viewmodel.RatesViewModelModule
import com.legacy.liverates.viewmodel.ViewModelFactoryProvider
import com.legacy.liverates.viewmodel.RatesViewModel

@Generated(value = ["dagger.internal.codegen.ComponentProcessor"], comments = "https://google.github.io/dagger")
class DaggerAppComponent private constructor(builder: Builder) : AppComponent {
    private var provideRepositoryProvider: Provider<RepoRates>? = null

    private var provideViewModelProvider: Provider<RatesViewModel>? = null

    init {
        initialize(builder)
    }

    private fun initialize(builder: Builder) {
        this.provideRepositoryProvider = DoubleCheck.provider(
                RepoFactoryProvider.create(builder.repositoryModule))
        this.provideViewModelProvider = DoubleCheck.provider(
                ViewModelFactoryProvider.create(
                        builder.ratesViewModelModule!!, provideRepositoryProvider!!))
    }

    override fun inject(activity: RatesActivity) {
        injectCurrenciesActivity(activity)
    }

    private fun injectCurrenciesActivity(instance: RatesActivity): RatesActivity {
        RatesViewModelInjection.injectViewModel(instance, provideViewModelProvider!!.get())
        return instance
    }

    class Builder() {
        var repositoryModule: RepositoryModule? = null

        var ratesViewModelModule: RatesViewModelModule? = null

        fun build(): AppComponent {
            if (repositoryModule == null) {
                throw IllegalStateException(RepositoryModule::class.java.canonicalName + " must be set")
            }
            if (ratesViewModelModule == null) {
                this.ratesViewModelModule = RatesViewModelModule()
            }
            return DaggerAppComponent(this)
        }

        fun repositoryModule(repositoryModule: RepositoryModule): Builder {
            this.repositoryModule = Preconditions.checkNotNull(repositoryModule)
            return this
        }

        fun currenciesViewModelModule(ratesViewModelModule: RatesViewModelModule): Builder {
            this.ratesViewModelModule = Preconditions.checkNotNull(ratesViewModelModule)
            return this
        }
    }

    companion object {

        fun builder(): Builder {
            return Builder()
        }
    }
}
