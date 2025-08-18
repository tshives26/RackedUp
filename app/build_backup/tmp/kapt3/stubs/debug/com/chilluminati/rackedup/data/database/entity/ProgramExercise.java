package com.chilluminati.rackedup.data.database.entity;

/**
 * ProgramExercise entity representing an exercise within a program day
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0006\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b.\b\u0087\b\u0018\u00002\u00020\u0001B\u00a3\u0001\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\t\u0012\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\f\u0012\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u0007\u0012\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\u0007\u0012\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\t\u0012\n\b\u0002\u0010\u0010\u001a\u0004\u0018\u00010\f\u0012\b\b\u0002\u0010\u0011\u001a\u00020\u0012\u0012\n\b\u0002\u0010\u0013\u001a\u0004\u0018\u00010\t\u0012\n\b\u0002\u0010\u0014\u001a\u0004\u0018\u00010\t\u0012\b\b\u0002\u0010\u0015\u001a\u00020\u0016\u00a2\u0006\u0002\u0010\u0017J\t\u0010/\u001a\u00020\u0003H\u00c6\u0003J\u000b\u00100\u001a\u0004\u0018\u00010\tH\u00c6\u0003J\u0010\u00101\u001a\u0004\u0018\u00010\fH\u00c6\u0003\u00a2\u0006\u0002\u0010$J\t\u00102\u001a\u00020\u0012H\u00c6\u0003J\u000b\u00103\u001a\u0004\u0018\u00010\tH\u00c6\u0003J\u000b\u00104\u001a\u0004\u0018\u00010\tH\u00c6\u0003J\t\u00105\u001a\u00020\u0016H\u00c6\u0003J\t\u00106\u001a\u00020\u0003H\u00c6\u0003J\t\u00107\u001a\u00020\u0003H\u00c6\u0003J\t\u00108\u001a\u00020\u0007H\u00c6\u0003J\t\u00109\u001a\u00020\tH\u00c6\u0003J\u000b\u0010:\u001a\u0004\u0018\u00010\tH\u00c6\u0003J\u0010\u0010;\u001a\u0004\u0018\u00010\fH\u00c6\u0003\u00a2\u0006\u0002\u0010$J\u0010\u0010<\u001a\u0004\u0018\u00010\u0007H\u00c6\u0003\u00a2\u0006\u0002\u0010)J\u0010\u0010=\u001a\u0004\u0018\u00010\u0007H\u00c6\u0003\u00a2\u0006\u0002\u0010)J\u00b4\u0001\u0010>\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\t2\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\t2\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\f2\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u00072\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\u00072\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\t2\n\b\u0002\u0010\u0010\u001a\u0004\u0018\u00010\f2\b\b\u0002\u0010\u0011\u001a\u00020\u00122\n\b\u0002\u0010\u0013\u001a\u0004\u0018\u00010\t2\n\b\u0002\u0010\u0014\u001a\u0004\u0018\u00010\t2\b\b\u0002\u0010\u0015\u001a\u00020\u0016H\u00c6\u0001\u00a2\u0006\u0002\u0010?J\u0013\u0010@\u001a\u00020\u00122\b\u0010A\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010B\u001a\u00020\u0007H\u00d6\u0001J\t\u0010C\u001a\u00020\tH\u00d6\u0001R\u0016\u0010\u0015\u001a\u00020\u00168\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0019R\u0016\u0010\u0005\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u001bR\u0016\u0010\u0002\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u001bR\u0016\u0010\u0011\u001a\u00020\u00128\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u001dR\u0018\u0010\u0014\u001a\u0004\u0018\u00010\t8\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u001fR\u0016\u0010\u0006\u001a\u00020\u00078\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b \u0010!R\u0016\u0010\u0004\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\"\u0010\u001bR\u001a\u0010\u0010\u001a\u0004\u0018\u00010\f8\u0006X\u0087\u0004\u00a2\u0006\n\n\u0002\u0010%\u001a\u0004\b#\u0010$R\u0018\u0010\u000f\u001a\u0004\u0018\u00010\t8\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b&\u0010\u001fR\u0018\u0010\n\u001a\u0004\u0018\u00010\t8\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\'\u0010\u001fR\u001a\u0010\r\u001a\u0004\u0018\u00010\u00078\u0006X\u0087\u0004\u00a2\u0006\n\n\u0002\u0010*\u001a\u0004\b(\u0010)R\u001a\u0010\u000e\u001a\u0004\u0018\u00010\u00078\u0006X\u0087\u0004\u00a2\u0006\n\n\u0002\u0010*\u001a\u0004\b+\u0010)R\u0016\u0010\b\u001a\u00020\t8\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b,\u0010\u001fR\u0018\u0010\u0013\u001a\u0004\u0018\u00010\t8\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b-\u0010\u001fR\u001a\u0010\u000b\u001a\u0004\u0018\u00010\f8\u0006X\u0087\u0004\u00a2\u0006\n\n\u0002\u0010%\u001a\u0004\b.\u0010$\u00a8\u0006D"}, d2 = {"Lcom/chilluminati/rackedup/data/database/entity/ProgramExercise;", "", "id", "", "programDayId", "exerciseId", "orderIndex", "", "sets", "", "reps", "weightPercentage", "", "restTimeSeconds", "rpeTarget", "progressionScheme", "progressionIncrement", "isSuperset", "", "supersetId", "notes", "createdAt", "Ljava/util/Date;", "(JJJILjava/lang/String;Ljava/lang/String;Ljava/lang/Double;Ljava/lang/Integer;Ljava/lang/Integer;Ljava/lang/String;Ljava/lang/Double;ZLjava/lang/String;Ljava/lang/String;Ljava/util/Date;)V", "getCreatedAt", "()Ljava/util/Date;", "getExerciseId", "()J", "getId", "()Z", "getNotes", "()Ljava/lang/String;", "getOrderIndex", "()I", "getProgramDayId", "getProgressionIncrement", "()Ljava/lang/Double;", "Ljava/lang/Double;", "getProgressionScheme", "getReps", "getRestTimeSeconds", "()Ljava/lang/Integer;", "Ljava/lang/Integer;", "getRpeTarget", "getSets", "getSupersetId", "getWeightPercentage", "component1", "component10", "component11", "component12", "component13", "component14", "component15", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "(JJJILjava/lang/String;Ljava/lang/String;Ljava/lang/Double;Ljava/lang/Integer;Ljava/lang/Integer;Ljava/lang/String;Ljava/lang/Double;ZLjava/lang/String;Ljava/lang/String;Ljava/util/Date;)Lcom/chilluminati/rackedup/data/database/entity/ProgramExercise;", "equals", "other", "hashCode", "toString", "app_debug"})
@androidx.room.Entity(tableName = "program_exercises", foreignKeys = {@androidx.room.ForeignKey(entity = com.chilluminati.rackedup.data.database.entity.ProgramDay.class, parentColumns = {"id"}, childColumns = {"program_day_id"}, onDelete = 5), @androidx.room.ForeignKey(entity = com.chilluminati.rackedup.data.database.entity.Exercise.class, parentColumns = {"id"}, childColumns = {"exercise_id"}, onDelete = 5)}, indices = {@androidx.room.Index(value = {"program_day_id"}), @androidx.room.Index(value = {"exercise_id"})})
public final class ProgramExercise {
    @androidx.room.PrimaryKey(autoGenerate = true)
    private final long id = 0L;
    @androidx.room.ColumnInfo(name = "program_day_id")
    private final long programDayId = 0L;
    @androidx.room.ColumnInfo(name = "exercise_id")
    private final long exerciseId = 0L;
    @androidx.room.ColumnInfo(name = "order_index")
    private final int orderIndex = 0;
    @androidx.room.ColumnInfo(name = "sets")
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String sets = null;
    @androidx.room.ColumnInfo(name = "reps")
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String reps = null;
    @androidx.room.ColumnInfo(name = "weight_percentage")
    @org.jetbrains.annotations.Nullable()
    private final java.lang.Double weightPercentage = null;
    @androidx.room.ColumnInfo(name = "rest_time_seconds")
    @org.jetbrains.annotations.Nullable()
    private final java.lang.Integer restTimeSeconds = null;
    @androidx.room.ColumnInfo(name = "rpe_target")
    @org.jetbrains.annotations.Nullable()
    private final java.lang.Integer rpeTarget = null;
    @androidx.room.ColumnInfo(name = "progression_scheme")
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String progressionScheme = null;
    @androidx.room.ColumnInfo(name = "progression_increment")
    @org.jetbrains.annotations.Nullable()
    private final java.lang.Double progressionIncrement = null;
    @androidx.room.ColumnInfo(name = "is_superset")
    private final boolean isSuperset = false;
    @androidx.room.ColumnInfo(name = "superset_id")
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String supersetId = null;
    @androidx.room.ColumnInfo(name = "notes")
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String notes = null;
    @androidx.room.ColumnInfo(name = "created_at")
    @org.jetbrains.annotations.NotNull()
    private final java.util.Date createdAt = null;
    
    public ProgramExercise(long id, long programDayId, long exerciseId, int orderIndex, @org.jetbrains.annotations.NotNull()
    java.lang.String sets, @org.jetbrains.annotations.Nullable()
    java.lang.String reps, @org.jetbrains.annotations.Nullable()
    java.lang.Double weightPercentage, @org.jetbrains.annotations.Nullable()
    java.lang.Integer restTimeSeconds, @org.jetbrains.annotations.Nullable()
    java.lang.Integer rpeTarget, @org.jetbrains.annotations.Nullable()
    java.lang.String progressionScheme, @org.jetbrains.annotations.Nullable()
    java.lang.Double progressionIncrement, boolean isSuperset, @org.jetbrains.annotations.Nullable()
    java.lang.String supersetId, @org.jetbrains.annotations.Nullable()
    java.lang.String notes, @org.jetbrains.annotations.NotNull()
    java.util.Date createdAt) {
        super();
    }
    
    public final long getId() {
        return 0L;
    }
    
    public final long getProgramDayId() {
        return 0L;
    }
    
    public final long getExerciseId() {
        return 0L;
    }
    
    public final int getOrderIndex() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getSets() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getReps() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Double getWeightPercentage() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Integer getRestTimeSeconds() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Integer getRpeTarget() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getProgressionScheme() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Double getProgressionIncrement() {
        return null;
    }
    
    public final boolean isSuperset() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getSupersetId() {
        return null;
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
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component10() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Double component11() {
        return null;
    }
    
    public final boolean component12() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component13() {
        return null;
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
    
    public final long component3() {
        return 0L;
    }
    
    public final int component4() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component5() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component6() {
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
    public final com.chilluminati.rackedup.data.database.entity.ProgramExercise copy(long id, long programDayId, long exerciseId, int orderIndex, @org.jetbrains.annotations.NotNull()
    java.lang.String sets, @org.jetbrains.annotations.Nullable()
    java.lang.String reps, @org.jetbrains.annotations.Nullable()
    java.lang.Double weightPercentage, @org.jetbrains.annotations.Nullable()
    java.lang.Integer restTimeSeconds, @org.jetbrains.annotations.Nullable()
    java.lang.Integer rpeTarget, @org.jetbrains.annotations.Nullable()
    java.lang.String progressionScheme, @org.jetbrains.annotations.Nullable()
    java.lang.Double progressionIncrement, boolean isSuperset, @org.jetbrains.annotations.Nullable()
    java.lang.String supersetId, @org.jetbrains.annotations.Nullable()
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