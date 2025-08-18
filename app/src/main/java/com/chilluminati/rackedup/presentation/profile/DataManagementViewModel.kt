package com.chilluminati.rackedup.presentation.profile

import android.content.Context
import android.net.Uri
import android.provider.DocumentsContract
import android.content.Intent
import androidx.documentfile.provider.DocumentFile
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.chilluminati.rackedup.core.util.Constants
import com.chilluminati.rackedup.reminders.AutoBackupWorker
import java.util.concurrent.TimeUnit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chilluminati.rackedup.data.repository.DataManagementRepository
import com.chilluminati.rackedup.data.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

/**
 * ViewModel for data management operations
 */
@HiltViewModel
class DataManagementViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val dataManagementRepository: DataManagementRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    /**
     * UI state for data management screen
     */
    data class DataManagementUiState(
        val isLoading: Boolean = false,
        val message: String? = null,
        val isError: Boolean = false,
        val storageInfo: DataManagementRepository.StorageInfo? = null,
        val lastBackupDate: String? = null,
        val autoBackup: Boolean = false,
        val backupFolderUri: String? = null,
        val interval: SettingsRepository.AutoBackupInterval = SettingsRepository.AutoBackupInterval.WEEKLY,
        val shouldRestartApp: Boolean = false
    )

    private val _uiState = MutableStateFlow(DataManagementUiState())
    val uiState: StateFlow<DataManagementUiState> = _uiState.asStateFlow()

    init {
        loadStorageInfo()
        observeSettings()
    }

    /**
     * Load storage information
     */
    private fun loadStorageInfo() {
        viewModelScope.launch {
            try {
                val storageInfo = dataManagementRepository.getStorageUsage()
                _uiState.update { it.copy(storageInfo = storageInfo) }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        isError = true, 
                        message = "Failed to load storage info: ${e.message}"
                    ) 
                }
            }
        }
    }

    private fun observeSettings() {
        viewModelScope.launch {
            combine(
                settingsRepository.autoBackup,
                settingsRepository.backupFolderUri,
                settingsRepository.autoBackupInterval
            ) { autoBackupEnabled, folderUri, interval ->
                Triple(autoBackupEnabled, folderUri, interval)
            }.collect { (enabled, folderUri, interval) ->
                _uiState.update { it.copy(autoBackup = enabled, backupFolderUri = folderUri, interval = interval) }
                scheduleOrCancelAutoBackup(enabled, interval)
            }
        }
    }

    fun updateAutoBackup(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setAutoBackup(enabled)
            scheduleOrCancelAutoBackup(enabled, uiState.value.interval)
        }
    }

    private fun scheduleOrCancelAutoBackup(enabled: Boolean, interval: SettingsRepository.AutoBackupInterval) {
        val workManager = WorkManager.getInstance(context)
        if (enabled) {
            val repeatInterval = when (interval) {
                SettingsRepository.AutoBackupInterval.DAILY -> 1L
                SettingsRepository.AutoBackupInterval.WEEKLY -> 7L
                SettingsRepository.AutoBackupInterval.MONTHLY -> 30L
            }
            val request = PeriodicWorkRequestBuilder<AutoBackupWorker>(repeatInterval, TimeUnit.DAYS)
                .addTag(Constants.AUTO_BACKUP_WORK_TAG)
                .build()
            workManager.enqueueUniquePeriodicWork(
                Constants.AUTO_BACKUP_WORK_TAG,
                ExistingPeriodicWorkPolicy.UPDATE,
                request
            )
        } else {
            workManager.cancelAllWorkByTag(Constants.AUTO_BACKUP_WORK_TAG)
        }
    }

    fun updateInterval(interval: SettingsRepository.AutoBackupInterval) {
        viewModelScope.launch {
            settingsRepository.setAutoBackupInterval(interval)
            if (uiState.value.autoBackup) {
                scheduleOrCancelAutoBackup(true, interval)
            }
        }
    }

    fun setBackupFolderUri(uri: Uri?) {
        viewModelScope.launch {
            settingsRepository.setBackupFolderUri(uri?.toString())
            if (uri != null) {
                context.contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                )
            }
        }
    }

    /**
     * Export complete backup
     */
    fun exportCompleteBackup(uri: Uri) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            try {
                context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                    val result = dataManagementRepository.exportCompleteBackup(outputStream)
                    
                    result.fold(
                        onSuccess = { message ->
                            _uiState.update { 
                                it.copy(
                                    isLoading = false,
                                    message = message,
                                    isError = false
                                )
                            }
                        },
                        onFailure = { error ->
                            _uiState.update { 
                                it.copy(
                                    isLoading = false,
                                    message = "Export failed: ${error.message}",
                                    isError = true
                                )
                            }
                        }
                    )
                }
            } catch (e: IOException) {
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        message = "Failed to write file: ${e.message}",
                        isError = true
                    )
                }
            }
        }
    }

    fun exportCompleteBackupToSelectedFolder() {
        val folderUriStr = uiState.value.backupFolderUri ?: return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val folder = DocumentFile.fromTreeUri(context, Uri.parse(folderUriStr))
                val timestamp = java.text.SimpleDateFormat(Constants.EXPORT_DATE_FORMAT, java.util.Locale.US).format(java.util.Date())
                val file = folder?.createFile("application/zip", "rackedup_backup_${timestamp}.zip")
                if (file == null) {
                    _uiState.update { it.copy(isLoading = false, isError = true, message = "Unable to create backup file") }
                    return@launch
                }
                context.contentResolver.openOutputStream(file.uri)?.use { outputStream ->
                    val result = dataManagementRepository.exportCompleteBackup(outputStream)
                    result.fold(
                        onSuccess = { message ->
                            _uiState.update { it.copy(isLoading = false, message = message, isError = false) }
                        },
                        onFailure = { error ->
                            _uiState.update { it.copy(isLoading = false, message = "Export failed: ${error.message}", isError = true) }
                        }
                    )
                } ?: _uiState.update { it.copy(isLoading = false, isError = true, message = "Failed to open output stream") }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, isError = true, message = e.message) }
            }
        }
    }

    /**
     * Export workout history as CSV
     */
    fun exportWorkoutHistoryCSV(uri: Uri) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            try {
                context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                    val result = dataManagementRepository.exportWorkoutHistoryCSV(outputStream)
                    
                    result.fold(
                        onSuccess = { message ->
                            _uiState.update { 
                                it.copy(
                                    isLoading = false,
                                    message = message,
                                    isError = false
                                )
                            }
                        },
                        onFailure = { error ->
                            _uiState.update { 
                                it.copy(
                                    isLoading = false,
                                    message = "CSV export failed: ${error.message}",
                                    isError = true
                                )
                            }
                        }
                    )
                }
            } catch (e: IOException) {
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        message = "Failed to write CSV file: ${e.message}",
                        isError = true
                    )
                }
            }
        }
    }

    /**
     * Export exercise library
     */
    fun exportExerciseLibrary(uri: Uri) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            try {
                context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                    val result = dataManagementRepository.exportExerciseLibrary(outputStream)
                    
                    result.fold(
                        onSuccess = { message ->
                            _uiState.update { 
                                it.copy(
                                    isLoading = false,
                                    message = message,
                                    isError = false
                                )
                            }
                        },
                        onFailure = { error ->
                            _uiState.update { 
                                it.copy(
                                    isLoading = false,
                                    message = "Exercise library export failed: ${error.message}",
                                    isError = true
                                )
                            }
                        }
                    )
                }
            } catch (e: IOException) {
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        message = "Failed to write exercise library file: ${e.message}",
                        isError = true
                    )
                }
            }
        }
    }

    /**
     * Import complete backup
     */
    fun importCompleteBackup(uri: Uri) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            try {
                context.contentResolver.openInputStream(uri)?.use { inputStream ->
                    val result = dataManagementRepository.importCompleteBackup(inputStream)
                    
                    result.fold(
                        onSuccess = { message ->
                            _uiState.update { 
                                it.copy(
                                    isLoading = false,
                                    message = message,
                                    isError = false
                                )
                            }
                            loadStorageInfo() // Refresh storage info after import
                        },
                        onFailure = { error ->
                            _uiState.update { 
                                it.copy(
                                    isLoading = false,
                                    message = "Import failed: ${error.message}",
                                    isError = true
                                )
                            }
                        }
                    )
                }
            } catch (e: IOException) {
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        message = "Failed to read file: ${e.message}",
                        isError = true
                    )
                }
            }
        }
    }

    /**
     * Import workout data from CSV
     */
    fun importWorkoutDataCSV(uri: Uri) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            try {
                context.contentResolver.openInputStream(uri)?.use { inputStream ->
                    val result = dataManagementRepository.importWorkoutDataCSV(inputStream)
                    
                    result.fold(
                        onSuccess = { message ->
                            _uiState.update { 
                                it.copy(
                                    isLoading = false,
                                    message = message,
                                    isError = false
                                )
                            }
                            loadStorageInfo() // Refresh storage info after import
                        },
                        onFailure = { error ->
                            _uiState.update { 
                                it.copy(
                                    isLoading = false,
                                    message = "CSV import failed: ${error.message}",
                                    isError = true
                                )
                            }
                        }
                    )
                }
            } catch (e: IOException) {
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        message = "Failed to read CSV file: ${e.message}",
                        isError = true
                    )
                }
            }
        }
    }

    fun importExerciseLibrary(uri: Uri) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                context.contentResolver.openInputStream(uri)?.use { inputStream ->
                    // Try Free Exercise DB schema first, fallback to legacy export format
                    val result = try {
                        dataManagementRepository.importFreeExerciseDbJson(inputStream)
                    } catch (e: Exception) {
                        context.contentResolver.openInputStream(uri)?.use { again ->
                            dataManagementRepository.importExerciseLibrary(again)
                        } ?: Result.failure(e)
                    }
                    result.fold(
                        onSuccess = { message ->
                            _uiState.update {
                                it.copy(isLoading = false, message = message, isError = false)
                            }
                        },
                        onFailure = { error ->
                            _uiState.update {
                                it.copy(isLoading = false, message = "Exercise import failed: ${error.message}", isError = true)
                            }
                        }
                    )
                }
            } catch (e: IOException) {
                _uiState.update { it.copy(isLoading = false, message = "Failed to read exercise file: ${e.message}", isError = true) }
            }
        }
    }

    /**
     * Clear app cache
     */
    fun clearCache() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            val result = dataManagementRepository.clearCache()
            
            result.fold(
                onSuccess = { message ->
                    _uiState.update { 
                        it.copy(
                            isLoading = false,
                            message = message,
                            isError = false
                        )
                    }
                    loadStorageInfo() // Refresh storage info after clearing cache
                },
                onFailure = { error ->
                    _uiState.update { 
                        it.copy(
                            isLoading = false,
                            message = "Failed to clear cache: ${error.message}",
                            isError = true
                        )
                    }
                }
            )
        }
    }

    /**
     * Reset all app data
     */
    fun resetAllData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            val result = dataManagementRepository.resetAllData()
            
            result.fold(
                onSuccess = { message ->
                    _uiState.update { 
                        it.copy(
                            isLoading = false,
                            message = message,
                            isError = false,
                            shouldRestartApp = true
                        )
                    }
                    loadStorageInfo() // Refresh storage info after reset
                },
                onFailure = { error ->
                    _uiState.update { 
                        it.copy(
                            isLoading = false,
                            message = "Failed to reset data: ${error.message}",
                            isError = true
                        )
                    }
                }
            )
        }
    }

    /**
     * Clear the current message
     */
    fun clearMessage() {
        _uiState.update { it.copy(message = null, isError = false) }
    }

    fun consumeRestartRequest() {
        _uiState.update { it.copy(shouldRestartApp = false) }
    }
}
