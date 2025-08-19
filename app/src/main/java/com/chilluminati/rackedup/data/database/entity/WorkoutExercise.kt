package com.chilluminati.rackedup.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import androidx.room.ForeignKey
import androidx.room.Index
import java.util.Date

/**
 * WorkoutExercise entity representing an exercise within a workout
 */
@Entity(
    tableName = "workout_exercises",
    foreignKeys = [
        ForeignKey(
            entity = Workout::class,
            parentColumns = ["id"],
            childColumns = ["workout_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Exercise::class,
            parentColumns = ["id"],
            childColumns = ["exercise_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["workout_id"]),
        Index(value = ["exercise_id"])
    ]
)
data class WorkoutExercise(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    
    @ColumnInfo(name = "workout_id")
    val workoutId: Long,
    
    @ColumnInfo(name = "exercise_id")
    val exerciseId: Long,
    
    @ColumnInfo(name = "order_index")
    val orderIndex: Int, // Order within the workout
    
    @ColumnInfo(name = "rest_time_seconds")
    val restTimeSeconds: Int? = null,
    
    @ColumnInfo(name = "notes")
    val notes: String? = null,
    
    @ColumnInfo(name = "is_superset")
    val isSuperset: Boolean = false,
    
    @ColumnInfo(name = "superset_id")
    val supersetId: String? = null, // Group exercises in supersets
    
    @ColumnInfo(name = "is_dropset")
    val isDropset: Boolean = false,
    
    @ColumnInfo(name = "target_sets")
    val targetSets: Int? = null,
    
    @ColumnInfo(name = "target_reps")
    val targetReps: Int? = null,
    
    @ColumnInfo(name = "rep_scheme")
    val repScheme: String? = null, // "AMRAP", "Till Failure", "8-12", etc.
    
    @ColumnInfo(name = "target_weight")
    val targetWeight: Double? = null,
    
    @ColumnInfo(name = "created_at")
    val createdAt: Date = Date()
)
