package com.chilluminati.rackedup.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chilluminati.rackedup.data.repository.WorkoutRepository
import com.chilluminati.rackedup.data.repository.ProgressRepository
import com.chilluminati.rackedup.data.repository.UserProfileRepository
import com.chilluminati.rackedup.data.repository.SettingsRepository
import com.chilluminati.rackedup.data.repository.ProgramRepository
import com.chilluminati.rackedup.data.database.entity.Workout
import com.chilluminati.rackedup.data.database.entity.UserProfile
import com.chilluminati.rackedup.data.database.entity.Program
import com.chilluminati.rackedup.di.IoDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

/**
 * ViewModel for the dashboard screen
 */
@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val workoutRepository: WorkoutRepository,
    private val progressRepository: ProgressRepository,
    private val userProfileRepository: UserProfileRepository,
    private val settingsRepository: SettingsRepository,
    private val programRepository: ProgramRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()
    
    init {
        // Set a stable motivational message for this ViewModel instance
        _uiState.value = _uiState.value.copy(
            motivationalMessage = getMotivationalMessage()
        )
        // Observe weight unit preference
        viewModelScope.launch(ioDispatcher) {
            settingsRepository.weightUnit.collect { unit ->
                _uiState.value = _uiState.value.copy(weightUnit = unit)
            }
        }
        observeDashboardData()
    }
    
    private fun observeDashboardData() {
        viewModelScope.launch(ioDispatcher) {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)
                
                // Load user profile once
                val profile = userProfileRepository.getActiveProfile()
                
                // Combine active program and workouts streams for reactive updates
                combine(
                    programRepository.getActiveProgramFlow(),
                    workoutRepository.getAllWorkouts()
                ) { activeProgram, workouts ->
                    val recentWorkouts = workouts.take(5)
                    
                    // Calculate this week's stats
                    val weeklyStats = calculateWeeklyStats(workouts)
                    
                    // Calculate monthly stats  
                    val monthlyStats = calculateMonthlyStats(workouts)
                    
                    // Derive program progress metrics
                    val (daysCompleted, lastCompletedLabel) = if (activeProgram != null) {
                        val completed = workouts
                            .filter { it.programId == activeProgram.id && (it.endTime != null || it.isCompleted) }
                        val count = completed.mapNotNull { it.programDayId }.distinct().size
                        val latest = completed.maxByOrNull { it.date.time }
                        val label: String? = try {
                            val dayId = latest?.programDayId
                            if (dayId != null) {
                                val days = programRepository.getProgramDays(activeProgram.id)
                                val matched = days.firstOrNull { it.id == dayId }
                                matched?.let { day -> 
                                    val sdf = SimpleDateFormat("MMM d", Locale.getDefault())
                                    "Last: ${'$'}{sdf.format(latest.date)} • Day ${'$'}{day.dayNumber} - ${'$'}{day.name}" 
                                }
                            } else null
                        } catch (_: Exception) { null }
                        count to label
                    } else 0 to null

                    _uiState.value = _uiState.value.copy(
                        profile = profile,
                        activeProgram = activeProgram,
                        recentWorkouts = recentWorkouts,
                        weeklyStats = weeklyStats,
                        monthlyStats = monthlyStats,
                        activeProgramDaysCompleted = daysCompleted,
                        activeProgramLastCompletedLabel = lastCompletedLabel,
                        isLoading = false,
                        error = null
                    )
                }.collect { /* Collection handled in combine block */ }
                
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to load dashboard data"
                )
            }
        }
    }
    
    private fun calculateWeeklyStats(workouts: List<Workout>): WeeklyStats {
        val calendar = Calendar.getInstance()
        val weekStart = calendar.apply {
            set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.time
        
        val weekEnd = calendar.apply {
            add(Calendar.DAY_OF_WEEK, 6)
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
        }.time
        
        val weeklyWorkouts = workouts.filter { workout ->
            workout.date.after(weekStart) && workout.date.before(weekEnd)
        }
        
        return WeeklyStats(
            workoutCount = weeklyWorkouts.size,
            totalVolume = weeklyWorkouts.sumOf { it.totalVolume },
            totalSets = weeklyWorkouts.sumOf { it.totalSets },
            totalDuration = weeklyWorkouts.sumOf { it.durationMinutes ?: 0 }
        )
    }
    
    private fun calculateMonthlyStats(workouts: List<Workout>): MonthlyStats {
        val calendar = Calendar.getInstance()
        val monthStart = calendar.apply {
            set(Calendar.DAY_OF_MONTH, 1)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.time
        
        val monthEnd = calendar.apply {
            set(Calendar.DAY_OF_MONTH, getActualMaximum(Calendar.DAY_OF_MONTH))
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
        }.time
        
        val monthlyWorkouts = workouts.filter { workout ->
            workout.date.after(monthStart) && workout.date.before(monthEnd)
        }
        
        return MonthlyStats(
            workoutCount = monthlyWorkouts.size,
            totalVolume = monthlyWorkouts.sumOf { it.totalVolume },
            totalSets = monthlyWorkouts.sumOf { it.totalSets },
            totalDuration = monthlyWorkouts.sumOf { it.durationMinutes ?: 0 },
            averageDuration = if (monthlyWorkouts.isNotEmpty()) {
                monthlyWorkouts.mapNotNull { it.durationMinutes }.average().toInt()
            } else 0
        )
    }
    
    fun refreshDashboard() {
        observeDashboardData()
    }
    
    fun refreshData() {
        observeDashboardData()
    }
    
    /**
     * Get program day name by ID
     */
    suspend fun getProgramDayName(programDayId: Long): String? {
        return try {
            val programDay = programRepository.getProgramDayById(programDayId)
            programDay?.name
        } catch (e: Exception) {
            null
        }
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
    
    fun getGreeting(): String {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        
        return when (hour) {
            in 0..11 -> "Good morning"
            in 12..17 -> "Good afternoon"
            else -> "Good evening"
        }
    }
    
    fun getMotivationalMessage(): String {
        val messages = listOf(
            "Ready to crush your workout?",
            "Every rep counts!",
            "Stronger than yesterday!",
            "Let's make progress today!",
            "Your only competition is yourself!",
            "Consistency is key!",
            "Transform your body, transform your life!",
            "The pain you feel today will be the strength you feel tomorrow!",
            "Success starts with self-discipline!"
        )
        
        return messages.random()
    }
}

/**
 * UI state for the dashboard
 */
data class DashboardUiState(
    val profile: UserProfile? = null,
    val activeProgram: Program? = null,
    val recentWorkouts: List<Workout> = emptyList(),
    val weeklyStats: WeeklyStats = WeeklyStats(),
    val monthlyStats: MonthlyStats = MonthlyStats(),
    val motivationalMessage: String = "Ready to crush your workout?",
    val weightUnit: String = "kg",
    val activeProgramDaysCompleted: Int = 0,
    val activeProgramLastCompletedLabel: String? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

/**
 * Weekly stats data class
 */
data class WeeklyStats(
    val workoutCount: Int = 0,
    val totalVolume: Double = 0.0,
    val totalSets: Int = 0,
    val totalDuration: Int = 0 // in minutes
)

/**
 * Monthly stats data class
 */
data class MonthlyStats(
    val workoutCount: Int = 0,
    val totalVolume: Double = 0.0,
    val totalSets: Int = 0,
    val totalDuration: Int = 0, // in minutes
    val averageDuration: Int = 0 // in minutes
)
