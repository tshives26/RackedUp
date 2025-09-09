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
     * Get personal records achieved in a specific workout
     */
    @Query("SELECT * FROM personal_records WHERE workout_id = :workoutId")
    suspend fun getPersonalRecordsForWorkout(workoutId: Long): List<PersonalRecord>

    /**
     * Get the best personal records achieved in a specific workout (one per exercise per record type)
     * This ensures we only count unique PRs per exercise, not multiple PRs for the same exercise
     */
    @Query("""
        SELECT pr.* FROM personal_records pr
        WHERE pr.workout_id = :workoutId
        AND pr.id IN (
            SELECT MAX(pr2.id) 
            FROM personal_records pr2 
            WHERE pr2.workout_id = :workoutId
            AND pr2.exercise_id = pr.exercise_id 
            AND pr2.record_type = pr.record_type
            GROUP BY pr2.exercise_id, pr2.record_type
        )
        ORDER BY pr.exercise_id, pr.record_type
    """)
    suspend fun getBestPersonalRecordsForWorkout(workoutId: Long): List<PersonalRecord>

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
    
    /**
     * Get unique personal records across all workouts
     * Returns only one PR per exercise (the best one) to avoid duplicates
     * Prioritizes: Volume > Max Weight > 1RM > Distance > Duration > Speed
     */
    @Query("""
        SELECT pr.* FROM personal_records pr
        WHERE pr.id IN (
            SELECT MAX(pr2.id)
            FROM personal_records pr2
            WHERE pr2.exercise_id = pr.exercise_id
            AND pr2.record_type = pr.record_type
            GROUP BY pr2.exercise_id, pr2.record_type
        )
        AND pr.id IN (
            SELECT CASE 
                WHEN EXISTS(SELECT 1 FROM personal_records WHERE exercise_id = pr.exercise_id AND record_type = 'Volume') 
                THEN (SELECT MAX(id) FROM personal_records WHERE exercise_id = pr.exercise_id AND record_type = 'Volume')
                WHEN EXISTS(SELECT 1 FROM personal_records WHERE exercise_id = pr.exercise_id AND record_type = 'Max Weight') 
                THEN (SELECT MAX(id) FROM personal_records WHERE exercise_id = pr.exercise_id AND record_type = 'Max Weight')
                WHEN EXISTS(SELECT 1 FROM personal_records WHERE exercise_id = pr.exercise_id AND record_type = '1RM') 
                THEN (SELECT MAX(id) FROM personal_records WHERE exercise_id = pr.exercise_id AND record_type = '1RM')
                WHEN EXISTS(SELECT 1 FROM personal_records WHERE exercise_id = pr.exercise_id AND record_type = 'Distance') 
                THEN (SELECT MAX(id) FROM personal_records WHERE exercise_id = pr.exercise_id AND record_type = 'Distance')
                WHEN EXISTS(SELECT 1 FROM personal_records WHERE exercise_id = pr.exercise_id AND record_type = 'Duration') 
                THEN (SELECT MAX(id) FROM personal_records WHERE exercise_id = pr.exercise_id AND record_type = 'Duration')
                WHEN EXISTS(SELECT 1 FROM personal_records WHERE exercise_id = pr.exercise_id AND record_type = 'Speed') 
                THEN (SELECT MAX(id) FROM personal_records WHERE exercise_id = pr.exercise_id AND record_type = 'Speed')
                ELSE (SELECT MAX(id) FROM personal_records WHERE exercise_id = pr.exercise_id)
            END
        )
        ORDER BY pr.exercise_id
    """)
    suspend fun getUniquePersonalRecords(): List<PersonalRecord>
}


