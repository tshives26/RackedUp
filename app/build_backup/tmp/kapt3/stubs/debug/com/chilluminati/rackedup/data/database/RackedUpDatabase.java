package com.chilluminati.rackedup.data.database;

/**
 * Main Room database for RackedUp
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000J\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\'\u0018\u0000 \u00172\u00020\u0001:\u0001\u0017B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H&J\b\u0010\u0005\u001a\u00020\u0006H&J\b\u0010\u0007\u001a\u00020\bH&J\b\u0010\t\u001a\u00020\nH&J\b\u0010\u000b\u001a\u00020\fH&J\b\u0010\r\u001a\u00020\u000eH&J\b\u0010\u000f\u001a\u00020\u0010H&J\b\u0010\u0011\u001a\u00020\u0012H&J\b\u0010\u0013\u001a\u00020\u0014H&J\b\u0010\u0015\u001a\u00020\u0016H&\u00a8\u0006\u0018"}, d2 = {"Lcom/chilluminati/rackedup/data/database/RackedUpDatabase;", "Landroidx/room/RoomDatabase;", "()V", "bodyMeasurementDao", "Lcom/chilluminati/rackedup/data/database/dao/BodyMeasurementDao;", "exerciseDao", "Lcom/chilluminati/rackedup/data/database/dao/ExerciseDao;", "exerciseSetDao", "Lcom/chilluminati/rackedup/data/database/dao/ExerciseSetDao;", "personalRecordDao", "Lcom/chilluminati/rackedup/data/database/dao/PersonalRecordDao;", "programDao", "Lcom/chilluminati/rackedup/data/database/dao/ProgramDao;", "programDayDao", "Lcom/chilluminati/rackedup/data/database/dao/ProgramDayDao;", "programExerciseDao", "Lcom/chilluminati/rackedup/data/database/dao/ProgramExerciseDao;", "userProfileDao", "Lcom/chilluminati/rackedup/data/database/dao/UserProfileDao;", "workoutDao", "Lcom/chilluminati/rackedup/data/database/dao/WorkoutDao;", "workoutExerciseDao", "Lcom/chilluminati/rackedup/data/database/dao/WorkoutExerciseDao;", "Companion", "app_debug"})
@androidx.room.Database(entities = {com.chilluminati.rackedup.data.database.entity.Exercise.class, com.chilluminati.rackedup.data.database.entity.Workout.class, com.chilluminati.rackedup.data.database.entity.WorkoutExercise.class, com.chilluminati.rackedup.data.database.entity.ExerciseSet.class, com.chilluminati.rackedup.data.database.entity.UserProfile.class, com.chilluminati.rackedup.data.database.entity.Program.class, com.chilluminati.rackedup.data.database.entity.ProgramDay.class, com.chilluminati.rackedup.data.database.entity.ProgramExercise.class, com.chilluminati.rackedup.data.database.entity.PersonalRecord.class, com.chilluminati.rackedup.data.database.entity.BodyMeasurement.class}, version = 1, exportSchema = true)
@androidx.room.TypeConverters(value = {com.chilluminati.rackedup.data.database.converter.Converters.class})
public abstract class RackedUpDatabase extends androidx.room.RoomDatabase {
    @kotlin.jvm.Volatile()
    @org.jetbrains.annotations.Nullable()
    private static volatile com.chilluminati.rackedup.data.database.RackedUpDatabase INSTANCE;
    @org.jetbrains.annotations.NotNull()
    public static final com.chilluminati.rackedup.data.database.RackedUpDatabase.Companion Companion = null;
    
    public RackedUpDatabase() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.chilluminati.rackedup.data.database.dao.ExerciseDao exerciseDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.chilluminati.rackedup.data.database.dao.WorkoutDao workoutDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.chilluminati.rackedup.data.database.dao.WorkoutExerciseDao workoutExerciseDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.chilluminati.rackedup.data.database.dao.ExerciseSetDao exerciseSetDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.chilluminati.rackedup.data.database.dao.UserProfileDao userProfileDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.chilluminati.rackedup.data.database.dao.ProgramDao programDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.chilluminati.rackedup.data.database.dao.ProgramDayDao programDayDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.chilluminati.rackedup.data.database.dao.ProgramExerciseDao programExerciseDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.chilluminati.rackedup.data.database.dao.PersonalRecordDao personalRecordDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.chilluminati.rackedup.data.database.dao.BodyMeasurementDao bodyMeasurementDao();
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0006\u001a\u00020\u0007R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\b"}, d2 = {"Lcom/chilluminati/rackedup/data/database/RackedUpDatabase$Companion;", "", "()V", "INSTANCE", "Lcom/chilluminati/rackedup/data/database/RackedUpDatabase;", "getDatabase", "context", "Landroid/content/Context;", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.chilluminati.rackedup.data.database.RackedUpDatabase getDatabase(@org.jetbrains.annotations.NotNull()
        android.content.Context context) {
            return null;
        }
    }
}