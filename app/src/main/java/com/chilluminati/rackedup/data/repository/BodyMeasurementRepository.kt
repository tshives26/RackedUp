package com.chilluminati.rackedup.data.repository

import com.chilluminati.rackedup.data.database.dao.BodyMeasurementDao
import com.chilluminati.rackedup.data.database.entity.BodyMeasurement
import com.chilluminati.rackedup.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository for body measurement data operations
 */
@Singleton
class BodyMeasurementRepository @Inject constructor(
    private val bodyMeasurementDao: BodyMeasurementDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {
    
    /**
     * Get all body measurements
     */
    suspend fun getAllBodyMeasurements(): List<BodyMeasurement> {
        return withContext(ioDispatcher) {
            bodyMeasurementDao.getAllBodyMeasurements()
        }
    }
    
    /**
     * Get body measurements by type
     */
    fun getBodyMeasurementsByType(type: String): Flow<List<BodyMeasurement>> {
        return bodyMeasurementDao.getBodyMeasurements(type)
    }
    
    /**
     * Get weight measurements
     */
    fun getWeightMeasurements(): Flow<List<BodyMeasurement>> {
        return getBodyMeasurementsByType("Weight")
    }
    
    /**
     * Get body fat measurements
     */
    fun getBodyFatMeasurements(): Flow<List<BodyMeasurement>> {
        return getBodyMeasurementsByType("Body Fat %")
    }
    
    /**
     * Get muscle mass measurements
     */
    fun getMuscleMassMeasurements(): Flow<List<BodyMeasurement>> {
        return getBodyMeasurementsByType("Muscle Mass")
    }
    
    /**
     * Get circumference measurements
     */
    fun getCircumferenceMeasurements(): Flow<List<BodyMeasurement>> {
        return getBodyMeasurementsByType("Circumference")
    }
    
    /**
     * Add a new body measurement
     */
    suspend fun addBodyMeasurement(
        measurementType: String,
        value: Double,
        unit: String,
        bodyPart: String? = null,
        measurementMethod: String? = null,
        notes: String? = null,
        photoUrl: String? = null,
        measuredAt: Date = Date()
    ): Long {
        return withContext(ioDispatcher) {
            val measurement = BodyMeasurement(
                measurementType = measurementType,
                value = value,
                unit = unit,
                bodyPart = bodyPart,
                measurementMethod = measurementMethod,
                notes = notes,
                photoUrl = photoUrl,
                measuredAt = measuredAt
            )
            bodyMeasurementDao.insertBodyMeasurement(measurement)
        }
    }
    
    /**
     * Update an existing body measurement
     */
    suspend fun updateBodyMeasurement(measurement: BodyMeasurement) {
        withContext(ioDispatcher) {
            bodyMeasurementDao.updateBodyMeasurement(measurement)
        }
    }
    
    /**
     * Delete a body measurement
     */
    suspend fun deleteBodyMeasurement(measurement: BodyMeasurement) {
        withContext(ioDispatcher) {
            bodyMeasurementDao.deleteBodyMeasurement(measurement)
        }
    }
    
    /**
     * Get measurement types available in the database
     */
    suspend fun getMeasurementTypes(): List<String> {
        return withContext(ioDispatcher) {
            val allMeasurements = bodyMeasurementDao.getAllBodyMeasurements()
            allMeasurements.map { it.measurementType }.distinct().sorted()
        }
    }
    
    /**
     * Get body parts available in the database
     */
    suspend fun getBodyParts(): List<String> {
        return withContext(ioDispatcher) {
            val allMeasurements = bodyMeasurementDao.getAllBodyMeasurements()
            allMeasurements.mapNotNull { it.bodyPart }.distinct().sorted()
        }
    }
    
    /**
     * Get recent measurements (last 30 days)
     */
    suspend fun getRecentMeasurements(days: Int = 30): List<BodyMeasurement> {
        return withContext(ioDispatcher) {
            val cutoffDate = Date(System.currentTimeMillis() - (days * 24 * 60 * 60 * 1000L))
            val allMeasurements = bodyMeasurementDao.getAllBodyMeasurements()
            allMeasurements.filter { it.measuredAt.after(cutoffDate) }
                .sortedByDescending { it.measuredAt }
        }
    }
    
    /**
     * Get measurements for a specific date range
     */
    suspend fun getMeasurementsInRange(startDate: Date, endDate: Date): List<BodyMeasurement> {
        return withContext(ioDispatcher) {
            val allMeasurements = bodyMeasurementDao.getAllBodyMeasurements()
            allMeasurements.filter { 
                it.measuredAt.after(startDate) && it.measuredAt.before(endDate) 
            }.sortedBy { it.measuredAt }
        }
    }
}
