package com.chilluminati.rackedup.di;

/**
 * Hilt module for database dependencies
 */
@dagger.Module()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000V\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0007J\u0010\u0010\u0007\u001a\u00020\b2\u0006\u0010\u0005\u001a\u00020\u0006H\u0007J\u0010\u0010\t\u001a\u00020\n2\u0006\u0010\u0005\u001a\u00020\u0006H\u0007J\u0010\u0010\u000b\u001a\u00020\f2\u0006\u0010\u0005\u001a\u00020\u0006H\u0007J\u0010\u0010\r\u001a\u00020\u000e2\u0006\u0010\u0005\u001a\u00020\u0006H\u0007J\u0010\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0005\u001a\u00020\u0006H\u0007J\u0010\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0005\u001a\u00020\u0006H\u0007J\u0012\u0010\u0013\u001a\u00020\u00062\b\b\u0001\u0010\u0014\u001a\u00020\u0015H\u0007J\u0010\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0005\u001a\u00020\u0006H\u0007J\u0010\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u0005\u001a\u00020\u0006H\u0007J\u0010\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u0005\u001a\u00020\u0006H\u0007\u00a8\u0006\u001c"}, d2 = {"Lcom/chilluminati/rackedup/di/DatabaseModule;", "", "()V", "provideBodyMeasurementDao", "Lcom/chilluminati/rackedup/data/database/dao/BodyMeasurementDao;", "database", "Lcom/chilluminati/rackedup/data/database/RackedUpDatabase;", "provideExerciseDao", "Lcom/chilluminati/rackedup/data/database/dao/ExerciseDao;", "provideExerciseSetDao", "Lcom/chilluminati/rackedup/data/database/dao/ExerciseSetDao;", "providePersonalRecordDao", "Lcom/chilluminati/rackedup/data/database/dao/PersonalRecordDao;", "provideProgramDao", "Lcom/chilluminati/rackedup/data/database/dao/ProgramDao;", "provideProgramDayDao", "Lcom/chilluminati/rackedup/data/database/dao/ProgramDayDao;", "provideProgramExerciseDao", "Lcom/chilluminati/rackedup/data/database/dao/ProgramExerciseDao;", "provideRackedUpDatabase", "context", "Landroid/content/Context;", "provideUserProfileDao", "Lcom/chilluminati/rackedup/data/database/dao/UserProfileDao;", "provideWorkoutDao", "Lcom/chilluminati/rackedup/data/database/dao/WorkoutDao;", "provideWorkoutExerciseDao", "Lcom/chilluminati/rackedup/data/database/dao/WorkoutExerciseDao;", "app_debug"})
@dagger.hilt.InstallIn(value = {dagger.hilt.components.SingletonComponent.class})
public final class DatabaseModule {
    @org.jetbrains.annotations.NotNull()
    public static final com.chilluminati.rackedup.di.DatabaseModule INSTANCE = null;
    
    private DatabaseModule() {
        super();
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.chilluminati.rackedup.data.database.RackedUpDatabase provideRackedUpDatabase(@dagger.hilt.android.qualifiers.ApplicationContext()
    @org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        return null;
    }
    
    @dagger.Provides()
    @org.jetbrains.annotations.NotNull()
    public final com.chilluminati.rackedup.data.database.dao.ExerciseDao provideExerciseDao(@org.jetbrains.annotations.NotNull()
    com.chilluminati.rackedup.data.database.RackedUpDatabase database) {
        return null;
    }
    
    @dagger.Provides()
    @org.jetbrains.annotations.NotNull()
    public final com.chilluminati.rackedup.data.database.dao.WorkoutDao provideWorkoutDao(@org.jetbrains.annotations.NotNull()
    com.chilluminati.rackedup.data.database.RackedUpDatabase database) {
        return null;
    }
    
    @dagger.Provides()
    @org.jetbrains.annotations.NotNull()
    public final com.chilluminati.rackedup.data.database.dao.WorkoutExerciseDao provideWorkoutExerciseDao(@org.jetbrains.annotations.NotNull()
    com.chilluminati.rackedup.data.database.RackedUpDatabase database) {
        return null;
    }
    
    @dagger.Provides()
    @org.jetbrains.annotations.NotNull()
    public final com.chilluminati.rackedup.data.database.dao.ExerciseSetDao provideExerciseSetDao(@org.jetbrains.annotations.NotNull()
    com.chilluminati.rackedup.data.database.RackedUpDatabase database) {
        return null;
    }
    
    @dagger.Provides()
    @org.jetbrains.annotations.NotNull()
    public final com.chilluminati.rackedup.data.database.dao.UserProfileDao provideUserProfileDao(@org.jetbrains.annotations.NotNull()
    com.chilluminati.rackedup.data.database.RackedUpDatabase database) {
        return null;
    }
    
    @dagger.Provides()
    @org.jetbrains.annotations.NotNull()
    public final com.chilluminati.rackedup.data.database.dao.ProgramDao provideProgramDao(@org.jetbrains.annotations.NotNull()
    com.chilluminati.rackedup.data.database.RackedUpDatabase database) {
        return null;
    }
    
    @dagger.Provides()
    @org.jetbrains.annotations.NotNull()
    public final com.chilluminati.rackedup.data.database.dao.ProgramDayDao provideProgramDayDao(@org.jetbrains.annotations.NotNull()
    com.chilluminati.rackedup.data.database.RackedUpDatabase database) {
        return null;
    }
    
    @dagger.Provides()
    @org.jetbrains.annotations.NotNull()
    public final com.chilluminati.rackedup.data.database.dao.ProgramExerciseDao provideProgramExerciseDao(@org.jetbrains.annotations.NotNull()
    com.chilluminati.rackedup.data.database.RackedUpDatabase database) {
        return null;
    }
    
    @dagger.Provides()
    @org.jetbrains.annotations.NotNull()
    public final com.chilluminati.rackedup.data.database.dao.PersonalRecordDao providePersonalRecordDao(@org.jetbrains.annotations.NotNull()
    com.chilluminati.rackedup.data.database.RackedUpDatabase database) {
        return null;
    }
    
    @dagger.Provides()
    @org.jetbrains.annotations.NotNull()
    public final com.chilluminati.rackedup.data.database.dao.BodyMeasurementDao provideBodyMeasurementDao(@org.jetbrains.annotations.NotNull()
    com.chilluminati.rackedup.data.database.RackedUpDatabase database) {
        return null;
    }
}