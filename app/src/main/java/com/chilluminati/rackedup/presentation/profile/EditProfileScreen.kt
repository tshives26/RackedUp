package com.chilluminati.rackedup.presentation.profile

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.Wc
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import android.content.Intent
import android.net.Uri
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import com.chilluminati.rackedup.presentation.components.AppTextFieldDefaults
import com.chilluminati.rackedup.presentation.components.AccentSectionHeader
import com.yalantis.ucrop.UCrop

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    onNavigateBack: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val profile = uiState.profile
    
    var name by remember { mutableStateOf(profile?.name ?: "") }
    var selectedDate by remember { mutableStateOf(profile?.birthday) }
    var selectedSex by remember { mutableStateOf(profile?.sex) }
    var avatarUri by remember { mutableStateOf(profile?.profileImageUrl?.toUri()) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showSexDialog by remember { mutableStateOf(false) }

    val sexOptions = listOf("Male", "Female", "Other", "Prefer not to say")
    val dateFormatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())

    // Keep form fields in sync when profile loads/changes
    LaunchedEffect(profile) {
        name = profile?.name ?: ""
        selectedDate = profile?.birthday
        selectedSex = profile?.sex
        avatarUri = profile?.profileImageUrl?.toUri()
    }

    // Photo picker and cropping for avatar
    val context = LocalContext.current
    var lastPersistedUri by remember { mutableStateOf<Uri?>(null) }

    fun hasPersisted(uri: Uri): Boolean {
        return context.contentResolver.persistedUriPermissions.any { it.uri == uri }
    }

    fun persistReadPermissionIfPossible(uri: Uri) {
        runCatching {
            context.contentResolver.takePersistableUriPermission(
                uri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            lastPersistedUri = uri
        }.onFailure {
            // Photo Picker on newer Android may not support persist; ignore.
        }
    }

    fun releasePersistedPermissionIfHeld(uri: Uri) {
        runCatching {
            if (hasPersisted(uri)) {
                context.contentResolver.releasePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            }
        }
    }

    // UCrop launcher
    val cropImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == android.app.Activity.RESULT_OK) {
            val resultUri = UCrop.getOutput(result.data!!)
            if (resultUri != null) {
                // Release any previously held persisted permission if different
                avatarUri?.let { previous ->
                    if (previous != resultUri) {
                        releasePersistedPermissionIfHeld(previous)
                    }
                }
                avatarUri = resultUri
                // Attempt to persist read permission when applicable
                persistReadPermissionIfPossible(resultUri)
            }
        }
    }

    // Image picker launcher
    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            // Start UCrop with the selected image
            val destinationUri = Uri.fromFile(File(context.cacheDir, "cropped_image_${System.currentTimeMillis()}.jpg"))
            
            val options = UCrop.Options()
            options.setCompressionQuality(90)
            options.setToolbarColor(context.getColor(android.R.color.black))
            options.setActiveControlsWidgetColor(context.getColor(android.R.color.white))
            options.setToolbarWidgetColor(context.getColor(android.R.color.white))
            options.setFreeStyleCropEnabled(false) // Disable free-style for consistent square crops
            options.setHideBottomControls(false) // Keep bottom controls visible
            options.setShowCropFrame(true) // Show crop frame
            options.setShowCropGrid(true) // Show crop grid
            options.setCropGridStrokeWidth(2) // Grid line thickness
            options.setMaxBitmapSize(1024) // Limit bitmap size for performance
            options.setToolbarTitle("Crop Photo") // Set toolbar title
            // Let UCrop use its default icon which should be properly styled
            
            val uCrop = UCrop.of(uri, destinationUri)
                .withOptions(options)
                .withAspectRatio(1f, 1f) // Square aspect ratio (1:1) - perfect for profile pictures
                .withMaxResultSize(512, 512) // Max 512x512px output - good for 64-72dp display sizes
            
            cropImageLauncher.launch(uCrop.getIntent(context))
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Profile") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    TextButton(
                        onClick = {
                            viewModel.updateProfile(
                                name = name.takeIf { it.isNotBlank() },
                                birthday = selectedDate,
                                sex = selectedSex,
                                profileImageUrl = avatarUri?.toString()
                            )
                            onNavigateBack()
                        },
                        enabled = name.isNotBlank()
                    ) {
                        Text("Save")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Avatar
            Row(verticalAlignment = Alignment.CenterVertically) {
                Card(onClick = {
                    pickImageLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                }) {
                    Box(modifier = Modifier.size(72.dp), contentAlignment = Alignment.Center) {
                        if (avatarUri != null) {
                            AsyncImage(
                                model = avatarUri,
                                contentDescription = "Profile photo",
                                modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            Icon(Icons.Default.Person, contentDescription = null)
                        }
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = "Change photo", modifier = Modifier.clickable {
                    pickImageLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                })
            }

            AccentSectionHeader(title = "Profile Details")

            // Name Input
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Full Name") },
                leadingIcon = {
                    Icon(Icons.Default.Person, contentDescription = null)
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                colors = AppTextFieldDefaults.colors()
            )



            // Birthday Selection
            OutlinedTextField(
                value = selectedDate?.let { dateFormatter.format(it) } ?: "",
                onValueChange = { },
                label = { Text("Birthday") },
                leadingIcon = {
                    Icon(Icons.Default.Cake, contentDescription = null)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showDatePicker = true },
                readOnly = true,
                colors = AppTextFieldDefaults.colors()
            )

            // Sex Selection
            OutlinedTextField(
                value = selectedSex ?: "",
                onValueChange = { },
                label = { Text("Sex") },
                leadingIcon = {
                    Icon(Icons.Default.Wc, contentDescription = null)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showSexDialog = true },
                readOnly = true,
                colors = AppTextFieldDefaults.colors()
            )

            // Error Display
            uiState.error?.let { errorMessage ->
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }

        // Date Picker Dialog
        if (showDatePicker) {
            val calendar = Calendar.getInstance()
            selectedDate?.let { calendar.time = it }
            
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(onClick = {
                        selectedDate = calendar.time
                        showDatePicker = false
                    }) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDatePicker = false }) {
                        Text("Cancel")
                    }
                }
            ) {
                DatePicker(
                    state = rememberDatePickerState(
                        initialSelectedDateMillis = calendar.timeInMillis
                    )
                )
            }
        }

        // Sex Selection Dialog
        if (showSexDialog) {
            AlertDialog(
                onDismissRequest = { showSexDialog = false },
                title = { Text("Select Sex") },
                text = {
                    Column {
                        sexOptions.forEach { option ->
                            TextButton(
                                onClick = {
                                    selectedSex = option
                                    showSexDialog = false
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(option)
                            }
                        }
                    }
                },
                confirmButton = { },
                dismissButton = {
                    TextButton(onClick = { showSexDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}
