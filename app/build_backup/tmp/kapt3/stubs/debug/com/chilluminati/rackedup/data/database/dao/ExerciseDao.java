package com.chilluminati.rackedup.data.database.dao;

/**
 * DAO for Exercise operations
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u000f\n\u0002\u0010\u000b\n\u0002\b\u0002\bg\u0018\u00002\u00020\u0001J\u0016\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u0016\u0010\u0007\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\tH\u00a7@\u00a2\u0006\u0002\u0010\nJ\u0014\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\r0\fH\u00a7@\u00a2\u0006\u0002\u0010\u000eJ\u0014\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\r0\fH\u00a7@\u00a2\u0006\u0002\u0010\u000eJ\u0014\u0010\u0010\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\f0\u0011H\'J\u0014\u0010\u0012\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\f0\u0011H\'J\u0018\u0010\u0013\u001a\u0004\u0018\u00010\u00052\u0006\u0010\b\u001a\u00020\tH\u00a7@\u00a2\u0006\u0002\u0010\nJ\u0018\u0010\u0014\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00050\u00112\u0006\u0010\b\u001a\u00020\tH\'J\u001c\u0010\u0015\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\f0\u00112\u0006\u0010\u0016\u001a\u00020\rH\'J\u0014\u0010\u0017\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\f0\u0011H\'J\u0016\u0010\u0018\u001a\u00020\t2\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u001c\u0010\u0019\u001a\u00020\u00032\f\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u00050\fH\u00a7@\u00a2\u0006\u0002\u0010\u001bJ\u001c\u0010\u001c\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\f0\u00112\u0006\u0010\u001d\u001a\u00020\rH\'J\u0016\u0010\u001e\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u001e\u0010\u001f\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\t2\u0006\u0010 \u001a\u00020!H\u00a7@\u00a2\u0006\u0002\u0010\"\u00a8\u0006#"}, d2 = {"Lcom/chilluminati/rackedup/data/database/dao/ExerciseDao;", "", "deleteExercise", "", "exercise", "Lcom/chilluminati/rackedup/data/database/entity/Exercise;", "(Lcom/chilluminati/rackedup/data/database/entity/Exercise;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteExerciseById", "exerciseId", "", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAllCategories", "", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAllEquipment", "getAllExercises", "Lkotlinx/coroutines/flow/Flow;", "getCustomExercises", "getExerciseById", "getExerciseByIdFlow", "getExercisesByCategory", "category", "getFavoriteExercises", "insertExercise", "insertExercises", "exercises", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "searchExercises", "searchQuery", "updateExercise", "updateFavoriteStatus", "isFavorite", "", "(JZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
@androidx.room.Dao()
public abstract interface ExerciseDao {
    
    @androidx.room.Query(value = "SELECT * FROM exercises ORDER BY name ASC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.chilluminati.rackedup.data.database.entity.Exercise>> getAllExercises();
    
    @androidx.room.Query(value = "SELECT * FROM exercises WHERE category = :category ORDER BY name ASC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.chilluminati.rackedup.data.database.entity.Exercise>> getExercisesByCategory(@org.jetbrains.annotations.NotNull()
    java.lang.String category);
    
    @androidx.room.Query(value = "SELECT * FROM exercises WHERE is_favorite = 1 ORDER BY name ASC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.chilluminati.rackedup.data.database.entity.Exercise>> getFavoriteExercises();
    
    @androidx.room.Query(value = "SELECT * FROM exercises WHERE is_custom = 1 ORDER BY name ASC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.chilluminati.rackedup.data.database.entity.Exercise>> getCustomExercises();
    
    @androidx.room.Query(value = "SELECT * FROM exercises WHERE name LIKE \'%\' || :searchQuery || \'%\' ORDER BY name ASC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.chilluminati.rackedup.data.database.entity.Exercise>> searchExercises(@org.jetbrains.annotations.NotNull()
    java.lang.String searchQuery);
    
    @androidx.room.Query(value = "SELECT * FROM exercises WHERE id = :exerciseId")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getExerciseById(long exerciseId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.chilluminati.rackedup.data.database.entity.Exercise> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM exercises WHERE id = :exerciseId")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<com.chilluminati.rackedup.data.database.entity.Exercise> getExerciseByIdFlow(long exerciseId);
    
    @androidx.room.Query(value = "SELECT DISTINCT category FROM exercises ORDER BY category ASC")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getAllCategories(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<java.lang.String>> $completion);
    
    @androidx.room.Query(value = "SELECT DISTINCT equipment FROM exercises ORDER BY equipment ASC")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getAllEquipment(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<java.lang.String>> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertExercise(@org.jetbrains.annotations.NotNull()
    com.chilluminati.rackedup.data.database.entity.Exercise exercise, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertExercises(@org.jetbrains.annotations.NotNull()
    java.util.List<com.chilluminati.rackedup.data.database.entity.Exercise> exercises, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Update()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateExercise(@org.jetbrains.annotations.NotNull()
    com.chilluminati.rackedup.data.database.entity.Exercise exercise, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Delete()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteExercise(@org.jetbrains.annotations.NotNull()
    com.chilluminati.rackedup.data.database.entity.Exercise exercise, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM exercises WHERE id = :exerciseId")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteExerciseById(long exerciseId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "UPDATE exercises SET is_favorite = :isFavorite WHERE id = :exerciseId")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateFavoriteStatus(long exerciseId, boolean isFavorite, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
}