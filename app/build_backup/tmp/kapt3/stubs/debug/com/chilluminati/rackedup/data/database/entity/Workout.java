package com.chilluminati.rackedup.data.database.entity;

/**
 * Workout entity representing a workout session
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0006\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b8\b\u0087\b\u0018\u00002\u00020\u0001B\u00c3\u0001\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\u0007\u0012\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\u0007\u0012\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u000b\u0012\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u0005\u0012\b\b\u0002\u0010\r\u001a\u00020\u000e\u0012\b\b\u0002\u0010\u000f\u001a\u00020\u000b\u0012\b\b\u0002\u0010\u0010\u001a\u00020\u000b\u0012\n\b\u0002\u0010\u0011\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0012\u001a\u0004\u0018\u00010\u0003\u0012\b\b\u0002\u0010\u0013\u001a\u00020\u0014\u0012\b\b\u0002\u0010\u0015\u001a\u00020\u0014\u0012\b\b\u0002\u0010\u0016\u001a\u00020\u0014\u0012\n\b\u0002\u0010\u0017\u001a\u0004\u0018\u00010\u000b\u0012\b\b\u0002\u0010\u0018\u001a\u00020\u0007\u0012\b\b\u0002\u0010\u0019\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\u001aJ\t\u00104\u001a\u00020\u0003H\u00c6\u0003J\t\u00105\u001a\u00020\u000bH\u00c6\u0003J\u0010\u00106\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003\u00a2\u0006\u0002\u0010)J\u0010\u00107\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003\u00a2\u0006\u0002\u0010)J\t\u00108\u001a\u00020\u0014H\u00c6\u0003J\t\u00109\u001a\u00020\u0014H\u00c6\u0003J\t\u0010:\u001a\u00020\u0014H\u00c6\u0003J\u0010\u0010;\u001a\u0004\u0018\u00010\u000bH\u00c6\u0003\u00a2\u0006\u0002\u0010\u001fJ\t\u0010<\u001a\u00020\u0007H\u00c6\u0003J\t\u0010=\u001a\u00020\u0007H\u00c6\u0003J\t\u0010>\u001a\u00020\u0005H\u00c6\u0003J\t\u0010?\u001a\u00020\u0007H\u00c6\u0003J\u000b\u0010@\u001a\u0004\u0018\u00010\u0007H\u00c6\u0003J\u000b\u0010A\u001a\u0004\u0018\u00010\u0007H\u00c6\u0003J\u0010\u0010B\u001a\u0004\u0018\u00010\u000bH\u00c6\u0003\u00a2\u0006\u0002\u0010\u001fJ\u000b\u0010C\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J\t\u0010D\u001a\u00020\u000eH\u00c6\u0003J\t\u0010E\u001a\u00020\u000bH\u00c6\u0003J\u00d0\u0001\u0010F\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00072\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\u00072\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\u00072\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u000b2\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u00052\b\b\u0002\u0010\r\u001a\u00020\u000e2\b\b\u0002\u0010\u000f\u001a\u00020\u000b2\b\b\u0002\u0010\u0010\u001a\u00020\u000b2\n\b\u0002\u0010\u0011\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0012\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\u0013\u001a\u00020\u00142\b\b\u0002\u0010\u0015\u001a\u00020\u00142\b\b\u0002\u0010\u0016\u001a\u00020\u00142\n\b\u0002\u0010\u0017\u001a\u0004\u0018\u00010\u000b2\b\b\u0002\u0010\u0018\u001a\u00020\u00072\b\b\u0002\u0010\u0019\u001a\u00020\u0007H\u00c6\u0001\u00a2\u0006\u0002\u0010GJ\u0013\u0010H\u001a\u00020\u00142\b\u0010I\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010J\u001a\u00020\u000bH\u00d6\u0001J\t\u0010K\u001a\u00020\u0005H\u00d6\u0001R\u0016\u0010\u0018\u001a\u00020\u00078\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u001cR\u0016\u0010\u0006\u001a\u00020\u00078\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u001cR\u001a\u0010\n\u001a\u0004\u0018\u00010\u000b8\u0006X\u0087\u0004\u00a2\u0006\n\n\u0002\u0010 \u001a\u0004\b\u001e\u0010\u001fR\u0018\u0010\t\u001a\u0004\u0018\u00010\u00078\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b!\u0010\u001cR\u0016\u0010\u0002\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\"\u0010#R\u0016\u0010\u0015\u001a\u00020\u00148\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010$R\u0016\u0010\u0016\u001a\u00020\u00148\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010$R\u0016\u0010\u0013\u001a\u00020\u00148\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010$R\u0016\u0010\u0004\u001a\u00020\u00058\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b%\u0010&R\u0018\u0010\f\u001a\u0004\u0018\u00010\u00058\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\'\u0010&R\u001a\u0010\u0012\u001a\u0004\u0018\u00010\u00038\u0006X\u0087\u0004\u00a2\u0006\n\n\u0002\u0010*\u001a\u0004\b(\u0010)R\u001a\u0010\u0011\u001a\u0004\u0018\u00010\u00038\u0006X\u0087\u0004\u00a2\u0006\n\n\u0002\u0010*\u001a\u0004\b+\u0010)R\u001a\u0010\u0017\u001a\u0004\u0018\u00010\u000b8\u0006X\u0087\u0004\u00a2\u0006\n\n\u0002\u0010 \u001a\u0004\b,\u0010\u001fR\u0018\u0010\b\u001a\u0004\u0018\u00010\u00078\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b-\u0010\u001cR\u0016\u0010\u0010\u001a\u00020\u000b8\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b.\u0010/R\u0016\u0010\u000f\u001a\u00020\u000b8\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b0\u0010/R\u0016\u0010\r\u001a\u00020\u000e8\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b1\u00102R\u0016\u0010\u0019\u001a\u00020\u00078\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b3\u0010\u001c\u00a8\u0006L"}, d2 = {"Lcom/chilluminati/rackedup/data/database/entity/Workout;", "", "id", "", "name", "", "date", "Ljava/util/Date;", "startTime", "endTime", "durationMinutes", "", "notes", "totalVolume", "", "totalSets", "totalReps", "programId", "programDayId", "isTemplate", "", "isCompleted", "isFavorite", "rating", "createdAt", "updatedAt", "(JLjava/lang/String;Ljava/util/Date;Ljava/util/Date;Ljava/util/Date;Ljava/lang/Integer;Ljava/lang/String;DIILjava/lang/Long;Ljava/lang/Long;ZZZLjava/lang/Integer;Ljava/util/Date;Ljava/util/Date;)V", "getCreatedAt", "()Ljava/util/Date;", "getDate", "getDurationMinutes", "()Ljava/lang/Integer;", "Ljava/lang/Integer;", "getEndTime", "getId", "()J", "()Z", "getName", "()Ljava/lang/String;", "getNotes", "getProgramDayId", "()Ljava/lang/Long;", "Ljava/lang/Long;", "getProgramId", "getRating", "getStartTime", "getTotalReps", "()I", "getTotalSets", "getTotalVolume", "()D", "getUpdatedAt", "component1", "component10", "component11", "component12", "component13", "component14", "component15", "component16", "component17", "component18", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "(JLjava/lang/String;Ljava/util/Date;Ljava/util/Date;Ljava/util/Date;Ljava/lang/Integer;Ljava/lang/String;DIILjava/lang/Long;Ljava/lang/Long;ZZZLjava/lang/Integer;Ljava/util/Date;Ljava/util/Date;)Lcom/chilluminati/rackedup/data/database/entity/Workout;", "equals", "other", "hashCode", "toString", "app_debug"})
@androidx.room.Entity(tableName = "workouts")
public final class Workout {
    @androidx.room.PrimaryKey(autoGenerate = true)
    private final long id = 0L;
    @androidx.room.ColumnInfo(name = "name")
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String name = null;
    @androidx.room.ColumnInfo(name = "date")
    @org.jetbrains.annotations.NotNull()
    private final java.util.Date date = null;
    @androidx.room.ColumnInfo(name = "start_time")
    @org.jetbrains.annotations.Nullable()
    private final java.util.Date startTime = null;
    @androidx.room.ColumnInfo(name = "end_time")
    @org.jetbrains.annotations.Nullable()
    private final java.util.Date endTime = null;
    @androidx.room.ColumnInfo(name = "duration_minutes")
    @org.jetbrains.annotations.Nullable()
    private final java.lang.Integer durationMinutes = null;
    @androidx.room.ColumnInfo(name = "notes")
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String notes = null;
    @androidx.room.ColumnInfo(name = "total_volume")
    private final double totalVolume = 0.0;
    @androidx.room.ColumnInfo(name = "total_sets")
    private final int totalSets = 0;
    @androidx.room.ColumnInfo(name = "total_reps")
    private final int totalReps = 0;
    @androidx.room.ColumnInfo(name = "program_id")
    @org.jetbrains.annotations.Nullable()
    private final java.lang.Long programId = null;
    @androidx.room.ColumnInfo(name = "program_day_id")
    @org.jetbrains.annotations.Nullable()
    private final java.lang.Long programDayId = null;
    @androidx.room.ColumnInfo(name = "is_template")
    private final boolean isTemplate = false;
    @androidx.room.ColumnInfo(name = "is_completed")
    private final boolean isCompleted = false;
    @androidx.room.ColumnInfo(name = "is_favorite")
    private final boolean isFavorite = false;
    @androidx.room.ColumnInfo(name = "rating")
    @org.jetbrains.annotations.Nullable()
    private final java.lang.Integer rating = null;
    @androidx.room.ColumnInfo(name = "created_at")
    @org.jetbrains.annotations.NotNull()
    private final java.util.Date createdAt = null;
    @androidx.room.ColumnInfo(name = "updated_at")
    @org.jetbrains.annotations.NotNull()
    private final java.util.Date updatedAt = null;
    
    public Workout(long id, @org.jetbrains.annotations.NotNull()
    java.lang.String name, @org.jetbrains.annotations.NotNull()
    java.util.Date date, @org.jetbrains.annotations.Nullable()
    java.util.Date startTime, @org.jetbrains.annotations.Nullable()
    java.util.Date endTime, @org.jetbrains.annotations.Nullable()
    java.lang.Integer durationMinutes, @org.jetbrains.annotations.Nullable()
    java.lang.String notes, double totalVolume, int totalSets, int totalReps, @org.jetbrains.annotations.Nullable()
    java.lang.Long programId, @org.jetbrains.annotations.Nullable()
    java.lang.Long programDayId, boolean isTemplate, boolean isCompleted, boolean isFavorite, @org.jetbrains.annotations.Nullable()
    java.lang.Integer rating, @org.jetbrains.annotations.NotNull()
    java.util.Date createdAt, @org.jetbrains.annotations.NotNull()
    java.util.Date updatedAt) {
        super();
    }
    
    public final long getId() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getName() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.Date getDate() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.util.Date getStartTime() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.util.Date getEndTime() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Integer getDurationMinutes() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getNotes() {
        return null;
    }
    
    public final double getTotalVolume() {
        return 0.0;
    }
    
    public final int getTotalSets() {
        return 0;
    }
    
    public final int getTotalReps() {
        return 0;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Long getProgramId() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Long getProgramDayId() {
        return null;
    }
    
    public final boolean isTemplate() {
        return false;
    }
    
    public final boolean isCompleted() {
        return false;
    }
    
    public final boolean isFavorite() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Integer getRating() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.Date getCreatedAt() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.Date getUpdatedAt() {
        return null;
    }
    
    public final long component1() {
        return 0L;
    }
    
    public final int component10() {
        return 0;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Long component11() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Long component12() {
        return null;
    }
    
    public final boolean component13() {
        return false;
    }
    
    public final boolean component14() {
        return false;
    }
    
    public final boolean component15() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Integer component16() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.Date component17() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.Date component18() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component2() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.Date component3() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.util.Date component4() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.util.Date component5() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Integer component6() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component7() {
        return null;
    }
    
    public final double component8() {
        return 0.0;
    }
    
    public final int component9() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.chilluminati.rackedup.data.database.entity.Workout copy(long id, @org.jetbrains.annotations.NotNull()
    java.lang.String name, @org.jetbrains.annotations.NotNull()
    java.util.Date date, @org.jetbrains.annotations.Nullable()
    java.util.Date startTime, @org.jetbrains.annotations.Nullable()
    java.util.Date endTime, @org.jetbrains.annotations.Nullable()
    java.lang.Integer durationMinutes, @org.jetbrains.annotations.Nullable()
    java.lang.String notes, double totalVolume, int totalSets, int totalReps, @org.jetbrains.annotations.Nullable()
    java.lang.Long programId, @org.jetbrains.annotations.Nullable()
    java.lang.Long programDayId, boolean isTemplate, boolean isCompleted, boolean isFavorite, @org.jetbrains.annotations.Nullable()
    java.lang.Integer rating, @org.jetbrains.annotations.NotNull()
    java.util.Date createdAt, @org.jetbrains.annotations.NotNull()
    java.util.Date updatedAt) {
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