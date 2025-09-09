package com.chilluminati.rackedup.presentation.progress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chilluminati.rackedup.data.database.entity.BodyMeasurement
import com.chilluminati.rackedup.data.repository.BodyMeasurementRepository
import com.chilluminati.rackedup.data.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

/**
 * ViewModel for body measurement tracking
 */
@HiltViewModel
class BodyMeasurementViewModel @Inject constructor(
    private val bodyMeasurementRepository: BodyMeasurementRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    // UI State
    private val _uiState = MutableStateFlow(BodyMeasurementUiState())
    val uiState: StateFlow<BodyMeasurementUiState> = _uiState.asStateFlow()

    // All measurements
    private val _measurements = MutableStateFlow<List<BodyMeasurement>>(emptyList())
    val measurements: StateFlow<List<BodyMeasurement>> = _measurements.asStateFlow()

    // Weight measurements
    private val _weightMeasurements = MutableStateFlow<List<BodyMeasurement>>(emptyList())
    val weightMeasurements: StateFlow<List<BodyMeasurement>> = _weightMeasurements.asStateFlow()

    // Body composition measurements
    private val _bodyCompositionMeasurements = MutableStateFlow<List<BodyMeasurement>>(emptyList())
    val bodyCompositionMeasurements: StateFlow<List<BodyMeasurement>> = _bodyCompositionMeasurements.asStateFlow()

    // Circumference measurements
    private val _circumferenceMeasurements = MutableStateFlow<List<BodyMeasurement>>(emptyList())
    val circumferenceMeasurements: StateFlow<List<BodyMeasurement>> = _circumferenceMeasurements.asStateFlow()

    // Loading state
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Weight unit preference
    val weightUnit: StateFlow<String> = settingsRepository.weightUnit.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = "kg"
    )

    // Distance unit preference
    val distanceUnit: StateFlow<String> = settingsRepository.distanceUnit.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = "cm"
    )

    // Measurements unit preference
    val measurementsUnit: StateFlow<String> = settingsRepository.measurementsUnit.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = "in"
    )

    init {
        loadMeasurements()
    }

    /**
     * Load all body measurements
     */
    fun loadMeasurements() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val allMeasurements = bodyMeasurementRepository.getAllBodyMeasurements()
                _measurements.value = allMeasurements.sortedByDescending { it.measuredAt }
                
                // Categorize measurements
                _weightMeasurements.value = allMeasurements.filter { it.measurementType == "Weight" }
                    .sortedBy { it.measuredAt }
                
                _bodyCompositionMeasurements.value = allMeasurements.filter { 
                    it.measurementType in listOf("Body Fat %", "Muscle Mass", "Body Fat") 
                }.sortedBy { it.measuredAt }
                
                _circumferenceMeasurements.value = allMeasurements.filter { 
                    it.measurementType == "Circumference" 
                }.sortedBy { it.measuredAt }
                
                updateUiState()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "Failed to load measurements"
                )
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Add a new body measurement
     */
    fun addMeasurement(
        measurementType: String,
        value: Double,
        unit: String,
        bodyPart: String? = null,
        measurementMethod: String? = null,
        notes: String? = null,
        measuredAt: Date = Date()
    ) {
        viewModelScope.launch {
            try {
                bodyMeasurementRepository.addBodyMeasurement(
                    measurementType = measurementType,
                    value = value,
                    unit = unit,
                    bodyPart = bodyPart,
                    measurementMethod = measurementMethod,
                    notes = notes,
                    measuredAt = measuredAt
                )
                loadMeasurements() // Reload data
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "Failed to add measurement"
                )
            }
        }
    }

    /**
     * Update an existing measurement
     */
    fun updateMeasurement(measurement: BodyMeasurement) {
        viewModelScope.launch {
            try {
                bodyMeasurementRepository.updateBodyMeasurement(measurement)
                loadMeasurements() // Reload data
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "Failed to update measurement"
                )
            }
        }
    }

    /**
     * Delete a measurement
     */
    fun deleteMeasurement(measurement: BodyMeasurement) {
        viewModelScope.launch {
            try {
                bodyMeasurementRepository.deleteBodyMeasurement(measurement)
                loadMeasurements() // Reload data
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "Failed to delete measurement"
                )
            }
        }
    }

    /**
     * Clear error state
     */
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    /**
     * Update UI state based on current data
     */
    private fun updateUiState() {
        val measurements = _measurements.value
        val weightMeasurements = _weightMeasurements.value
        val bodyCompositionMeasurements = _bodyCompositionMeasurements.value
        val circumferenceMeasurements = _circumferenceMeasurements.value

        _uiState.value = _uiState.value.copy(
            hasData = measurements.isNotEmpty(),
            totalMeasurements = measurements.size,
            latestWeight = weightMeasurements.lastOrNull()?.value,
            latestWeightUnit = weightMeasurements.lastOrNull()?.unit,
            latestBodyFat = bodyCompositionMeasurements.filter { it.measurementType == "Body Fat %" }.lastOrNull()?.value,
            latestMuscleMass = bodyCompositionMeasurements.filter { it.measurementType == "Muscle Mass" }.lastOrNull()?.value,
            measurementTypes = measurements.map { it.measurementType }.distinct().sorted(),
            bodyParts = measurements.mapNotNull { it.bodyPart }.distinct().sorted()
        )
    }

    /**
     * Get chart data for weight measurements
     */
    fun getWeightChartData(): List<Pair<Date, Double>> {
        return _weightMeasurements.value.map { measurement ->
            measurement.measuredAt to measurement.value
        }.sortedBy { it.first }
    }

    /**
     * Get chart data for body composition measurements
     */
    fun getBodyCompositionChartData(): List<Pair<Date, Map<String, Double>>> {
        val bodyFatMeasurements = _bodyCompositionMeasurements.value.filter { it.measurementType == "Body Fat %" }
        val muscleMassMeasurements = _bodyCompositionMeasurements.value.filter { it.measurementType == "Muscle Mass" }
        
        val allDates = (bodyFatMeasurements.map { it.measuredAt } + muscleMassMeasurements.map { it.measuredAt }).distinct().sorted()
        
        return allDates.map { date ->
            val bodyFat = bodyFatMeasurements.find { it.measuredAt == date }?.value
            val muscleMass = muscleMassMeasurements.find { it.measuredAt == date }?.value
            
            val measurements = mutableMapOf<String, Double>()
            bodyFat?.let { measurements["Body Fat %"] = it }
            muscleMass?.let { measurements["Muscle Mass"] = it }
            
            date to measurements
        }
    }

    /**
     * Get chart data for circumference measurements
     */
    fun getCircumferenceChartData(): List<Pair<Date, Map<String, Double>>> {
        val circumferenceMeasurements = _circumferenceMeasurements.value
        
        return circumferenceMeasurements.groupBy { it.measuredAt }
            .map { (date, measurements) ->
                val measurementMap = measurements.associate { measurement ->
                    (measurement.bodyPart ?: measurement.measurementType) to measurement.value
                }
                date to measurementMap
            }.sortedBy { it.first }
    }
}

/**
 * UI State for body measurement screen
 */
data class BodyMeasurementUiState(
    val hasData: Boolean = false,
    val totalMeasurements: Int = 0,
    val latestWeight: Double? = null,
    val latestWeightUnit: String? = null,
    val latestBodyFat: Double? = null,
    val latestMuscleMass: Double? = null,
    val measurementTypes: List<String> = emptyList(),
    val bodyParts: List<String> = emptyList(),
    val error: String? = null
)
