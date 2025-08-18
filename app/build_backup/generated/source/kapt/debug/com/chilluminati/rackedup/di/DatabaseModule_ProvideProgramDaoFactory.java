package com.chilluminati.rackedup.di;

import com.chilluminati.rackedup.data.database.RackedUpDatabase;
import com.chilluminati.rackedup.data.database.dao.ProgramDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class DatabaseModule_ProvideProgramDaoFactory implements Factory<ProgramDao> {
  private final Provider<RackedUpDatabase> databaseProvider;

  public DatabaseModule_ProvideProgramDaoFactory(Provider<RackedUpDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public ProgramDao get() {
    return provideProgramDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideProgramDaoFactory create(
      Provider<RackedUpDatabase> databaseProvider) {
    return new DatabaseModule_ProvideProgramDaoFactory(databaseProvider);
  }

  public static ProgramDao provideProgramDao(RackedUpDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideProgramDao(database));
  }
}
