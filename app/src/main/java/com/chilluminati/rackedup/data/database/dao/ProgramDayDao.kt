package com.chilluminati.rackedup.data.database.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.chilluminati.rackedup.data.database.entity.ProgramDay

@Dao
interface ProgramDayDao {
    @Query("SELECT * FROM program_days WHERE program_id = :programId ORDER BY day_number ASC")
    fun getProgramDays(programId: Long): Flow<List<ProgramDay>>

    @Query("SELECT * FROM program_days WHERE id = :programDayId")
    suspend fun getProgramDayById(programDayId: Long): ProgramDay?

    @Query("SELECT * FROM program_days")
    suspend fun getAllProgramDays(): List<ProgramDay>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProgramDay(programDay: ProgramDay): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProgramDays(programDays: List<ProgramDay>)

    @Update
    suspend fun updateProgramDay(programDay: ProgramDay)

    @Delete
    suspend fun deleteProgramDay(programDay: ProgramDay)
}


