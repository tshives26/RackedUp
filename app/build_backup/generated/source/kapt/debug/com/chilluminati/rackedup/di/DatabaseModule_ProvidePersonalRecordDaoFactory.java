package com.chilluminati.rackedup.di;

import com.chilluminati.rackedup.data.database.RackedUpDatabase;
import com.chilluminati.rackedup.data.database.dao.PersonalRecordDao;
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
public final class DatabaseModule_ProvidePersonalRecordDaoFactory implements Factory<PersonalRecordDao> {
  private final Provider<RackedUpDatabase> databaseProvider;

  public DatabaseModule_ProvidePersonalRecordDaoFactory(
      Provider<RackedUpDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public PersonalRecordDao get() {
    return providePersonalRecordDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvidePersonalRecordDaoFactory create(
      Provider<RackedUpDatabase> databaseProvider) {
    return new DatabaseModule_ProvidePersonalRecordDaoFactory(databaseProvider);
  }

  public static PersonalRecordDao providePersonalRecordDao(RackedUpDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.providePersonalRecordDao(database));
  }
}
