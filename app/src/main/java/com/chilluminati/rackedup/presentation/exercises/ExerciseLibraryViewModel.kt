package com.chilluminati.rackedup.presentation.exercises

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chilluminati.rackedup.data.repository.ExerciseRepository
import com.chilluminati.rackedup.data.database.entity.Exercise
import com.chilluminati.rackedup.di.IoDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for exercise library screen
 */
@HiltViewModel
class ExerciseLibraryViewModel @Inject constructor(
    private val exerciseRepository: ExerciseRepository,
    private val dataManagementRepository: com.chilluminati.rackedup.data.repository.DataManagementRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(ExerciseLibraryUiState())
    val uiState: StateFlow<ExerciseLibraryUiState> = _uiState.asStateFlow()
    
    // Search and filter states
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    
    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()
    private val _categories = MutableStateFlow(listOf("All"))
    val categories: StateFlow<List<String>> = _categories.asStateFlow()

    private val _showFavoritesOnly = MutableStateFlow(false)
    val showFavoritesOnly: StateFlow<Boolean> = _showFavoritesOnly.asStateFlow()

    // Additional filters
    private val _selectedEquipment = MutableStateFlow("All")
    val selectedEquipment: StateFlow<String> = _selectedEquipment.asStateFlow()
    private val _equipmentOptions = MutableStateFlow(listOf("All"))
    val equipmentOptions: StateFlow<List<String>> = _equipmentOptions.asStateFlow()

    private val _selectedMechanic = MutableStateFlow("All")
    val selectedMechanic: StateFlow<String> = _selectedMechanic.asStateFlow()
    private val _mechanicOptions = MutableStateFlow(listOf("All"))
    val mechanicOptions: StateFlow<List<String>> = _mechanicOptions.asStateFlow()

    // Force filter (Push / Pull / Static)
    private val _selectedForce = MutableStateFlow("All")
    val selectedForce: StateFlow<String> = _selectedForce.asStateFlow()
    private val _forceOptions = MutableStateFlow(listOf("All"))
    val forceOptions: StateFlow<List<String>> = _forceOptions.asStateFlow()

    private val _selectedPrimaryMuscle = MutableStateFlow("All")
    val selectedPrimaryMuscle: StateFlow<String> = _selectedPrimaryMuscle.asStateFlow()
    private val _primaryMuscleOptions = MutableStateFlow(listOf("All"))
    val primaryMuscleOptions: StateFlow<List<String>> = _primaryMuscleOptions.asStateFlow()
    

    
    private fun loadExercises() {
        android.util.Log.i("ExerciseLibraryViewModel", "loadExercises() called")
        viewModelScope.launch(ioDispatcher) {
            try {
                android.util.Log.i("ExerciseLibraryViewModel", "Starting to load exercises...")
                _uiState.value = _uiState.value.copy(isLoading = true)

                data class Filters(
                    val query: String,
                    val category: String,
                    val favoritesOnly: Boolean,
                    val equipment: String,
                    val mechanic: String,
                    val primary: String,
                    val force: String
                )

                data class Extra(
                    val equipment: String,
                    val mechanic: String,
                    val primary: String,
                    val force: String
                )

                val filtersFlow: Flow<Filters> =
                    combine(
                        combine(_searchQuery, _selectedCategory, _showFavoritesOnly) { q, c, f ->
                            Triple(q, c, f)
                        },
                        combine(_selectedEquipment, _selectedMechanic, _selectedPrimaryMuscle, _selectedForce) { e, m, p, fo ->
                            Extra(equipment = e, mechanic = m, primary = p, force = fo)
                        }
                    ) { left, right ->
                        Filters(
                            query = left.first,
                            category = left.second,
                            favoritesOnly = left.third,
                            equipment = right.equipment,
                            mechanic = right.mechanic,
                            primary = right.primary,
                            force = right.force
                        )
                    }

                combine(
                    exerciseRepository.getAllExercises(),
                    filtersFlow
                ) { exercises, filters ->
                    filterExercises(
                        exercises = exercises,
                        query = filters.query,
                        category = filters.category,
                        favoritesOnly = filters.favoritesOnly,
                        equipment = filters.equipment,
                        mechanic = filters.mechanic,
                        primaryMuscle = filters.primary,
                        force = filters.force
                    )
                }.collect { filteredExercises ->
                    android.util.Log.i("ExerciseLibraryViewModel", "Loaded ${filteredExercises.size} exercises")
                    _uiState.value = _uiState.value.copy(
                        exercises = filteredExercises,
                        isLoading = false,
                        error = null
                    )
                }
            } catch (e: Exception) {
                android.util.Log.e("ExerciseLibraryViewModel", "Error loading exercises", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to load exercises"
                )
            }
        }
    }
    
    private fun filterExercises(
        exercises: List<Exercise>,
        query: String,
        category: String,
        favoritesOnly: Boolean,
        equipment: String,
        mechanic: String,
        primaryMuscle: String,
        force: String
    ): List<Exercise> {
        return exercises.filter { exercise ->
            val matchesSearch = if (query.isEmpty()) {
                true
            } else {
                val searchWords = query.trim().split("\\s+".toRegex()).filter { it.isNotBlank() }
                searchWords.all { word ->
                    exercise.name.contains(word, ignoreCase = true) ||
                    exercise.muscleGroups.any { it.contains(word, ignoreCase = true) }
                }
            }
            
            val matchesCategory = category == "All" || exercise.category.equals(category, ignoreCase = true)
            val matchesFavorite = !favoritesOnly || exercise.isFavorite
            val matchesEquipment = equipment == "All" || exercise.equipment.equals(equipment, ignoreCase = true)
            val matchesMechanic = mechanic == "All" || (exercise.mechanic?.equals(mechanic, ignoreCase = true) == true)
            val matchesPrimary = primaryMuscle == "All" || exercise.muscleGroups.any { it.equals(primaryMuscle, ignoreCase = true) }
            val matchesForce = force == "All" || (exercise.force?.equals(force, ignoreCase = true) == true)
            
            matchesSearch && matchesCategory && matchesFavorite && matchesEquipment && matchesMechanic && matchesPrimary && matchesForce
        }
    }
    
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }
    
    fun updateSelectedCategory(category: String) {
        _selectedCategory.value = category
    }

    fun toggleFavoritesOnly() {
        _showFavoritesOnly.value = !_showFavoritesOnly.value
    }

    fun updateSelectedEquipment(value: String) { _selectedEquipment.value = value }
    fun updateSelectedMechanic(value: String) { _selectedMechanic.value = value }
    fun updateSelectedPrimaryMuscle(value: String) { _selectedPrimaryMuscle.value = value }
    fun updateSelectedForce(value: String) { _selectedForce.value = value }

    private fun String.capitalized(): String = if (isEmpty()) this else replaceFirstChar { it.uppercase() }

    init {
        android.util.Log.i("ExerciseLibraryViewModel", "ViewModel init started")
        loadExercises()
        // Build dynamic lists for filters
        viewModelScope.launch(ioDispatcher) {
            exerciseRepository.getAllExercises()
                .map { list -> list.map { it.category }.filter { it.isNotBlank() }.distinct().sorted() }
                .collect { cats -> _categories.value = listOf("All") + cats.map { it.capitalized() } }
        }
        viewModelScope.launch(ioDispatcher) {
            exerciseRepository.getAllExercises()
                .map { list -> list.map { it.equipment }.filter { it.isNotBlank() }.distinct().sorted() }
                .collect { opts -> _equipmentOptions.value = listOf("All") + opts.map { it.capitalized() } }
        }
        viewModelScope.launch(ioDispatcher) {
            exerciseRepository.getAllExercises()
                .map { list -> list.mapNotNull { it.mechanic }.filter { it.isNotBlank() }.distinct().sorted() }
                .collect { opts -> _mechanicOptions.value = listOf("All") + opts.map { it.capitalized() } }
        }
        viewModelScope.launch(ioDispatcher) {
            exerciseRepository.getAllExercises()
                .map { list -> list.mapNotNull { it.force }.filter { it.isNotBlank() }.distinct().sorted() }
                .collect { opts -> _forceOptions.value = listOf("All") + opts.map { it.capitalized() } }
        }
        viewModelScope.launch(ioDispatcher) {
            exerciseRepository.getAllExercises()
                .map { list -> list.flatMap { it.muscleGroups }.filter { it.isNotBlank() }.distinct().sorted() }
                .collect { opts -> _primaryMuscleOptions.value = listOf("All") + opts.map { it.capitalized() } }
        }
    }
    
    fun getExerciseById(exerciseId: Long): Flow<Exercise?> {
        return exerciseRepository.getExerciseByIdFlow(exerciseId)
    }
    
    fun toggleFavorite(exerciseId: Long, isFavorite: Boolean) {
        viewModelScope.launch(ioDispatcher) {
            try {
                exerciseRepository.toggleFavorite(exerciseId, isFavorite)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "Failed to update favorite"
                )
            }
        }
    }
    
    // Removed default seeding. Dataset is imported via Data Management.
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
    
    /**
     * Manually refresh exercises (useful if initial seeding failed)
     */
    fun refreshExercises() {
        android.util.Log.i("ExerciseLibraryViewModel", "refreshExercises() called")
        
        // First try to seed exercises if database is empty
        viewModelScope.launch(ioDispatcher) {
            try {
                val count = exerciseRepository.getAllExercisesList().size
                android.util.Log.i("ExerciseLibraryViewModel", "Current exercise count: $count")
                
                if (count == 0) {
                    android.util.Log.i("ExerciseLibraryViewModel", "Database is empty, attempting to seed exercises...")
                    
                    // Show downloading state
                    _uiState.value = _uiState.value.copy(
                        isDownloading = true,
                        downloadMessage = "Downloading exercise library..."
                    )
                    
                    // Try to manually seed exercises from assets
                    try {
                        _uiState.value = _uiState.value.copy(
                            downloadMessage = "Loading exercises from local database..."
                        )
                        
                        val result = dataManagementRepository.importFreeExerciseDbFromAssets()
                        result.fold(
                            onSuccess = { message ->
                                android.util.Log.i("ExerciseLibraryViewModel", "Successfully seeded exercises: $message")
                                _uiState.value = _uiState.value.copy(
                                    isDownloading = false,
                                    downloadMessage = "Successfully loaded $message"
                                )
                            },
                            onFailure = { error ->
                                android.util.Log.e("ExerciseLibraryViewModel", "Failed to seed exercises from assets, trying remote", error)
                                
                                _uiState.value = _uiState.value.copy(
                                    downloadMessage = "Local database failed, downloading from internet..."
                                )
                                
                                // Try remote download as fallback
                                val remoteResult = dataManagementRepository.importFreeExerciseDbFromRemote()
                                remoteResult.fold(
                                    onSuccess = { remoteMessage ->
                                        android.util.Log.i("ExerciseLibraryViewModel", "Successfully seeded exercises from remote: $remoteMessage")
                                        _uiState.value = _uiState.value.copy(
                                            isDownloading = false,
                                            downloadMessage = "Successfully downloaded $remoteMessage"
                                        )
                                    },
                                    onFailure = { remoteError ->
                                        android.util.Log.e("ExerciseLibraryViewModel", "Failed to seed exercises from remote", remoteError)
                                        _uiState.value = _uiState.value.copy(
                                            isDownloading = false,
                                            downloadMessage = "Failed to download exercises. Please try again.",
                                            error = remoteError.message
                                        )
                                    }
                                )
                            }
                        )
                    } catch (e: Exception) {
                        android.util.Log.e("ExerciseLibraryViewModel", "Error during manual seeding", e)
                        _uiState.value = _uiState.value.copy(
                            isDownloading = false,
                            downloadMessage = "Error downloading exercises. Please try again.",
                            error = e.message
                        )
                    }
                } else {
                    // Exercises already exist, just reload
                    _uiState.value = _uiState.value.copy(
                        downloadMessage = "Exercises already loaded. Refreshing..."
                    )
                }
            } catch (e: Exception) {
                android.util.Log.e("ExerciseLibraryViewModel", "Error checking exercise count", e)
                _uiState.value = _uiState.value.copy(
                    isDownloading = false,
                    downloadMessage = "Error checking exercise database. Please try again.",
                    error = e.message
                )
            }
        }
        
        loadExercises()
    }
}

/**
 * UI state for exercise library screen
 */
data class ExerciseLibraryUiState(
    val exercises: List<Exercise> = emptyList(),
    val isLoading: Boolean = false,
    val isDownloading: Boolean = false,
    val downloadMessage: String? = null,
    val error: String? = null
)
