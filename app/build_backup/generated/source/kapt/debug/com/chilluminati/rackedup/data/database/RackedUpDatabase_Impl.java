package com.chilluminati.rackedup.data.database;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.chilluminati.rackedup.data.database.dao.BodyMeasurementDao;
import com.chilluminati.rackedup.data.database.dao.BodyMeasurementDao_Impl;
import com.chilluminati.rackedup.data.database.dao.ExerciseDao;
import com.chilluminati.rackedup.data.database.dao.ExerciseDao_Impl;
import com.chilluminati.rackedup.data.database.dao.ExerciseSetDao;
import com.chilluminati.rackedup.data.database.dao.ExerciseSetDao_Impl;
import com.chilluminati.rackedup.data.database.dao.PersonalRecordDao;
import com.chilluminati.rackedup.data.database.dao.PersonalRecordDao_Impl;
import com.chilluminati.rackedup.data.database.dao.ProgramDao;
import com.chilluminati.rackedup.data.database.dao.ProgramDao_Impl;
import com.chilluminati.rackedup.data.database.dao.ProgramDayDao;
import com.chilluminati.rackedup.data.database.dao.ProgramDayDao_Impl;
import com.chilluminati.rackedup.data.database.dao.ProgramExerciseDao;
import com.chilluminati.rackedup.data.database.dao.ProgramExerciseDao_Impl;
import com.chilluminati.rackedup.data.database.dao.UserProfileDao;
import com.chilluminati.rackedup.data.database.dao.UserProfileDao_Impl;
import com.chilluminati.rackedup.data.database.dao.WorkoutDao;
import com.chilluminati.rackedup.data.database.dao.WorkoutDao_Impl;
import com.chilluminati.rackedup.data.database.dao.WorkoutExerciseDao;
import com.chilluminati.rackedup.data.database.dao.WorkoutExerciseDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class RackedUpDatabase_Impl extends RackedUpDatabase {
  private volatile ExerciseDao _exerciseDao;

  private volatile WorkoutDao _workoutDao;

  private volatile WorkoutExerciseDao _workoutExerciseDao;

  private volatile ExerciseSetDao _exerciseSetDao;

  private volatile UserProfileDao _userProfileDao;

  private volatile ProgramDao _programDao;

  private volatile ProgramDayDao _programDayDao;

  private volatile ProgramExerciseDao _programExerciseDao;

  private volatile PersonalRecordDao _personalRecordDao;

  private volatile BodyMeasurementDao _bodyMeasurementDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `exercises` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `category` TEXT NOT NULL, `subcategory` TEXT, `equipment` TEXT NOT NULL, `exercise_type` TEXT NOT NULL, `difficulty_level` TEXT NOT NULL, `instructions` TEXT, `tips` TEXT, `muscle_groups` TEXT NOT NULL, `secondary_muscles` TEXT NOT NULL, `is_compound` INTEGER NOT NULL, `is_unilateral` INTEGER NOT NULL, `is_custom` INTEGER NOT NULL, `is_favorite` INTEGER NOT NULL, `image_url` TEXT, `video_url` TEXT, `created_at` INTEGER NOT NULL, `updated_at` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `workouts` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `date` INTEGER NOT NULL, `start_time` INTEGER, `end_time` INTEGER, `duration_minutes` INTEGER, `notes` TEXT, `total_volume` REAL NOT NULL, `total_sets` INTEGER NOT NULL, `total_reps` INTEGER NOT NULL, `program_id` INTEGER, `program_day_id` INTEGER, `is_template` INTEGER NOT NULL, `is_completed` INTEGER NOT NULL, `is_favorite` INTEGER NOT NULL, `rating` INTEGER, `created_at` INTEGER NOT NULL, `updated_at` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `workout_exercises` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `workout_id` INTEGER NOT NULL, `exercise_id` INTEGER NOT NULL, `order_index` INTEGER NOT NULL, `rest_time_seconds` INTEGER, `notes` TEXT, `is_superset` INTEGER NOT NULL, `superset_id` TEXT, `is_dropset` INTEGER NOT NULL, `target_sets` INTEGER, `target_reps` INTEGER, `target_weight` REAL, `created_at` INTEGER NOT NULL, FOREIGN KEY(`workout_id`) REFERENCES `workouts`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`exercise_id`) REFERENCES `exercises`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_workout_exercises_workout_id` ON `workout_exercises` (`workout_id`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_workout_exercises_exercise_id` ON `workout_exercises` (`exercise_id`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `exercise_sets` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `workout_exercise_id` INTEGER NOT NULL, `set_number` INTEGER NOT NULL, `weight` REAL, `reps` INTEGER, `duration_seconds` INTEGER, `distance` REAL, `rest_time_seconds` INTEGER, `rpe` INTEGER, `is_warmup` INTEGER NOT NULL, `is_dropset` INTEGER NOT NULL, `is_failure` INTEGER NOT NULL, `is_completed` INTEGER NOT NULL, `notes` TEXT, `created_at` INTEGER NOT NULL, FOREIGN KEY(`workout_exercise_id`) REFERENCES `workout_exercises`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_exercise_sets_workout_exercise_id` ON `exercise_sets` (`workout_exercise_id`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `user_profiles` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT, `email` TEXT, `age` INTEGER, `gender` TEXT, `height_cm` REAL, `weight_kg` REAL, `activity_level` TEXT, `fitness_goal` TEXT, `experience_level` TEXT, `preferred_weight_unit` TEXT NOT NULL, `preferred_distance_unit` TEXT NOT NULL, `default_rest_time` INTEGER NOT NULL, `timezone` TEXT, `profile_image_url` TEXT, `is_active` INTEGER NOT NULL, `created_at` INTEGER NOT NULL, `updated_at` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `programs` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `description` TEXT, `duration_weeks` INTEGER, `difficulty_level` TEXT NOT NULL, `program_type` TEXT NOT NULL, `days_per_week` INTEGER NOT NULL, `goal` TEXT, `author` TEXT, `is_custom` INTEGER NOT NULL, `is_template` INTEGER NOT NULL, `is_active` INTEGER NOT NULL, `is_favorite` INTEGER NOT NULL, `current_week` INTEGER NOT NULL, `current_day` INTEGER NOT NULL, `progression_scheme` TEXT, `deload_week` INTEGER, `notes` TEXT, `start_date` INTEGER, `end_date` INTEGER, `created_at` INTEGER NOT NULL, `updated_at` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `program_days` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `program_id` INTEGER NOT NULL, `day_number` INTEGER NOT NULL, `week_number` INTEGER NOT NULL, `name` TEXT NOT NULL, `description` TEXT, `day_type` TEXT, `muscle_groups` TEXT NOT NULL, `estimated_duration` INTEGER, `is_rest_day` INTEGER NOT NULL, `notes` TEXT, `created_at` INTEGER NOT NULL, FOREIGN KEY(`program_id`) REFERENCES `programs`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_program_days_program_id` ON `program_days` (`program_id`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `program_exercises` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `program_day_id` INTEGER NOT NULL, `exercise_id` INTEGER NOT NULL, `order_index` INTEGER NOT NULL, `sets` TEXT NOT NULL, `reps` TEXT, `weight_percentage` REAL, `rest_time_seconds` INTEGER, `rpe_target` INTEGER, `progression_scheme` TEXT, `progression_increment` REAL, `is_superset` INTEGER NOT NULL, `superset_id` TEXT, `notes` TEXT, `created_at` INTEGER NOT NULL, FOREIGN KEY(`program_day_id`) REFERENCES `program_days`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`exercise_id`) REFERENCES `exercises`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_program_exercises_program_day_id` ON `program_exercises` (`program_day_id`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_program_exercises_exercise_id` ON `program_exercises` (`exercise_id`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `personal_records` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `exercise_id` INTEGER NOT NULL, `record_type` TEXT NOT NULL, `weight` REAL, `reps` INTEGER, `distance` REAL, `duration_seconds` INTEGER, `volume` REAL, `estimated_1rm` REAL, `previous_value` REAL, `improvement` REAL, `workout_id` INTEGER, `notes` TEXT, `achieved_at` INTEGER NOT NULL, `created_at` INTEGER NOT NULL, FOREIGN KEY(`exercise_id`) REFERENCES `exercises`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_personal_records_exercise_id` ON `personal_records` (`exercise_id`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `body_measurements` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `measurement_type` TEXT NOT NULL, `value` REAL NOT NULL, `unit` TEXT NOT NULL, `body_part` TEXT, `measurement_method` TEXT, `notes` TEXT, `photo_url` TEXT, `measured_at` INTEGER NOT NULL, `created_at` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '3b79c4f0f5b524f4f4835c83f741c2f1')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `exercises`");
        db.execSQL("DROP TABLE IF EXISTS `workouts`");
        db.execSQL("DROP TABLE IF EXISTS `workout_exercises`");
        db.execSQL("DROP TABLE IF EXISTS `exercise_sets`");
        db.execSQL("DROP TABLE IF EXISTS `user_profiles`");
        db.execSQL("DROP TABLE IF EXISTS `programs`");
        db.execSQL("DROP TABLE IF EXISTS `program_days`");
        db.execSQL("DROP TABLE IF EXISTS `program_exercises`");
        db.execSQL("DROP TABLE IF EXISTS `personal_records`");
        db.execSQL("DROP TABLE IF EXISTS `body_measurements`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        db.execSQL("PRAGMA foreign_keys = ON");
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsExercises = new HashMap<String, TableInfo.Column>(19);
        _columnsExercises.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExercises.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExercises.put("category", new TableInfo.Column("category", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExercises.put("subcategory", new TableInfo.Column("subcategory", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExercises.put("equipment", new TableInfo.Column("equipment", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExercises.put("exercise_type", new TableInfo.Column("exercise_type", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExercises.put("difficulty_level", new TableInfo.Column("difficulty_level", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExercises.put("instructions", new TableInfo.Column("instructions", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExercises.put("tips", new TableInfo.Column("tips", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExercises.put("muscle_groups", new TableInfo.Column("muscle_groups", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExercises.put("secondary_muscles", new TableInfo.Column("secondary_muscles", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExercises.put("is_compound", new TableInfo.Column("is_compound", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExercises.put("is_unilateral", new TableInfo.Column("is_unilateral", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExercises.put("is_custom", new TableInfo.Column("is_custom", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExercises.put("is_favorite", new TableInfo.Column("is_favorite", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExercises.put("image_url", new TableInfo.Column("image_url", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExercises.put("video_url", new TableInfo.Column("video_url", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExercises.put("created_at", new TableInfo.Column("created_at", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExercises.put("updated_at", new TableInfo.Column("updated_at", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysExercises = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesExercises = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoExercises = new TableInfo("exercises", _columnsExercises, _foreignKeysExercises, _indicesExercises);
        final TableInfo _existingExercises = TableInfo.read(db, "exercises");
        if (!_infoExercises.equals(_existingExercises)) {
          return new RoomOpenHelper.ValidationResult(false, "exercises(com.chilluminati.rackedup.data.database.entity.Exercise).\n"
                  + " Expected:\n" + _infoExercises + "\n"
                  + " Found:\n" + _existingExercises);
        }
        final HashMap<String, TableInfo.Column> _columnsWorkouts = new HashMap<String, TableInfo.Column>(18);
        _columnsWorkouts.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWorkouts.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWorkouts.put("date", new TableInfo.Column("date", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWorkouts.put("start_time", new TableInfo.Column("start_time", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWorkouts.put("end_time", new TableInfo.Column("end_time", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWorkouts.put("duration_minutes", new TableInfo.Column("duration_minutes", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWorkouts.put("notes", new TableInfo.Column("notes", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWorkouts.put("total_volume", new TableInfo.Column("total_volume", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWorkouts.put("total_sets", new TableInfo.Column("total_sets", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWorkouts.put("total_reps", new TableInfo.Column("total_reps", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWorkouts.put("program_id", new TableInfo.Column("program_id", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWorkouts.put("program_day_id", new TableInfo.Column("program_day_id", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWorkouts.put("is_template", new TableInfo.Column("is_template", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWorkouts.put("is_completed", new TableInfo.Column("is_completed", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWorkouts.put("is_favorite", new TableInfo.Column("is_favorite", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWorkouts.put("rating", new TableInfo.Column("rating", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWorkouts.put("created_at", new TableInfo.Column("created_at", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWorkouts.put("updated_at", new TableInfo.Column("updated_at", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysWorkouts = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesWorkouts = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoWorkouts = new TableInfo("workouts", _columnsWorkouts, _foreignKeysWorkouts, _indicesWorkouts);
        final TableInfo _existingWorkouts = TableInfo.read(db, "workouts");
        if (!_infoWorkouts.equals(_existingWorkouts)) {
          return new RoomOpenHelper.ValidationResult(false, "workouts(com.chilluminati.rackedup.data.database.entity.Workout).\n"
                  + " Expected:\n" + _infoWorkouts + "\n"
                  + " Found:\n" + _existingWorkouts);
        }
        final HashMap<String, TableInfo.Column> _columnsWorkoutExercises = new HashMap<String, TableInfo.Column>(13);
        _columnsWorkoutExercises.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWorkoutExercises.put("workout_id", new TableInfo.Column("workout_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWorkoutExercises.put("exercise_id", new TableInfo.Column("exercise_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWorkoutExercises.put("order_index", new TableInfo.Column("order_index", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWorkoutExercises.put("rest_time_seconds", new TableInfo.Column("rest_time_seconds", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWorkoutExercises.put("notes", new TableInfo.Column("notes", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWorkoutExercises.put("is_superset", new TableInfo.Column("is_superset", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWorkoutExercises.put("superset_id", new TableInfo.Column("superset_id", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWorkoutExercises.put("is_dropset", new TableInfo.Column("is_dropset", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWorkoutExercises.put("target_sets", new TableInfo.Column("target_sets", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWorkoutExercises.put("target_reps", new TableInfo.Column("target_reps", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWorkoutExercises.put("target_weight", new TableInfo.Column("target_weight", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWorkoutExercises.put("created_at", new TableInfo.Column("created_at", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysWorkoutExercises = new HashSet<TableInfo.ForeignKey>(2);
        _foreignKeysWorkoutExercises.add(new TableInfo.ForeignKey("workouts", "CASCADE", "NO ACTION", Arrays.asList("workout_id"), Arrays.asList("id")));
        _foreignKeysWorkoutExercises.add(new TableInfo.ForeignKey("exercises", "CASCADE", "NO ACTION", Arrays.asList("exercise_id"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesWorkoutExercises = new HashSet<TableInfo.Index>(2);
        _indicesWorkoutExercises.add(new TableInfo.Index("index_workout_exercises_workout_id", false, Arrays.asList("workout_id"), Arrays.asList("ASC")));
        _indicesWorkoutExercises.add(new TableInfo.Index("index_workout_exercises_exercise_id", false, Arrays.asList("exercise_id"), Arrays.asList("ASC")));
        final TableInfo _infoWorkoutExercises = new TableInfo("workout_exercises", _columnsWorkoutExercises, _foreignKeysWorkoutExercises, _indicesWorkoutExercises);
        final TableInfo _existingWorkoutExercises = TableInfo.read(db, "workout_exercises");
        if (!_infoWorkoutExercises.equals(_existingWorkoutExercises)) {
          return new RoomOpenHelper.ValidationResult(false, "workout_exercises(com.chilluminati.rackedup.data.database.entity.WorkoutExercise).\n"
                  + " Expected:\n" + _infoWorkoutExercises + "\n"
                  + " Found:\n" + _existingWorkoutExercises);
        }
        final HashMap<String, TableInfo.Column> _columnsExerciseSets = new HashMap<String, TableInfo.Column>(15);
        _columnsExerciseSets.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExerciseSets.put("workout_exercise_id", new TableInfo.Column("workout_exercise_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExerciseSets.put("set_number", new TableInfo.Column("set_number", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExerciseSets.put("weight", new TableInfo.Column("weight", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExerciseSets.put("reps", new TableInfo.Column("reps", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExerciseSets.put("duration_seconds", new TableInfo.Column("duration_seconds", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExerciseSets.put("distance", new TableInfo.Column("distance", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExerciseSets.put("rest_time_seconds", new TableInfo.Column("rest_time_seconds", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExerciseSets.put("rpe", new TableInfo.Column("rpe", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExerciseSets.put("is_warmup", new TableInfo.Column("is_warmup", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExerciseSets.put("is_dropset", new TableInfo.Column("is_dropset", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExerciseSets.put("is_failure", new TableInfo.Column("is_failure", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExerciseSets.put("is_completed", new TableInfo.Column("is_completed", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExerciseSets.put("notes", new TableInfo.Column("notes", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExerciseSets.put("created_at", new TableInfo.Column("created_at", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysExerciseSets = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysExerciseSets.add(new TableInfo.ForeignKey("workout_exercises", "CASCADE", "NO ACTION", Arrays.asList("workout_exercise_id"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesExerciseSets = new HashSet<TableInfo.Index>(1);
        _indicesExerciseSets.add(new TableInfo.Index("index_exercise_sets_workout_exercise_id", false, Arrays.asList("workout_exercise_id"), Arrays.asList("ASC")));
        final TableInfo _infoExerciseSets = new TableInfo("exercise_sets", _columnsExerciseSets, _foreignKeysExerciseSets, _indicesExerciseSets);
        final TableInfo _existingExerciseSets = TableInfo.read(db, "exercise_sets");
        if (!_infoExerciseSets.equals(_existingExerciseSets)) {
          return new RoomOpenHelper.ValidationResult(false, "exercise_sets(com.chilluminati.rackedup.data.database.entity.ExerciseSet).\n"
                  + " Expected:\n" + _infoExerciseSets + "\n"
                  + " Found:\n" + _existingExerciseSets);
        }
        final HashMap<String, TableInfo.Column> _columnsUserProfiles = new HashMap<String, TableInfo.Column>(18);
        _columnsUserProfiles.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserProfiles.put("name", new TableInfo.Column("name", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserProfiles.put("email", new TableInfo.Column("email", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserProfiles.put("age", new TableInfo.Column("age", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserProfiles.put("gender", new TableInfo.Column("gender", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserProfiles.put("height_cm", new TableInfo.Column("height_cm", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserProfiles.put("weight_kg", new TableInfo.Column("weight_kg", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserProfiles.put("activity_level", new TableInfo.Column("activity_level", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserProfiles.put("fitness_goal", new TableInfo.Column("fitness_goal", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserProfiles.put("experience_level", new TableInfo.Column("experience_level", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserProfiles.put("preferred_weight_unit", new TableInfo.Column("preferred_weight_unit", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserProfiles.put("preferred_distance_unit", new TableInfo.Column("preferred_distance_unit", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserProfiles.put("default_rest_time", new TableInfo.Column("default_rest_time", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserProfiles.put("timezone", new TableInfo.Column("timezone", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserProfiles.put("profile_image_url", new TableInfo.Column("profile_image_url", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserProfiles.put("is_active", new TableInfo.Column("is_active", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserProfiles.put("created_at", new TableInfo.Column("created_at", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserProfiles.put("updated_at", new TableInfo.Column("updated_at", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysUserProfiles = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesUserProfiles = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoUserProfiles = new TableInfo("user_profiles", _columnsUserProfiles, _foreignKeysUserProfiles, _indicesUserProfiles);
        final TableInfo _existingUserProfiles = TableInfo.read(db, "user_profiles");
        if (!_infoUserProfiles.equals(_existingUserProfiles)) {
          return new RoomOpenHelper.ValidationResult(false, "user_profiles(com.chilluminati.rackedup.data.database.entity.UserProfile).\n"
                  + " Expected:\n" + _infoUserProfiles + "\n"
                  + " Found:\n" + _existingUserProfiles);
        }
        final HashMap<String, TableInfo.Column> _columnsPrograms = new HashMap<String, TableInfo.Column>(22);
        _columnsPrograms.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPrograms.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPrograms.put("description", new TableInfo.Column("description", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPrograms.put("duration_weeks", new TableInfo.Column("duration_weeks", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPrograms.put("difficulty_level", new TableInfo.Column("difficulty_level", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPrograms.put("program_type", new TableInfo.Column("program_type", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPrograms.put("days_per_week", new TableInfo.Column("days_per_week", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPrograms.put("goal", new TableInfo.Column("goal", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPrograms.put("author", new TableInfo.Column("author", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPrograms.put("is_custom", new TableInfo.Column("is_custom", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPrograms.put("is_template", new TableInfo.Column("is_template", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPrograms.put("is_active", new TableInfo.Column("is_active", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPrograms.put("is_favorite", new TableInfo.Column("is_favorite", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPrograms.put("current_week", new TableInfo.Column("current_week", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPrograms.put("current_day", new TableInfo.Column("current_day", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPrograms.put("progression_scheme", new TableInfo.Column("progression_scheme", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPrograms.put("deload_week", new TableInfo.Column("deload_week", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPrograms.put("notes", new TableInfo.Column("notes", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPrograms.put("start_date", new TableInfo.Column("start_date", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPrograms.put("end_date", new TableInfo.Column("end_date", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPrograms.put("created_at", new TableInfo.Column("created_at", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPrograms.put("updated_at", new TableInfo.Column("updated_at", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysPrograms = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesPrograms = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoPrograms = new TableInfo("programs", _columnsPrograms, _foreignKeysPrograms, _indicesPrograms);
        final TableInfo _existingPrograms = TableInfo.read(db, "programs");
        if (!_infoPrograms.equals(_existingPrograms)) {
          return new RoomOpenHelper.ValidationResult(false, "programs(com.chilluminati.rackedup.data.database.entity.Program).\n"
                  + " Expected:\n" + _infoPrograms + "\n"
                  + " Found:\n" + _existingPrograms);
        }
        final HashMap<String, TableInfo.Column> _columnsProgramDays = new HashMap<String, TableInfo.Column>(12);
        _columnsProgramDays.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProgramDays.put("program_id", new TableInfo.Column("program_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProgramDays.put("day_number", new TableInfo.Column("day_number", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProgramDays.put("week_number", new TableInfo.Column("week_number", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProgramDays.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProgramDays.put("description", new TableInfo.Column("description", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProgramDays.put("day_type", new TableInfo.Column("day_type", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProgramDays.put("muscle_groups", new TableInfo.Column("muscle_groups", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProgramDays.put("estimated_duration", new TableInfo.Column("estimated_duration", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProgramDays.put("is_rest_day", new TableInfo.Column("is_rest_day", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProgramDays.put("notes", new TableInfo.Column("notes", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProgramDays.put("created_at", new TableInfo.Column("created_at", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysProgramDays = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysProgramDays.add(new TableInfo.ForeignKey("programs", "CASCADE", "NO ACTION", Arrays.asList("program_id"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesProgramDays = new HashSet<TableInfo.Index>(1);
        _indicesProgramDays.add(new TableInfo.Index("index_program_days_program_id", false, Arrays.asList("program_id"), Arrays.asList("ASC")));
        final TableInfo _infoProgramDays = new TableInfo("program_days", _columnsProgramDays, _foreignKeysProgramDays, _indicesProgramDays);
        final TableInfo _existingProgramDays = TableInfo.read(db, "program_days");
        if (!_infoProgramDays.equals(_existingProgramDays)) {
          return new RoomOpenHelper.ValidationResult(false, "program_days(com.chilluminati.rackedup.data.database.entity.ProgramDay).\n"
                  + " Expected:\n" + _infoProgramDays + "\n"
                  + " Found:\n" + _existingProgramDays);
        }
        final HashMap<String, TableInfo.Column> _columnsProgramExercises = new HashMap<String, TableInfo.Column>(15);
        _columnsProgramExercises.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProgramExercises.put("program_day_id", new TableInfo.Column("program_day_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProgramExercises.put("exercise_id", new TableInfo.Column("exercise_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProgramExercises.put("order_index", new TableInfo.Column("order_index", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProgramExercises.put("sets", new TableInfo.Column("sets", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProgramExercises.put("reps", new TableInfo.Column("reps", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProgramExercises.put("weight_percentage", new TableInfo.Column("weight_percentage", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProgramExercises.put("rest_time_seconds", new TableInfo.Column("rest_time_seconds", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProgramExercises.put("rpe_target", new TableInfo.Column("rpe_target", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProgramExercises.put("progression_scheme", new TableInfo.Column("progression_scheme", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProgramExercises.put("progression_increment", new TableInfo.Column("progression_increment", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProgramExercises.put("is_superset", new TableInfo.Column("is_superset", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProgramExercises.put("superset_id", new TableInfo.Column("superset_id", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProgramExercises.put("notes", new TableInfo.Column("notes", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProgramExercises.put("created_at", new TableInfo.Column("created_at", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysProgramExercises = new HashSet<TableInfo.ForeignKey>(2);
        _foreignKeysProgramExercises.add(new TableInfo.ForeignKey("program_days", "CASCADE", "NO ACTION", Arrays.asList("program_day_id"), Arrays.asList("id")));
        _foreignKeysProgramExercises.add(new TableInfo.ForeignKey("exercises", "CASCADE", "NO ACTION", Arrays.asList("exercise_id"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesProgramExercises = new HashSet<TableInfo.Index>(2);
        _indicesProgramExercises.add(new TableInfo.Index("index_program_exercises_program_day_id", false, Arrays.asList("program_day_id"), Arrays.asList("ASC")));
        _indicesProgramExercises.add(new TableInfo.Index("index_program_exercises_exercise_id", false, Arrays.asList("exercise_id"), Arrays.asList("ASC")));
        final TableInfo _infoProgramExercises = new TableInfo("program_exercises", _columnsProgramExercises, _foreignKeysProgramExercises, _indicesProgramExercises);
        final TableInfo _existingProgramExercises = TableInfo.read(db, "program_exercises");
        if (!_infoProgramExercises.equals(_existingProgramExercises)) {
          return new RoomOpenHelper.ValidationResult(false, "program_exercises(com.chilluminati.rackedup.data.database.entity.ProgramExercise).\n"
                  + " Expected:\n" + _infoProgramExercises + "\n"
                  + " Found:\n" + _existingProgramExercises);
        }
        final HashMap<String, TableInfo.Column> _columnsPersonalRecords = new HashMap<String, TableInfo.Column>(15);
        _columnsPersonalRecords.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPersonalRecords.put("exercise_id", new TableInfo.Column("exercise_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPersonalRecords.put("record_type", new TableInfo.Column("record_type", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPersonalRecords.put("weight", new TableInfo.Column("weight", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPersonalRecords.put("reps", new TableInfo.Column("reps", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPersonalRecords.put("distance", new TableInfo.Column("distance", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPersonalRecords.put("duration_seconds", new TableInfo.Column("duration_seconds", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPersonalRecords.put("volume", new TableInfo.Column("volume", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPersonalRecords.put("estimated_1rm", new TableInfo.Column("estimated_1rm", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPersonalRecords.put("previous_value", new TableInfo.Column("previous_value", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPersonalRecords.put("improvement", new TableInfo.Column("improvement", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPersonalRecords.put("workout_id", new TableInfo.Column("workout_id", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPersonalRecords.put("notes", new TableInfo.Column("notes", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPersonalRecords.put("achieved_at", new TableInfo.Column("achieved_at", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPersonalRecords.put("created_at", new TableInfo.Column("created_at", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysPersonalRecords = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysPersonalRecords.add(new TableInfo.ForeignKey("exercises", "CASCADE", "NO ACTION", Arrays.asList("exercise_id"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesPersonalRecords = new HashSet<TableInfo.Index>(1);
        _indicesPersonalRecords.add(new TableInfo.Index("index_personal_records_exercise_id", false, Arrays.asList("exercise_id"), Arrays.asList("ASC")));
        final TableInfo _infoPersonalRecords = new TableInfo("personal_records", _columnsPersonalRecords, _foreignKeysPersonalRecords, _indicesPersonalRecords);
        final TableInfo _existingPersonalRecords = TableInfo.read(db, "personal_records");
        if (!_infoPersonalRecords.equals(_existingPersonalRecords)) {
          return new RoomOpenHelper.ValidationResult(false, "personal_records(com.chilluminati.rackedup.data.database.entity.PersonalRecord).\n"
                  + " Expected:\n" + _infoPersonalRecords + "\n"
                  + " Found:\n" + _existingPersonalRecords);
        }
        final HashMap<String, TableInfo.Column> _columnsBodyMeasurements = new HashMap<String, TableInfo.Column>(10);
        _columnsBodyMeasurements.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBodyMeasurements.put("measurement_type", new TableInfo.Column("measurement_type", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBodyMeasurements.put("value", new TableInfo.Column("value", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBodyMeasurements.put("unit", new TableInfo.Column("unit", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBodyMeasurements.put("body_part", new TableInfo.Column("body_part", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBodyMeasurements.put("measurement_method", new TableInfo.Column("measurement_method", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBodyMeasurements.put("notes", new TableInfo.Column("notes", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBodyMeasurements.put("photo_url", new TableInfo.Column("photo_url", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBodyMeasurements.put("measured_at", new TableInfo.Column("measured_at", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBodyMeasurements.put("created_at", new TableInfo.Column("created_at", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysBodyMeasurements = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesBodyMeasurements = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoBodyMeasurements = new TableInfo("body_measurements", _columnsBodyMeasurements, _foreignKeysBodyMeasurements, _indicesBodyMeasurements);
        final TableInfo _existingBodyMeasurements = TableInfo.read(db, "body_measurements");
        if (!_infoBodyMeasurements.equals(_existingBodyMeasurements)) {
          return new RoomOpenHelper.ValidationResult(false, "body_measurements(com.chilluminati.rackedup.data.database.entity.BodyMeasurement).\n"
                  + " Expected:\n" + _infoBodyMeasurements + "\n"
                  + " Found:\n" + _existingBodyMeasurements);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "3b79c4f0f5b524f4f4835c83f741c2f1", "183e712a69677ded4b7e5067d7f8348d");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "exercises","workouts","workout_exercises","exercise_sets","user_profiles","programs","program_days","program_exercises","personal_records","body_measurements");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    final boolean _supportsDeferForeignKeys = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP;
    try {
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = FALSE");
      }
      super.beginTransaction();
      if (_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA defer_foreign_keys = TRUE");
      }
      _db.execSQL("DELETE FROM `exercises`");
      _db.execSQL("DELETE FROM `workouts`");
      _db.execSQL("DELETE FROM `workout_exercises`");
      _db.execSQL("DELETE FROM `exercise_sets`");
      _db.execSQL("DELETE FROM `user_profiles`");
      _db.execSQL("DELETE FROM `programs`");
      _db.execSQL("DELETE FROM `program_days`");
      _db.execSQL("DELETE FROM `program_exercises`");
      _db.execSQL("DELETE FROM `personal_records`");
      _db.execSQL("DELETE FROM `body_measurements`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = TRUE");
      }
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(ExerciseDao.class, ExerciseDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(WorkoutDao.class, WorkoutDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(WorkoutExerciseDao.class, WorkoutExerciseDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ExerciseSetDao.class, ExerciseSetDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(UserProfileDao.class, UserProfileDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ProgramDao.class, ProgramDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ProgramDayDao.class, ProgramDayDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ProgramExerciseDao.class, ProgramExerciseDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(PersonalRecordDao.class, PersonalRecordDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(BodyMeasurementDao.class, BodyMeasurementDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public ExerciseDao exerciseDao() {
    if (_exerciseDao != null) {
      return _exerciseDao;
    } else {
      synchronized(this) {
        if(_exerciseDao == null) {
          _exerciseDao = new ExerciseDao_Impl(this);
        }
        return _exerciseDao;
      }
    }
  }

  @Override
  public WorkoutDao workoutDao() {
    if (_workoutDao != null) {
      return _workoutDao;
    } else {
      synchronized(this) {
        if(_workoutDao == null) {
          _workoutDao = new WorkoutDao_Impl(this);
        }
        return _workoutDao;
      }
    }
  }

  @Override
  public WorkoutExerciseDao workoutExerciseDao() {
    if (_workoutExerciseDao != null) {
      return _workoutExerciseDao;
    } else {
      synchronized(this) {
        if(_workoutExerciseDao == null) {
          _workoutExerciseDao = new WorkoutExerciseDao_Impl(this);
        }
        return _workoutExerciseDao;
      }
    }
  }

  @Override
  public ExerciseSetDao exerciseSetDao() {
    if (_exerciseSetDao != null) {
      return _exerciseSetDao;
    } else {
      synchronized(this) {
        if(_exerciseSetDao == null) {
          _exerciseSetDao = new ExerciseSetDao_Impl(this);
        }
        return _exerciseSetDao;
      }
    }
  }

  @Override
  public UserProfileDao userProfileDao() {
    if (_userProfileDao != null) {
      return _userProfileDao;
    } else {
      synchronized(this) {
        if(_userProfileDao == null) {
          _userProfileDao = new UserProfileDao_Impl(this);
        }
        return _userProfileDao;
      }
    }
  }

  @Override
  public ProgramDao programDao() {
    if (_programDao != null) {
      return _programDao;
    } else {
      synchronized(this) {
        if(_programDao == null) {
          _programDao = new ProgramDao_Impl(this);
        }
        return _programDao;
      }
    }
  }

  @Override
  public ProgramDayDao programDayDao() {
    if (_programDayDao != null) {
      return _programDayDao;
    } else {
      synchronized(this) {
        if(_programDayDao == null) {
          _programDayDao = new ProgramDayDao_Impl(this);
        }
        return _programDayDao;
      }
    }
  }

  @Override
  public ProgramExerciseDao programExerciseDao() {
    if (_programExerciseDao != null) {
      return _programExerciseDao;
    } else {
      synchronized(this) {
        if(_programExerciseDao == null) {
          _programExerciseDao = new ProgramExerciseDao_Impl(this);
        }
        return _programExerciseDao;
      }
    }
  }

  @Override
  public PersonalRecordDao personalRecordDao() {
    if (_personalRecordDao != null) {
      return _personalRecordDao;
    } else {
      synchronized(this) {
        if(_personalRecordDao == null) {
          _personalRecordDao = new PersonalRecordDao_Impl(this);
        }
        return _personalRecordDao;
      }
    }
  }

  @Override
  public BodyMeasurementDao bodyMeasurementDao() {
    if (_bodyMeasurementDao != null) {
      return _bodyMeasurementDao;
    } else {
      synchronized(this) {
        if(_bodyMeasurementDao == null) {
          _bodyMeasurementDao = new BodyMeasurementDao_Impl(this);
        }
        return _bodyMeasurementDao;
      }
    }
  }
}
