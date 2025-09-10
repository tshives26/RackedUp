package com.chilluminati.rackedup.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chilluminati.rackedup.data.database.entity.UserProfile
import com.chilluminati.rackedup.data.repository.UserProfileRepository
import com.chilluminati.rackedup.di.IoDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userProfileRepository: UserProfileRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    private val _onboardingState = MutableStateFlow(OnboardingUiState())
    val onboardingState: StateFlow<OnboardingUiState> = _onboardingState.asStateFlow()

    init {
        loadProfile()
        checkOnboardingStatus()
    }

    private fun loadProfile() {
        viewModelScope.launch(ioDispatcher) {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)
                userProfileRepository.observeActiveProfile().collect { profile ->
                    _uiState.value = _uiState.value.copy(
                        profile = profile,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "Failed to load profile",
                    isLoading = false
                )
            }
        }
    }

    private fun checkOnboardingStatus() {
        viewModelScope.launch(ioDispatcher) {
            try {
                // Keep loading state true until we've checked
                _onboardingState.value = _onboardingState.value.copy(isLoading = true)
                
                // Check if we have an active profile with a name
                val activeProfile = userProfileRepository.getActiveProfile()
                
                // Check if we have a complete profile (this is for app startup)
                val hasCompletedOnboarding = activeProfile?.name != null && activeProfile.name.isNotBlank()
                
                _onboardingState.value = _onboardingState.value.copy(
                    hasCompletedOnboarding = hasCompletedOnboarding,
                    isLoading = false
                )
            } catch (e: Exception) {
                _onboardingState.value = _onboardingState.value.copy(
                    error = e.message ?: "Failed to check onboarding status",
                    isLoading = false
                )
            }
        }
    }

    fun createProfile(name: String, birthday: Date?, sex: String?) {
        viewModelScope.launch(ioDispatcher) {
            try {
                _onboardingState.value = _onboardingState.value.copy(isLoading = true)
                
                val profileId = userProfileRepository.createProfile(
                    name = name,
                    birthday = birthday,
                    sex = sex
                )
                
                _onboardingState.value = _onboardingState.value.copy(
                    isLoading = false,
                    profileCreated = true,
                    // Don't mark as completed yet - wait for user to finish preferences
                    hasCompletedOnboarding = false
                )
                loadProfile() // Reload profile after creation
            } catch (e: Exception) {
                _onboardingState.value = _onboardingState.value.copy(
                    error = e.message ?: "Failed to create profile",
                    isLoading = false
                )
            }
        }
    }

    /**
     * Mark onboarding as completed. Used after finishing all onboarding steps.
     */
    fun completeOnboarding() {
        _onboardingState.value = _onboardingState.value.copy(
            hasCompletedOnboarding = true,
            isLoading = false
        )
    }

    fun updateProfile(
        name: String? = null,
        birthday: Date? = null,
        sex: String? = null,
        email: String? = null,
        profileImageUrl: String? = null,
        age: Int? = null,
        heightCm: Double? = null,
        weightKg: Double? = null,
        activityLevel: String? = null,
        fitnessGoal: String? = null,
        experienceLevel: String? = null
    ) {
        viewModelScope.launch(ioDispatcher) {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)
                val currentProfile = _uiState.value.profile
                currentProfile?.let { _ ->
                    userProfileRepository.updateProfileFields(
                        name = name,
                        birthday = birthday,
                        sex = sex,
                        email = email,
                        profileImageUrl = profileImageUrl,
                        age = age,
                        heightCm = heightCm,
                        weightKg = weightKg,
                        activityLevel = activityLevel,
                        fitnessGoal = fitnessGoal,
                        experienceLevel = experienceLevel
                    )
                    loadProfile() // Reload profile after update
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "Failed to update profile",
                    isLoading = false
                )
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
        _onboardingState.value = _onboardingState.value.copy(error = null)
    }

    fun formatMemberSinceDate(date: Date): String {
        val formatter = SimpleDateFormat("MMM yyyy", Locale.getDefault())
        return "Member since ${formatter.format(date)}"
    }

    fun calculateAge(birthday: Date): Int {
        return userProfileRepository.calculateAge(birthday)
    }
    
}

data class ProfileUiState(
    val profile: UserProfile? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

data class OnboardingUiState(
    val hasCompletedOnboarding: Boolean = false,
    val isLoading: Boolean = true,
    val error: String? = null,
    val profileCreated: Boolean = false
)
