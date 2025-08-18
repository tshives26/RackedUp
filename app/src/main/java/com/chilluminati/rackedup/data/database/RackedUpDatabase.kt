package com.chilluminati.rackedup.data.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
import com.chilluminati.rackedup.data.database.dao.*
import com.chilluminati.rackedup.data.database.entity.*
import com.chilluminati.rackedup.data.database.converter.Converters
import com.chilluminati.rackedup.core.util.Constants.DATABASE_NAME

/**
 * Main Room database for RackedUp
 */
@Database(
    entities = [
        Exercise::class,
        Workout::class,
        WorkoutExercise::class,
        ExerciseSet::class,
        UserProfile::class,
        Program::class,
        ProgramDay::class,
        ProgramExercise::class,
        PersonalRecord::class,
        BodyMeasurement::class
    ],
    version = 2,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class RackedUpDatabase : RoomDatabase() {

    abstract fun exerciseDao(): ExerciseDao
    abstract fun workoutDao(): WorkoutDao
    abstract fun workoutExerciseDao(): WorkoutExerciseDao
    abstract fun exerciseSetDao(): ExerciseSetDao
    abstract fun userProfileDao(): UserProfileDao
    abstract fun programDao(): ProgramDao
    abstract fun programDayDao(): ProgramDayDao
    abstract fun programExerciseDao(): ProgramExerciseDao
    abstract fun personalRecordDao(): PersonalRecordDao
    abstract fun bodyMeasurementDao(): BodyMeasurementDao
    
    /**
     * Clear all tables in the database
     */
    suspend fun clearData() {
        clearAllTables()
    }

    companion object {
        @Volatile
        private var INSTANCE: RackedUpDatabase? = null

        fun getDatabase(context: Context): RackedUpDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RackedUpDatabase::class.java,
                    DATABASE_NAME
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
