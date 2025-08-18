package com.chilluminati.rackedup.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import com.chilluminati.rackedup.core.util.Constants.PreferenceKeys
import com.chilluminati.rackedup.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository for managing app settings and preferences
 */
@Singleton
class SettingsRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {
    enum class AutoBackupInterval(val days: Long) {
        DAILY(1),
        WEEKLY(7),
        MONTHLY(30)
    }

    
    /**
     * Theme mode enum
     */
    enum class ThemeMode(val value: String) {
        LIGHT("light"),
        DARK("dark")
    }
    
    /**
     * Color theme enum
     */
    enum class ColorTheme(val value: String, val displayName: String) {
        FOREST("forest", "Forest 🌲"),
        OCEAN("ocean", "Ocean 🌊"),
        SUNSET("sunset", "Sunset 🌅"),
        DESERT("desert", "Desert 🏜️"),
        GLACIER("glacier", "Glacier ❄️"),
        VOLCANO("volcano", "Fire 🔥"),
        MEADOW("meadow", "Meadow 🌼"),
        TWILIGHT("twilight", "Twilight 🌌"),
        AUTUMN("autumn", "Autumn 🍂"),
        CORAL_REEF("coral_reef", "Coral Reef 🪸"),
        STORM("storm", "Storm ⛈️"),
        AURORA("aurora", "Aurora 🌠"),
        CHERRY_BLOSSOM("cherry_blossom", "Cherry Blossom 🌸"),
        MIDNIGHT_GOLD("midnight_gold", "Midnight Gold ✨"),
        ARCTIC_NIGHT("arctic_night", "Arctic Night 🐧"),
        MONOCHROME("monochrome", "Monochrome ⚫⚪"),
        PINK_PARADISE("pink_paradise", "Pink Paradise 💖")
    }
    
