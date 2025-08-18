package com.chilluminati.rackedup.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chilluminati.rackedup.data.repository.SettingsRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import android.content.Context
import android.app.NotificationManager
import androidx.core.app.NotificationCompat
import com.chilluminati.rackedup.core.util.Constants
import com.chilluminati.rackedup.R
import android.media.RingtoneManager
import android.os.VibrationEffect
import android.os.Vibrator
import android.net.Uri
import android.media.MediaPlayer
import android.media.AudioAttributes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for settings screen
 */
@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
    @ApplicationContext private val appContext: Context
) : ViewModel() {
    
    /**
     * UI state for settings screen
     */
    data class SettingsUiState(
        val themeMode: SettingsRepository.ThemeMode = SettingsRepository.ThemeMode.LIGHT,
        val dynamicColor: Boolean = true,
        val colorTheme: SettingsRepository.ColorTheme = SettingsRepository.ColorTheme.MONOCHROME,
        val restTimerSound: Boolean = true,
        val vibration: Boolean = true,
        val defaultRestSeconds: Int = 120,
        val autoBackup: Boolean = false,
        val weightUnit: String? = null,
        val distanceUnit: String? = null,
        val isLoading: Boolean = false
    )
    
    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()
    
    init {
        loadSettings()
    }
    
    /**
     * Load settings from repository
     */
    private fun loadSettings() {
        viewModelScope.launch {
            combine(
                settingsRepository.themeMode,
                settingsRepository.dynamicColor,
                settingsRepository.colorTheme,
                settingsRepository.restTimerSound,
                settingsRepository.vibration,
                settingsRepository.autoBackup,
                settingsRepository.defaultRestSeconds,
                settingsRepository.weightUnit,
                settingsRepository.distanceUnit
            ) { values ->
                val themeMode = values[0] as SettingsRepository.ThemeMode
                val dynamicColor = values[1] as Boolean
                val colorTheme = values[2] as SettingsRepository.ColorTheme
                val restTimerSound = values[3] as Boolean
                val vibration = values[4] as Boolean
                val autoBackup = values[5] as Boolean
                val defaultRestSeconds = values[6] as Int
                val weightUnit = values[7] as String
                val distanceUnit = values[8] as String
                
                SettingsUiState(
                    themeMode = themeMode,
                    dynamicColor = dynamicColor,
                    colorTheme = colorTheme,
                    restTimerSound = restTimerSound,
                    vibration = vibration,
                    defaultRestSeconds = defaultRestSeconds,
                    autoBackup = autoBackup,
                    weightUnit = weightUnit,
                    distanceUnit = distanceUnit,
                    isLoading = false
                )
            }.collect { state ->
                _uiState.value = state
            }
        }
    }
    
    /**
     * Update theme mode
     */
    fun updateThemeMode(themeMode: SettingsRepository.ThemeMode) {
        viewModelScope.launch {
            settingsRepository.setThemeMode(themeMode)
        }
    }
    
    /**
     * Update dynamic color preference
     */
    fun updateDynamicColor(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setDynamicColor(enabled)
        }
    }
    
    /**
     * Update color theme
     */
    fun updateColorTheme(colorTheme: SettingsRepository.ColorTheme) {
        viewModelScope.launch {
            settingsRepository.setColorTheme(colorTheme)
        }
    }
    

    
    /**
     * Update rest timer sound preference
     */
    fun updateRestTimerSound(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setRestTimerSound(enabled)
        }
    }
    
    /**
     * Update vibration preference
     */
    fun updateVibration(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setVibration(enabled)
        }
    }
    
    /**
     * Update auto backup preference
     */
    fun updateAutoBackup(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setAutoBackup(enabled)
        }
    }



    fun updateDefaultRestSeconds(seconds: Int) {
        viewModelScope.launch {
            settingsRepository.setDefaultRestSeconds(seconds)
        }
    }

    // Debug helpers
    fun debugPlayRestSound() {
        val uri = Uri.parse("android.resource://" + appContext.packageName + "/" + R.raw.rest_timer_complete)
        try {
            MediaPlayer().apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_ALARM)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                )
                setDataSource(appContext, uri)
                setOnCompletionListener { mp ->
                    try { mp.release() } catch (_: Exception) {}
                }
                prepare()
                start()
            }
        } catch (e: Exception) {
            // Fallback to notification with explicit sound
            val manager = appContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val notification = NotificationCompat.Builder(appContext, Constants.ALARM_NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(appContext.getString(R.string.app_name))
                .setContentText("Playing test sound")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setSound(uri)
                .build()
            manager.notify(3002, notification)
        }
    }

    fun debugVibrate() {
        @Suppress("DEPRECATION")
        val vibrator = appContext.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(300)
        }
    }



    // Alarm-style immediate test (bypasses DND where allowed)
    fun debugSendAlarmNotification() {
        val uri = Uri.parse("android.resource://" + appContext.packageName + "/" + R.raw.rest_timer_complete)
        val manager = appContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = NotificationCompat.Builder(appContext, Constants.ALARM_NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_stat_bolt)
            .setContentTitle(appContext.getString(R.string.app_name))
            .setContentText("Rest timer complete")
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setSound(uri)
            .setAutoCancel(true)
            .build()
        manager.notify(3003, notification)
    }
    
    /**
     * Update weight unit preference
     */
    fun updateWeightUnit(unit: String) {
        viewModelScope.launch {
            settingsRepository.setWeightUnit(unit)
        }
    }
    
    /**
     * Update distance unit preference
     */
    fun updateDistanceUnit(unit: String) {
        viewModelScope.launch {
            settingsRepository.setDistanceUnit(unit)
        }
    }
}
