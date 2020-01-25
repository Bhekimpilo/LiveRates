package com.legacy.liverates.view

import dagger.MembersInjector
import com.legacy.liverates.viewmodel.RatesViewModel
import javax.annotation.Generated
import javax.inject.Provider

@Generated(value = ["dagger.internal.codegen.ComponentProcessor"], comments = "https://google.github.io/dagger")
class RatesViewModelInjection(private val viewModelProvider: Provider<RatesViewModel>) : MembersInjector<RatesActivity> {

    override fun injectMembers(instance: RatesActivity) {
        injectViewModel(instance, viewModelProvider.get())
    }

    companion object {
        fun injectViewModel(instance: RatesActivity, viewModel: RatesViewModel) {
            instance.viewModel = viewModel
        }
    }
}