    /**
     * Get theme mode as Flow
     */
    val themeMode: Flow<ThemeMode> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val themeValue = preferences[themeModeKey] ?: ThemeMode.LIGHT.value
            ThemeMode.values().find { it.value == themeValue } ?: ThemeMode.LIGHT
        }
    
    /**
     * Get dynamic color preference as Flow
     */
    val dynamicColor: Flow<Boolean> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[dynamicColorKey] ?: true
        }
    
    /**
     * Get color theme as Flow
     */
    val colorTheme: Flow<ColorTheme> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val themeValue = preferences[colorThemeKey] ?: ColorTheme.SUNSET.value
            ColorTheme.values().find { it.value == themeValue } ?: ColorTheme.SUNSET
        }
    
    /**
     * Get workout reminders preference as Flow
     */
    val workoutReminders: Flow<Boolean> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[workoutRemindersKey] ?: true
        }
    
    /**
     * Get rest timer sound preference as Flow
     */
    val restTimerSound: Flow<Boolean> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[restTimerSoundKey] ?: true
        }
    
    /**
     * Get vibration preference as Flow
     */
    val vibration: Flow<Boolean> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[vibrationKey] ?: true
        }
    
    /**
     * Get auto backup preference as Flow
     */
    val autoBackup: Flow<Boolean> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[autoBackupKey] ?: false
        }

    /**
     * Persisted URI permission to a user-selected backup folder (e.g., Google Drive)
     */
    val backupFolderUri: Flow<String?> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[backupFolderUriKey]
        }

    /**
     * Auto backup interval preference
     */
    val autoBackupInterval: Flow<AutoBackupInterval> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            when (preferences[autoBackupIntervalKey] ?: "weekly") {
                "daily" -> AutoBackupInterval.DAILY
                "monthly" -> AutoBackupInterval.MONTHLY
                else -> AutoBackupInterval.WEEKLY
            }
        }

    /**
     * Get reminder time in minutes from midnight as Flow
     */
    val reminderTimeMinutes: Flow<Int> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            // Default to 18:00 (6 PM)
            preferences[reminderTimeMinutesKey] ?: (18 * 60)
        }

    /**
     * Get default rest time (seconds) as Flow
     */
    val defaultRestSeconds: Flow<Int> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            // Default to 120s
            preferences[defaultRestSecondsKey] ?: 120
        }
    
    /**
     * Get weight unit preference as Flow
     */
    val weightUnit: Flow<String> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[weightUnitKey] ?: "lbs"
        }
    
    /**
     * Get distance unit preference as Flow
     */
    val distanceUnit: Flow<String> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[distanceUnitKey] ?: "miles"
        }
    
    /**
     * Set theme mode
     */
    suspend fun setThemeMode(themeMode: ThemeMode) = withContext(ioDispatcher) {
        dataStore.edit { preferences ->
            preferences[themeModeKey] = themeMode.value
        }
    }
    
    /**
     * Set dynamic color preference
     */
    suspend fun setDynamicColor(enabled: Boolean) = withContext(ioDispatcher) {
        dataStore.edit { preferences ->
            preferences[dynamicColorKey] = enabled
        }
    }
    
    /**
     * Set color theme
     */
    suspend fun setColorTheme(colorTheme: ColorTheme) = withContext(ioDispatcher) {
        dataStore.edit { preferences ->
            preferences[colorThemeKey] = colorTheme.value
        }
    }
    
    /**
     * Set workout reminders preference
     */
    suspend fun setWorkoutReminders(enabled: Boolean) = withContext(ioDispatcher) {
        dataStore.edit { preferences ->
            preferences[workoutRemindersKey] = enabled
        }
    }
    
    /**
     * Set rest timer sound preference
     */
    suspend fun setRestTimerSound(enabled: Boolean) = withContext(ioDispatcher) {
        dataStore.edit { preferences ->
            preferences[restTimerSoundKey] = enabled
        }
    }
    
    /**
     * Set vibration preference
     */
    suspend fun setVibration(enabled: Boolean) = withContext(ioDispatcher) {
        dataStore.edit { preferences ->
            preferences[vibrationKey] = enabled
        }
    }
    
    /**
     * Set auto backup preference
     */
    suspend fun setAutoBackup(enabled: Boolean) = withContext(ioDispatcher) {
        dataStore.edit { preferences ->
            preferences[autoBackupKey] = enabled
        }
    }

    /**
     * Save selected backup folder tree URI string
     */
    suspend fun setBackupFolderUri(uri: String?) = withContext(ioDispatcher) {
        dataStore.edit { preferences ->
            if (uri == null) {
                preferences.remove(backupFolderUriKey)
            } else {
                preferences[backupFolderUriKey] = uri
            }
        }
    }

    suspend fun setAutoBackupInterval(interval: AutoBackupInterval) = withContext(ioDispatcher) {
        dataStore.edit { preferences ->
            val value = when (interval) {
                AutoBackupInterval.DAILY -> "daily"
                AutoBackupInterval.WEEKLY -> "weekly"
                AutoBackupInterval.MONTHLY -> "monthly"
            }
            preferences[autoBackupIntervalKey] = value
        }
    }

    /**
     * Set reminder time in minutes from midnight
     */
    suspend fun setReminderTimeMinutes(minutesFromMidnight: Int) = withContext(ioDispatcher) {
        dataStore.edit { preferences ->
            preferences[reminderTimeMinutesKey] = minutesFromMidnight
        }
    }

    /**
     * Set default rest seconds
     */
    suspend fun setDefaultRestSeconds(seconds: Int) = withContext(ioDispatcher) {
        dataStore.edit { preferences ->
            preferences[defaultRestSecondsKey] = seconds
        }
    }
    
    /**
     * Set weight unit preference
     */
    suspend fun setWeightUnit(unit: String) = withContext(ioDispatcher) {
        dataStore.edit { preferences ->
            preferences[weightUnitKey] = unit
        }
    }
    
    /**
     * Set distance unit preference
     */
    suspend fun setDistanceUnit(unit: String) = withContext(ioDispatcher) {
        dataStore.edit { preferences ->
            preferences[distanceUnitKey] = unit
        }
    }
    
    companion object {
        private val themeModeKey = stringPreferencesKey(PreferenceKeys.THEME_MODE)
        private val dynamicColorKey = booleanPreferencesKey(PreferenceKeys.DYNAMIC_COLOR)
        private val colorThemeKey = stringPreferencesKey(PreferenceKeys.COLOR_THEME)
        private val workoutRemindersKey = booleanPreferencesKey(PreferenceKeys.WORKOUT_REMINDERS_ENABLED)
        private val reminderTimeMinutesKey = intPreferencesKey(PreferenceKeys.REMINDER_TIME_MINUTES)
        private val restTimerSoundKey = booleanPreferencesKey(PreferenceKeys.REST_TIMER_SOUND_ENABLED)
        private val vibrationKey = booleanPreferencesKey(PreferenceKeys.VIBRATION_ENABLED)
        private val autoBackupKey = booleanPreferencesKey(PreferenceKeys.AUTO_BACKUP_ENABLED)
        private val backupFolderUriKey = stringPreferencesKey(PreferenceKeys.BACKUP_FOLDER_URI)
        private val autoBackupIntervalKey = stringPreferencesKey(PreferenceKeys.AUTO_BACKUP_INTERVAL)
        private val weightUnitKey = stringPreferencesKey(PreferenceKeys.WEIGHT_UNIT)
        private val distanceUnitKey = stringPreferencesKey(PreferenceKeys.DISTANCE_UNIT)
        private val defaultRestSecondsKey = intPreferencesKey(PreferenceKeys.DEFAULT_REST_SECONDS)
    }
}
