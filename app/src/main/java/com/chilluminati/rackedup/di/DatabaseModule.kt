package com.chilluminati.rackedup.di

import android.content.Context
import androidx.room.Room
import com.chilluminati.rackedup.core.util.Constants.DATABASE_NAME
import com.chilluminati.rackedup.data.database.RackedUpDatabase
import com.chilluminati.rackedup.data.database.dao.*
import com.chilluminati.rackedup.data.database.migrations.MIGRATION_1_2
import com.chilluminati.rackedup.data.database.migrations.MIGRATION_2_3
import com.chilluminati.rackedup.data.database.migrations.MIGRATION_3_4
import com.chilluminati.rackedup.data.database.migrations.MIGRATION_4_5
import com.chilluminati.rackedup.data.repository.UserProfileRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module for database dependencies
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideRackedUpDatabase(@ApplicationContext context: Context): RackedUpDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            RackedUpDatabase::class.java,
            DATABASE_NAME
        )
            // Remove fallbackToDestructiveMigration() to prevent data loss
            // Add proper migrations instead
            .addMigrations(
                MIGRATION_1_2,
                MIGRATION_2_3,
                MIGRATION_3_4,
                MIGRATION_4_5
            )
            .build()
    }

    @Provides
    fun provideExerciseDao(database: RackedUpDatabase): ExerciseDao = database.exerciseDao()

    @Provides
    fun provideWorkoutDao(database: RackedUpDatabase): WorkoutDao = database.workoutDao()

    @Provides
    fun provideWorkoutExerciseDao(database: RackedUpDatabase): WorkoutExerciseDao = 
        database.workoutExerciseDao()

    @Provides
    fun provideExerciseSetDao(database: RackedUpDatabase): ExerciseSetDao = 
        database.exerciseSetDao()

    @Provides
    fun provideUserProfileDao(database: RackedUpDatabase): UserProfileDao = 
        database.userProfileDao()

    @Provides
    fun provideProgramDao(database: RackedUpDatabase): ProgramDao = database.programDao()

    @Provides
    fun provideProgramDayDao(database: RackedUpDatabase): ProgramDayDao = 
        database.programDayDao()

    @Provides
    fun provideProgramExerciseDao(database: RackedUpDatabase): ProgramExerciseDao = 
        database.programExerciseDao()

    @Provides
    fun providePersonalRecordDao(database: RackedUpDatabase): PersonalRecordDao = 
        database.personalRecordDao()

    @Provides
    fun provideBodyMeasurementDao(database: RackedUpDatabase): BodyMeasurementDao = 
        database.bodyMeasurementDao()

    @Provides
    @Singleton
    fun provideUserProfileRepository(userProfileDao: UserProfileDao): UserProfileRepository =
        UserProfileRepository(userProfileDao)
}
