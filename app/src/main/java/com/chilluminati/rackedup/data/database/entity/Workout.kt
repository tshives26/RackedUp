package com.chilluminati.rackedup.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import java.util.Date

/**
 * Workout entity representing a workout session
 */
@Entity(tableName = "workouts")
data class Workout(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    
    @ColumnInfo(name = "name")
    val name: String,
    
    @ColumnInfo(name = "date")
    val date: Date,
    
    @ColumnInfo(name = "start_time")
    val startTime: Date? = null,
    
    @ColumnInfo(name = "end_time")
    val endTime: Date? = null,
    
    @ColumnInfo(name = "duration_minutes")
    val durationMinutes: Int? = null,
    
    @ColumnInfo(name = "notes")
    val notes: String? = null,
    
    @ColumnInfo(name = "total_volume")
    val totalVolume: Double = 0.0, // Total weight × reps × sets
    
    @ColumnInfo(name = "total_sets")
    val totalSets: Int = 0,
    
    @ColumnInfo(name = "total_reps")
    val totalReps: Int = 0,
    
    @ColumnInfo(name = "program_id")
    val programId: Long? = null, // If part of a program
    
    @ColumnInfo(name = "program_day_id")
    val programDayId: Long? = null,
    
    @ColumnInfo(name = "is_template")
    val isTemplate: Boolean = false,
    
    @ColumnInfo(name = "is_completed")
    val isCompleted: Boolean = false,
    
    @ColumnInfo(name = "is_favorite")
    val isFavorite: Boolean = false,
    
    @ColumnInfo(name = "rating")
    val rating: Int? = null, // 1-5 rating
    
    @ColumnInfo(name = "created_at")
    val createdAt: Date = Date(),
    
    @ColumnInfo(name = "updated_at")
    val updatedAt: Date = Date()
)
