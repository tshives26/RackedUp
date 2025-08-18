package com.chilluminati.rackedup.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
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
 * Repository to persist and observe the currently active workout session state across the app.
 * Backed by DataStore Preferences for simple resilience across process death and navigation.
 */
@Singleton
class ActiveWorkoutSessionRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {

    data class ActiveSession(
        val workoutId: Long,
        val isPaused: Boolean,
        val workoutElapsedSeconds: Int,
        val isResting: Boolean,
        val restRemainingSeconds: Int,
        // Epoch millis of the last time we persisted a timer tick. Used to derive elapsed time
        // while the UI is not on screen, so the timer "catches up" when the user returns.
        val lastTickEpochMs: Long,
        // JSON string encoding of per-exercise draft inputs: {"<workoutExerciseId>": {"weight":"...","reps":"..."}}
        val draftsJson: String?
    )

    val activeSession: Flow<ActiveSession?> = dataStore.data
        .catch { e -> if (e is IOException) emit(androidx.datastore.preferences.core.emptyPreferences()) else throw e }
        .map { prefs ->
            val workoutId = prefs[KEY_WORKOUT_ID] ?: return@map null
            val isActive = prefs[KEY_IS_ACTIVE] ?: return@map null
            if (!isActive || workoutId == 0L) return@map null
            ActiveSession(
                workoutId = workoutId,
                isPaused = prefs[KEY_IS_PAUSED] ?: false,
                workoutElapsedSeconds = prefs[KEY_WORKOUT_ELAPSED] ?: 0,
                isResting = prefs[KEY_IS_RESTING] ?: false,
                restRemainingSeconds = prefs[KEY_REST_REMAINING] ?: 0,
                lastTickEpochMs = prefs[KEY_LAST_TICK_MS] ?: 0L,
                draftsJson = prefs[KEY_DRAFTS_JSON]
            )
        }

    suspend fun setActiveWorkout(
        workoutId: Long,
        isPaused: Boolean = false,
        workoutElapsedSeconds: Int = 0,
        isResting: Boolean = false,
        restRemainingSeconds: Int = 0,
        draftsJson: String? = null
    ) = withContext(ioDispatcher) {
        dataStore.edit { prefs ->
            prefs[KEY_IS_ACTIVE] = true
            prefs[KEY_WORKOUT_ID] = workoutId
            prefs[KEY_IS_PAUSED] = isPaused
            prefs[KEY_WORKOUT_ELAPSED] = workoutElapsedSeconds
            prefs[KEY_IS_RESTING] = isResting
            prefs[KEY_REST_REMAINING] = restRemainingSeconds
            prefs[KEY_LAST_TICK_MS] = System.currentTimeMillis()
            if (draftsJson == null) prefs.remove(KEY_DRAFTS_JSON) else prefs[KEY_DRAFTS_JSON] = draftsJson
        }
    }

    suspend fun updateTimers(
        workoutElapsedSeconds: Int? = null,
        isPaused: Boolean? = null,
        isResting: Boolean? = null,
        restRemainingSeconds: Int? = null
    ) = withContext(ioDispatcher) {
        dataStore.edit { prefs ->
            workoutElapsedSeconds?.let { prefs[KEY_WORKOUT_ELAPSED] = it }
            isPaused?.let { prefs[KEY_IS_PAUSED] = it }
            isResting?.let { prefs[KEY_IS_RESTING] = it }
            restRemainingSeconds?.let { prefs[KEY_REST_REMAINING] = it }
            // Always update the last tick so we can derive elapsed time while away
            prefs[KEY_LAST_TICK_MS] = System.currentTimeMillis()
        }
    }

    suspend fun updateDraftsJson(draftsJson: String?) = withContext(ioDispatcher) {
        dataStore.edit { prefs ->
            if (draftsJson == null) prefs.remove(KEY_DRAFTS_JSON) else prefs[KEY_DRAFTS_JSON] = draftsJson
        }
    }

    suspend fun clearSession() = withContext(ioDispatcher) {
        dataStore.edit { prefs ->
            prefs[KEY_IS_ACTIVE] = false
            prefs.remove(KEY_WORKOUT_ID)
            prefs.remove(KEY_IS_PAUSED)
            prefs.remove(KEY_WORKOUT_ELAPSED)
            prefs.remove(KEY_IS_RESTING)
            prefs.remove(KEY_REST_REMAINING)
            prefs.remove(KEY_LAST_TICK_MS)
            prefs.remove(KEY_DRAFTS_JSON)
        }
    }

    companion object {
        private val KEY_IS_ACTIVE = booleanPreferencesKey("active_session_is_active")
        private val KEY_WORKOUT_ID = longPreferencesKey("active_session_workout_id")
        private val KEY_IS_PAUSED = booleanPreferencesKey("active_session_is_paused")
        private val KEY_WORKOUT_ELAPSED = intPreferencesKey("active_session_workout_elapsed_seconds")
        private val KEY_IS_RESTING = booleanPreferencesKey("active_session_is_resting")
        private val KEY_REST_REMAINING = intPreferencesKey("active_session_rest_remaining_seconds")
        private val KEY_LAST_TICK_MS = longPreferencesKey("active_session_last_tick_ms")
        private val KEY_DRAFTS_JSON = stringPreferencesKey("active_session_drafts_json")
    }
}


