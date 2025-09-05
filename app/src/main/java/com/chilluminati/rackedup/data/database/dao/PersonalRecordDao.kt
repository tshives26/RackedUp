package com.chilluminati.rackedup.data.database.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.chilluminati.rackedup.data.database.entity.PersonalRecord

@Dao
interface PersonalRecordDao {
    @Query("SELECT * FROM personal_records WHERE exercise_id = :exerciseId ORDER BY achieved_at DESC")
    fun getPersonalRecords(exerciseId: Long): Flow<List<PersonalRecord>>

    @Query("SELECT * FROM personal_records WHERE exercise_id = :exerciseId ORDER BY achieved_at DESC")
    suspend fun getPersonalRecordsSync(exerciseId: Long): List<PersonalRecord>

    @Query("SELECT * FROM personal_records")
    suspend fun getAllPersonalRecords(): List<PersonalRecord>

    @Query("SELECT * FROM personal_records")
    fun getAllPersonalRecordsFlow(): Flow<List<PersonalRecord>>

    /**
     * Get the best volume personal record for each exercise (highest volume per exercise)
     * This query ensures only one record per exercise by selecting the most recent record
     * when multiple records have the same volume
     */
    @Query("""
        SELECT pr.* FROM personal_records pr
        INNER JOIN (
            SELECT exercise_id, MAX(volume) as max_volume
            FROM personal_records 
            WHERE record_type = 'Volume' AND volume IS NOT NULL
            GROUP BY exercise_id
        ) max_volumes ON pr.exercise_id = max_volumes.exercise_id 
        AND pr.volume = max_volumes.max_volume
        WHERE pr.record_type = 'Volume'
        AND pr.id = (
            SELECT MAX(id) 
            FROM personal_records pr2 
            WHERE pr2.exercise_id = pr.exercise_id 
            AND pr2.volume = max_volumes.max_volume
            AND pr2.record_type = 'Volume'
        )
        ORDER BY pr.volume DESC
    """)
    suspend fun getBestVolumePersonalRecords(): List<PersonalRecord>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPersonalRecord(personalRecord: PersonalRecord): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPersonalRecords(personalRecords: List<PersonalRecord>)

    @Update
    suspend fun updatePersonalRecord(personalRecord: PersonalRecord)

    @Delete
    suspend fun deletePersonalRecord(personalRecord: PersonalRecord)
    
    @Query("DELETE FROM personal_records")
    suspend fun deleteAllPersonalRecords()

    /**
     * Clean up duplicate volume personal records, keeping only the best one per exercise
     */
    @Query("""
        DELETE FROM personal_records 
        WHERE record_type = 'Volume' 
        AND id NOT IN (
            SELECT pr.id FROM personal_records pr
            INNER JOIN (
                SELECT exercise_id, MAX(volume) as max_volume
                FROM personal_records 
                WHERE record_type = 'Volume' AND volume IS NOT NULL
                GROUP BY exercise_id
            ) max_volumes ON pr.exercise_id = max_volumes.exercise_id 
            AND pr.volume = max_volumes.max_volume
            WHERE pr.record_type = 'Volume'
            AND pr.id = (
                SELECT MAX(id) 
                FROM personal_records pr2 
                WHERE pr2.exercise_id = pr.exercise_id 
                AND pr2.volume = max_volumes.max_volume
                AND pr2.record_type = 'Volume'
            )
        )
    """)
    suspend fun cleanupDuplicateVolumeRecords()
}


