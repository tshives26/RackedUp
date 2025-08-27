package com.chilluminati.rackedup.presentation.progress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chilluminati.rackedup.data.repository.ProgressRepository
import com.chilluminati.rackedup.data.repository.WorkoutRepository
import com.chilluminati.rackedup.data.repository.SettingsRepository
import com.chilluminati.rackedup.data.repository.ProgramRepository
import com.chilluminati.rackedup.data.repository.AchievementsRepository
import com.chilluminati.rackedup.data.database.entity.PersonalRecord
import com.chilluminati.rackedup.data.database.entity.BodyMeasurement
import com.chilluminati.rackedup.data.database.entity.Workout
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

/**
 * ViewModel for the Progress screen
 */
@HiltViewModel
class ProgressViewModel @Inject constructor(
    private val progressRepository: ProgressRepository,
    private val workoutRepository: WorkoutRepository,
    private val settingsRepository: SettingsRepository,
    private val programRepository: ProgramRepository,
    private val achievementsRepository: AchievementsRepository
) : ViewModel() {

    // Volume data for the past 30 days
    private val _volumeData = MutableStateFlow<List<Pair<Date, Double>>>(emptyList())
    val volumeData: StateFlow<List<Pair<Date, Double>>> = _volumeData.asStateFlow()

    // Strength data (1RM) for major lifts
    private val _strengthData = MutableStateFlow<List<Pair<Date, Map<String, Double>>>>(emptyList())
    val strengthData: StateFlow<List<Pair<Date, Map<String, Double>>>> = _strengthData.asStateFlow()

    // Universal strength metrics
    private val _universalStrengthData = MutableStateFlow<List<Pair<Date, Double>>>(emptyList())
    val universalStrengthData: StateFlow<List<Pair<Date, Double>>> = _universalStrengthData.asStateFlow()

    // Volume load data
    private val _volumeLoadData = MutableStateFlow<List<Pair<Date, Double>>>(emptyList())
    val volumeLoadData: StateFlow<List<Pair<Date, Double>>> = _volumeLoadData.asStateFlow()

    // Workout density data
    private val _workoutDensityData = MutableStateFlow<List<Pair<Date, Double>>>(emptyList())
    val workoutDensityData: StateFlow<List<Pair<Date, Double>>> = _workoutDensityData.asStateFlow()

    // Workout efficiency data
    private val _workoutEfficiencyData = MutableStateFlow<List<Pair<Date, Double>>>(emptyList())
    val workoutEfficiencyData: StateFlow<List<Pair<Date, Double>>> = _workoutEfficiencyData.asStateFlow()

    // Progression data
    private val _progressionData = MutableStateFlow<List<Pair<Date, Double>>>(emptyList())
    val progressionData: StateFlow<List<Pair<Date, Double>>> = _progressionData.asStateFlow()

    // Weekly progress data
    private val _weeklyProgressData = MutableStateFlow<List<Pair<Date, Double>>>(emptyList())
    val weeklyProgressData: StateFlow<List<Pair<Date, Double>>> = _weeklyProgressData.asStateFlow()

    // Exercise variety data
    private val _exerciseVarietyData = MutableStateFlow<List<Pair<Date, Int>>>(emptyList())
    val exerciseVarietyData: StateFlow<List<Pair<Date, Int>>> = _exerciseVarietyData.asStateFlow()

    // Muscle group variety data
    private val _muscleGroupVarietyData = MutableStateFlow<List<Pair<Date, Int>>>(emptyList())
    val muscleGroupVarietyData: StateFlow<List<Pair<Date, Int>>> = _muscleGroupVarietyData.asStateFlow()

    // Body measurements data
    private val _measurementData = MutableStateFlow<List<Pair<Date, Map<String, Double>>>>(emptyList())
    val measurementData: StateFlow<List<Pair<Date, Map<String, Double>>>> = _measurementData.asStateFlow()

    // Personal records
    private val _personalRecords = MutableStateFlow<List<PersonalRecord>>(emptyList())
    val personalRecords: StateFlow<List<PersonalRecord>> = _personalRecords.asStateFlow()

    // Volume-based personal records
    private val _volumeBasedPersonalRecords = MutableStateFlow<List<com.chilluminati.rackedup.data.repository.VolumeBasedPersonalRecord>>(emptyList())
    val volumeBasedPersonalRecords: StateFlow<List<com.chilluminati.rackedup.data.repository.VolumeBasedPersonalRecord>> = _volumeBasedPersonalRecords.asStateFlow()

    // Loading states
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Weight unit preference for formatting
    val weightUnit: StateFlow<String> = settingsRepository.weightUnit.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = "kg"
    )

    init {
        loadProgressData()
        observeWorkoutHistory()
        // Fix any existing workouts that might have missing totals
        fixExistingWorkoutTotals()
        observeConsistency()
        observeAchievements()
        // Initialize PRs from historical data if needed
        initializePRsFromHistory()
        // Clean up any duplicate volume records
        cleanupDuplicateVolumeRecords()
        observeAggregateStats()
    }

    private fun loadProgressData() {
        viewModelScope.launch {
            try {
                // Load volume data using available method
                val volumePoints = progressRepository.getWorkoutVolumeOverTime(30).first()
                _volumeData.value = volumePoints.map { point ->
                    point.date to point.volume
                }.sortedBy { it.first }

                // Load strength data (1RMs) - using placeholder data since methods aren't implemented yet
                val records = progressRepository.getPersonalRecords()
                val strengthMap = records.filter { it.recordType == "1RM" }
                    .groupBy { it.achievedAt }
                    .mapValues { entry ->
                        entry.value.associate { record ->
                            "Exercise ${record.exerciseId}" to (record.estimated1RM ?: 0.0)
                        }
                    }
                    .toList()
                    .sortedBy { it.first }
                
                _strengthData.value = strengthMap

                // Load body measurements - using placeholder data since methods aren't implemented yet
                val measurements = progressRepository.getBodyMeasurements()
                val measurementMap = measurements.groupBy { it.measuredAt }
                    .mapValues { entry ->
                        entry.value.associate { measurement ->
                            measurement.measurementType to measurement.value
                        }
                    }
                    .toList()
                    .sortedBy { it.first }
                
                _measurementData.value = measurementMap

                // Load personal records
                _personalRecords.value = records.sortedByDescending { it.achievedAt }

                // Load volume-based personal records
                val volumeRecords = progressRepository.getVolumeBasedPersonalRecords()
                _volumeBasedPersonalRecords.value = volumeRecords

                // Load universal strength metrics
                val universalStrengthPoints = progressRepository.getUniversalStrengthMetrics(30).first()
                _universalStrengthData.value = universalStrengthPoints.map { point ->
                    point.date to point.relativeStrength
                }
                _volumeLoadData.value = universalStrengthPoints.map { point ->
                    point.date to point.volumeLoad
                }

                // Load workout density metrics
                val densityPoints = progressRepository.getWorkoutDensityMetrics(30).first()
                _workoutDensityData.value = densityPoints.map { point ->
                    point.date to point.volumePerMinute
                }
                _workoutEfficiencyData.value = densityPoints.map { point ->
                    point.date to point.setsPerMinute
                }

                // Load progression metrics
                val progressionPoints = progressRepository.getProgressionMetrics(30).first()
                _progressionData.value = progressionPoints.map { point ->
                    point.date to point.improvementPercentage
                }
                _weeklyProgressData.value = progressionPoints.map { point ->
                    point.date to point.weeklyProgressRate
                }

                // Load exercise variety metrics
                val varietyPoints = progressRepository.getExerciseVarietyMetrics(30).first()
                _exerciseVarietyData.value = varietyPoints.map { point ->
                    point.date to point.uniqueExercises
                }
                _muscleGroupVarietyData.value = varietyPoints.map { point ->
                    point.date to point.muscleGroups
                }

            } catch (e: Exception) {
                // Handle any errors during data loading
                // For now, just log the error and continue with empty data
            }
        }
    }

    // Workout history (all saved workouts)
    private val _workoutHistory = MutableStateFlow<List<Workout>>(emptyList())
    val workoutHistory: StateFlow<List<Workout>> = _workoutHistory.asStateFlow()

    // History items ready for UI display (program/day names + formatted date)
    private val _workoutHistoryDisplay = MutableStateFlow<List<WorkoutHistoryDisplay>>(emptyList())
    val workoutHistoryDisplay: StateFlow<List<WorkoutHistoryDisplay>> = _workoutHistoryDisplay.asStateFlow()

    private fun observeWorkoutHistory() {
        viewModelScope.launch {
            workoutRepository.getAllWorkouts().collect { workouts ->
                _workoutHistory.value = workouts

                // Build UI items with program/day names and formatted date
                val displayItems = mutableListOf<WorkoutHistoryDisplay>()
                for (workout in workouts) {
                    val title = buildDisplayTitleForWorkout(workout)
                    val dateStr = formatDateShort(workout.date)
                    val durationStr = workout.durationMinutes?.let { "${it} min" } ?: ""
                    val setsText = "${workout.totalSets} sets"
                    displayItems.add(
                        WorkoutHistoryDisplay(
                            id = workout.id,
                            title = title,
                            date = dateStr,
                            duration = durationStr,
                            sets = setsText,
                            volume = workout.totalVolume
                        )
                    )
                }
                _workoutHistoryDisplay.value = displayItems
            }
        }
    }

    // Consistency (workouts per week)
    private val _consistencyData = MutableStateFlow<List<com.chilluminati.rackedup.data.repository.ConsistencyDataPoint>>(emptyList())
    val consistencyData: StateFlow<List<com.chilluminati.rackedup.data.repository.ConsistencyDataPoint>> = _consistencyData.asStateFlow()

    private fun observeConsistency() {
        viewModelScope.launch {
            progressRepository.getWorkoutConsistency(weeks = 12).collect { points ->
                _consistencyData.value = points
            }
        }
    }

    // Achievements
    private val _achievements = MutableStateFlow<List<AchievementsRepository.State>>(emptyList())
    val achievements: StateFlow<List<AchievementsRepository.State>> = _achievements.asStateFlow()

    private fun observeAchievements() {
        viewModelScope.launch {
            achievementsRepository.observeAchievements().collect { states ->
                _achievements.value = states
            }
        }
    }

    // Weekly and monthly stats, all-time totals, and streaks
    private val _weeklyStats = MutableStateFlow(WeeklyStats())
    val weeklyStats: StateFlow<WeeklyStats> = _weeklyStats.asStateFlow()

    private val _monthlyStats = MutableStateFlow(MonthlyStats())
    val monthlyStats: StateFlow<MonthlyStats> = _monthlyStats.asStateFlow()

    private val _totalAllTimeVolume = MutableStateFlow(0.0)
    val totalAllTimeVolume: StateFlow<Double> = _totalAllTimeVolume.asStateFlow()

    private val _currentStreak = MutableStateFlow(0)
    val currentStreak: StateFlow<Int> = _currentStreak.asStateFlow()

    private val _longestStreak = MutableStateFlow(0)
    val longestStreak: StateFlow<Int> = _longestStreak.asStateFlow()

    private fun observeAggregateStats() {
        viewModelScope.launch {
            workoutRepository.getAllWorkouts().collect { workouts ->
                refreshStats(workouts)
            }
        }
    }

    private fun refreshStats(workouts: List<Workout>) {
        _weeklyStats.value = calculateWeeklyStats(workouts)
        _monthlyStats.value = calculateMonthlyStats(workouts)
        _totalAllTimeVolume.value = workouts.sumOf { it.totalVolume }
        val (cur, longest) = computeStreaks(workouts)
        _currentStreak.value = cur
        _longestStreak.value = longest
        
        // Also refresh other data
        loadProgressData()
    }

    /**
     * Initialize personal records from historical workout data
     */
    private fun initializePRsFromHistory() {
        viewModelScope.launch {
            try {
                progressRepository.initializeVolumePRsFromHistory()
            } catch (e: Exception) {
                // Log error but don't crash the app
                e.printStackTrace()
            }
        }
    }

    /**
     * Clean up duplicate volume personal records
     */
    private fun cleanupDuplicateVolumeRecords() {
        viewModelScope.launch {
            try {
                progressRepository.cleanupDuplicateVolumeRecords()
            } catch (e: Exception) {
                // Log error but don't crash the app
                e.printStackTrace()
            }
        }
    }
    


    private fun calculateWeeklyStats(workouts: List<Workout>): WeeklyStats {
		// Derive weekEnd from weekStart to avoid locale-dependent issues where
		// Sunday may be considered the first day of the week, which can make
		// the computed end date precede the start date.
		val calendar = Calendar.getInstance()
		val start = calendar.apply {
			set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
			set(Calendar.HOUR_OF_DAY, 0)
			set(Calendar.MINUTE, 0)
			set(Calendar.SECOND, 0)
			set(Calendar.MILLISECOND, 0)
		}.time
		val end = Calendar.getInstance().apply {
			time = start
			add(Calendar.DAY_OF_WEEK, 6)
			set(Calendar.HOUR_OF_DAY, 23)
			set(Calendar.MINUTE, 59)
			set(Calendar.SECOND, 59)
			set(Calendar.MILLISECOND, 999)
		}.time
		val list = workouts.filter { it.date.after(start) && it.date.before(end) }
        return WeeklyStats(
            workoutCount = list.size,
            totalVolume = list.sumOf { it.totalVolume },
            totalSets = list.sumOf { it.totalSets },
            totalDuration = list.sumOf { it.durationMinutes ?: 0 }
        )
    }

    private fun calculateMonthlyStats(workouts: List<Workout>): MonthlyStats {
        val calendar = Calendar.getInstance()
        val start = calendar.apply {
            set(Calendar.DAY_OF_MONTH, 1)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.time
        val end = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_MONTH, getActualMaximum(Calendar.DAY_OF_MONTH))
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
        }.time
        val list = workouts.filter { it.date.after(start) && it.date.before(end) }
        val avgDuration = if (list.isNotEmpty()) list.mapNotNull { it.durationMinutes }.average().toInt() else 0
        return MonthlyStats(
            workoutCount = list.size,
            totalVolume = list.sumOf { it.totalVolume },
            totalSets = list.sumOf { it.totalSets },
            totalDuration = list.sumOf { it.durationMinutes ?: 0 },
            averageDuration = avgDuration
        )
    }

    private fun computeStreaks(workouts: List<Workout>): Pair<Int, Int> {
        if (workouts.isEmpty()) return 0 to 0
        val days = workouts.map { truncateToDay(it.date) }.toSet().toList().sorted()
        var current = 1
        var longest = 1
        val cal = Calendar.getInstance()
        for (i in 1 until days.size) {
            cal.time = days[i - 1]
            cal.add(Calendar.DAY_OF_YEAR, 1)
            val expected = cal.time
            if (!days[i].before(expected) && !days[i].after(expected)) {
                current += 1
                if (current > longest) longest = current
            } else {
                current = 1
            }
        }
        // Determine current streak ending today
        val today = truncateToDay(Date())
        val hasToday = days.any { it == today }
        if (!hasToday) {
            // If no workout today, check if yesterday continues the streak
            val yesterday = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -1) }.let { cal2 ->
                cal2.set(Calendar.HOUR_OF_DAY, 0)
                cal2.set(Calendar.MINUTE, 0)
                cal2.set(Calendar.SECOND, 0)
                cal2.set(Calendar.MILLISECOND, 0)
                cal2.time
            }
            val hasYesterday = days.any { it == yesterday }
            if (!hasYesterday) {
                // Streak broken
                current = 0
            }
        }
        return current to longest
    }

    private fun truncateToDay(date: Date): Date {
        val cal = Calendar.getInstance().apply { time = date }
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.time
    }

    fun refreshData() {
        loadProgressData()
    }
    
    fun fixWorkoutTotals() {
        fixExistingWorkoutTotals()
    }
    
    /**
     * Delete a workout and refresh data
     */
    fun deleteWorkout(workoutId: Long) {
        viewModelScope.launch {
            try {
                val workout = workoutRepository.getAllWorkouts().first().find { it.id == workoutId }
                workout?.let {
                    workoutRepository.deleteWorkout(it)
                    // Refresh data after deletion
                    refreshAllStats()
                }
            } catch (e: Exception) {
                // Handle error - in a real app you'd show a snackbar
                println("Error deleting workout: ${e.message}")
            }
        }
    }
    
    /**
     * Refresh all stats and data
     */
    fun refreshAllStats() {
        viewModelScope.launch {
            try {
                val workouts = workoutRepository.getAllWorkouts().first()
                refreshStats(workouts)
                // Explicitly refresh PR data to ensure it's up to date
                refreshPersonalRecords()
            } catch (e: Exception) {
                println("Error refreshing stats: ${e.message}")
            }
        }
    }

    /**
     * Refresh personal records data specifically
     */
    fun refreshPersonalRecords() {
        viewModelScope.launch {
            try {
                // Load personal records
                val records = progressRepository.getPersonalRecords()
                _personalRecords.value = records.sortedByDescending { it.achievedAt }

                // Load volume-based personal records
                val volumeRecords = progressRepository.getVolumeBasedPersonalRecords()
                _volumeBasedPersonalRecords.value = volumeRecords
            } catch (e: Exception) {
                println("Error refreshing personal records: ${e.message}")
            }
        }
    }

    private suspend fun buildDisplayTitleForWorkout(workout: Workout): String {
        val programId = workout.programId
        if (programId != null) {
            val programName = try { programRepository.getProgramById(programId)?.name } catch (e: Exception) { null }
            val dayName = try {
                if (workout.programDayId != null) {
                    programRepository.getProgramDayById(workout.programDayId)?.name
                } else {
                    null
                }
            } catch (e: Exception) { null }
            val parts = listOfNotNull(programName, dayName)
            if (parts.isNotEmpty()) return parts.joinToString(" - ")
        }
        return workout.name
    }

    private fun formatDateShort(date: Date): String {
        val sdf = java.text.SimpleDateFormat("EEE MMM dd", java.util.Locale.getDefault())
        return sdf.format(date)
    }
    
    private fun fixExistingWorkoutTotals() {
        viewModelScope.launch {
            try {
                val workouts = workoutRepository.getAllWorkouts().first()
                val workoutsWithMissingTotals = workouts.filter { 
                    it.isCompleted && (it.totalVolume == 0.0 || it.totalSets == 0) 
                }
                
                workoutsWithMissingTotals.forEach { workout ->
                    workoutRepository.recalculateWorkoutTotals(workout.id)
                }
                
                if (workoutsWithMissingTotals.isNotEmpty()) {
                    // Reload progress data after fixing totals
                    loadProgressData()
                }
            } catch (e: Exception) {
                // Log error but don't crash the app
                println("Error fixing workout totals: ${e.message}")
            }
        }
    }
}

data class WeeklyStats(
    val workoutCount: Int = 0,
    val totalVolume: Double = 0.0,
    val totalSets: Int = 0,
    val totalDuration: Int = 0
)

data class MonthlyStats(
    val workoutCount: Int = 0,
    val totalVolume: Double = 0.0,
    val totalSets: Int = 0,
    val totalDuration: Int = 0,
    val averageDuration: Int = 0
)

data class WorkoutHistoryDisplay(
    val id: Long,
    val title: String,
    val date: String,
    val duration: String,
    val sets: String,
    val volume: Double
)