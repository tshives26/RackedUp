package com.chilluminati.rackedup.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import java.util.Date

/**
 * Exercise entity representing a single exercise in the database
 */
@Entity(tableName = "exercises")
data class Exercise(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    // Stable external identifier from source dataset (e.g., Free Exercise DB "id")
    @ColumnInfo(name = "source_id")
    val sourceId: String? = null,
    
    @ColumnInfo(name = "name")
    val name: String,
    
    @ColumnInfo(name = "category")
    val category: String, // Chest, Back, Shoulders, Arms, Legs, Core, Cardio
    
    @ColumnInfo(name = "subcategory")
    val subcategory: String? = null, // Biceps, Triceps, Quads, etc.
    
    @ColumnInfo(name = "equipment")
    val equipment: String, // Barbell, Dumbbell, Machine, Bodyweight, Cable
    
    @ColumnInfo(name = "exercise_type")
    val exerciseType: String, // Strength, Cardio, Isometric, Stretching
    
    @ColumnInfo(name = "difficulty_level")
    val difficultyLevel: String, // Beginner, Intermediate, Advanced
    
    @ColumnInfo(name = "instructions")
    val instructions: String? = null,

    // Structured instruction steps if available from source
    @ColumnInfo(name = "instruction_steps")
    val instructionSteps: List<String> = emptyList(),
    
    @ColumnInfo(name = "tips")
    val tips: String? = null,
    
    @ColumnInfo(name = "muscle_groups")
    val muscleGroups: List<String> = emptyList(), // Primary muscles worked
    
    @ColumnInfo(name = "secondary_muscles")
    val secondaryMuscles: List<String> = emptyList(),

    // Additional attributes from Free Exercise DB
    @ColumnInfo(name = "force")
    val force: String? = null, // push, pull, static

    @ColumnInfo(name = "mechanic")
    val mechanic: String? = null, // isolation, compound
    
    @ColumnInfo(name = "is_compound")
    val isCompound: Boolean = false,
    
    @ColumnInfo(name = "is_unilateral")
    val isUnilateral: Boolean = false,
    
    @ColumnInfo(name = "is_custom")
    val isCustom: Boolean = false, // User-created exercise
    
    @ColumnInfo(name = "is_favorite")
    val isFavorite: Boolean = false,
    
    @ColumnInfo(name = "image_url")
    val imageUrl: String? = null,

    // If multiple images are available (e.g., from Free Exercise DB)
    @ColumnInfo(name = "image_paths")
    val imagePaths: List<String> = emptyList(),
    
    @ColumnInfo(name = "video_url")
    val videoUrl: String? = null,
    
    @ColumnInfo(name = "created_at")
    val createdAt: Date = Date(),
    
    @ColumnInfo(name = "updated_at")
    val updatedAt: Date = Date()
)
