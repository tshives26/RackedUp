package com.chilluminati.rackedup.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chilluminati.rackedup.data.repository.MultiProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

/**
 * ViewModel for creating a new user profile
 */
@HiltViewModel
class CreateProfileViewModel @Inject constructor(
    private val multiProfileRepository: MultiProfileRepository
) : ViewModel() {

    /**
     * UI state for create profile screen
     */
    data class CreateProfileUiState(
        val isLoading: Boolean = false,
        val isProfileCreated: Boolean = false,
        val message: String? = null,
        val isError: Boolean = false,
        val hasTriedToSubmit: Boolean = false
    )

    private val _uiState = MutableStateFlow(CreateProfileUiState())
    val uiState: StateFlow<CreateProfileUiState> = _uiState.asStateFlow()

    /**
     * Create a new profile with the provided information
     */
    fun createProfile(
        name: String,
        email: String? = null,
        birthday: Date? = null,
        sex: String? = null,
        heightCm: Double? = null,
        weightKg: Double? = null,
        activityLevel: String? = null,
        fitnessGoal: String? = null,
        experienceLevel: String? = null,
        makeActive: Boolean = false
    ) {
        // Mark that user has tried to submit
        _uiState.update { it.copy(hasTriedToSubmit = true) }
        
        // Validate required fields
        if (name.isBlank()) {
            _uiState.update {
                it.copy(
                    message = "Profile name is required",
                    isError = true
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, message = null, isError = false) }

            val result = multiProfileRepository.createProfile(
                name = name,
                email = email,
                birthday = birthday,
                sex = sex,
                heightCm = heightCm,
                weightKg = weightKg,
                activityLevel = activityLevel,
                fitnessGoal = fitnessGoal,
                experienceLevel = experienceLevel,
                makeActive = makeActive
            )

            result.fold(
                onSuccess = { _ ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isProfileCreated = true,
                            message = "Profile '$name' created successfully!",
                            isError = false
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            message = "Failed to create profile: ${error.message}",
                            isError = true
                        )
                    }
                }
            )
        }
    }

    /**
     * Clear the current message
     */
    fun clearMessage() {
        _uiState.update { it.copy(message = null, isError = false) }
    }

    /**
     * Reset the creation state
     */
    fun resetCreationState() {
        _uiState.update { it.copy(isProfileCreated = false, hasTriedToSubmit = false) }
    }
}
