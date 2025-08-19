@file:Suppress("UNRESOLVED_REFERENCE")
package com.chilluminati.rackedup.data.repository

import android.content.Context
import com.chilluminati.rackedup.data.database.RackedUpDatabase
import com.chilluminati.rackedup.data.database.entity.*
import com.chilluminati.rackedup.di.IoDispatcher
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import org.json.JSONObject
import org.json.JSONArray
import java.io.*
import okhttp3.OkHttpClient
import okhttp3.Request
import java.text.SimpleDateFormat
import java.util.*
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository for data management operations including backup, export, and import
 */
@Singleton
class DataManagementRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val database: RackedUpDatabase,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {
    
    /**
     * Data class for workout with exercises and sets
     */
    data class WorkoutWithDetails(
        val workout: Workout,
        val workoutExercises: List<WorkoutExercise>
    )

    /**
     * Create JSON backup string
     */
    private fun createBackupJson(
        userProfiles: List<UserProfile>,
        exercises: List<Exercise>, 
        workouts: List<Workout>,
        workoutExercises: List<WorkoutExercise>,
        exerciseSets: List<ExerciseSet>,
        programs: List<Program>,
        programDays: List<ProgramDay>,
        programExercises: List<ProgramExercise>,
        personalRecords: List<PersonalRecord>,
        bodyMeasurements: List<BodyMeasurement>
    ): String {
        val root = JSONObject().apply {
            put("version", "1.0")
            put("exportDate", SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US).format(Date()))
        }

        fun <T> JSONArray.addAll(items: List<T>, toJson: (T) -> JSONObject) {
            items.forEach { put(toJson(it)) }
        }

        root.put("userProfiles", JSONArray().apply {
            addAll(userProfiles) { u -> JSONObject().apply {
                put("id", u.id); put("name", u.name); put("isActive", u.isActive); put("createdAt", u.createdAt.time)
            } }
        })

        root.put("exercises", JSONArray().apply {
            addAll(exercises) { e -> JSONObject().apply {
                put("id", e.id); put("name", e.name); put("category", e.category)
                put("equipment", e.equipment); put("exerciseType", e.exerciseType); put("difficultyLevel", e.difficultyLevel)
                put("instructions", e.instructions ?: ""); put("isCustom", e.isCustom)
                put("muscleGroups", JSONArray(e.muscleGroups))
            } }
        })

        root.put("workouts", JSONArray().apply {
            addAll(workouts) { w -> JSONObject().apply {
                put("id", w.id); put("name", w.name); put("date", w.date.time); put("notes", w.notes)
                put("programId", w.programId); put("programDayId", w.programDayId)
            } }
        })

        root.put("workoutExercises", JSONArray().apply {
            addAll(workoutExercises) { we -> JSONObject().apply {
                put("id", we.id); put("workoutId", we.workoutId); put("exerciseId", we.exerciseId); put("orderIndex", we.orderIndex)
            } }
        })

        root.put("exerciseSets", JSONArray().apply {
            addAll(exerciseSets) { s -> JSONObject().apply {
                put("id", s.id); put("workoutExerciseId", s.workoutExerciseId); put("setNumber", s.setNumber)
                put("weight", s.weight); put("reps", s.reps); put("restTimeSeconds", s.restTimeSeconds); put("notes", s.notes)
            } }
        })

        root.put("programs", JSONArray().apply {
            addAll(programs) { p -> JSONObject().apply {
                put("id", p.id); put("name", p.name); put("description", p.description)
                put("durationWeeks", p.durationWeeks); put("difficultyLevel", p.difficultyLevel)
                put("programType", p.programType); put("daysPerWeek", p.daysPerWeek)
                put("goal", p.goal); put("author", p.author); put("isCustom", p.isCustom)
                put("isTemplate", p.isTemplate); put("isActive", p.isActive); put("isFavorite", p.isFavorite)
                put("currentWeek", p.currentWeek); put("currentDay", p.currentDay)
                put("progressionScheme", p.progressionScheme); put("deloadWeek", p.deloadWeek)
                put("notes", p.notes); put("startDate", p.startDate?.time); put("endDate", p.endDate?.time)
            } }
        })

