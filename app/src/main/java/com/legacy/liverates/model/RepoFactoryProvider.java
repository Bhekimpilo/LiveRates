package com.legacy.liverates.model;

import javax.annotation.Generated;

import dagger.internal.Factory;
import dagger.internal.Preconditions;

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class RepoFactoryProvider
    implements Factory<RepoRates> {
  private final RepositoryModule module;

  public RepoFactoryProvider(RepositoryModule module) {
    this.module = module;
  }

  @Override
  public RepoRates get() {
    return Preconditions.checkNotNull(
        module.provideRepository(), "Returned null");
  }

  public static Factory<RepoRates> create(RepositoryModule module) {
    return new RepoFactoryProvider(module);
  }

  public static RepoRates proxyProvideRepository(RepositoryModule instance) {
    return instance.provideRepository();
  }
}
