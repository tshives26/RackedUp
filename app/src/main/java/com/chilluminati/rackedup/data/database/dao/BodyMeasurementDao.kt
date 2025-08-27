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

    // Achievement-related queries
    @Query("SELECT COUNT(*) FROM body_measurements")
    suspend fun getBodyMeasurementCount(): Int

    @Query("SELECT measured_at FROM body_measurements ORDER BY measured_at ASC LIMIT 1")
    suspend fun getFirstMeasurementDate(): java.util.Date?

    @Query("SELECT measured_at FROM body_measurements ORDER BY measured_at ASC LIMIT 1 OFFSET :n-1")
    suspend fun getNthMeasurementDate(n: Int): java.util.Date?
}


