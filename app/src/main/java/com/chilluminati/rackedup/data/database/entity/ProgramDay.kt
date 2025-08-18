package com.chilluminati.rackedup.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import androidx.room.ForeignKey
import androidx.room.Index
import java.util.Date

/**
 * ProgramDay entity representing a day within a program
 */
@Entity(
    tableName = "program_days",
    foreignKeys = [
        ForeignKey(
            entity = Program::class,
            parentColumns = ["id"],
            childColumns = ["program_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["program_id"])
    ]
)
data class ProgramDay(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    
    @ColumnInfo(name = "program_id")
    val programId: Long,
    
    @ColumnInfo(name = "day_number")
    val dayNumber: Int, // Day within the program (1, 2, 3...)
    
    @ColumnInfo(name = "week_number")
    val weekNumber: Int = 1, // Which week this day belongs to
    
    @ColumnInfo(name = "name")
    val name: String, // "Push Day", "Pull Day", "Legs", etc.
    
    @ColumnInfo(name = "description")
    val description: String? = null,
    
    @ColumnInfo(name = "day_type")
    val dayType: String? = null, // Push, Pull, Legs, Upper, Lower, Full Body, Rest
    
    @ColumnInfo(name = "muscle_groups")
    val muscleGroups: List<String> = emptyList(),
    
    @ColumnInfo(name = "estimated_duration")
    val estimatedDuration: Int? = null, // minutes
    
    @ColumnInfo(name = "is_rest_day")
    val isRestDay: Boolean = false,
    
    @ColumnInfo(name = "notes")
    val notes: String? = null,
    
    @ColumnInfo(name = "created_at")
    val createdAt: Date = Date()
)
