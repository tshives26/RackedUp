package com.chilluminati.rackedup.data.database.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.chilluminati.rackedup.data.database.entity.ExerciseSet

@Dao
interface ExerciseSetDao {
    @Query("SELECT * FROM exercise_sets WHERE workout_exercise_id = :workoutExerciseId ORDER BY set_number ASC")
    fun getExerciseSets(workoutExerciseId: Long): Flow<List<ExerciseSet>>
    
    @Query("SELECT * FROM exercise_sets WHERE workout_exercise_id = :workoutExerciseId ORDER BY set_number ASC")
    suspend fun getSetsByWorkoutExerciseId(workoutExerciseId: Long): List<ExerciseSet>
    
    @Query("SELECT * FROM exercise_sets")
    suspend fun getAllExerciseSets(): List<ExerciseSet>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExerciseSet(exerciseSet: ExerciseSet): Long
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExerciseSets(exerciseSets: List<ExerciseSet>)
    
    @Update
    suspend fun updateExerciseSet(exerciseSet: ExerciseSet)
    
    @Delete
    suspend fun deleteExerciseSet(exerciseSet: ExerciseSet)
}



@Dao
interface ProgramDao {
    @Query("SELECT * FROM programs ORDER BY created_at DESC")
    fun getAllPrograms(): Flow<List<com.chilluminati.rackedup.data.database.entity.Program>>
    
    @Query("SELECT * FROM programs ORDER BY created_at DESC")
    suspend fun getAllProgramsList(): List<com.chilluminati.rackedup.data.database.entity.Program>
    
    @Query("SELECT * FROM programs WHERE id = :programId LIMIT 1")
    suspend fun getProgramById(programId: Long): com.chilluminati.rackedup.data.database.entity.Program?
    
    @Query("SELECT * FROM programs WHERE is_active = 1 LIMIT 1")
    suspend fun getActiveProgram(): com.chilluminati.rackedup.data.database.entity.Program?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProgram(program: com.chilluminati.rackedup.data.database.entity.Program): Long
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPrograms(programs: List<com.chilluminati.rackedup.data.database.entity.Program>)
    
    @Update
    suspend fun updateProgram(program: com.chilluminati.rackedup.data.database.entity.Program)
    
    @Delete
    suspend fun deleteProgram(program: com.chilluminati.rackedup.data.database.entity.Program)
}

@Dao
interface ProgramDayDao {
    @Query("SELECT * FROM program_days WHERE program_id = :programId ORDER BY day_number ASC")
    fun getProgramDays(programId: Long): Flow<List<com.chilluminati.rackedup.data.database.entity.ProgramDay>>
    
    @Query("SELECT * FROM program_days WHERE id = :programDayId")
    suspend fun getProgramDayById(programDayId: Long): com.chilluminati.rackedup.data.database.entity.ProgramDay?
    
    @Query("SELECT * FROM program_days")
    suspend fun getAllProgramDays(): List<com.chilluminati.rackedup.data.database.entity.ProgramDay>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProgramDay(programDay: com.chilluminati.rackedup.data.database.entity.ProgramDay): Long
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProgramDays(programDays: List<com.chilluminati.rackedup.data.database.entity.ProgramDay>)
    
    @Update
    suspend fun updateProgramDay(programDay: com.chilluminati.rackedup.data.database.entity.ProgramDay)
    
    @Delete
    suspend fun deleteProgramDay(programDay: com.chilluminati.rackedup.data.database.entity.ProgramDay)
}

@Dao
interface ProgramExerciseDao {
    @Query("SELECT * FROM program_exercises WHERE program_day_id = :programDayId ORDER BY order_index ASC")
    fun getProgramExercises(programDayId: Long): Flow<List<com.chilluminati.rackedup.data.database.entity.ProgramExercise>>
    
    @Query("SELECT * FROM program_exercises")
    suspend fun getAllProgramExercises(): List<com.chilluminati.rackedup.data.database.entity.ProgramExercise>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProgramExercise(programExercise: com.chilluminati.rackedup.data.database.entity.ProgramExercise): Long
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProgramExercises(programExercises: List<com.chilluminati.rackedup.data.database.entity.ProgramExercise>)
    
    @Update
    suspend fun updateProgramExercise(programExercise: com.chilluminati.rackedup.data.database.entity.ProgramExercise)
    
    @Delete
    suspend fun deleteProgramExercise(programExercise: com.chilluminati.rackedup.data.database.entity.ProgramExercise)
}

@Dao
interface PersonalRecordDao {
    @Query("SELECT * FROM personal_records WHERE exercise_id = :exerciseId ORDER BY achieved_at DESC")
    fun getPersonalRecords(exerciseId: Long): Flow<List<com.chilluminati.rackedup.data.database.entity.PersonalRecord>>
    
    @Query("SELECT * FROM personal_records")
    suspend fun getAllPersonalRecords(): List<com.chilluminati.rackedup.data.database.entity.PersonalRecord>
    
    @Query("SELECT * FROM personal_records")
    fun getAllPersonalRecordsFlow(): Flow<List<com.chilluminati.rackedup.data.database.entity.PersonalRecord>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPersonalRecord(personalRecord: com.chilluminati.rackedup.data.database.entity.PersonalRecord): Long
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPersonalRecords(personalRecords: List<com.chilluminati.rackedup.data.database.entity.PersonalRecord>)
    
    @Update
    suspend fun updatePersonalRecord(personalRecord: com.chilluminati.rackedup.data.database.entity.PersonalRecord)
    
    @Delete
    suspend fun deletePersonalRecord(personalRecord: com.chilluminati.rackedup.data.database.entity.PersonalRecord)
}

@Dao
interface BodyMeasurementDao {
    @Query("SELECT * FROM body_measurements WHERE measurement_type = :type ORDER BY measured_at DESC")
    fun getBodyMeasurements(type: String): Flow<List<com.chilluminati.rackedup.data.database.entity.BodyMeasurement>>
    
    @Query("SELECT * FROM body_measurements")
    suspend fun getAllBodyMeasurements(): List<com.chilluminati.rackedup.data.database.entity.BodyMeasurement>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBodyMeasurement(bodyMeasurement: com.chilluminati.rackedup.data.database.entity.BodyMeasurement): Long
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBodyMeasurements(bodyMeasurements: List<com.chilluminati.rackedup.data.database.entity.BodyMeasurement>)
    
    @Update
    suspend fun updateBodyMeasurement(bodyMeasurement: com.chilluminati.rackedup.data.database.entity.BodyMeasurement)
    
    @Delete
    suspend fun deleteBodyMeasurement(bodyMeasurement: com.chilluminati.rackedup.data.database.entity.BodyMeasurement)
}
