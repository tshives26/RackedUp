package com.chilluminati.rackedup.data.repository

import com.chilluminati.rackedup.data.database.dao.ProgramDao
import com.chilluminati.rackedup.data.database.dao.ProgramDayDao
import com.chilluminati.rackedup.data.database.dao.ProgramExerciseDao
import com.chilluminati.rackedup.data.database.entity.Program
import com.chilluminati.rackedup.data.database.entity.ProgramDay
import com.chilluminati.rackedup.data.database.entity.ProgramExercise
import com.chilluminati.rackedup.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton
import android.util.Log

/**
 * Repository for program and routine management
 */
@Singleton
class ProgramRepository @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val programDao: ProgramDao,
    private val programDayDao: ProgramDayDao,
    private val programExerciseDao: ProgramExerciseDao
) {
    
    /**
     * Get all programs
     */
    fun getAllPrograms(): Flow<List<Program>> =
        programDao.getAllPrograms().map { db ->
            // Ensure templates are always present; merge by id without duplicates
            val existingIds = db.map { it.id }.toSet()
            val merged = db + getTemplatePrograms().filter { it.id !in existingIds }
            Log.d("ProgramRepository", "getAllPrograms: db=${db.size} merged=${merged.size}")
            merged
        }
    
    /**
     * Get user's custom programs
     */
    fun getUserPrograms(): Flow<List<Program>> = programDao.getAllPrograms().map { list -> list.filter { it.isCustom } }
    
    /**
     * Get template programs
     */
    fun getTemplatePrograms(): List<Program> {
        return listOf(
            Program(
                id = 1,
                name = "Starting Strength",
                description = "A simple and effective 3-day program for beginners focusing on compound movements.",
                durationWeeks = 12,
                difficultyLevel = "Beginner",
                programType = "Full Body",
                daysPerWeek = 3,
                isTemplate = true,
                isCustom = false
            ),
            Program(
                id = 2,
                name = "Push Pull Legs",
                description = "A 6-day program alternating between push, pull, and leg exercises.",
                durationWeeks = 8,
                difficultyLevel = "Intermediate",
                programType = "Push/Pull/Legs",
                daysPerWeek = 6,
                isTemplate = true,
                isCustom = false
            ),
            Program(
                id = 3,
                name = "Upper Lower Split",
                description = "A 4-day program alternating between upper and lower body workouts.",
                durationWeeks = 10,
                difficultyLevel = "Intermediate",
                programType = "Upper/Lower",
                daysPerWeek = 4,
                isTemplate = true,
                isCustom = false
            ),
            Program(
                id = 4,
                name = "5/3/1 for Beginners",
                description = "Jim Wendler's 5/3/1 program adapted for beginners with core lifts.",
                durationWeeks = 16,
                difficultyLevel = "Beginner",
                programType = "Percentage-based",
                daysPerWeek = 4,
                isTemplate = true,
                isCustom = false
            ),
            Program(
                id = 5,
                name = "Bodyweight Basics",
                description = "A beginner-friendly program using only bodyweight exercises.",
                durationWeeks = 8,
                difficultyLevel = "Beginner",
                programType = "Bodyweight",
                daysPerWeek = 3,
                isTemplate = true,
                isCustom = false
            )
        )
    }
    
    /**
     * Get program by ID
     */
    suspend fun getProgramById(programId: Long): Program? {
        return withContext(ioDispatcher) {
            val all = programDao.getAllProgramsList()
            val match = all.find { it.id == programId }
            Log.d("ProgramRepository", "getProgramById: id=$programId foundInDb=${match != null}")
            match ?: getTemplatePrograms().find { it.id == programId }
        }
    }
    
    /**
     * Get the currently active program
     */
    suspend fun getActiveProgram(): Program? {
        return withContext(ioDispatcher) {
            programDao.getActiveProgram()
        }
    }
    
    /**
     * Observe the active program
     */
    fun getActiveProgramFlow(): Flow<Program?> {
        return programDao.getAllPrograms().map { programs ->
            programs.firstOrNull { it.isActive }
        }
    }
    
    /**
     * Get program days for a program
     */
    suspend fun getProgramDays(programId: Long): List<ProgramDay> {
        return withContext(ioDispatcher) {
            // If this program exists in the DB and is not a template, always use DB-backed days
            val program = programDao.getProgramById(programId)
            if (program != null && !program.isTemplate) {
                return@withContext programDayDao
                    .getAllProgramDays()
                    .filter { it.programId == programId }
                    .sortedBy { it.dayNumber }
            }

            // Otherwise, fall back to built-in templates by ID
            when (programId) {
                1L -> getStartingStrengthDays()
                2L -> getPushPullLegsDays()
                3L -> getUpperLowerDays()
                4L -> get531Days()
                5L -> getBodyweightDays()
                else -> programDayDao
                    .getAllProgramDays()
                    .filter { it.programId == programId }
                    .sortedBy { it.dayNumber }
            }
        }
    }
    
    /**
     * Get a specific program day by ID
     */
    suspend fun getProgramDayById(programDayId: Long): ProgramDay? {
        return withContext(ioDispatcher) {
            programDayDao.getProgramDayById(programDayId)
        }
    }
    
    /**
     * Get program exercises for a program day
     */
    suspend fun getProgramExercises(programDayId: Long): List<ProgramExercise> {
        return withContext(ioDispatcher) {
            val list = programExerciseDao.getAllProgramExercises().filter { it.programDayId == programDayId }.sortedBy { it.orderIndex }
            Log.d("ProgramRepository", "getProgramExercises: dayId=$programDayId count=${list.size}")
            list
        }
    }
    
    /**
     * Create custom program
     */
    suspend fun createProgram(
        name: String,
        description: String?,
        durationWeeks: Int?,
        difficultyLevel: String,
        programType: String,
        daysPerWeek: Int = 3
    ): Long {
        return withContext(ioDispatcher) {
            val program = Program(
                id = 0,
                name = name,
                description = description,
                durationWeeks = durationWeeks,
                difficultyLevel = difficultyLevel,
                programType = programType,
                daysPerWeek = daysPerWeek,
                isTemplate = false,
                isCustom = true
            )
            val insertedId = programDao.insertProgram(program)
            Log.d("ProgramRepository", "createProgram: insertedId=$insertedId name=$name")
            insertedId
        }
    }

    /**
     * Persist the day/exercise structure for a custom program created via builder.
     * Incoming day IDs may be temporary (negative). This will assign new positive IDs
     * and remap exercises to the new day IDs.
     */
    suspend fun saveProgramStructure(
        programId: Long,
        days: List<ProgramDay>,
        exercisesByTempDayId: Map<Long, List<ProgramExercise>>
    ) {
        withContext(ioDispatcher) {
            Log.d("ProgramRepository", "saveProgramStructure: incoming days=${days.map { it.name }.joinToString()}")
            // Get program to ensure we create all required days
            val program = programDao.getProgramById(programId)
            if (program == null) {
                Log.e("ProgramRepository", "saveProgramStructure: program not found id=$programId")
                return@withContext
            }

            // Use the actual days from the builder, preserving their day numbers
            val requiredDays = days.map { day ->
                day.copy(
                    weekNumber = 1
                )
            }
            Log.d("ProgramRepository", "saveProgramStructure: requiredDays=${requiredDays.map { "${it.name} (Day ${it.dayNumber})" }.joinToString()}")

            // Create real days in DB
            val newDays = requiredDays.sortedBy { it.dayNumber }.map { day ->
                Log.d("ProgramRepository", "saveProgramStructure: inserting day=${day.name} with dayNumber=${day.dayNumber}")
                val realId = programDayDao.insertProgramDay(
                    ProgramDay(
                        id = 0,
                        programId = programId,
                        name = day.name,
                        dayNumber = day.dayNumber,
                        weekNumber = day.weekNumber,
                        description = day.description,
                        dayType = day.dayType,
                        muscleGroups = day.muscleGroups,
                        estimatedDuration = day.estimatedDuration,
                        isRestDay = day.isRestDay,
                        notes = day.notes
                    )
                )
                day.id to realId
            }.toMap()

            // Persist exercises remapped to real day IDs
            exercisesByTempDayId.forEach { (tempId, list) ->
                val newDayId = newDays[tempId] ?: return@forEach
                val mapped = list.sortedBy { it.orderIndex }.mapIndexed { idx, ex ->
                    ex.copy(id = 0, programDayId = newDayId, orderIndex = idx)
                }
                programExerciseDao.insertProgramExercises(mapped)
                Log.d("ProgramRepository", "saveProgramStructure: dayTempId=$tempId -> newDayId=$newDayId exercises=${mapped.size}")
            }
        }
    }
    
    /**
     * Update program
     */
    suspend fun updateProgram(program: Program) {
        withContext(ioDispatcher) {
            programDao.updateProgram(program)
            Log.d("ProgramRepository", "updateProgram: id=${program.id} name=${program.name}")
        }
    }

    /**
     * Replace the entire day/exercise structure for an existing program. This is used when
     * editing a program from the builder. The existing structure is removed, then the provided
     * structure is inserted fresh to keep IDs consistent and avoid complicated diffs.
     */
    suspend fun replaceProgramStructure(
        programId: Long,
        days: List<ProgramDay>,
        exercisesByTempDayId: Map<Long, List<ProgramExercise>>
    ) {
        withContext(ioDispatcher) {
            // Get program to ensure we create all required days
            val program = programDao.getProgramById(programId)
            if (program == null) {
                Log.e("ProgramRepository", "replaceProgramStructure: program not found id=$programId")
                return@withContext
            }

            // 1) Delete existing exercises and days for this program
            val allDays = programDayDao.getAllProgramDays().filter { it.programId == programId }
            val allExercises = programExerciseDao.getAllProgramExercises()
            allDays.forEach { day ->
                allExercises.filter { it.programDayId == day.id }.forEach { ex ->
                    programExerciseDao.deleteProgramExercise(ex)
                }
            }
            allDays.forEach { day -> programDayDao.deleteProgramDay(day) }

            // Use the actual days from the builder, ensuring they have correct day numbers
            val requiredDays = days.mapIndexed { index, day ->
                day.copy(
                    dayNumber = index + 1,
                    weekNumber = 1
                )
            }

            // 2) Create real days in DB
            val newDays = requiredDays.sortedBy { it.dayNumber }.map { day ->
                val realId = programDayDao.insertProgramDay(
                    ProgramDay(
                        id = 0,
                        programId = programId,
                        name = day.name,
                        dayNumber = day.dayNumber,
                        weekNumber = day.weekNumber,
                        description = day.description,
                        dayType = day.dayType,
                        muscleGroups = day.muscleGroups,
                        estimatedDuration = day.estimatedDuration,
                        isRestDay = day.isRestDay,
                        notes = day.notes
                    )
                )
                day.id to realId
            }.toMap()

            // 3) Persist exercises remapped to real day IDs
            exercisesByTempDayId.forEach { (tempId, list) ->
                val newDayId = newDays[tempId] ?: return@forEach
                val mapped = list.sortedBy { it.orderIndex }.mapIndexed { idx, ex ->
                    ex.copy(id = 0, programDayId = newDayId, orderIndex = idx)
                }
                programExerciseDao.insertProgramExercises(mapped)
                Log.d("ProgramRepository", "replaceProgramStructure: dayTempId=$tempId -> newDayId=$newDayId exercises=${mapped.size}")
            }
        }
    }
    
    /**
     * Delete program
     */
    suspend fun deleteProgram(program: Program) {
        withContext(ioDispatcher) {
            // Delete structure first
            val allDays = programDayDao.getAllProgramDays().filter { it.programId == program.id }
            val allExercises = programExerciseDao.getAllProgramExercises()
            allDays.forEach { day ->
                allExercises.filter { it.programDayId == day.id }.forEach { ex ->
                    programExerciseDao.deleteProgramExercise(ex)
                }
            }
            allDays.forEach { day -> programDayDao.deleteProgramDay(day) }

            // Delete program itself
            programDao.deleteProgram(program)
            Log.d("ProgramRepository", "deleteProgram: id=${program.id}")
        }
    }
    
    /**
     * Copy template program for user customization
     */
    suspend fun copyTemplateProgram(_templateId: Long, _newName: String): Long {
        return withContext(ioDispatcher) {
            // TODO: Implement template copying
            Log.d("ProgramRepository", "copyTemplateProgram: templateId=${_templateId} newName=${_newName}")
            System.currentTimeMillis()
        }
    }

    suspend fun markActiveProgram(program: Program) {
        withContext(ioDispatcher) {
            val all = programDao.getAllProgramsList()
            all.forEach { p ->
                val updated = p.copy(isActive = p.id == program.id)
                programDao.updateProgram(updated)
            }
        }
    }

    /** Set program start date if not already set and mark active. */
    suspend fun startProgram(programId: Long) {
        withContext(ioDispatcher) {
            val program = programDao.getProgramById(programId) ?: return@withContext
            
            // First deactivate all other programs
            val allPrograms = programDao.getAllProgramsList()
            allPrograms.forEach { p ->
                if (p.id != programId && p.isActive) {
                    programDao.updateProgram(p.copy(isActive = false))
                }
            }
            
            // Then activate the selected program
            val updated = program.copy(
                isActive = true,
                startDate = program.startDate ?: java.util.Date()
            )
            programDao.updateProgram(updated)
        }
    }

    /** Mark program finished with endDate and deactivate. */
    suspend fun finishProgram(programId: Long) {
        withContext(ioDispatcher) {
            val program = programDao.getProgramById(programId) ?: return@withContext
            val updated = program.copy(
                isActive = false,
                endDate = program.endDate ?: java.util.Date()
            )
            programDao.updateProgram(updated)
        }
    }
    
    // Template program day definitions
    private fun getStartingStrengthDays(): List<ProgramDay> {
        return listOf(
            ProgramDay(id = 1, programId = 1, name = "Day A", dayNumber = 1, weekNumber = 1),
            ProgramDay(id = 2, programId = 1, name = "Day B", dayNumber = 2, weekNumber = 1),
            ProgramDay(id = 3, programId = 1, name = "Day A", dayNumber = 3, weekNumber = 1)
        )
    }
    
    private fun getPushPullLegsDays(): List<ProgramDay> {
        return listOf(
            ProgramDay(id = 4, programId = 2, name = "Push", dayNumber = 1, weekNumber = 1),
            ProgramDay(id = 5, programId = 2, name = "Pull", dayNumber = 2, weekNumber = 1),
            ProgramDay(id = 6, programId = 2, name = "Legs", dayNumber = 3, weekNumber = 1),
            ProgramDay(id = 7, programId = 2, name = "Push", dayNumber = 4, weekNumber = 1),
            ProgramDay(id = 8, programId = 2, name = "Pull", dayNumber = 5, weekNumber = 1),
            ProgramDay(id = 9, programId = 2, name = "Legs", dayNumber = 6, weekNumber = 1)
        )
    }
    
    private fun getUpperLowerDays(): List<ProgramDay> {
        return listOf(
            ProgramDay(id = 10, programId = 3, name = "Upper", dayNumber = 1, weekNumber = 1),
            ProgramDay(id = 11, programId = 3, name = "Lower", dayNumber = 2, weekNumber = 1),
            ProgramDay(id = 12, programId = 3, name = "Upper", dayNumber = 3, weekNumber = 1),
            ProgramDay(id = 13, programId = 3, name = "Lower", dayNumber = 4, weekNumber = 1)
        )
    }
    
    private fun get531Days(): List<ProgramDay> {
        return listOf(
            ProgramDay(id = 14, programId = 4, name = "Squat", dayNumber = 1, weekNumber = 1),
            ProgramDay(id = 15, programId = 4, name = "Bench", dayNumber = 2, weekNumber = 1),
            ProgramDay(id = 16, programId = 4, name = "Deadlift", dayNumber = 3, weekNumber = 1),
            ProgramDay(id = 17, programId = 4, name = "OHP", dayNumber = 4, weekNumber = 1)
        )
    }
    
    private fun getBodyweightDays(): List<ProgramDay> {
        return listOf(
            ProgramDay(id = 18, programId = 5, name = "Full Body A", dayNumber = 1, weekNumber = 1),
            ProgramDay(id = 19, programId = 5, name = "Full Body B", dayNumber = 2, weekNumber = 1),
            ProgramDay(id = 20, programId = 5, name = "Full Body C", dayNumber = 3, weekNumber = 1)
        )
    }
}
