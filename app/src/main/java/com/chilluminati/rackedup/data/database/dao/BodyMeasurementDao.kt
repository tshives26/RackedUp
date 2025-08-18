package com.chilluminati.rackedup.data.database.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.chilluminati.rackedup.data.database.entity.BodyMeasurement

@Dao
interface BodyMeasurementDao {
    @Query("SELECT * FROM body_measurements WHERE measurement_type = :type ORDER BY measured_at DESC")
    fun getBodyMeasurements(type: String): Flow<List<BodyMeasurement>>

    @Query("SELECT * FROM body_measurements")
    suspend fun getAllBodyMeasurements(): List<BodyMeasurement>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBodyMeasurement(bodyMeasurement: BodyMeasurement): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBodyMeasurements(bodyMeasurements: List<BodyMeasurement>)

    @Update
    suspend fun updateBodyMeasurement(bodyMeasurement: BodyMeasurement)

    @Delete
    suspend fun deleteBodyMeasurement(bodyMeasurement: BodyMeasurement)
}


