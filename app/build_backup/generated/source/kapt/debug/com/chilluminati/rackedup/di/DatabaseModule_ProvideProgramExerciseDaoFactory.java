package com.chilluminati.rackedup.di;

import com.chilluminati.rackedup.data.database.RackedUpDatabase;
import com.chilluminati.rackedup.data.database.dao.ProgramExerciseDao;
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
public final class DatabaseModule_ProvideProgramExerciseDaoFactory implements Factory<ProgramExerciseDao> {
  private final Provider<RackedUpDatabase> databaseProvider;

  public DatabaseModule_ProvideProgramExerciseDaoFactory(
      Provider<RackedUpDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public ProgramExerciseDao get() {
    return provideProgramExerciseDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideProgramExerciseDaoFactory create(
      Provider<RackedUpDatabase> databaseProvider) {
    return new DatabaseModule_ProvideProgramExerciseDaoFactory(databaseProvider);
  }

  public static ProgramExerciseDao provideProgramExerciseDao(RackedUpDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideProgramExerciseDao(database));
  }
}
