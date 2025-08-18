package com.chilluminati.rackedup.prefetch

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import coil.Coil
import coil.request.ImageRequest
import com.chilluminati.rackedup.data.database.dao.ExerciseDao
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class ImagePrefetchWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val exerciseDao: ExerciseDao
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            val exercises = exerciseDao.getAllExercisesList()
            val loader = Coil.imageLoader(applicationContext)

            exercises.forEach { ex ->
                ex.imagePaths.forEach { url ->
                    if (url.isNotBlank()) {
                        val request = ImageRequest.Builder(applicationContext)
                            .data(url)
                            .size(512)
                            .build()
                        loader.execute(request)
                    }
                }
            }
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}


