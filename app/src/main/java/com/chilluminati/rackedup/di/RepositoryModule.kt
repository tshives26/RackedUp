package com.chilluminati.rackedup.di

import com.chilluminati.rackedup.data.database.dao.PersonalRecordDao
import com.chilluminati.rackedup.data.database.dao.ProgramDao
import com.chilluminati.rackedup.data.database.dao.WorkoutDao
import com.chilluminati.rackedup.data.database.dao.WorkoutExerciseDao
import com.chilluminati.rackedup.data.database.dao.ExerciseDao
import com.chilluminati.rackedup.data.database.dao.ExerciseSetDao
import com.chilluminati.rackedup.data.database.dao.BodyMeasurementDao
import com.chilluminati.rackedup.data.database.dao.UserProfileDao
import com.chilluminati.rackedup.data.repository.AchievementsRepository
import com.chilluminati.rackedup.data.repository.ProgramRepository
import com.chilluminati.rackedup.data.repository.BodyMeasurementRepository
import com.chilluminati.rackedup.data.repository.ProgressRepository
import com.chilluminati.rackedup.data.repository.WorkoutRepository
// (remove duplicate ProgramDao import)
import com.chilluminati.rackedup.data.database.dao.ProgramDayDao
import com.chilluminati.rackedup.data.database.dao.ProgramExerciseDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineDispatcher

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideAchievementsRepository(
        workoutDao: WorkoutDao,
        personalRecordDao: PersonalRecordDao,
        programDao: ProgramDao,
        workoutExerciseDao: WorkoutExerciseDao,
        exerciseSetDao: ExerciseSetDao,
        exerciseDao: ExerciseDao,
        bodyMeasurementDao: BodyMeasurementDao,
        userProfileDao: UserProfileDao,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): AchievementsRepository = AchievementsRepository(
        workoutDao = workoutDao,
        personalRecordDao = personalRecordDao,
        programDao = programDao,
        workoutExerciseDao = workoutExerciseDao,
        exerciseSetDao = exerciseSetDao,
        exerciseDao = exerciseDao,
        bodyMeasurementDao = bodyMeasurementDao,
        userProfileDao = userProfileDao,
        ioDispatcher = ioDispatcher
    )

    @Provides
    @Singleton
    fun provideProgramRepository(
        @IoDispatcher ioDispatcher: CoroutineDispatcher,
        programDao: ProgramDao,
        programDayDao: ProgramDayDao,
        programExerciseDao: ProgramExerciseDao
    ): ProgramRepository = ProgramRepository(
        ioDispatcher = ioDispatcher,
        programDao = programDao,
        programDayDao = programDayDao,
        programExerciseDao = programExerciseDao
    )

    @Provides
    @Singleton
    fun provideBodyMeasurementRepository(
        bodyMeasurementDao: BodyMeasurementDao,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): BodyMeasurementRepository = BodyMeasurementRepository(
        bodyMeasurementDao = bodyMeasurementDao,
        ioDispatcher = ioDispatcher
    )

    @Provides
    @Singleton
    fun provideProgressRepository(
        workoutDao: WorkoutDao,
        exerciseSetDao: ExerciseSetDao,
        exerciseDao: ExerciseDao,
        workoutExerciseDao: WorkoutExerciseDao,
        personalRecordDao: PersonalRecordDao,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): ProgressRepository = ProgressRepository(
        workoutDao = workoutDao,
        exerciseSetDao = exerciseSetDao,
        exerciseDao = exerciseDao,
        workoutExerciseDao = workoutExerciseDao,
        personalRecordDao = personalRecordDao,
        ioDispatcher = ioDispatcher
    )

    @Provides
    @Singleton
    fun provideWorkoutRepository(
        workoutDao: WorkoutDao,
        workoutExerciseDao: WorkoutExerciseDao,
        exerciseSetDao: ExerciseSetDao,
        progressRepository: ProgressRepository,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): WorkoutRepository = WorkoutRepository(
        workoutDao = workoutDao,
        workoutExerciseDao = workoutExerciseDao,
        exerciseSetDao = exerciseSetDao,
        progressRepository = progressRepository,
        ioDispatcher = ioDispatcher
    )
}


