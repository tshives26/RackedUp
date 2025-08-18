package com.chilluminati.rackedup.reminders

import android.content.Context
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.chilluminati.rackedup.core.util.Constants
import com.chilluminati.rackedup.data.repository.DataManagementRepository
import com.chilluminati.rackedup.data.repository.SettingsRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@HiltWorker
class AutoBackupWorker @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted params: WorkerParameters,
    private val dataManagementRepository: DataManagementRepository,
    private val settingsRepository: SettingsRepository
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        return try {
            val enabled = settingsRepository.autoBackup.first()
            if (!enabled) return Result.success()

            val folderUriStr = settingsRepository.backupFolderUri.first()
            if (folderUriStr.isNullOrEmpty()) return Result.retry()

            val folderUri = Uri.parse(folderUriStr)
            val folder = DocumentFile.fromTreeUri(appContext, folderUri) ?: return Result.retry()
            if (!folder.canWrite()) return Result.retry()

            val timestamp = SimpleDateFormat(Constants.EXPORT_DATE_FORMAT, Locale.US).format(Date())
            val fileName = "rackedup_backup_" + timestamp + ".zip"
            val backupFile = folder.createFile("application/zip", fileName) ?: return Result.retry()

            appContext.contentResolver.openOutputStream(backupFile.uri)?.use { outputStream ->
                val res = dataManagementRepository.exportCompleteBackup(outputStream)
                res.fold(
                    onSuccess = { Result.success() },
                    onFailure = { Result.retry() }
                )
            } ?: Result.retry()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}


