package com.chilluminati.rackedup.di;

import android.content.Context;
import com.chilluminati.rackedup.data.database.RackedUpDatabase;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class DatabaseModule_ProvideRackedUpDatabaseFactory implements Factory<RackedUpDatabase> {
  private final Provider<Context> contextProvider;

  public DatabaseModule_ProvideRackedUpDatabaseFactory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public RackedUpDatabase get() {
    return provideRackedUpDatabase(contextProvider.get());
  }

  public static DatabaseModule_ProvideRackedUpDatabaseFactory create(
      Provider<Context> contextProvider) {
    return new DatabaseModule_ProvideRackedUpDatabaseFactory(contextProvider);
  }

  public static RackedUpDatabase provideRackedUpDatabase(Context context) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideRackedUpDatabase(context));
  }
}
