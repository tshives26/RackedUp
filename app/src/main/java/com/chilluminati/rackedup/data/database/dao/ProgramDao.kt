package com.chilluminati.rackedup.data.database.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.chilluminati.rackedup.data.database.entity.Program

@Dao
interface ProgramDao {
    @Query("SELECT * FROM programs ORDER BY created_at DESC")
    fun getAllPrograms(): Flow<List<Program>>

    @Query("SELECT * FROM programs ORDER BY created_at DESC")
    suspend fun getAllProgramsList(): List<Program>

    @Query("SELECT * FROM programs WHERE id = :programId LIMIT 1")
    suspend fun getProgramById(programId: Long): Program?

    @Query("SELECT * FROM programs WHERE is_active = 1 LIMIT 1")
    suspend fun getActiveProgram(): Program?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProgram(program: Program): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPrograms(programs: List<Program>)

    @Update
    suspend fun updateProgram(program: Program)

    @Delete
    suspend fun deleteProgram(program: Program)
}


