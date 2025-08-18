package com.chilluminati.rackedup.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import androidx.room.ForeignKey
import androidx.room.Index
import java.util.Date

/**
 * ExerciseSet entity representing a single set of an exercise
 */
@Entity(
    tableName = "exercise_sets",
    foreignKeys = [
        ForeignKey(
            entity = WorkoutExercise::class,
            parentColumns = ["id"],
            childColumns = ["workout_exercise_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["workout_exercise_id"])
    ]
)
data class ExerciseSet(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    
    @ColumnInfo(name = "workout_exercise_id")
    val workoutExerciseId: Long,
    
    @ColumnInfo(name = "set_number")
    val setNumber: Int,
    
    @ColumnInfo(name = "weight")
    val weight: Double? = null, // In kg or lbs based on user preference
    
    @ColumnInfo(name = "reps")
    val reps: Int? = null,
    
    @ColumnInfo(name = "duration_seconds")
    val durationSeconds: Int? = null, // For time-based exercises
    
    @ColumnInfo(name = "distance")
    val distance: Double? = null, // For cardio exercises (km/miles)
    
    @ColumnInfo(name = "rest_time_seconds")
    val restTimeSeconds: Int? = null,
    
    @ColumnInfo(name = "rpe")
    val rpe: Int? = null, // Rate of Perceived Exertion (1-10)
    
    @ColumnInfo(name = "is_warmup")
    val isWarmup: Boolean = false,
    
    @ColumnInfo(name = "is_dropset")
    val isDropset: Boolean = false,
    
    @ColumnInfo(name = "is_failure")
    val isFailure: Boolean = false,
    
    @ColumnInfo(name = "is_completed")
    val isCompleted: Boolean = false,
    
    @ColumnInfo(name = "notes")
    val notes: String? = null,
    
    @ColumnInfo(name = "created_at")
    val createdAt: Date = Date()
)
