package com.chilluminati.rackedup.di;

import com.chilluminati.rackedup.data.database.RackedUpDatabase;
import com.chilluminati.rackedup.data.database.dao.BodyMeasurementDao;
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
public final class DatabaseModule_ProvideBodyMeasurementDaoFactory implements Factory<BodyMeasurementDao> {
  private final Provider<RackedUpDatabase> databaseProvider;

  public DatabaseModule_ProvideBodyMeasurementDaoFactory(
      Provider<RackedUpDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public BodyMeasurementDao get() {
    return provideBodyMeasurementDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideBodyMeasurementDaoFactory create(
      Provider<RackedUpDatabase> databaseProvider) {
    return new DatabaseModule_ProvideBodyMeasurementDaoFactory(databaseProvider);
  }

  public static BodyMeasurementDao provideBodyMeasurementDao(RackedUpDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideBodyMeasurementDao(database));
  }
}
