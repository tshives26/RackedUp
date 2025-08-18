package com.chilluminati.rackedup

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.content.Context
import android.os.StrictMode
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject
import coil.Coil
import coil.ImageLoader
import coil.disk.DiskCache
import coil.memory.MemoryCache
import com.chilluminati.rackedup.core.util.Constants
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.app.Notification
import androidx.room.Room
import com.chilluminati.rackedup.data.database.RackedUpDatabase
import com.chilluminati.rackedup.data.repository.DataManagementRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader
import androidx.work.WorkManager
import androidx.work.OneTimeWorkRequestBuilder
import com.chilluminati.rackedup.prefetch.ImagePrefetchWorker

@HiltAndroidApp
class RackedUpApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()
        enableStrictModeInDebug()
        configureCoilImageLoader()
        createNotificationChannels()
        seedExercisesFromAssetsIfEmpty()
    }

    private fun enableStrictModeInDebug() {
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(
                StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build()
            )
            StrictMode.setVmPolicy(
                StrictMode.VmPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build()
            )
        }
    }

    private fun configureCoilImageLoader() {
        val imageLoader = ImageLoader.Builder(this)
            .memoryCache(
                MemoryCache.Builder(this)
                    .maxSizePercent(0.25) // Use up to 25% of app memory for images
                    .build()
            )
            .diskCache(
                DiskCache.Builder()
                    .directory(cacheDir.resolve("image_cache"))
                    .maxSizeBytes(150L * 1024L * 1024L) // 150MB
                    .build()
            )
            .crossfade(true)
            .respectCacheHeaders(false)
            .build()
        Coil.setImageLoader(imageLoader)
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val workoutChannel = NotificationChannel(
                Constants.WORKOUT_NOTIFICATION_CHANNEL_ID,
                "Workout Notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Timers, rest periods, and active workout notifications"
                enableVibration(true)
            }



            val alarmChannel = NotificationChannel(
                Constants.ALARM_NOTIFICATION_CHANNEL_ID,
                "Alarms",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Critical timer alarms"
                enableVibration(true)
                val soundUri: Uri = Uri.parse("android.resource://" + packageName + "/" + R.raw.rest_timer_complete)
                val attrs = AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .build()
                setSound(soundUri, attrs)
                setBypassDnd(true)
                lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            }

            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(workoutChannel)
            manager.createNotificationChannel(alarmChannel)
        }
    }

    private fun seedExercisesFromAssetsIfEmpty() {
        // Seed on a background thread
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val db = RackedUpDatabase.getDatabase(this@RackedUpApplication)
                val count = db.exerciseDao().getAllExercisesList().size
                if (count == 0) {
                    // Read prebundled JSON from assets
                    val json = assets.open("exercises/exercises.json").bufferedReader().use { it.readText() }
                    // Use repository-style import to map schema
                    val repo = DataManagementRepository(this@RackedUpApplication, db, Dispatchers.IO)
                    val result = repo.importFreeExerciseDbJson(
                        json.byteInputStream(),
                        baseImageUrl = "https://raw.githubusercontent.com/yuhonas/free-exercise-db/main/exercises/"
                    )
                    result.fold(
                        onSuccess = { message ->
                            android.util.Log.i("RackedUpApplication", "Successfully seeded exercises: $message")
                            // Prefetch a subset of images in the background (best-effort)
                            WorkManager.getInstance(this@RackedUpApplication)
                                .enqueue(OneTimeWorkRequestBuilder<ImagePrefetchWorker>().build())
                        },
                        onFailure = { error ->
                            android.util.Log.e("RackedUpApplication", "Failed to seed exercises", error)
                        }
                    )
                } else {
                    android.util.Log.i("RackedUpApplication", "Exercises already seeded (count: $count)")
                }
            } catch (e: Exception) {
                android.util.Log.e("RackedUpApplication", "Error in seedExercisesFromAssetsIfEmpty", e)
            }
        }
    }


}
