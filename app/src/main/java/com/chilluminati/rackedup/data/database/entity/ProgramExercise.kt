package com.chilluminati.rackedup.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import androidx.room.ForeignKey
import androidx.room.Index
import java.util.Date

/**
 * ProgramExercise entity representing an exercise within a program day
 */
@Entity(
    tableName = "program_exercises",
    foreignKeys = [
        ForeignKey(
            entity = ProgramDay::class,
            parentColumns = ["id"],
            childColumns = ["program_day_id"],
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
        Index(value = ["program_day_id"]),
        Index(value = ["exercise_id"])
    ]
)
data class ProgramExercise(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    
    @ColumnInfo(name = "program_day_id")
    val programDayId: Long,
    
    @ColumnInfo(name = "exercise_id")
    val exerciseId: Long,
    
    @ColumnInfo(name = "order_index")
    val orderIndex: Int,
    
    @ColumnInfo(name = "sets")
    val sets: String, // "3x8-12", "5x5", "3x8,6,4"
    
    @ColumnInfo(name = "reps")
    val reps: String? = null, // "8-12", "5", "AMRAP"
    
    @ColumnInfo(name = "weight_percentage")
    val weightPercentage: Double? = null, // Percentage of 1RM
    
    @ColumnInfo(name = "rest_time_seconds")
    val restTimeSeconds: Int? = null,
    
    @ColumnInfo(name = "rpe_target")
    val rpeTarget: Int? = null, // Target RPE
    
    @ColumnInfo(name = "progression_scheme")
    val progressionScheme: String? = null, // Linear, Double Progression, etc.
    
    @ColumnInfo(name = "progression_increment")
    val progressionIncrement: Double? = null,
    
    @ColumnInfo(name = "is_superset")
    val isSuperset: Boolean = false,
    
    @ColumnInfo(name = "superset_id")
    val supersetId: String? = null,
    
    @ColumnInfo(name = "notes")
    val notes: String? = null,
    
    @ColumnInfo(name = "created_at")
    val createdAt: Date = Date()
)
