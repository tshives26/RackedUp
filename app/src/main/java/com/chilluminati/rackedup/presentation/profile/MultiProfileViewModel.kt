package com.chilluminati.rackedup.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chilluminati.rackedup.data.database.entity.UserProfile
import com.chilluminati.rackedup.data.repository.MultiProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for multi-profile management screen
 */
@HiltViewModel
class MultiProfileViewModel @Inject constructor(
    private val multiProfileRepository: MultiProfileRepository
) : ViewModel() {

    /**
     * UI state for multi-profile screen
     */
    data class MultiProfileUiState(
        val profiles: List<UserProfile> = emptyList(),
        val profileStats: MultiProfileRepository.ProfileStats? = null,
        val isLoading: Boolean = false,
        val message: String? = null,
        val isError: Boolean = false
    )

    private val _uiState = MutableStateFlow(MultiProfileUiState())
    val uiState: StateFlow<MultiProfileUiState> = _uiState.asStateFlow()

    init {
        loadProfiles()
    }

    /**
     * Load all profiles and stats
     */
    private fun loadProfiles() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            try {
                multiProfileRepository.getAllProfiles().collect { profiles ->
                    val stats = multiProfileRepository.getProfileStats()
                    _uiState.update {
                        it.copy(
                            profiles = profiles,
                            profileStats = stats,
                            isLoading = false
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isError = true,
                        message = "Failed to load profiles: ${e.message}"
                    )
                }
            }
        }
    }

    /**
     * Switch to a different profile
     */
    fun switchToProfile(profileId: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            val result = multiProfileRepository.switchToProfile(profileId)
            result.fold(
                onSuccess = { profile ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            message = "Switched to profile: ${profile.name}",
                            isError = false
                        )
                    }
                    // Profiles will be updated automatically via Flow
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            message = "Failed to switch profile: ${error.message}",
                            isError = true
                        )
                    }
                }
            )
        }
    }

    /**
     * Delete a profile
     */
    fun deleteProfile(profileId: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            val result = multiProfileRepository.deleteProfile(profileId)
            result.fold(
                onSuccess = { message ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            message = message,
                            isError = false
                        )
                    }
                    // Profiles will be updated automatically via Flow
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            message = "Failed to delete profile: ${error.message}",
                            isError = true
                        )
                    }
                }
            )
        }
    }

    /**
     * Duplicate a profile
     */
    fun duplicateProfile(sourceProfileId: Long, newName: String, makeActive: Boolean = false) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            val result = multiProfileRepository.duplicateProfile(sourceProfileId, newName, makeActive)
            result.fold(
                onSuccess = { _ ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            message = "Profile duplicated successfully",
                            isError = false
                        )
                    }
                    // Profiles will be updated automatically via Flow
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            message = "Failed to duplicate profile: ${error.message}",
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
     * Refresh profiles
     */
    fun refreshProfiles() {
        loadProfiles()
    }
}
