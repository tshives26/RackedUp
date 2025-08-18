package com.chilluminati.rackedup.core.util;

/**
 * App-wide constants
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u000b\n\u0002\u0010\t\n\u0002\b\u0003\b\u00c7\u0002\u0018\u00002\u00020\u0001:\u0001\u0014B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0006X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0006X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0006X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0006X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u0006X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0012X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0015"}, d2 = {"Lcom/chilluminati/rackedup/core/util/Constants;", "", "()V", "BACKUP_FILE_EXTENSION", "", "CHART_ANIMATION_DURATION", "", "DATABASE_NAME", "DEFAULT_REST_TIME_SECONDS", "DEFAULT_WORKOUT_TIME_SECONDS", "EXPORT_DATE_FORMAT", "HEALTH_CONNECT_PACKAGE_NAME", "MAX_PROGRESS_PHOTOS", "MIN_WORKOUTS_FOR_AI_SUGGESTIONS", "PHOTO_COMPRESSION_QUALITY", "PREFERENCES_NAME", "REMINDER_NOTIFICATION_CHANNEL_ID", "SPLASH_DELAY_MS", "", "WORKOUT_NOTIFICATION_CHANNEL_ID", "PreferenceKeys", "app_debug"})
public final class Constants {
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String DATABASE_NAME = "rackedup_database";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String PREFERENCES_NAME = "rackedup_preferences";
    public static final int DEFAULT_REST_TIME_SECONDS = 60;
    public static final int DEFAULT_WORKOUT_TIME_SECONDS = 180;
    public static final int CHART_ANIMATION_DURATION = 1000;
    public static final long SPLASH_DELAY_MS = 2000L;
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String EXPORT_DATE_FORMAT = "yyyy-MM-dd_HH-mm-ss";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String BACKUP_FILE_EXTENSION = ".rub";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String HEALTH_CONNECT_PACKAGE_NAME = "com.google.android.apps.healthdata";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String WORKOUT_NOTIFICATION_CHANNEL_ID = "workout_channel";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String REMINDER_NOTIFICATION_CHANNEL_ID = "reminder_channel";
    public static final int MAX_PROGRESS_PHOTOS = 10;
    public static final int PHOTO_COMPRESSION_QUALITY = 80;
    public static final int MIN_WORKOUTS_FOR_AI_SUGGESTIONS = 5;
    @org.jetbrains.annotations.NotNull()
    public static final com.chilluminati.rackedup.core.util.Constants INSTANCE = null;
    
    private Constants() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\b\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\f"}, d2 = {"Lcom/chilluminati/rackedup/core/util/Constants$PreferenceKeys;", "", "()V", "AUTO_BACKUP_ENABLED", "", "DYNAMIC_COLOR", "FIRST_TIME_USER", "REST_TIMER_SOUND_ENABLED", "SELECTED_PROFILE_ID", "THEME_MODE", "VIBRATION_ENABLED", "WORKOUT_REMINDERS_ENABLED", "app_debug"})
    public static final class PreferenceKeys {
        @org.jetbrains.annotations.NotNull()
        public static final java.lang.String THEME_MODE = "theme_mode";
        @org.jetbrains.annotations.NotNull()
        public static final java.lang.String DYNAMIC_COLOR = "dynamic_color";
        @org.jetbrains.annotations.NotNull()
        public static final java.lang.String FIRST_TIME_USER = "first_time_user";
        @org.jetbrains.annotations.NotNull()
        public static final java.lang.String SELECTED_PROFILE_ID = "selected_profile_id";
        @org.jetbrains.annotations.NotNull()
        public static final java.lang.String AUTO_BACKUP_ENABLED = "auto_backup_enabled";
        @org.jetbrains.annotations.NotNull()
        public static final java.lang.String WORKOUT_REMINDERS_ENABLED = "workout_reminders_enabled";
        @org.jetbrains.annotations.NotNull()
        public static final java.lang.String REST_TIMER_SOUND_ENABLED = "rest_timer_sound_enabled";
        @org.jetbrains.annotations.NotNull()
        public static final java.lang.String VIBRATION_ENABLED = "vibration_enabled";
        @org.jetbrains.annotations.NotNull()
        public static final com.chilluminati.rackedup.core.util.Constants.PreferenceKeys INSTANCE = null;
        
        private PreferenceKeys() {
            super();
        }
    }
}