package com.chilluminati.rackedup.data.repository

import com.chilluminati.rackedup.data.database.dao.UserProfileDao
import com.chilluminati.rackedup.data.database.entity.UserProfile
import com.chilluminati.rackedup.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository for managing multiple user profiles
 */
@Singleton
class MultiProfileRepository @Inject constructor(
    private val userProfileDao: UserProfileDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {

    /**
     * Get all profiles as Flow
     */
    fun getAllProfiles(): Flow<List<UserProfile>> = userProfileDao.getAllProfilesFlow()

    /**
     * Get the currently active profile
     */
    suspend fun getActiveProfile(): UserProfile? = withContext(ioDispatcher) {
        userProfileDao.getActiveProfile()
    }

    /**
     * Get profile by ID
     */
    suspend fun getProfileById(profileId: Long): UserProfile? = withContext(ioDispatcher) {
        userProfileDao.getProfileById(profileId)
    }

    /**
     * Create a new profile
     */
    suspend fun createProfile(
        name: String,
        email: String? = null,
        birthday: Date? = null,
        sex: String? = null,
        heightCm: Double? = null,
        weightKg: Double? = null,
        activityLevel: String? = null,
        fitnessGoal: String? = null,
        experienceLevel: String? = null,
        makeActive: Boolean = false
    ): Result<Long> = withContext(ioDispatcher) {
        try {
            val profile = UserProfile(
                name = name,
                email = email,
                birthday = birthday,
                sex = sex,
                heightCm = heightCm,
                weightKg = weightKg,
                activityLevel = activityLevel,
                fitnessGoal = fitnessGoal,
                experienceLevel = experienceLevel,
                isActive = makeActive,
                createdAt = Date(),
                updatedAt = Date()
            )

            // If this should be the active profile, deactivate others first
            if (makeActive) {
                userProfileDao.deactivateAllProfiles()
            }

            val profileId = userProfileDao.insertProfile(profile)
            Result.success(profileId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Switch to a different profile
     */
    suspend fun switchToProfile(profileId: Long): Result<UserProfile> = withContext(ioDispatcher) {
        try {
            val profile = userProfileDao.getProfileById(profileId)
                ?: return@withContext Result.failure(IllegalArgumentException("Profile not found"))

            // Deactivate all profiles first
            userProfileDao.deactivateAllProfiles()
            
            // Activate the selected profile
            userProfileDao.activateProfile(profileId)

            Result.success(profile)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Update profile information
     */
    suspend fun updateProfile(profile: UserProfile): Result<Unit> = withContext(ioDispatcher) {
        try {
            val updatedProfile = profile.copy(updatedAt = Date())
            userProfileDao.updateProfile(updatedProfile)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Delete a profile (cannot delete if it's the only profile)
     */
    suspend fun deleteProfile(profileId: Long): Result<String> = withContext(ioDispatcher) {
        try {
            val profileCount = userProfileDao.getProfileCount()
            
            if (profileCount <= 1) {
                return@withContext Result.failure(
                    IllegalStateException("Cannot delete the last remaining profile")
                )
            }

            val profileToDelete = userProfileDao.getProfileById(profileId)
                ?: return@withContext Result.failure(IllegalArgumentException("Profile not found"))

            val wasActive = profileToDelete.isActive

            // Delete the profile
            userProfileDao.deleteProfileById(profileId)

            // If the deleted profile was active, activate the first available profile
            if (wasActive) {
                val remainingProfiles = userProfileDao.getAllProfiles()
                if (remainingProfiles.isNotEmpty()) {
                    userProfileDao.activateProfile(remainingProfiles.first().id)
                }
            }

            Result.success("Profile '${profileToDelete.name}' deleted successfully")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Check if this is the only profile
     */
    suspend fun isOnlyProfile(): Boolean = withContext(ioDispatcher) {
        userProfileDao.getProfileCount() <= 1
    }

    /**
     * Get profile count
     */
    suspend fun getProfileCount(): Int = withContext(ioDispatcher) {
        userProfileDao.getProfileCount()
    }

    /**
     * Ensure there's at least one profile (create default if none exist)
     */
    suspend fun ensureDefaultProfile(): Result<UserProfile> = withContext(ioDispatcher) {
        try {
            val existingProfile = userProfileDao.getActiveProfile()
            if (existingProfile != null) {
                return@withContext Result.success(existingProfile)
            }

            // Check if any profiles exist
            val allProfiles = userProfileDao.getAllProfiles()
            if (allProfiles.isNotEmpty()) {
                // Activate the first profile
                val firstProfile = allProfiles.first()
                userProfileDao.activateProfile(firstProfile.id)
                return@withContext Result.success(firstProfile)
            }

            // Create a default profile
            val defaultProfile = UserProfile(
                name = "Default User",
                isActive = true,
                createdAt = Date(),
                updatedAt = Date()
            )

            val profileId = userProfileDao.insertProfile(defaultProfile)
            val createdProfile = defaultProfile.copy(id = profileId)
            
            Result.success(createdProfile)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Duplicate a profile with a new name
     */
    suspend fun duplicateProfile(
        sourceProfileId: Long,
        newName: String,
        makeActive: Boolean = false
    ): Result<Long> = withContext(ioDispatcher) {
        try {
            val sourceProfile = userProfileDao.getProfileById(sourceProfileId)
                ?: return@withContext Result.failure(IllegalArgumentException("Source profile not found"))

            val duplicatedProfile = sourceProfile.copy(
                id = 0, // Will be auto-generated
                name = newName,
                isActive = makeActive,
                createdAt = Date(),
                updatedAt = Date()
            )

            // If this should be the active profile, deactivate others first
            if (makeActive) {
                userProfileDao.deactivateAllProfiles()
            }

            val newProfileId = userProfileDao.insertProfile(duplicatedProfile)
            Result.success(newProfileId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Get profile statistics
     */
    data class ProfileStats(
        val totalProfiles: Int,
        val activeProfileName: String?,
        val oldestProfileDate: Date?,
        val newestProfileDate: Date?
    )

    suspend fun getProfileStats(): ProfileStats = withContext(ioDispatcher) {
        val allProfiles = userProfileDao.getAllProfiles()
        val activeProfile = userProfileDao.getActiveProfile()
        
        ProfileStats(
            totalProfiles = allProfiles.size,
            activeProfileName = activeProfile?.name,
            oldestProfileDate = allProfiles.minByOrNull { it.createdAt }?.createdAt,
            newestProfileDate = allProfiles.maxByOrNull { it.createdAt }?.createdAt
        )
    }
}
