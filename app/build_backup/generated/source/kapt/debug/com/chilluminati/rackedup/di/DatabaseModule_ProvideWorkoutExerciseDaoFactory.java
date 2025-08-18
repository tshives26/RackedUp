package com.chilluminati.rackedup.di;

import com.chilluminati.rackedup.data.database.RackedUpDatabase;
import com.chilluminati.rackedup.data.database.dao.WorkoutExerciseDao;
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
public final class DatabaseModule_ProvideWorkoutExerciseDaoFactory implements Factory<WorkoutExerciseDao> {
  private final Provider<RackedUpDatabase> databaseProvider;

  public DatabaseModule_ProvideWorkoutExerciseDaoFactory(
      Provider<RackedUpDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public WorkoutExerciseDao get() {
    return provideWorkoutExerciseDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideWorkoutExerciseDaoFactory create(
      Provider<RackedUpDatabase> databaseProvider) {
    return new DatabaseModule_ProvideWorkoutExerciseDaoFactory(databaseProvider);
  }

  public static WorkoutExerciseDao provideWorkoutExerciseDao(RackedUpDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideWorkoutExerciseDao(database));
  }
}