        root.put("programDays", JSONArray().apply {
            addAll(programDays) { pd -> JSONObject().apply {
                put("id", pd.id); put("programId", pd.programId); put("dayNumber", pd.dayNumber)
                put("weekNumber", pd.weekNumber); put("name", pd.name); put("description", pd.description)
                put("dayType", pd.dayType); put("muscleGroups", JSONArray(pd.muscleGroups))
                put("estimatedDuration", pd.estimatedDuration); put("isRestDay", pd.isRestDay)
                put("notes", pd.notes); put("createdAt", pd.createdAt.time)
            } }
        })

        root.put("programExercises", JSONArray().apply {
            addAll(programExercises) { pe -> JSONObject().apply {
                put("id", pe.id); put("programDayId", pe.programDayId); put("exerciseId", pe.exerciseId); put("orderIndex", pe.orderIndex)
                put("sets", pe.sets); put("reps", pe.reps); put("weightPercentage", pe.weightPercentage)
                put("restTimeSeconds", pe.restTimeSeconds); put("rpeTarget", pe.rpeTarget); put("progressionScheme", pe.progressionScheme)
                put("progressionIncrement", pe.progressionIncrement); put("isSuperset", pe.isSuperset); put("supersetId", pe.supersetId)
                put("notes", pe.notes); put("createdAt", pe.createdAt.time)
            } }
        })

        root.put("personalRecords", JSONArray().apply {
            addAll(personalRecords) { pr -> JSONObject().apply {
                put("id", pr.id); put("exerciseId", pr.exerciseId); put("recordType", pr.recordType)
                put("weight", pr.weight); put("reps", pr.reps); put("distance", pr.distance); put("durationSeconds", pr.durationSeconds)
                put("volume", pr.volume); put("estimated1RM", pr.estimated1RM); put("previousValue", pr.previousValue); put("improvement", pr.improvement)
                put("workoutId", pr.workoutId); put("notes", pr.notes); put("achievedAt", pr.achievedAt.time); put("createdAt", pr.createdAt.time)
            } }
        })

        root.put("bodyMeasurements", JSONArray().apply {
            addAll(bodyMeasurements) { bm -> JSONObject().apply {
                put("id", bm.id); put("measurementType", bm.measurementType); put("value", bm.value); put("unit", bm.unit)
                put("bodyPart", bm.bodyPart); put("measurementMethod", bm.measurementMethod); put("notes", bm.notes); put("photoUrl", bm.photoUrl)
                put("measuredAt", bm.measuredAt.time); put("createdAt", bm.createdAt.time)
            } }
        })

