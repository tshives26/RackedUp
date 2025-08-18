package com.chilluminati.rackedup.reminders

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import dagger.hilt.android.qualifiers.ApplicationContext
import android.os.Build
import javax.inject.Inject
import javax.inject.Singleton
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId

@Singleton
class ReminderScheduler @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun scheduleDailyReminder(minutesFromMidnight: Int) {
        // Cancel any existing alarm
        cancelDailyReminder()

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pendingIntent = createReminderPendingIntent(context, minutesFromMidnight)

        val now = LocalDateTime.now()
        val targetTime = LocalTime.of(minutesFromMidnight / 60, minutesFromMidnight % 60)
        var nextRun = now.withHour(targetTime.hour).withMinute(targetTime.minute).withSecond(0).withNano(0)
        if (nextRun.isBefore(now)) {
            nextRun = nextRun.plusDays(1)
        }
        val triggerAtMillis = nextRun.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

        val canUseExact = if (Build.VERSION.SDK_INT >= 31) {
            alarmManager.canScheduleExactAlarms()
        } else {
            true
        }

        if (canUseExact) {
            try {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerAtMillis,
                    pendingIntent
                )
            } catch (_: SecurityException) {
                // Fallback to inexact alarm to avoid crash
                alarmManager.set(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent)
            }
        } else {
            // Fallback without exact permission
            alarmManager.set(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent)
        }
    }

    fun cancelDailyReminder() {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pendingIntent = createReminderPendingIntent(context, null)
        alarmManager.cancel(pendingIntent)
    }

    private fun createReminderPendingIntent(context: Context, minutesFromMidnight: Int?): PendingIntent {
        val intent = Intent(context, ReminderReceiver::class.java).apply {
            if (minutesFromMidnight != null) {
                putExtra(ReminderReceiver.EXTRA_MINUTES_FROM_MIDNIGHT, minutesFromMidnight)
            }
        }
        return PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }
}


