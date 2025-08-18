package com.chilluminati.rackedup.core.util

/**
 * App-wide constants
 */
object Constants {
    
    // Database
    const val DATABASE_NAME = "rackedup_database"
    
    // Preferences
    const val PREFERENCES_NAME = "rackedup_preferences"
    
    // Workout Timer
    const val DEFAULT_REST_TIME_SECONDS = 60
    const val DEFAULT_WORKOUT_TIME_SECONDS = 180
    
    // Charts
    const val CHART_ANIMATION_DURATION = 1000
    

    
    // File Export
    const val EXPORT_DATE_FORMAT = "yyyy-MM-dd_HH-mm-ss"
    const val BACKUP_FILE_EXTENSION = ".rub" // RackedUp Backup
    const val AUTO_BACKUP_WORK_TAG = "auto_backup_work"
    
    // Health Connect
    const val HEALTH_CONNECT_PACKAGE_NAME = "com.google.android.apps.healthdata"
    
    // Notifications
    const val WORKOUT_NOTIFICATION_CHANNEL_ID = "workout_channel"
    const val ALARM_NOTIFICATION_CHANNEL_ID = "alarm_channel"
    
    // Progress Photos
    const val MAX_PROGRESS_PHOTOS = 10
    const val PHOTO_COMPRESSION_QUALITY = 80
    
    // AI/ML
    const val MIN_WORKOUTS_FOR_AI_SUGGESTIONS = 5
    
    // Shared Preferences Keys
    object PreferenceKeys {
        const val THEME_MODE = "theme_mode"
        const val DYNAMIC_COLOR = "dynamic_color"
        const val COLOR_THEME = "color_theme"
        const val FIRST_TIME_USER = "first_time_user"
        const val SELECTED_PROFILE_ID = "selected_profile_id"
        const val AUTO_BACKUP_ENABLED = "auto_backup_enabled"
        const val BACKUP_FOLDER_URI = "backup_folder_uri"
        const val AUTO_BACKUP_INTERVAL = "auto_backup_interval"
        const val REST_TIMER_SOUND_ENABLED = "rest_timer_sound_enabled"
        const val VIBRATION_ENABLED = "vibration_enabled"
        const val WEIGHT_UNIT = "weight_unit"
        const val DISTANCE_UNIT = "distance_unit"
        const val DEFAULT_REST_SECONDS = "default_rest_seconds"
    }
}
