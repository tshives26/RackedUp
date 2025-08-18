package com.chilluminati.rackedup.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import java.util.Date

/**
 * Program entity representing a workout program/routine
 */
@Entity(tableName = "programs")
data class Program(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    
    @ColumnInfo(name = "name")
    val name: String,
    
    @ColumnInfo(name = "description")
    val description: String? = null,
    
    @ColumnInfo(name = "duration_weeks")
    val durationWeeks: Int? = null,
    
    @ColumnInfo(name = "difficulty_level")
    val difficultyLevel: String, // Beginner, Intermediate, Advanced
    
    @ColumnInfo(name = "program_type")
    val programType: String, // Push/Pull/Legs, Upper/Lower, Full Body, etc.
    
    @ColumnInfo(name = "days_per_week")
    val daysPerWeek: Int,
    
    @ColumnInfo(name = "goal")
    val goal: String? = null, // Strength, Hypertrophy, Endurance, Weight Loss
    
    @ColumnInfo(name = "author")
    val author: String? = null,
    
    @ColumnInfo(name = "is_custom")
    val isCustom: Boolean = false,
    
    @ColumnInfo(name = "is_template")
    val isTemplate: Boolean = false,
    
    @ColumnInfo(name = "is_active")
    val isActive: Boolean = false,
    
    @ColumnInfo(name = "is_favorite")
    val isFavorite: Boolean = false,
    
    @ColumnInfo(name = "current_week")
    val currentWeek: Int = 1,
    
    @ColumnInfo(name = "current_day")
    val currentDay: Int = 1,
    
    @ColumnInfo(name = "progression_scheme")
    val progressionScheme: String? = null, // Linear, Percentage, Double Progression
    
    @ColumnInfo(name = "deload_week")
    val deloadWeek: Int? = null,
    
    @ColumnInfo(name = "notes")
    val notes: String? = null,
    
    @ColumnInfo(name = "start_date")
    val startDate: Date? = null,
    
    @ColumnInfo(name = "end_date")
    val endDate: Date? = null,
    
    @ColumnInfo(name = "created_at")
    val createdAt: Date = Date(),
    
    @ColumnInfo(name = "updated_at")
    val updatedAt: Date = Date()
)
