package com.chilluminati.rackedup.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import java.util.Date

/**
 * BodyMeasurement entity for tracking body composition and measurements
 */
@Entity(tableName = "body_measurements")
data class BodyMeasurement(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    
    @ColumnInfo(name = "measurement_type")
    val measurementType: String, // Weight, Body Fat, Muscle Mass, Waist, Chest, Arms, etc.
    
    @ColumnInfo(name = "value")
    val value: Double,
    
    @ColumnInfo(name = "unit")
    val unit: String, // kg, lbs, cm, inches, %
    
    @ColumnInfo(name = "body_part")
    val bodyPart: String? = null, // For circumference measurements
    
    @ColumnInfo(name = "measurement_method")
    val measurementMethod: String? = null, // Scale, Calipers, DEXA, Tape Measure
    
    @ColumnInfo(name = "notes")
    val notes: String? = null,
    
    @ColumnInfo(name = "photo_url")
    val photoUrl: String? = null, // Progress photo for this measurement
    
    @ColumnInfo(name = "measured_at")
    val measuredAt: Date = Date(),
    
    @ColumnInfo(name = "created_at")
    val createdAt: Date = Date()
)
