package com.chilluminati.rackedup.data.database.dao;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\bg\u0018\u00002\u00020\u0001J\u0016\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u001c\u0010\u0007\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\t0\b2\u0006\u0010\n\u001a\u00020\u000bH\'J\u0016\u0010\f\u001a\u00020\r2\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u0016\u0010\u000e\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006\u00a8\u0006\u000f"}, d2 = {"Lcom/chilluminati/rackedup/data/database/dao/BodyMeasurementDao;", "", "deleteBodyMeasurement", "", "bodyMeasurement", "Lcom/chilluminati/rackedup/data/database/entity/BodyMeasurement;", "(Lcom/chilluminati/rackedup/data/database/entity/BodyMeasurement;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getBodyMeasurements", "Lkotlinx/coroutines/flow/Flow;", "", "type", "", "insertBodyMeasurement", "", "updateBodyMeasurement", "app_debug"})
@androidx.room.Dao()
public abstract interface BodyMeasurementDao {
    
    @androidx.room.Query(value = "SELECT * FROM body_measurements WHERE measurement_type = :type ORDER BY measured_at DESC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.chilluminati.rackedup.data.database.entity.BodyMeasurement>> getBodyMeasurements(@org.jetbrains.annotations.NotNull()
    java.lang.String type);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertBodyMeasurement(@org.jetbrains.annotations.NotNull()
    com.chilluminati.rackedup.data.database.entity.BodyMeasurement bodyMeasurement, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion);
    
    @androidx.room.Update()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateBodyMeasurement(@org.jetbrains.annotations.NotNull()
    com.chilluminati.rackedup.data.database.entity.BodyMeasurement bodyMeasurement, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Delete()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteBodyMeasurement(@org.jetbrains.annotations.NotNull()
    com.chilluminati.rackedup.data.database.entity.BodyMeasurement bodyMeasurement, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
}