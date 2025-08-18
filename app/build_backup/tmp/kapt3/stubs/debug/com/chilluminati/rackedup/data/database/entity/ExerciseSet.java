package com.chilluminati.rackedup.data.database.entity;

/**
 * ExerciseSet entity representing a single set of an exercise
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b+\b\u0087\b\u0018\u00002\u00020\u0001B\u00a5\u0001\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\b\u0012\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\u0006\u0012\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u0006\u0012\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\b\u0012\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u0006\u0012\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u0006\u0012\b\b\u0002\u0010\u000e\u001a\u00020\u000f\u0012\b\b\u0002\u0010\u0010\u001a\u00020\u000f\u0012\b\b\u0002\u0010\u0011\u001a\u00020\u000f\u0012\b\b\u0002\u0010\u0012\u001a\u00020\u000f\u0012\n\b\u0002\u0010\u0013\u001a\u0004\u0018\u00010\u0014\u0012\b\b\u0002\u0010\u0015\u001a\u00020\u0016\u00a2\u0006\u0002\u0010\u0017J\t\u0010,\u001a\u00020\u0003H\u00c6\u0003J\t\u0010-\u001a\u00020\u000fH\u00c6\u0003J\t\u0010.\u001a\u00020\u000fH\u00c6\u0003J\t\u0010/\u001a\u00020\u000fH\u00c6\u0003J\t\u00100\u001a\u00020\u000fH\u00c6\u0003J\u000b\u00101\u001a\u0004\u0018\u00010\u0014H\u00c6\u0003J\t\u00102\u001a\u00020\u0016H\u00c6\u0003J\t\u00103\u001a\u00020\u0003H\u00c6\u0003J\t\u00104\u001a\u00020\u0006H\u00c6\u0003J\u0010\u00105\u001a\u0004\u0018\u00010\bH\u00c6\u0003\u00a2\u0006\u0002\u0010\u001bJ\u0010\u00106\u001a\u0004\u0018\u00010\u0006H\u00c6\u0003\u00a2\u0006\u0002\u0010\u001eJ\u0010\u00107\u001a\u0004\u0018\u00010\u0006H\u00c6\u0003\u00a2\u0006\u0002\u0010\u001eJ\u0010\u00108\u001a\u0004\u0018\u00010\bH\u00c6\u0003\u00a2\u0006\u0002\u0010\u001bJ\u0010\u00109\u001a\u0004\u0018\u00010\u0006H\u00c6\u0003\u00a2\u0006\u0002\u0010\u001eJ\u0010\u0010:\u001a\u0004\u0018\u00010\u0006H\u00c6\u0003\u00a2\u0006\u0002\u0010\u001eJ\u00b2\u0001\u0010;\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00062\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\b2\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\u00062\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u00062\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\b2\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u00062\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u00062\b\b\u0002\u0010\u000e\u001a\u00020\u000f2\b\b\u0002\u0010\u0010\u001a\u00020\u000f2\b\b\u0002\u0010\u0011\u001a\u00020\u000f2\b\b\u0002\u0010\u0012\u001a\u00020\u000f2\n\b\u0002\u0010\u0013\u001a\u0004\u0018\u00010\u00142\b\b\u0002\u0010\u0015\u001a\u00020\u0016H\u00c6\u0001\u00a2\u0006\u0002\u0010<J\u0013\u0010=\u001a\u00020\u000f2\b\u0010>\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010?\u001a\u00020\u0006H\u00d6\u0001J\t\u0010@\u001a\u00020\u0014H\u00d6\u0001R\u0016\u0010\u0015\u001a\u00020\u00168\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0019R\u001a\u0010\u000b\u001a\u0004\u0018\u00010\b8\u0006X\u0087\u0004\u00a2\u0006\n\n\u0002\u0010\u001c\u001a\u0004\b\u001a\u0010\u001bR\u001a\u0010\n\u001a\u0004\u0018\u00010\u00068\u0006X\u0087\u0004\u00a2\u0006\n\n\u0002\u0010\u001f\u001a\u0004\b\u001d\u0010\u001eR\u0016\u0010\u0002\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b \u0010!R\u0016\u0010\u0012\u001a\u00020\u000f8\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\"R\u0016\u0010\u0010\u001a\u00020\u000f8\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\"R\u0016\u0010\u0011\u001a\u00020\u000f8\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\"R\u0016\u0010\u000e\u001a\u00020\u000f8\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\"R\u0018\u0010\u0013\u001a\u0004\u0018\u00010\u00148\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b#\u0010$R\u001a\u0010\t\u001a\u0004\u0018\u00010\u00068\u0006X\u0087\u0004\u00a2\u0006\n\n\u0002\u0010\u001f\u001a\u0004\b%\u0010\u001eR\u001a\u0010\f\u001a\u0004\u0018\u00010\u00068\u0006X\u0087\u0004\u00a2\u0006\n\n\u0002\u0010\u001f\u001a\u0004\b&\u0010\u001eR\u001a\u0010\r\u001a\u0004\u0018\u00010\u00068\u0006X\u0087\u0004\u00a2\u0006\n\n\u0002\u0010\u001f\u001a\u0004\b\'\u0010\u001eR\u0016\u0010\u0005\u001a\u00020\u00068\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b(\u0010)R\u001a\u0010\u0007\u001a\u0004\u0018\u00010\b8\u0006X\u0087\u0004\u00a2\u0006\n\n\u0002\u0010\u001c\u001a\u0004\b*\u0010\u001bR\u0016\u0010\u0004\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b+\u0010!\u00a8\u0006A"}, d2 = {"Lcom/chilluminati/rackedup/data/database/entity/ExerciseSet;", "", "id", "", "workoutExerciseId", "setNumber", "", "weight", "", "reps", "durationSeconds", "distance", "restTimeSeconds", "rpe", "isWarmup", "", "isDropset", "isFailure", "isCompleted", "notes", "", "createdAt", "Ljava/util/Date;", "(JJILjava/lang/Double;Ljava/lang/Integer;Ljava/lang/Integer;Ljava/lang/Double;Ljava/lang/Integer;Ljava/lang/Integer;ZZZZLjava/lang/String;Ljava/util/Date;)V", "getCreatedAt", "()Ljava/util/Date;", "getDistance", "()Ljava/lang/Double;", "Ljava/lang/Double;", "getDurationSeconds", "()Ljava/lang/Integer;", "Ljava/lang/Integer;", "getId", "()J", "()Z", "getNotes", "()Ljava/lang/String;", "getReps", "getRestTimeSeconds", "getRpe", "getSetNumber", "()I", "getWeight", "getWorkoutExerciseId", "component1", "component10", "component11", "component12", "component13", "component14", "component15", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "(JJILjava/lang/Double;Ljava/lang/Integer;Ljava/lang/Integer;Ljava/lang/Double;Ljava/lang/Integer;Ljava/lang/Integer;ZZZZLjava/lang/String;Ljava/util/Date;)Lcom/chilluminati/rackedup/data/database/entity/ExerciseSet;", "equals", "other", "hashCode", "toString", "app_debug"})
@androidx.room.Entity(tableName = "exercise_sets", foreignKeys = {@androidx.room.ForeignKey(entity = com.chilluminati.rackedup.data.database.entity.WorkoutExercise.class, parentColumns = {"id"}, childColumns = {"workout_exercise_id"}, onDelete = 5)}, indices = {@androidx.room.Index(value = {"workout_exercise_id"})})
public final class ExerciseSet {
    @androidx.room.PrimaryKey(autoGenerate = true)
    private final long id = 0L;
    @androidx.room.ColumnInfo(name = "workout_exercise_id")
    private final long workoutExerciseId = 0L;
    @androidx.room.ColumnInfo(name = "set_number")
    private final int setNumber = 0;
    @androidx.room.ColumnInfo(name = "weight")
    @org.jetbrains.annotations.Nullable()
    private final java.lang.Double weight = null;
    @androidx.room.ColumnInfo(name = "reps")
    @org.jetbrains.annotations.Nullable()
    private final java.lang.Integer reps = null;
    @androidx.room.ColumnInfo(name = "duration_seconds")
    @org.jetbrains.annotations.Nullable()
    private final java.lang.Integer durationSeconds = null;
    @androidx.room.ColumnInfo(name = "distance")
    @org.jetbrains.annotations.Nullable()
    private final java.lang.Double distance = null;
    @androidx.room.ColumnInfo(name = "rest_time_seconds")
    @org.jetbrains.annotations.Nullable()
    private final java.lang.Integer restTimeSeconds = null;
    @androidx.room.ColumnInfo(name = "rpe")
    @org.jetbrains.annotations.Nullable()
    private final java.lang.Integer rpe = null;
    @androidx.room.ColumnInfo(name = "is_warmup")
    private final boolean isWarmup = false;
    @androidx.room.ColumnInfo(name = "is_dropset")
    private final boolean isDropset = false;
    @androidx.room.ColumnInfo(name = "is_failure")
    private final boolean isFailure = false;
    @androidx.room.ColumnInfo(name = "is_completed")
    private final boolean isCompleted = false;
    @androidx.room.ColumnInfo(name = "notes")
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String notes = null;
    @androidx.room.ColumnInfo(name = "created_at")
    @org.jetbrains.annotations.NotNull()
    private final java.util.Date createdAt = null;
    
    public ExerciseSet(long id, long workoutExerciseId, int setNumber, @org.jetbrains.annotations.Nullable()
    java.lang.Double weight, @org.jetbrains.annotations.Nullable()
    java.lang.Integer reps, @org.jetbrains.annotations.Nullable()
    java.lang.Integer durationSeconds, @org.jetbrains.annotations.Nullable()
    java.lang.Double distance, @org.jetbrains.annotations.Nullable()
    java.lang.Integer restTimeSeconds, @org.jetbrains.annotations.Nullable()
    java.lang.Integer rpe, boolean isWarmup, boolean isDropset, boolean isFailure, boolean isCompleted, @org.jetbrains.annotations.Nullable()
    java.lang.String notes, @org.jetbrains.annotations.NotNull()
    java.util.Date createdAt) {
        super();
    }
    
    public final long getId() {
        return 0L;
    }
    
    public final long getWorkoutExerciseId() {
        return 0L;
    }
    
    public final int getSetNumber() {
        return 0;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Double getWeight() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Integer getReps() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Integer getDurationSeconds() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Double getDistance() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Integer getRestTimeSeconds() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Integer getRpe() {
        return null;
    }
    
    public final boolean isWarmup() {
        return false;
    }
    
    public final boolean isDropset() {
        return false;
    }
    
    public final boolean isFailure() {
        return false;
    }
    
    public final boolean isCompleted() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getNotes() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.Date getCreatedAt() {
        return null;
    }
    
    public final long component1() {
        return 0L;
    }
    
    public final boolean component10() {
        return false;
    }
    
    public final boolean component11() {
        return false;
    }
    
    public final boolean component12() {
        return false;
    }
    
    public final boolean component13() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component14() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.Date component15() {
        return null;
    }
    
    public final long component2() {
        return 0L;
    }
    
    public final int component3() {
        return 0;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Double component4() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Integer component5() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Integer component6() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Double component7() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Integer component8() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Integer component9() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.chilluminati.rackedup.data.database.entity.ExerciseSet copy(long id, long workoutExerciseId, int setNumber, @org.jetbrains.annotations.Nullable()
    java.lang.Double weight, @org.jetbrains.annotations.Nullable()
    java.lang.Integer reps, @org.jetbrains.annotations.Nullable()
    java.lang.Integer durationSeconds, @org.jetbrains.annotations.Nullable()
    java.lang.Double distance, @org.jetbrains.annotations.Nullable()
    java.lang.Integer restTimeSeconds, @org.jetbrains.annotations.Nullable()
    java.lang.Integer rpe, boolean isWarmup, boolean isDropset, boolean isFailure, boolean isCompleted, @org.jetbrains.annotations.Nullable()
    java.lang.String notes, @org.jetbrains.annotations.NotNull()
    java.util.Date createdAt) {
        return null;
    }
    
    @java.lang.Override()
    public boolean equals(@org.jetbrains.annotations.Nullable()
    java.lang.Object other) {
        return false;
    }
    
    @java.lang.Override()
    public int hashCode() {
        return 0;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public java.lang.String toString() {
        return null;
    }
}