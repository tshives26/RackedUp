package com.chilluminati.rackedup.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import androidx.room.ForeignKey
import androidx.room.Index
import java.util.Date

/**
 * PersonalRecord entity for tracking personal records
 */
@Entity(
    tableName = "personal_records",
    foreignKeys = [
        ForeignKey(
            entity = Exercise::class,
            parentColumns = ["id"],
            childColumns = ["exercise_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["exercise_id"])
    ]
)
data class PersonalRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    
    @ColumnInfo(name = "exercise_id")
    val exerciseId: Long,
    
    @ColumnInfo(name = "record_type")
    val recordType: String, // 1RM, Volume, Distance, Duration, etc.
    
    @ColumnInfo(name = "weight")
    val weight: Double? = null,
    
    @ColumnInfo(name = "reps")
    val reps: Int? = null,
    
    @ColumnInfo(name = "distance")
    val distance: Double? = null,
    
    @ColumnInfo(name = "duration_seconds")
    val durationSeconds: Int? = null,
    
    @ColumnInfo(name = "volume")
    val volume: Double? = null, // weight × reps × sets
    
    @ColumnInfo(name = "estimated_1rm")
    val estimated1RM: Double? = null,
    
    @ColumnInfo(name = "previous_value")
    val previousValue: Double? = null, // For comparison
    
    @ColumnInfo(name = "improvement")
    val improvement: Double? = null, // Percentage improvement
    
    @ColumnInfo(name = "workout_id")
    val workoutId: Long? = null, // Which workout this PR was achieved in
    
    @ColumnInfo(name = "notes")
    val notes: String? = null,
    
    @ColumnInfo(name = "achieved_at")
    val achievedAt: Date = Date(),
    
    @ColumnInfo(name = "created_at")
    val createdAt: Date = Date()
)
