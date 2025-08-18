package com.chilluminati.rackedup;

import androidx.hilt.work.HiltWorkerFactory;
import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast"
})
public final class RackedUpApplication_MembersInjector implements MembersInjector<RackedUpApplication> {
  private final Provider<HiltWorkerFactory> workerFactoryProvider;

  public RackedUpApplication_MembersInjector(Provider<HiltWorkerFactory> workerFactoryProvider) {
    this.workerFactoryProvider = workerFactoryProvider;
  }

  public static MembersInjector<RackedUpApplication> create(
      Provider<HiltWorkerFactory> workerFactoryProvider) {
    return new RackedUpApplication_MembersInjector(workerFactoryProvider);
  }

  @Override
  public void injectMembers(RackedUpApplication instance) {
    injectWorkerFactory(instance, workerFactoryProvider.get());
  }

  @InjectedFieldSignature("com.chilluminati.rackedup.RackedUpApplication.workerFactory")
  public static void injectWorkerFactory(RackedUpApplication instance,
      HiltWorkerFactory workerFactory) {
    instance.workerFactory = workerFactory;
  }
}
