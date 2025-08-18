package com.chilluminati.rackedup.reminders

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.chilluminati.rackedup.R
import com.chilluminati.rackedup.core.util.Constants

class ReminderReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        // Post the reminder notification
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = NotificationCompat.Builder(context, Constants.REMINDER_NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_stat_bolt)
            .setContentTitle(context.getString(R.string.app_name))
            .setContentText("Time to work out! Stay consistent and crush your goals.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()
        manager.notify(NOTIFICATION_ID, notification)

        // Reschedule for the next day at the same time
        val minutesFromMidnight = intent?.getIntExtra(EXTRA_MINUTES_FROM_MIDNIGHT, DEFAULT_MINUTES) ?: DEFAULT_MINUTES
        // Reschedule using app scheduler
        ReminderScheduler(context).scheduleDailyReminder(minutesFromMidnight)
    }

    companion object {
        const val EXTRA_MINUTES_FROM_MIDNIGHT = "minutes_from_midnight"
        private const val DEFAULT_MINUTES = 18 * 60
        private const val NOTIFICATION_ID = 2002
    }
}


