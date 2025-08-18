package com.chilluminati.rackedup.di

import com.chilluminati.rackedup.data.database.dao.PersonalRecordDao
import com.chilluminati.rackedup.data.database.dao.ProgramDao
import com.chilluminati.rackedup.data.database.dao.WorkoutDao
import com.chilluminati.rackedup.data.database.dao.WorkoutExerciseDao
import com.chilluminati.rackedup.data.database.dao.ExerciseDao
import com.chilluminati.rackedup.data.database.dao.BodyMeasurementDao
import com.chilluminati.rackedup.data.database.dao.UserProfileDao
import com.chilluminati.rackedup.data.repository.AchievementsRepository
import com.chilluminati.rackedup.data.repository.ProgramRepository
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
        exerciseDao: ExerciseDao,
        bodyMeasurementDao: BodyMeasurementDao,
        userProfileDao: UserProfileDao,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): AchievementsRepository = AchievementsRepository(
        workoutDao = workoutDao,
        personalRecordDao = personalRecordDao,
        programDao = programDao,
        workoutExerciseDao = workoutExerciseDao,
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
}


