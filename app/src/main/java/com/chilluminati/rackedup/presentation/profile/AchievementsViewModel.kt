package com.chilluminati.rackedup.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chilluminati.rackedup.data.repository.AchievementsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AchievementsViewModel @Inject constructor(
    private val achievementsRepository: AchievementsRepository
) : ViewModel() {

    private val _achievements = MutableStateFlow<List<AchievementsRepository.State>>(emptyList())
    val achievements: StateFlow<List<AchievementsRepository.State>> = _achievements.asStateFlow()

    init {
        viewModelScope.launch {
            achievementsRepository.observeAchievements().collectLatest { states ->
                _achievements.value = states
            }
        }
    }

    val unlockedCount: Int
        get() = _achievements.value.count { it.isUnlocked }

    val totalCount: Int
        get() = _achievements.value.size
}


