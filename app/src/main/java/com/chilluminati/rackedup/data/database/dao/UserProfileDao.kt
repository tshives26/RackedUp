package com.chilluminati.rackedup.data.database.dao

import androidx.room.*
import com.chilluminati.rackedup.data.database.entity.UserProfile
import kotlinx.coroutines.flow.Flow

@Dao
interface UserProfileDao {
    @Query("SELECT * FROM user_profiles WHERE is_active = 1 LIMIT 1")
    suspend fun getActiveProfile(): UserProfile?
    
    @Query("SELECT * FROM user_profiles WHERE is_active = 1 LIMIT 1")
    fun getActiveProfileFlow(): Flow<UserProfile?>
    
    @Query("SELECT * FROM user_profiles")
    suspend fun getAllProfiles(): List<UserProfile>
    
    @Query("SELECT * FROM user_profiles ORDER BY created_at ASC")
    fun getAllProfilesFlow(): Flow<List<UserProfile>>
    
    @Query("SELECT * FROM user_profiles WHERE id = :profileId")
    suspend fun getProfileById(profileId: Long): UserProfile?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: UserProfile): Long
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfiles(profiles: List<UserProfile>)
    
    @Update
    suspend fun updateProfile(profile: UserProfile)
    
    @Delete
    suspend fun deleteProfile(profile: UserProfile)
    
    @Query("DELETE FROM user_profiles WHERE id = :profileId")
    suspend fun deleteProfileById(profileId: Long)
    
    @Query("UPDATE user_profiles SET is_active = 0")
    suspend fun deactivateAllProfiles()
    
    @Query("UPDATE user_profiles SET is_active = 1 WHERE id = :profileId")
    suspend fun activateProfile(profileId: Long)
    
    @Query("SELECT COUNT(*) FROM user_profiles")
    suspend fun getProfileCount(): Int
}
