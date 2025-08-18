package com.chilluminati.rackedup.data.database.dao;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0004\bg\u0018\u00002\u00020\u0001J\u0016\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u0014\u0010\u0007\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\t0\bH\'J\u0018\u0010\n\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u000b\u001a\u00020\fH\u00a7@\u00a2\u0006\u0002\u0010\rJ\u0016\u0010\u000e\u001a\u00020\f2\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u0016\u0010\u000f\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006\u00a8\u0006\u0010"}, d2 = {"Lcom/chilluminati/rackedup/data/database/dao/WorkoutDao;", "", "deleteWorkout", "", "workout", "Lcom/chilluminati/rackedup/data/database/entity/Workout;", "(Lcom/chilluminati/rackedup/data/database/entity/Workout;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAllWorkouts", "Lkotlinx/coroutines/flow/Flow;", "", "getWorkoutById", "workoutId", "", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insertWorkout", "updateWorkout", "app_debug"})
@androidx.room.Dao()
public abstract interface WorkoutDao {
    
    @androidx.room.Query(value = "SELECT * FROM workouts ORDER BY date DESC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.chilluminati.rackedup.data.database.entity.Workout>> getAllWorkouts();
    
    @androidx.room.Query(value = "SELECT * FROM workouts WHERE id = :workoutId")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getWorkoutById(long workoutId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.chilluminati.rackedup.data.database.entity.Workout> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertWorkout(@org.jetbrains.annotations.NotNull()
    com.chilluminati.rackedup.data.database.entity.Workout workout, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion);
    
    @androidx.room.Update()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateWorkout(@org.jetbrains.annotations.NotNull()
    com.chilluminati.rackedup.data.database.entity.Workout workout, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Delete()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteWorkout(@org.jetbrains.annotations.NotNull()
    com.chilluminati.rackedup.data.database.entity.Workout workout, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
}