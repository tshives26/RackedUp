package com.chilluminati.rackedup.di

import android.content.Context
import com.chilluminati.rackedup.core.sound.SoundManager
import com.chilluminati.rackedup.data.repository.SettingsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SoundModule {
    @Provides
    @Singleton
    fun provideSoundManager(
        @ApplicationContext context: Context,
        settingsRepository: SettingsRepository
    ): SoundManager {
        return SoundManager(context, settingsRepository)
    }
}
