package com.chilluminati.rackedup.presentation.profile

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import android.net.Uri
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.chilluminati.rackedup.R
import com.chilluminati.rackedup.data.repository.DataManagementRepository
import com.chilluminati.rackedup.data.repository.SettingsRepository

/**
 * Data management screen for backup, export, and import features
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DataManagementScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DataManagementViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    
    // File picker launchers
    val exportBackupLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/zip")
    ) { uri: Uri? ->
        uri?.let { viewModel.exportCompleteBackup(it) }
    }

    val driveCreateDocumentLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val uri = result.data?.data
        if (result.resultCode == Activity.RESULT_OK && uri != null) {
            viewModel.exportCompleteBackup(uri)
        }
    }
    
    val exportCSVLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("text/csv")
    ) { uri: Uri? ->
        uri?.let { viewModel.exportWorkoutHistoryCSV(it) }
    }
    
    val exportExercisesLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/json")
    ) { uri: Uri? ->
        uri?.let { viewModel.exportExerciseLibrary(it) }
    }
    
    val importBackupLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri?.let { viewModel.importCompleteBackup(it) }
    }
    
    val importCSVLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri?.let { viewModel.importWorkoutDataCSV(it) }
    }

    val importExercisesLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri?.let { viewModel.importExerciseLibrary(it) }
    }

    // Select backup folder (e.g., Google Drive) using SAF tree
    val selectFolderLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocumentTree()
    ) { uri: Uri? ->
        viewModel.setBackupFolderUri(uri)
    }

    // Drive-targeted folder picker (preferred)
    val driveFolderPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val uri = result.data?.data
            viewModel.setBackupFolderUri(uri)
        }
    }
    
    // Trigger app restart after destructive reset
    if (uiState.shouldRestartApp) {
        LaunchedEffect("restart") {
            val pm = context.packageManager
            val intent = pm.getLaunchIntentForPackage(context.packageName)
            intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
            viewModel.consumeRestartRequest()
        }
    }

    // Show message snackbar
    uiState.message?.let { message ->
        LaunchedEffect(message) {
            // Auto-clear message after showing
            kotlinx.coroutines.delay(3000)
            viewModel.clearMessage()
        }
    }
    Column(modifier = modifier.fillMaxSize()) {
        // Top App Bar
        TopAppBar(
            title = { Text(stringResource(R.string.data_management)) },
            navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back)
                    )
                }
            }
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Backup Section
            item {
                Text(
                    text = "Cloud Backup",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            item {
                Box(modifier = Modifier.fillMaxWidth()) {
                    CloudBackupCard(
                        modifier = Modifier.blur(6.dp),
                    storageInfo = uiState.storageInfo,
                    isLoading = uiState.isLoading,
                    autoBackupEnabled = uiState.autoBackup,
                    backupFolderUri = uiState.backupFolderUri,
                    interval = uiState.interval,
                    onToggleAutoBackup = { viewModel.updateAutoBackup(it) },
                    onChangeInterval = { viewModel.updateInterval(it) },
                    onSelectFolder = {
                        // Prefer Google Drive's picker; fall back to generic picker if unavailable
                        try {
                            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE).apply {
                                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or
                                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION or
                                    Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
                                setPackage("com.google.android.apps.docs")
                            }
                            driveFolderPickerLauncher.launch(intent)
                        } catch (e: ActivityNotFoundException) {
                            selectFolderLauncher.launch(null)
                        }
                    },
                        onCreateBackupNow = {
                        if (uiState.backupFolderUri != null) {
                            viewModel.exportCompleteBackupToSelectedFolder()
                        } else {
                            // Try Drive-targeted Create Document first
                            try {
                                val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                                    type = "application/zip"
                                    putExtra(Intent.EXTRA_TITLE, "rackedup_backup_${System.currentTimeMillis()}.zip")
                                    setPackage("com.google.android.apps.docs")
                                }
                                driveCreateDocumentLauncher.launch(intent)
                            } catch (e: ActivityNotFoundException) {
                                exportBackupLauncher.launch("rackedup_backup_${System.currentTimeMillis()}.zip")
                            }
                        }
                    }
                    )

                    // Coming Soon overlay
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .pointerInput(Unit) {}
                            .background(Color.Black.copy(alpha = 0.5f))
                    ) {
                        Text(
                            text = "Coming Soon",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.White,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
            }


            // Export Section
            item {
                Text(
                    text = "Export Data",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            item {
                ExportOptionsCard(
                    isLoading = uiState.isLoading,
                    onExportCompleteBackup = { 
                        exportBackupLauncher.launch("rackedup_backup_${System.currentTimeMillis()}.zip") 
                    },
                    onExportWorkoutCSV = { 
                        exportCSVLauncher.launch("workout_history_${System.currentTimeMillis()}.csv") 
                    },
                    onExportExerciseLibrary = { 
                        exportExercisesLauncher.launch("exercise_library_${System.currentTimeMillis()}.json") 
                    }
                )
            }

            // Import Section
            item {
                Text(
                    text = "Import Data",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            item {
                ImportOptionsCard(
                    isLoading = uiState.isLoading,
                    onImportBackup = { 
                        importBackupLauncher.launch(arrayOf("application/zip")) 
                    },
                    onImportCSV = { 
                        importCSVLauncher.launch(arrayOf("text/csv", "text/plain")) 
                    },
                    onImportExerciseLibrary = {
                        importExercisesLauncher.launch(arrayOf("application/json", "text/json"))
                    }
                )
            }

            // Data Management
            item {
                Text(
                    text = "Data Management",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            item {
                DataManagementCard(
                    storageInfo = uiState.storageInfo,
                    isLoading = uiState.isLoading,
                    onClearCache = { viewModel.clearCache() },
                    onResetAllData = { viewModel.resetAllData() }
                )
            }
        }
    }
}

@Composable
private fun CloudBackupCard(
    modifier: Modifier = Modifier,
    storageInfo: DataManagementRepository.StorageInfo?,
    isLoading: Boolean,
    autoBackupEnabled: Boolean,
    backupFolderUri: String?,
    interval: SettingsRepository.AutoBackupInterval,
    onToggleAutoBackup: (Boolean) -> Unit,
    onChangeInterval: (SettingsRepository.AutoBackupInterval) -> Unit,
    onSelectFolder: () -> Unit,
    onCreateBackupNow: () -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "App Storage",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                    Text(
                        text = storageInfo?.getTotalSizeMB() ?: "Loading...",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
                
                Icon(
                    imageVector = if (autoBackupEnabled && !backupFolderUri.isNullOrEmpty()) Icons.Default.CloudDone else Icons.Default.CloudOff,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Row: folder + backup now button
            ElevatedButton(
                onClick = onSelectFolder,
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading,
                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.onSurface
                )
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Folder,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(if (backupFolderUri.isNullOrEmpty()) "Select Backup Folder (Google Drive)" else "Change Backup Folder")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Switch(checked = autoBackupEnabled, onCheckedChange = onToggleAutoBackup, enabled = !backupFolderUri.isNullOrEmpty())
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text("Auto Backup", style = MaterialTheme.typography.bodyLarge)
                    Text(
                        text = if (backupFolderUri.isNullOrEmpty()) "Select a Google Drive folder to enable" else "Backs up daily to selected folder",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }

            if (autoBackupEnabled && !backupFolderUri.isNullOrEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text("Schedule", style = MaterialTheme.typography.labelLarge)
                Spacer(modifier = Modifier.height(4.dp))
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    RadioRow(
                        selected = interval == SettingsRepository.AutoBackupInterval.DAILY,
                        label = "Every day",
                        onClick = { onChangeInterval(SettingsRepository.AutoBackupInterval.DAILY) }
                    )
                    RadioRow(
                        selected = interval == SettingsRepository.AutoBackupInterval.WEEKLY,
                        label = "Every week",
                        onClick = { onChangeInterval(SettingsRepository.AutoBackupInterval.WEEKLY) }
                    )
                    RadioRow(
                        selected = interval == SettingsRepository.AutoBackupInterval.MONTHLY,
                        label = "Every month",
                        onClick = { onChangeInterval(SettingsRepository.AutoBackupInterval.MONTHLY) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(
                onClick = onCreateBackupNow,
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            ) {
                Icon(imageVector = Icons.Default.CloudUpload, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Create Backup Now")
            }
        }
    }
}

@Composable
private fun RadioRow(selected: Boolean, label: String, onClick: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        RadioButton(selected = selected, onClick = onClick)
        Spacer(modifier = Modifier.width(8.dp))
        Text(label)
    }
}

@Composable
private fun ExportOptionsCard(
    isLoading: Boolean,
    onExportCompleteBackup: () -> Unit,
    onExportWorkoutCSV: () -> Unit,
    onExportExerciseLibrary: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Export Options",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            ExportOptionItem(
                title = "Complete Backup",
                description = "All data including workouts, exercises, and settings",
                icon = Icons.Default.Archive,
                onClick = onExportCompleteBackup,
                isLoading = isLoading
            )
            
            ExportOptionItem(
                title = "Workout History (CSV)",
                description = "Export workout data in spreadsheet format",
                icon = Icons.Default.TableChart,
                onClick = onExportWorkoutCSV,
                isLoading = isLoading
            )
            
            ExportOptionItem(
                title = "Exercise Library",
                description = "Export your custom exercises and routines",
                icon = Icons.Default.Book,
                onClick = onExportExerciseLibrary,
                iconRotation = 180f,
                isLoading = isLoading
            )
            

        }
    }
}

@Composable
private fun ImportOptionsCard(
    isLoading: Boolean,
    onImportBackup: () -> Unit,
    onImportCSV: () -> Unit,
    onImportExerciseLibrary: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Import Options",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            ImportOptionItem(
                title = "RackedUp Backup",
                description = "Restore from a previous backup file",
                icon = Icons.Default.Restore,
                onClick = onImportBackup,
                isLoading = isLoading
            )
            
            ImportOptionItem(
                title = "CSV Data",
                description = "Import workout data from spreadsheet",
                icon = Icons.Default.Upload,
                onClick = onImportCSV,
                isLoading = isLoading
            )

            ImportOptionItem(
                title = "Exercise Library",
                description = "Import your custom exercises",
                icon = Icons.Default.SystemUpdateAlt,
                onClick = onImportExerciseLibrary,
                iconRotation = 180f,
                isLoading = isLoading
            )
        }
    }
}

@Composable
private fun DataManagementCard(
    storageInfo: DataManagementRepository.StorageInfo?,
    isLoading: Boolean,
    onClearCache: () -> Unit,
    onResetAllData: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        var showStorageDialog by remember { mutableStateOf(false) }
        var showResetDialog by remember { mutableStateOf(false) }
        var countdown by remember(showResetDialog) { mutableStateOf(10) }

        if (showResetDialog) {
            LaunchedEffect(Unit) {
                countdown = 10
                while (countdown > 0) {
                    kotlinx.coroutines.delay(1000)
                    countdown--
                }
            }
        }

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Data Management",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            DataManagementItem(
                title = "Storage Usage",
                description = "Database: ${storageInfo?.getDatabaseSizeMB() ?: "..."}, Cache: ${storageInfo?.getCacheSizeMB() ?: "..."}",
                icon = Icons.Default.Storage,
                onClick = { showStorageDialog = true },
                isLoading = isLoading
            )
            
            DataManagementItem(
                title = "Reset App Data",
                description = "Clear all data and start fresh - THIS CANNOT BE UNDONE!",
                icon = Icons.Default.Delete,
                onClick = { showResetDialog = true },
                isDestructive = true,
                isLoading = isLoading
            )
        }

        if (showStorageDialog) {
            AlertDialog(
                onDismissRequest = { showStorageDialog = false },
                title = { Text("Storage Usage") },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("Database: ${storageInfo?.getDatabaseSizeMB() ?: "..."}")
                        Text("Cache: ${storageInfo?.getCacheSizeMB() ?: "..."}")
                        Text("Files: ${storageInfo?.getFilesSizeMB() ?: "..."}")
                        Text("Total: ${storageInfo?.getTotalSizeMB() ?: "..."}")
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showStorageDialog = false }) {
                        Text("Close")
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        showStorageDialog = false
                        onClearCache()
                    }) {
                        Text("Clear Cache")
                    }
                }
            )
        }

        if (showResetDialog) {
            AlertDialog(
                onDismissRequest = { showResetDialog = false },
                title = { Text("Reset App Data") },
                text = {
                    Text("This will permanently delete all workouts, exercises, programs, and settings from this device. This cannot be undone.")
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showResetDialog = false
                            onResetAllData()
                        },
                        enabled = countdown == 0
                    ) {
                        Text(if (countdown == 0) "OK" else "OK (${countdown}s)")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showResetDialog = false }) { Text("Cancel") }
                }
            )
        }
    }
}

@Composable
private fun ExportOptionItem(
    title: String,
    description: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit,
    isLoading: Boolean = false,
    iconRotation: Float = 0f
) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(16.dp),
        enabled = !isLoading
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(24.dp).rotate(iconRotation)
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Icon(
                imageVector = Icons.Default.Download,
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
private fun ImportOptionItem(
    title: String,
    description: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit,
    isLoading: Boolean = false,
    iconRotation: Float = 0f
) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(16.dp),
        enabled = !isLoading
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(24.dp).rotate(iconRotation)
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Icon(
                imageVector = Icons.Default.Upload,
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
private fun DataManagementItem(
    title: String,
    description: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit,
    isDestructive: Boolean = false,
    isLoading: Boolean = false
) {
    TextButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(12.dp),
        enabled = !isLoading,
        colors = ButtonDefaults.textButtonColors(
            contentColor = if (isDestructive) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = if (isDestructive) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isDestructive) 
                        MaterialTheme.colorScheme.error.copy(alpha = 0.7f) 
                    else 
                        MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = if (isDestructive) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
