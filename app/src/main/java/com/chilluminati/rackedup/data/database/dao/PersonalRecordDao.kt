package com.chilluminati.rackedup.data.database.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.chilluminati.rackedup.data.database.entity.PersonalRecord

@Dao
interface PersonalRecordDao {
    @Query("SELECT * FROM personal_records WHERE exercise_id = :exerciseId ORDER BY achieved_at DESC")
    fun getPersonalRecords(exerciseId: Long): Flow<List<PersonalRecord>>

    @Query("SELECT * FROM personal_records")
    suspend fun getAllPersonalRecords(): List<PersonalRecord>

    @Query("SELECT * FROM personal_records")
    fun getAllPersonalRecordsFlow(): Flow<List<PersonalRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPersonalRecord(personalRecord: PersonalRecord): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPersonalRecords(personalRecords: List<PersonalRecord>)

    @Update
    suspend fun updatePersonalRecord(personalRecord: PersonalRecord)

    @Delete
    suspend fun deletePersonalRecord(personalRecord: PersonalRecord)
}


