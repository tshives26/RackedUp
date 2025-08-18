package com.chilluminati.rackedup.di;

import com.chilluminati.rackedup.data.database.RackedUpDatabase;
import com.chilluminati.rackedup.data.database.dao.ExerciseSetDao;
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
public final class DatabaseModule_ProvideExerciseSetDaoFactory implements Factory<ExerciseSetDao> {
  private final Provider<RackedUpDatabase> databaseProvider;

  public DatabaseModule_ProvideExerciseSetDaoFactory(Provider<RackedUpDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public ExerciseSetDao get() {
    return provideExerciseSetDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideExerciseSetDaoFactory create(
      Provider<RackedUpDatabase> databaseProvider) {
    return new DatabaseModule_ProvideExerciseSetDaoFactory(databaseProvider);
  }

  public static ExerciseSetDao provideExerciseSetDao(RackedUpDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideExerciseSetDao(database));
  }
}
