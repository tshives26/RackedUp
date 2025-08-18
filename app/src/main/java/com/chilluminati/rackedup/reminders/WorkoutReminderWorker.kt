package com.chilluminati.rackedup.reminders

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.chilluminati.rackedup.R
import com.chilluminati.rackedup.core.util.Constants
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import android.net.Uri

@HiltWorker
class WorkoutReminderWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {
        val manager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val soundUri = Uri.parse("android.resource://" + applicationContext.packageName + "/" + R.raw.rest_timer_complete)
        val notification = NotificationCompat.Builder(applicationContext, Constants.REMINDER_NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_stat_bolt)
            .setContentTitle(applicationContext.getString(R.string.app_name))
            .setContentText("Time to work out! Stay consistent and crush your goals.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setSound(soundUri)
            .build()
        manager.notify(NOTIFICATION_ID, notification)
        return Result.success()
    }

    companion object {
        private const val NOTIFICATION_ID = 2001
    }
}


