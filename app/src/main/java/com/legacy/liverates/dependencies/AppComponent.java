package com.legacy.liverates.dependencies;

import javax.inject.Singleton;

import dagger.Component;

import com.legacy.liverates.model.RepositoryModule;
import com.legacy.liverates.view.RatesActivity;
import com.legacy.liverates.viewmodel.RatesViewModelModule;

@Singleton
@Component(modules = {RepositoryModule.class, RatesViewModelModule.class})
public interface AppComponent {
    void inject(RatesActivity activity);
}
