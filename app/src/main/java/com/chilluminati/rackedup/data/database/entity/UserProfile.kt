package com.chilluminati.rackedup.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import java.util.Date

/**
 * UserProfile entity for user information and preferences
 */
@Entity(tableName = "user_profiles")
data class UserProfile(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    
    @ColumnInfo(name = "name")
    val name: String? = null,
    
    @ColumnInfo(name = "email")
    val email: String? = null,
    
    @ColumnInfo(name = "birthday")
    val birthday: Date? = null,
    
    @ColumnInfo(name = "sex")
    val sex: String? = null, // Male, Female, Other, Prefer not to say
    
    @ColumnInfo(name = "age")
    val age: Int? = null,
    
    @ColumnInfo(name = "gender")
    val gender: String? = null, // Male, Female, Other
    
    @ColumnInfo(name = "height_cm")
    val heightCm: Double? = null,
    
    @ColumnInfo(name = "weight_kg")
    val weightKg: Double? = null,
    
    @ColumnInfo(name = "activity_level")
    val activityLevel: String? = null, // Sedentary, Lightly Active, Moderately Active, Very Active
    
    @ColumnInfo(name = "fitness_goal")
    val fitnessGoal: String? = null, // Lose Weight, Gain Muscle, Maintain, Strength, Endurance
    
    @ColumnInfo(name = "experience_level")
    val experienceLevel: String? = null, // Beginner, Intermediate, Advanced
    
    @ColumnInfo(name = "preferred_weight_unit")
    val preferredWeightUnit: String = "kg", // kg or lbs
    
    @ColumnInfo(name = "preferred_distance_unit")
    val preferredDistanceUnit: String = "km", // km or miles
    
    @ColumnInfo(name = "default_rest_time")
    val defaultRestTime: Int = 120, // seconds
    
    @ColumnInfo(name = "timezone")
    val timezone: String? = null,
    
    @ColumnInfo(name = "profile_image_url")
    val profileImageUrl: String? = null,
    
    @ColumnInfo(name = "is_active")
    val isActive: Boolean = true,
    
    @ColumnInfo(name = "created_at")
    val createdAt: Date = Date(),
    
    @ColumnInfo(name = "updated_at")
    val updatedAt: Date = Date()
)