        return root.toString()
    }

    /**
     * Export complete app data as ZIP backup
     */
    suspend fun exportCompleteBackup(outputStream: OutputStream): Result<String> = withContext(ioDispatcher) {
        try {
            val userProfiles = database.userProfileDao().getAllProfiles()
            val exercises = database.exerciseDao().getAllExercisesList()
            val workouts = database.workoutDao().getAllWorkoutsList()
            val workoutExercises = database.workoutExerciseDao().getAllWorkoutExercises()
            val exerciseSets = database.exerciseSetDao().getAllExerciseSets()
            val programs = database.programDao().getAllProgramsList()
            val programDays = database.programDayDao().getAllProgramDays()
            val programExercises = database.programExerciseDao().getAllProgramExercises()
            val personalRecords = database.personalRecordDao().getAllPersonalRecords()
            val bodyMeasurements = database.bodyMeasurementDao().getAllBodyMeasurements()

            ZipOutputStream(outputStream).use { zipOut ->
                // Add main data file
                zipOut.putNextEntry(ZipEntry("rackedup_backup.json"))
                val jsonData = createBackupJson(
                    userProfiles, exercises, workouts, workoutExercises, exerciseSets,
                    programs, programDays, programExercises, personalRecords, bodyMeasurements
                )
                zipOut.write(jsonData.toByteArray())
                zipOut.closeEntry()
                
                // Add metadata file
                zipOut.putNextEntry(ZipEntry("metadata.txt"))
                val metadata = """
                    RackedUp Backup
                    Version: 1.0
                    Export Date: ${SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US).format(Date())}
                    Total Workouts: ${workouts.size}
                    Total Exercises: ${exercises.size}
                    Total Programs: ${programs.size}
                """.trimIndent()
                zipOut.write(metadata.toByteArray())
                zipOut.closeEntry()
            }

            Result.success("Backup exported successfully with ${workouts.size} workouts and ${exercises.size} exercises")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Import complete app data from ZIP backup
     */
    suspend fun importCompleteBackup(inputStream: InputStream): Result<String> = withContext(ioDispatcher) {
        try {
            var backupJson: String? = null
            
            ZipInputStream(inputStream).use { zipIn ->
                var entry = zipIn.nextEntry
                while (entry != null) {
                    if (entry.name == "rackedup_backup.json") {
                        backupJson = zipIn.readBytes().toString(Charsets.UTF_8)
                        break
                    }
                    entry = zipIn.nextEntry
                }
            }

            backupJson?.let { jsonData ->
                val root = JSONObject(jsonData)
                
                // Safety check: Log the operation for debugging
                android.util.Log.w("DataManagementRepository", "IMPORT BACKUP OPERATION - This will clear existing data and replace with backup!")
                
                // Check existing data before clearing
                val existingWorkoutCount = database.workoutDao().getAllWorkoutsList().size
                val existingExerciseCount = database.exerciseDao().getAllExercisesList().size
                android.util.Log.i("DataManagementRepository", "Existing data before import - Workouts: $existingWorkoutCount, Exercises: $existingExerciseCount")
                
                // Overwrite strategy: clear tables that are fully represented, then reinsert data
                database.clearData()

                fun JSONArray.toList(): List<JSONObject> = (0 until length()).map { getJSONObject(it) }

                // Exercises
                root.optJSONArray("exercises")?.toList()?.let { arr ->
                    val list = arr.map { o ->
                        Exercise(
                            id = o.optLong("id", 0),
                            name = o.getString("name"),
                            category = o.getString("category"),
                            equipment = o.getString("equipment"),
                            exerciseType = o.getString("exerciseType"),
                            difficultyLevel = o.getString("difficultyLevel"),
                            instructions = if (o.isNull("instructions")) null else o.optString("instructions"),
                            muscleGroups = listOf(),
                            isCustom = o.optBoolean("isCustom", false)
                        )
                    }
                    database.exerciseDao().insertExercises(list)
                }

                // Workouts
                root.optJSONArray("workouts")?.toList()?.let { arr ->
                    val list = arr.map { o ->
                        Workout(
                            id = o.optLong("id", 0),
                            name = o.getString("name"),
                            date = Date(o.getLong("date")),
                            notes = if (o.has("notes") && !o.isNull("notes")) o.getString("notes") else null,
                            programId = if (o.has("programId") && !o.isNull("programId")) o.getLong("programId") else null,
                            programDayId = if (o.has("programDayId") && !o.isNull("programDayId")) o.getLong("programDayId") else null
                        )
                    }
                    database.workoutDao().insertWorkouts(list)
                }

                // Workout exercises
                root.optJSONArray("workoutExercises")?.toList()?.let { arr ->
                    val list = arr.map { o ->
                        WorkoutExercise(
                            id = o.optLong("id", 0),
                            workoutId = o.getLong("workoutId"),
                            exerciseId = o.getLong("exerciseId"),
                            orderIndex = o.getInt("orderIndex")
                        )
                    }
                    database.workoutExerciseDao().insertWorkoutExercises(list)
                }

                // Sets
                root.optJSONArray("exerciseSets")?.toList()?.let { arr ->
                    val list = arr.map { o ->
                        ExerciseSet(
                            id = o.optLong("id", 0),
                            workoutExerciseId = o.getLong("workoutExerciseId"),
                            setNumber = o.getInt("setNumber"),
                            weight = if (o.isNull("weight")) null else o.getDouble("weight"),
                            reps = if (o.isNull("reps")) null else o.getInt("reps"),
                            restTimeSeconds = if (o.isNull("restTimeSeconds")) null else o.getInt("restTimeSeconds"),
                            notes = if (o.has("notes") && !o.isNull("notes")) o.getString("notes") else null
                        )
                    }
                    database.exerciseSetDao().insertExerciseSets(list)
                }

                // Programs
                root.optJSONArray("programs")?.toList()?.let { arr ->
                    val list = arr.map { o ->
                        Program(
                            id = o.optLong("id",0),
                            name = o.getString("name"),
                            description = if (o.isNull("description")) null else o.optString("description"),
                            durationWeeks = if (o.isNull("durationWeeks")) null else o.getInt("durationWeeks"),
                            difficultyLevel = o.optString("difficultyLevel", ""),
                            programType = o.optString("programType", ""),
                            daysPerWeek = o.optInt("daysPerWeek", 0),
                            goal = if (o.isNull("goal")) null else o.optString("goal"),
                            author = if (o.isNull("author")) null else o.optString("author"),
                            isCustom = o.optBoolean("isCustom", false),
                            isTemplate = o.optBoolean("isTemplate", false),
                            isActive = o.optBoolean("isActive", false),
                            isFavorite = o.optBoolean("isFavorite", false),
                            currentWeek = o.optInt("currentWeek", 1),
                            currentDay = o.optInt("currentDay", 1),
                            progressionScheme = if (o.isNull("progressionScheme")) null else o.optString("progressionScheme"),
                            deloadWeek = if (o.isNull("deloadWeek")) null else o.getInt("deloadWeek"),
                            notes = if (o.isNull("notes")) null else o.optString("notes"),
                            startDate = if (o.isNull("startDate")) null else Date(o.getLong("startDate")),
                            endDate = if (o.isNull("endDate")) null else Date(o.getLong("endDate"))
                        )
                    }
                    database.programDao().insertPrograms(list)
                }

                // Program days
                root.optJSONArray("programDays")?.toList()?.let { arr ->
                    val list = arr.map { o ->
                        ProgramDay(
                            id = o.optLong("id",0),
                            programId = o.getLong("programId"),
                            dayNumber = o.getInt("dayNumber"),
                            weekNumber = o.optInt("weekNumber", 1),
                            name = o.optString("name", ""),
                            description = if (o.isNull("description")) null else o.optString("description"),
                            dayType = if (o.isNull("dayType")) null else o.optString("dayType"),
                            muscleGroups = emptyList(),
                            estimatedDuration = if (o.isNull("estimatedDuration")) null else o.getInt("estimatedDuration"),
                            isRestDay = o.optBoolean("isRestDay", false),
                            notes = if (o.isNull("notes")) null else o.optString("notes")
                        )
                    }
                    database.programDayDao().insertProgramDays(list)
                }

                // Program exercises
                root.optJSONArray("programExercises")?.toList()?.let { arr ->
                    val list = arr.map { o ->
                        ProgramExercise(
                            id = o.optLong("id",0),
                            programDayId = o.getLong("programDayId"),
                            exerciseId = o.getLong("exerciseId"),
                            orderIndex = o.getInt("orderIndex"),
                            sets = o.optString("sets", ""),
                            reps = if (o.isNull("reps")) null else o.optString("reps"),
                            weightPercentage = if (o.isNull("weightPercentage")) null else o.getDouble("weightPercentage"),
                            restTimeSeconds = if (o.isNull("restTimeSeconds")) null else o.getInt("restTimeSeconds"),
                            rpeTarget = if (o.isNull("rpeTarget")) null else o.getInt("rpeTarget"),
                            progressionScheme = if (o.isNull("progressionScheme")) null else o.optString("progressionScheme"),
                            progressionIncrement = if (o.isNull("progressionIncrement")) null else o.getDouble("progressionIncrement"),
                            isSuperset = o.optBoolean("isSuperset", false),
                            supersetId = if (o.isNull("supersetId")) null else o.optString("supersetId"),
                            notes = if (o.isNull("notes")) null else o.optString("notes")
                        )
                    }
                    database.programExerciseDao().insertProgramExercises(list)
                }

                // Personal records
                root.optJSONArray("personalRecords")?.toList()?.let { arr ->
                    val list = arr.map { o ->
                        PersonalRecord(
                            id = o.optLong("id",0),
                            exerciseId = o.getLong("exerciseId"),
                            recordType = o.optString("recordType", ""),
                            weight = if (o.isNull("weight")) null else o.getDouble("weight"),
                            reps = if (o.isNull("reps")) null else o.getInt("reps"),
                            distance = if (o.isNull("distance")) null else o.getDouble("distance"),
                            durationSeconds = if (o.isNull("durationSeconds")) null else o.getInt("durationSeconds"),
                            volume = if (o.isNull("volume")) null else o.getDouble("volume"),
                            estimated1RM = if (o.isNull("estimated1RM")) null else o.getDouble("estimated1RM"),
                            previousValue = if (o.isNull("previousValue")) null else o.getDouble("previousValue"),
                            improvement = if (o.isNull("improvement")) null else o.getDouble("improvement"),
                            workoutId = if (o.isNull("workoutId")) null else o.getLong("workoutId"),
                            notes = if (o.isNull("notes")) null else o.optString("notes"),
                            achievedAt = Date(o.getLong("achievedAt"))
                        )
                    }
                    database.personalRecordDao().insertPersonalRecords(list)
                }

                // Body measurements
                root.optJSONArray("bodyMeasurements")?.toList()?.let { arr ->
                    val list = arr.map { o ->
                        BodyMeasurement(
                            id = o.optLong("id",0),
                            measurementType = o.getString("measurementType"),
                            value = o.getDouble("value"),
                            unit = o.getString("unit"),
                            bodyPart = if (o.isNull("bodyPart")) null else o.optString("bodyPart"),
                            measurementMethod = if (o.isNull("measurementMethod")) null else o.optString("measurementMethod"),
                            notes = if (o.isNull("notes")) null else o.optString("notes"),
                            photoUrl = if (o.isNull("photoUrl")) null else o.optString("photoUrl"),
                            measuredAt = Date(o.getLong("measuredAt"))
                        )
                    }
                    database.bodyMeasurementDao().insertBodyMeasurements(list)
                }

                Result.success("Backup imported successfully")
            } ?: Result.failure(IllegalArgumentException("Invalid backup file format"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Get all workouts with their exercises
     */
    private suspend fun getAllWorkoutsWithDetails(): List<WorkoutWithDetails> {
        val workouts = database.workoutDao().getAllWorkoutsList()
        return workouts.map { workout ->
            val workoutExercises = database.workoutExerciseDao().getAllWorkoutExercises()
                .filter { it.workoutId == workout.id }
            WorkoutWithDetails(workout, workoutExercises)
        }
    }

    /**
     * Export workout history as CSV
     */
    suspend fun exportWorkoutHistoryCSV(outputStream: OutputStream): Result<String> = withContext(ioDispatcher) {
        try {
            val workouts = getAllWorkoutsWithDetails()
            var totalSets = 0
            
            outputStream.bufferedWriter().use { writer ->
                // CSV Header
                writer.write("Date,Workout Name,Exercise,Set Number,Weight,Reps,Rest Time,Notes\n")
                
                workouts.forEach { workout ->
                    workout.workoutExercises.forEach { workoutExercise ->
                        val exercise = database.exerciseDao().getExerciseById(workoutExercise.exerciseId)
                        val sets = database.exerciseSetDao().getSetsByWorkoutExerciseId(workoutExercise.id)
                        
                        sets.forEachIndexed { index, set ->
                            writer.write("${SimpleDateFormat("yyyy-MM-dd", Locale.US).format(workout.workout.date)},")
                            writer.write("\"${workout.workout.name}\",")
                            writer.write("\"${exercise?.name ?: "Unknown Exercise"}\",")
                            writer.write("${index + 1},")
                            writer.write("${set.weight ?: ""},")
                            writer.write("${set.reps ?: ""},")
                            writer.write("${set.restTimeSeconds ?: ""},")
                            writer.write("\"${set.notes ?: ""}\"\n")
                            totalSets++
                        }
                    }
                }
            }

            Result.success("CSV exported successfully with $totalSets sets from ${workouts.size} workouts")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Export exercise library as JSON
     */
    suspend fun exportExerciseLibrary(outputStream: OutputStream): Result<String> = withContext(ioDispatcher) {
        try {
            val exercises = database.exerciseDao().getAllExercisesList()
            val jsonArray = JSONArray()
            
            exercises.forEach { exercise ->
                val exerciseJson = JSONObject().apply {
                    put("id", exercise.id)
                    put("name", exercise.name)
                    put("category", exercise.category)
                    put("equipment", exercise.equipment)
                    put("exerciseType", exercise.exerciseType)
                    put("difficultyLevel", exercise.difficultyLevel)
                    put("instructions", exercise.instructions ?: "")
                    put("isCustom", exercise.isCustom)
                }
                jsonArray.put(exerciseJson)
            }
            
            outputStream.write(jsonArray.toString().toByteArray())
            
            Result.success("Exercise library exported successfully with ${exercises.size} exercises")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Import workout data from CSV
     */
    suspend fun importWorkoutDataCSV(inputStream: InputStream): Result<String> = withContext(ioDispatcher) {
        try {
            val reader = inputStream.bufferedReader()
            val lines = reader.readLines()
            
            if (lines.isEmpty()) {
                return@withContext Result.failure(IllegalArgumentException("CSV file is empty"))
            }
            
            // Skip header
            val dataLines = lines.drop(1)
            var importedWorkouts = 0
            var importedSets = 0
            
            // Group by date and workout name
            val workoutGroups = dataLines.groupBy { line ->
                val parts = line.split(",")
                if (parts.size >= 2) "${parts[0]}-${parts[1].trim('\"')}" else ""
            }
            
            workoutGroups.forEach { (key, lines) ->
                if (key.isNotEmpty() && lines.isNotEmpty()) {
                    val firstLine = lines.first().split(",")
                    val dateStr = firstLine[0]
                    val workoutName = firstLine[1].trim('\"')
                    
                    // Create workout
                    val date = SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(dateStr) ?: Date()
                    val workout = Workout(
                        name = workoutName,
                        date = date,
                        notes = "Imported from CSV"
                    )
                    val workoutId = database.workoutDao().insertWorkout(workout)
                    importedWorkouts++
                    
                    // Process exercises for this workout
                    val exerciseGroups = lines.groupBy { it.split(",")[2].trim('\"') }
                    exerciseGroups.forEach { (exerciseName, exerciseLines) ->
                        // Find or create exercise
                        var exercise = database.exerciseDao().getExerciseByName(exerciseName)
                        if (exercise == null) {
                            exercise = Exercise(
                                name = exerciseName,
                                category = "Imported",
                                equipment = "Unknown",
                                exerciseType = "Strength",
                                difficultyLevel = "Beginner",
                                muscleGroups = listOf("Unknown")
                            )
                            val exerciseId = database.exerciseDao().insertExercise(exercise)
                            exercise = exercise.copy(id = exerciseId)
                        }
                        
                        // Create workout exercise
                        val workoutExercise = WorkoutExercise(
                            workoutId = workoutId,
                            exerciseId = exercise.id,
                            orderIndex = 0
                        )
                        val workoutExerciseId = database.workoutExerciseDao().insertWorkoutExercise(workoutExercise)
                        
                        // Create sets
                        exerciseLines.forEach { line ->
                            val parts = line.split(",")
                            if (parts.size >= 6) {
                                val weight = parts[4].toDoubleOrNull()
                                val reps = parts[5].toIntOrNull()
                                val restTime = if (parts.size > 6) parts[6].toIntOrNull() else null
                                val notes = if (parts.size > 7) parts[7].trim('\"') else null
                                
                                val set = ExerciseSet(
                                    workoutExerciseId = workoutExerciseId,
                                    setNumber = importedSets + 1,
                                    weight = weight,
                                    reps = reps,
                                    restTimeSeconds = restTime,
                                    notes = notes
                                )
                                database.exerciseSetDao().insertExerciseSet(set)
                                importedSets++
                            }
                        }
                    }
                }
            }
            
            Result.success("CSV imported successfully: $importedWorkouts workouts, $importedSets sets")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Import exercise library from JSON (the same format produced by exportExerciseLibrary)
     */
    suspend fun importExerciseLibrary(inputStream: InputStream): Result<String> = withContext(ioDispatcher) {
        return@withContext try {
            val json = inputStream.bufferedReader().readText()
            val arr = JSONArray(json)
            val toInsert = mutableListOf<Exercise>()
            for (i in 0 until arr.length()) {
                val o = arr.getJSONObject(i)
                val exercise = Exercise(
                    name = o.optString("name", ""),
                    category = o.optString("category", ""),
                    equipment = o.optString("equipment", ""),
                    exerciseType = o.optString("exerciseType", "Strength"),
                    difficultyLevel = o.optString("difficultyLevel", "Beginner"),
                    muscleGroups = emptyList(),
                    instructions = if (o.isNull("instructions")) null else o.optString("instructions"),
                    isCustom = o.optBoolean("isCustom", false)
                )
                toInsert.add(exercise)
            }
            database.exerciseDao().clearAll()
            database.exerciseDao().insertExercises(toInsert)
            Result.success("Exercise library imported: ${toInsert.size} exercises (replaced existing)")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /** Import Free Exercise DB JSON format and map to our schema. baseImageUrl can be GitHub raw or assets URI. */
    suspend fun importFreeExerciseDbJson(
        inputStream: InputStream,
        baseImageUrl: String = "https://raw.githubusercontent.com/yuhonas/free-exercise-db/main/exercises/"
    ): Result<String> = withContext(ioDispatcher) {
        return@withContext try {
            val json = inputStream.bufferedReader().readText()
            val arr = JSONArray(json)
            val toInsert = mutableListOf<Exercise>()
            for (i in 0 until arr.length()) {
                val o = arr.getJSONObject(i)
                val sourceId = if (o.isNull("id")) null else o.optString("id")
                val name = o.optString("name", sourceId ?: "")
                val category = o.optString("category", "")
                val equipment = o.optString("equipment", "")
                val level = o.optString("level", "Beginner")
                val force = if (o.isNull("force")) null else o.optString("force")
                val mechanic = if (o.isNull("mechanic")) null else o.optString("mechanic")

                val primary = o.optJSONArray("primaryMuscles")?.let { ja ->
                    (0 until ja.length()).mapNotNull { idx ->
                        val s = ja.optString(idx)
                        if (s.isNullOrEmpty()) null else s
                    }
                } ?: emptyList()
                val secondary = o.optJSONArray("secondaryMuscles")?.let { ja ->
                    (0 until ja.length()).mapNotNull { idx ->
                        val s = ja.optString(idx)
                        if (s.isNullOrEmpty()) null else s
                    }
                } ?: emptyList()
                val steps = o.optJSONArray("instructions")?.let { ja ->
                    (0 until ja.length()).mapNotNull { idx ->
                        val s = ja.optString(idx)
                        if (s.isNullOrEmpty()) null else s
                    }
                } ?: emptyList()
                val images = o.optJSONArray("images")?.let { ja ->
                    (0 until ja.length()).mapNotNull { idx ->
                        val s = ja.optString(idx)
                        if (s.isNullOrEmpty()) null else s
                    }
                } ?: emptyList()
                val imageUrls = images.map { path -> baseImageUrl + path }
                val instructionsText = if (steps.isNotEmpty()) steps.mapIndexed { idx, s -> "${idx + 1}. $s" }.joinToString("\n") else null

                // Map category to exercise type
                val exerciseType = when (category.lowercase()) {
                    "cardio" -> "Cardio"
                    "stretching", "olympic weightlifting" -> "Other"
                    else -> "Strength"
                }

                // Ensure required fields have default values
                val exercise = Exercise(
                    sourceId = sourceId,
                    name = name.ifEmpty { "Unknown Exercise" },
                    category = category.ifEmpty { "Other" },
                    equipment = equipment.ifEmpty { "None" },
                    exerciseType = exerciseType,
                    difficultyLevel = level.replaceFirstChar { it.titlecase() },
                    instructions = instructionsText,
                    instructionSteps = steps.ifEmpty { listOf("No instructions available") },
                    muscleGroups = primary.ifEmpty { listOf("General") },
                    secondaryMuscles = secondary,
                    isCompound = mechanic.equals("compound", ignoreCase = true),
                    isUnilateral = false,
                    isCustom = false,
                    isFavorite = false,
                    imageUrl = imageUrls.firstOrNull(),
                    videoUrl = null,
                    force = force,
                    mechanic = mechanic,
                    imagePaths = imageUrls.ifEmpty { listOf("placeholder.png") }
                )
                toInsert.add(exercise)
            }
            database.exerciseDao().clearAll()
            database.exerciseDao().insertExercises(toInsert)
            Result.success("Free Exercise DB imported: ${toInsert.size} exercises (replaced existing)")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /** Download and import Free Exercise DB from remote */
    suspend fun importFreeExerciseDbFromRemote(): Result<String> = withContext(ioDispatcher) {
        return@withContext try {
            val client = OkHttpClient()
            val request = Request.Builder()
                .url("https://raw.githubusercontent.com/yuhonas/free-exercise-db/main/dist/exercises.json")
                .build()
            val response = client.newCall(request).execute()
            if (!response.isSuccessful) throw IOException("Unexpected code ${response.code}")
            response.body?.byteStream()?.use { stream ->
                importFreeExerciseDbJson(stream)
            } ?: Result.failure(IllegalStateException("Empty body"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Get app storage usage information
     */
    suspend fun getStorageUsage(): StorageInfo = withContext(ioDispatcher) {
        val dbFile = File(context.getDatabasePath("rackedup_database").absolutePath)
        val cacheDir = context.cacheDir
        val filesDir = context.filesDir
        
        StorageInfo(
            databaseSize = if (dbFile.exists()) dbFile.length() else 0L,
            cacheSize = cacheDir.walkTopDown().filter { it.isFile }.map { it.length() }.sum(),
            filesSize = filesDir.walkTopDown().filter { it.isFile }.map { it.length() }.sum(),
            totalSize = dbFile.length() + cacheDir.walkTopDown().filter { it.isFile }.map { it.length() }.sum() + 
                       filesDir.walkTopDown().filter { it.isFile }.map { it.length() }.sum()
        )
    }

    /**
     * Clear app cache
     */
    suspend fun clearCache(): Result<String> = withContext(ioDispatcher) {
        try {
            val cacheDir = context.cacheDir
            val deletedFiles = cacheDir.walkTopDown().filter { it.isFile }.count()
            cacheDir.deleteRecursively()
            Result.success("Cache cleared: $deletedFiles files deleted")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Reset all app data (dangerous operation)
     */
    suspend fun resetAllData(): Result<String> = withContext(ioDispatcher) {
        try {
            // Safety check: Log the operation for debugging
            android.util.Log.w("DataManagementRepository", "RESET ALL DATA OPERATION INITIATED - This will delete all user data!")
            
            // Check if there's any user data before clearing
            val workoutCount = database.workoutDao().getAllWorkoutsList().size
            val exerciseCount = database.exerciseDao().getAllExercisesList().size
            val profileCount = database.userProfileDao().getProfileCount()
            
            android.util.Log.i("DataManagementRepository", "Data before reset - Workouts: $workoutCount, Exercises: $exerciseCount, Profiles: $profileCount")
            
            // Only proceed if this is explicitly called from the data management screen
            // (This is a safety measure - the UI should have proper confirmation dialogs)
            
            database.clearData()
            context.cacheDir.deleteRecursively()
            
            android.util.Log.w("DataManagementRepository", "All app data has been reset successfully")
            Result.success("All app data has been reset")
        } catch (e: Exception) {
            android.util.Log.e("DataManagementRepository", "Failed to reset data", e)
            Result.failure(e)
        }
    }

    /**
     * Data class for storage information
     */
    data class StorageInfo(
        val databaseSize: Long,
        val cacheSize: Long,
        val filesSize: Long,
        val totalSize: Long
    ) {
        fun getDatabaseSizeMB(): String = "%.2f MB".format(databaseSize / 1024.0 / 1024.0)
        fun getCacheSizeMB(): String = "%.2f MB".format(cacheSize / 1024.0 / 1024.0)
        fun getFilesSizeMB(): String = "%.2f MB".format(filesSize / 1024.0 / 1024.0)
        fun getTotalSizeMB(): String = "%.2f MB".format(totalSize / 1024.0 / 1024.0)
    }
}
