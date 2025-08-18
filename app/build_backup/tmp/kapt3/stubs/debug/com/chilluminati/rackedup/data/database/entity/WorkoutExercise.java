package com.chilluminati.rackedup.data.database.entity;

/**
 * WorkoutExercise entity representing an exercise within a workout
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\u0006\n\u0000\n\u0002\u0018\u0002\n\u0002\b)\b\u0087\b\u0018\u00002\u00020\u0001B\u008d\u0001\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\u0007\u0012\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\n\u0012\b\b\u0002\u0010\u000b\u001a\u00020\f\u0012\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\n\u0012\b\b\u0002\u0010\u000e\u001a\u00020\f\u0012\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\u0007\u0012\n\b\u0002\u0010\u0010\u001a\u0004\u0018\u00010\u0007\u0012\n\b\u0002\u0010\u0011\u001a\u0004\u0018\u00010\u0012\u0012\b\b\u0002\u0010\u0013\u001a\u00020\u0014\u00a2\u0006\u0002\u0010\u0015J\t\u0010*\u001a\u00020\u0003H\u00c6\u0003J\u0010\u0010+\u001a\u0004\u0018\u00010\u0007H\u00c6\u0003\u00a2\u0006\u0002\u0010!J\u0010\u0010,\u001a\u0004\u0018\u00010\u0007H\u00c6\u0003\u00a2\u0006\u0002\u0010!J\u0010\u0010-\u001a\u0004\u0018\u00010\u0012H\u00c6\u0003\u00a2\u0006\u0002\u0010\'J\t\u0010.\u001a\u00020\u0014H\u00c6\u0003J\t\u0010/\u001a\u00020\u0003H\u00c6\u0003J\t\u00100\u001a\u00020\u0003H\u00c6\u0003J\t\u00101\u001a\u00020\u0007H\u00c6\u0003J\u0010\u00102\u001a\u0004\u0018\u00010\u0007H\u00c6\u0003\u00a2\u0006\u0002\u0010!J\u000b\u00103\u001a\u0004\u0018\u00010\nH\u00c6\u0003J\t\u00104\u001a\u00020\fH\u00c6\u0003J\u000b\u00105\u001a\u0004\u0018\u00010\nH\u00c6\u0003J\t\u00106\u001a\u00020\fH\u00c6\u0003J\u009c\u0001\u00107\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00072\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\u00072\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\n2\b\b\u0002\u0010\u000b\u001a\u00020\f2\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\n2\b\b\u0002\u0010\u000e\u001a\u00020\f2\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\u00072\n\b\u0002\u0010\u0010\u001a\u0004\u0018\u00010\u00072\n\b\u0002\u0010\u0011\u001a\u0004\u0018\u00010\u00122\b\b\u0002\u0010\u0013\u001a\u00020\u0014H\u00c6\u0001\u00a2\u0006\u0002\u00108J\u0013\u00109\u001a\u00020\f2\b\u0010:\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010;\u001a\u00020\u0007H\u00d6\u0001J\t\u0010<\u001a\u00020\nH\u00d6\u0001R\u0016\u0010\u0013\u001a\u00020\u00148\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0017R\u0016\u0010\u0005\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0019R\u0016\u0010\u0002\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u0019R\u0016\u0010\u000e\u001a\u00020\f8\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u001bR\u0016\u0010\u000b\u001a\u00020\f8\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\u001bR\u0018\u0010\t\u001a\u0004\u0018\u00010\n8\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u001dR\u0016\u0010\u0006\u001a\u00020\u00078\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u001fR\u001a\u0010\b\u001a\u0004\u0018\u00010\u00078\u0006X\u0087\u0004\u00a2\u0006\n\n\u0002\u0010\"\u001a\u0004\b \u0010!R\u0018\u0010\r\u001a\u0004\u0018\u00010\n8\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b#\u0010\u001dR\u001a\u0010\u0010\u001a\u0004\u0018\u00010\u00078\u0006X\u0087\u0004\u00a2\u0006\n\n\u0002\u0010\"\u001a\u0004\b$\u0010!R\u001a\u0010\u000f\u001a\u0004\u0018\u00010\u00078\u0006X\u0087\u0004\u00a2\u0006\n\n\u0002\u0010\"\u001a\u0004\b%\u0010!R\u001a\u0010\u0011\u001a\u0004\u0018\u00010\u00128\u0006X\u0087\u0004\u00a2\u0006\n\n\u0002\u0010(\u001a\u0004\b&\u0010\'R\u0016\u0010\u0004\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b)\u0010\u0019\u00a8\u0006="}, d2 = {"Lcom/chilluminati/rackedup/data/database/entity/WorkoutExercise;", "", "id", "", "workoutId", "exerciseId", "orderIndex", "", "restTimeSeconds", "notes", "", "isSuperset", "", "supersetId", "isDropset", "targetSets", "targetReps", "targetWeight", "", "createdAt", "Ljava/util/Date;", "(JJJILjava/lang/Integer;Ljava/lang/String;ZLjava/lang/String;ZLjava/lang/Integer;Ljava/lang/Integer;Ljava/lang/Double;Ljava/util/Date;)V", "getCreatedAt", "()Ljava/util/Date;", "getExerciseId", "()J", "getId", "()Z", "getNotes", "()Ljava/lang/String;", "getOrderIndex", "()I", "getRestTimeSeconds", "()Ljava/lang/Integer;", "Ljava/lang/Integer;", "getSupersetId", "getTargetReps", "getTargetSets", "getTargetWeight", "()Ljava/lang/Double;", "Ljava/lang/Double;", "getWorkoutId", "component1", "component10", "component11", "component12", "component13", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "(JJJILjava/lang/Integer;Ljava/lang/String;ZLjava/lang/String;ZLjava/lang/Integer;Ljava/lang/Integer;Ljava/lang/Double;Ljava/util/Date;)Lcom/chilluminati/rackedup/data/database/entity/WorkoutExercise;", "equals", "other", "hashCode", "toString", "app_debug"})
@androidx.room.Entity(tableName = "workout_exercises", foreignKeys = {@androidx.room.ForeignKey(entity = com.chilluminati.rackedup.data.database.entity.Workout.class, parentColumns = {"id"}, childColumns = {"workout_id"}, onDelete = 5), @androidx.room.ForeignKey(entity = com.chilluminati.rackedup.data.database.entity.Exercise.class, parentColumns = {"id"}, childColumns = {"exercise_id"}, onDelete = 5)}, indices = {@androidx.room.Index(value = {"workout_id"}), @androidx.room.Index(value = {"exercise_id"})})
public final class WorkoutExercise {
    @androidx.room.PrimaryKey(autoGenerate = true)
    private final long id = 0L;
    @androidx.room.ColumnInfo(name = "workout_id")
    private final long workoutId = 0L;
    @androidx.room.ColumnInfo(name = "exercise_id")
    private final long exerciseId = 0L;
    @androidx.room.ColumnInfo(name = "order_index")
    private final int orderIndex = 0;
    @androidx.room.ColumnInfo(name = "rest_time_seconds")
    @org.jetbrains.annotations.Nullable()
    private final java.lang.Integer restTimeSeconds = null;
    @androidx.room.ColumnInfo(name = "notes")
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String notes = null;
    @androidx.room.ColumnInfo(name = "is_superset")
    private final boolean isSuperset = false;
    @androidx.room.ColumnInfo(name = "superset_id")
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String supersetId = null;
    @androidx.room.ColumnInfo(name = "is_dropset")
    private final boolean isDropset = false;
    @androidx.room.ColumnInfo(name = "target_sets")
    @org.jetbrains.annotations.Nullable()
    private final java.lang.Integer targetSets = null;
    @androidx.room.ColumnInfo(name = "target_reps")
    @org.jetbrains.annotations.Nullable()
    private final java.lang.Integer targetReps = null;
    @androidx.room.ColumnInfo(name = "target_weight")
    @org.jetbrains.annotations.Nullable()
    private final java.lang.Double targetWeight = null;
    @androidx.room.ColumnInfo(name = "created_at")
    @org.jetbrains.annotations.NotNull()
    private final java.util.Date createdAt = null;
    
    public WorkoutExercise(long id, long workoutId, long exerciseId, int orderIndex, @org.jetbrains.annotations.Nullable()
    java.lang.Integer restTimeSeconds, @org.jetbrains.annotations.Nullable()
    java.lang.String notes, boolean isSuperset, @org.jetbrains.annotations.Nullable()
    java.lang.String supersetId, boolean isDropset, @org.jetbrains.annotations.Nullable()
    java.lang.Integer targetSets, @org.jetbrains.annotations.Nullable()
    java.lang.Integer targetReps, @org.jetbrains.annotations.Nullable()
    java.lang.Double targetWeight, @org.jetbrains.annotations.NotNull()
    java.util.Date createdAt) {
        super();
    }
    
    public final long getId() {
        return 0L;
    }
    
    public final long getWorkoutId() {
        return 0L;
    }
    
    public final long getExerciseId() {
        return 0L;
    }
    
    public final int getOrderIndex() {
        return 0;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Integer getRestTimeSeconds() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getNotes() {
        return null;
    }
    
    public final boolean isSuperset() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getSupersetId() {
        return null;
    }
    
    public final boolean isDropset() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Integer getTargetSets() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Integer getTargetReps() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Double getTargetWeight() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.Date getCreatedAt() {
        return null;
    }
    
    public final long component1() {
        return 0L;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Integer component10() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Integer component11() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Double component12() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.Date component13() {
        return null;
    }
    
    public final long component2() {
        return 0L;
    }
    
    public final long component3() {
        return 0L;
    }
    
    public final int component4() {
        return 0;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Integer component5() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component6() {
        return null;
    }
    
    public final boolean component7() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component8() {
        return null;
    }
    
    public final boolean component9() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.chilluminati.rackedup.data.database.entity.WorkoutExercise copy(long id, long workoutId, long exerciseId, int orderIndex, @org.jetbrains.annotations.Nullable()
    java.lang.Integer restTimeSeconds, @org.jetbrains.annotations.Nullable()
    java.lang.String notes, boolean isSuperset, @org.jetbrains.annotations.Nullable()
    java.lang.String supersetId, boolean isDropset, @org.jetbrains.annotations.Nullable()
    java.lang.Integer targetSets, @org.jetbrains.annotations.Nullable()
    java.lang.Integer targetReps, @org.jetbrains.annotations.Nullable()
    java.lang.Double targetWeight, @org.jetbrains.annotations.NotNull()
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