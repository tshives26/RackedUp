package com.chilluminati.rackedup.data.database.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.chilluminati.rackedup.data.database.entity.ProgramExercise

@Dao
interface ProgramExerciseDao {
    @Query("SELECT * FROM program_exercises WHERE program_day_id = :programDayId ORDER BY order_index ASC")
    fun getProgramExercises(programDayId: Long): Flow<List<ProgramExercise>>

    @Query("SELECT * FROM program_exercises")
    suspend fun getAllProgramExercises(): List<ProgramExercise>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProgramExercise(programExercise: ProgramExercise): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProgramExercises(programExercises: List<ProgramExercise>)

    @Update
    suspend fun updateProgramExercise(programExercise: ProgramExercise)

    @Delete
    suspend fun deleteProgramExercise(programExercise: ProgramExercise)
}


