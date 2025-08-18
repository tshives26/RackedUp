package com.chilluminati.rackedup.core.sound

import android.app.NotificationManager
import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import androidx.core.app.NotificationCompat
import com.chilluminati.rackedup.R
import com.chilluminati.rackedup.core.util.Constants
import com.chilluminati.rackedup.data.repository.SettingsRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

class SoundManager(
    @ApplicationContext private val appContext: Context,
    private val settingsRepository: SettingsRepository
) {
    private var mediaPlayer: MediaPlayer? = null

    fun playRestTimerComplete() {
        val uri = Uri.parse("android.resource://${appContext.packageName}/${R.raw.rest_timer_complete}")
        try {
            // Release any existing player
            mediaPlayer?.release()
            
            // Create and configure new player
            mediaPlayer = MediaPlayer().apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_ALARM)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                )
                setDataSource(appContext, uri)
                setOnCompletionListener { mp ->
                    try { mp.release() } catch (_: Exception) {}
                    mediaPlayer = null
                }
                prepare()
                start()
            }
        } catch (e: Exception) {
            // Fallback to notification with sound if MediaPlayer fails
            val manager = appContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val notification = NotificationCompat.Builder(appContext, Constants.ALARM_NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_stat_bolt)
                .setContentTitle(appContext.getString(R.string.app_name))
                .setContentText("Rest timer complete")
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setSound(uri)
                .setAutoCancel(true)
                .build()
            manager.notify(NOTIFICATION_ID, notification)
        }
    }

    fun release() {
        mediaPlayer?.release()
        mediaPlayer = null
    }

    companion object {
        private const val NOTIFICATION_ID = 3003
    }
}
