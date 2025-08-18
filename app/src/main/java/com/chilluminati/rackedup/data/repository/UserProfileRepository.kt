package com.chilluminati.rackedup.data.repository

import com.chilluminati.rackedup.data.database.dao.UserProfileDao
import com.chilluminati.rackedup.data.database.entity.UserProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserProfileRepository @Inject constructor(
    private val userProfileDao: UserProfileDao
) {
    
    /**
     * Get the active user profile
     */
    suspend fun getActiveProfile(): UserProfile? {
        return userProfileDao.getActiveProfile()
    }

    /**
     * Observe the active user profile reactively
     */
    fun observeActiveProfile(): Flow<UserProfile?> = userProfileDao.getActiveProfileFlow()
    
    /**
     * Create a new user profile
     */
    suspend fun createProfile(
        name: String,
        birthday: Date? = null,
        sex: String? = null,
        email: String? = null
    ): Long {
        val profile = UserProfile(
            name = name,
            birthday = birthday,
            sex = sex,
            email = email,
            createdAt = Date(),
            updatedAt = Date()
        )
        return userProfileDao.insertProfile(profile)
    }
    
    /**
     * Update user profile
     */
    suspend fun updateProfile(profile: UserProfile) {
        val updatedProfile = profile.copy(updatedAt = Date())
        userProfileDao.updateProfile(updatedProfile)
    }
    
    /**
     * Update specific profile fields
     */
    suspend fun updateProfileFields(
        name: String? = null,
        birthday: Date? = null,
        sex: String? = null,
        email: String? = null,
        profileImageUrl: String? = null,
        age: Int? = null,
        heightCm: Double? = null,
        weightKg: Double? = null,
        activityLevel: String? = null,
        fitnessGoal: String? = null,
        experienceLevel: String? = null
    ) {
        val currentProfile = userProfileDao.getActiveProfile()
        currentProfile?.let { profile ->
            val updatedProfile = profile.copy(
                name = name ?: profile.name,
                birthday = birthday ?: profile.birthday,
                sex = sex ?: profile.sex,
                email = email ?: profile.email,
                profileImageUrl = profileImageUrl ?: profile.profileImageUrl,
                age = age ?: profile.age,
                heightCm = heightCm ?: profile.heightCm,
                weightKg = weightKg ?: profile.weightKg,
                activityLevel = activityLevel ?: profile.activityLevel,
                fitnessGoal = fitnessGoal ?: profile.fitnessGoal,
                experienceLevel = experienceLevel ?: profile.experienceLevel,
                updatedAt = Date()
            )
            userProfileDao.updateProfile(updatedProfile)
        }
    }
    
    /**
     * Check if user has completed onboarding
     */
    suspend fun hasCompletedOnboarding(): Boolean {
        val profile = userProfileDao.getActiveProfile()
        return profile?.name != null && profile.name.isNotBlank()
    }
    
    /**
     * Calculate age from birthday
     */
    fun calculateAge(birthday: Date): Int {
        val today = Date()
        val diffInMillis = today.time - birthday.time
        val diffInYears = diffInMillis / (1000L * 60 * 60 * 24 * 365)
        return diffInYears.toInt()
    }
}
