package com.chilluminati.rackedup.presentation.exercises

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.material3.MenuAnchorType
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import coil.compose.AsyncImage
import com.chilluminati.rackedup.data.repository.ExerciseRepository
import com.chilluminati.rackedup.presentation.components.AccentSectionHeader
import com.chilluminati.rackedup.presentation.components.AppTextFieldDefaults
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ExerciseCreateScreen(
    onNavigateBack: () -> Unit,
    onCreated: () -> Unit,
    viewModel: ExerciseCreateViewModel = hiltViewModel()
) {
    val categories by viewModel.categoryOptions.collectAsStateWithLifecycle()
    val equipmentOptions by viewModel.equipmentOptions.collectAsStateWithLifecycle()
    val muscleOptions by viewModel.muscleOptions.collectAsStateWithLifecycle()

    var name by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var equipment by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("Strength") }
    var level by remember { mutableStateOf("Beginner") }
    val primarySelected = remember { mutableStateListOf<String>() }
    val secondarySelected = remember { mutableStateListOf<String>() }
    var instructions by remember { mutableStateOf("") }
    var force by remember { mutableStateOf("Push") }
    var mechanic by remember { mutableStateOf("Compound") }
    var imageUrl by remember { mutableStateOf("") }
    // No subcategory field in UI; keep null when saving
    var videoUrl by remember { mutableStateOf("") }
    var isCompound by remember { mutableStateOf(false) }
    var isUnilateral by remember { mutableStateOf(false) }
    var instructionStepsText by remember { mutableStateOf("") }

    // Dropdown expand states
    // legacy expand state removed in favor of generic DropdownField

    // Dialogs for multi-select
    var showPrimaryDialog by remember { mutableStateOf(false) }
    var showSecondaryDialog by remember { mutableStateOf(false) }

    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri -> if (uri != null) imageUrl = uri.toString() }
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Create Exercise") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) { Icon(Icons.Default.Close, contentDescription = null) }
                },
                actions = {
                    IconButton(onClick = {
                        viewModel.create(
                            name = name,
                            category = category,
                            subcategory = null,
                            equipment = equipment,
                            exerciseType = type,
                            difficultyLevel = level,
                            muscleGroups = primarySelected.toList(),
                            secondaryMuscles = secondarySelected.toList(),
                            instructions = instructions.ifBlank { null },
                            instructionSteps = instructionStepsText.lines().map { it.trim() }.filter { it.isNotEmpty() },
                            tips = null,
                            imageUrl = imageUrl.ifBlank { null },
                            videoUrl = videoUrl.ifBlank { null },
                            force = force.ifBlank { null },
                            mechanic = mechanic.ifBlank { null },
                            isCompound = isCompound,
                            isUnilateral = isUnilateral
                        ) { onCreated() }
                    }) { Icon(Icons.Default.Check, contentDescription = null) }
                }
            )
        }
    ) { padding ->
        val scrollState = rememberScrollState()
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 16.dp,
                    bottom = 16.dp + WindowInsets.ime.asPaddingValues().calculateBottomPadding()
                )
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AccentSectionHeader(title = "Basics")
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth(),
                colors = AppTextFieldDefaults.outlinedColors()
            )

            DropdownField(
                label = "Category",
                value = category,
                options = (if (categories.isNotEmpty()) categories else DEFAULT_CATEGORIES),
                onSelect = { category = it },
                capitalizeDisplay = true
            )

            DropdownField(
                label = "Equipment",
                value = equipment,
                options = (if (equipmentOptions.isNotEmpty()) equipmentOptions else DEFAULT_EQUIPMENT),
                onSelect = { equipment = it },
                capitalizeDisplay = true
            )

            AccentSectionHeader(title = "Classification")
            DropdownField(
                label = "Type",
                value = type,
                options = TYPE_OPTIONS,
                onSelect = { type = it },
                capitalizeDisplay = true
            )

            DropdownField(
                label = "Level",
                value = level,
                options = LEVEL_OPTIONS,
                onSelect = { level = it },
                capitalizeDisplay = true
            )

            DropdownField(
                label = "Force",
                value = force,
                options = FORCE_OPTIONS,
                onSelect = { force = it },
                capitalizeDisplay = true
            )

            DropdownField(
                label = "Mechanic",
                value = mechanic,
                options = MECHANIC_OPTIONS,
                onSelect = { mechanic = it },
                capitalizeDisplay = true
            )

            AccentSectionHeader(title = "Muscles")
            // Primary muscles
            Text(text = "Primary Muscles", style = MaterialTheme.typography.labelLarge)
            ChipRow(
                items = primarySelected,
                onRemove = { item -> primarySelected.remove(item) },
                onAddClicked = { showPrimaryDialog = true }
            )

            // Secondary muscles
            Text(text = "Secondary Muscles", style = MaterialTheme.typography.labelLarge)
            ChipRow(
                items = secondarySelected,
                onRemove = { item -> secondarySelected.remove(item) },
                onAddClicked = { showSecondaryDialog = true }
            )

            AccentSectionHeader(title = "Content")
            OutlinedTextField(value = instructions, onValueChange = { instructions = it }, label = { Text("Instructions") }, modifier = Modifier.fillMaxWidth(), minLines = 3, colors = AppTextFieldDefaults.outlinedColors())
            OutlinedTextField(value = instructionStepsText, onValueChange = { instructionStepsText = it }, label = { Text("Instruction Steps (one per line)") }, modifier = Modifier.fillMaxWidth(), minLines = 3, colors = AppTextFieldDefaults.outlinedColors())

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
            ) {
                ElevatedButton(
                    onClick = { pickImageLauncher.launch("image/*") },
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    ),
                    modifier = Modifier
                        .height(56.dp)
                        .padding(top = 2.dp)
                ) { Text("Upload Image") }
                OutlinedTextField(
                    value = imageUrl,
                    onValueChange = { imageUrl = it },
                    label = { Text("Image URL (optional)") },
                    modifier = Modifier.weight(1f),
                    colors = AppTextFieldDefaults.outlinedColors()
                )
            }
            if (imageUrl.isNotBlank()) {
                AsyncImage(model = imageUrl, contentDescription = null, modifier = Modifier.fillMaxWidth().height(160.dp))
            }

            OutlinedTextField(value = videoUrl, onValueChange = { videoUrl = it }, label = { Text("Video URL (optional)") }, modifier = Modifier.fillMaxWidth(), colors = AppTextFieldDefaults.outlinedColors())

            // Removed Attributes section per request
        }
    }

    if (showPrimaryDialog) {
        MultiSelectDialog(
            title = "Select Primary Muscles",
            options = if (muscleOptions.isNotEmpty()) muscleOptions else DEFAULT_MUSCLES,
            initiallySelected = primarySelected.toSet(),
            onConfirm = { selected ->
                primarySelected.clear();
                primarySelected.addAll(selected)
                showPrimaryDialog = false
            },
            onDismiss = { showPrimaryDialog = false }
        )
    }

    if (showSecondaryDialog) {
        MultiSelectDialog(
            title = "Select Secondary Muscles",
            options = if (muscleOptions.isNotEmpty()) muscleOptions else DEFAULT_MUSCLES,
            initiallySelected = secondarySelected.toSet(),
            onConfirm = { selected ->
                secondarySelected.clear();
                secondarySelected.addAll(selected)
                showSecondaryDialog = false
            },
            onDismiss = { showSecondaryDialog = false }
        )
    }
}

@HiltViewModel
class ExerciseCreateViewModel @Inject constructor(
    private val repository: ExerciseRepository
) : androidx.lifecycle.ViewModel() {
    private val _categoryOptions = MutableStateFlow<List<String>>(emptyList())
    val categoryOptions: StateFlow<List<String>> = _categoryOptions.asStateFlow()

    private val _equipmentOptions = MutableStateFlow<List<String>>(emptyList())
    val equipmentOptions: StateFlow<List<String>> = _equipmentOptions.asStateFlow()

    private val _muscleOptions = MutableStateFlow<List<String>>(emptyList())
    val muscleOptions: StateFlow<List<String>> = _muscleOptions.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getAllExercises().collect { list ->
                _categoryOptions.value = list.map { it.category }
                    .filter { it.isNotBlank() }
                    .distinct()
                    .sorted()
                _equipmentOptions.value = list.map { it.equipment }
                    .filter { it.isNotBlank() }
                    .distinct()
                    .sorted()
                _muscleOptions.value = list.flatMap { it.muscleGroups }
                    .filter { it.isNotBlank() }
                    .distinct()
                    .sorted()
            }
        }
    }

    fun create(
        name: String,
        category: String,
        subcategory: String?,
        equipment: String,
        exerciseType: String,
        difficultyLevel: String,
        muscleGroups: List<String>,
        secondaryMuscles: List<String>,
        instructions: String?,
        instructionSteps: List<String>,
        tips: String?,
        imageUrl: String?,
        videoUrl: String?,
        force: String?,
        mechanic: String?,
        isCompound: Boolean,
        isUnilateral: Boolean,
        onDone: () -> Unit
    ) {
        viewModelScope.launch {
            repository.createExercise(
                name = name.ifBlank { "" },
                category = category.ifBlank { "" },
                subcategory = subcategory,
                equipment = equipment.ifBlank { "" },
                exerciseType = exerciseType.ifBlank { "" },
                difficultyLevel = difficultyLevel.ifBlank { "" },
                muscleGroups = muscleGroups,
                secondaryMuscles = secondaryMuscles,
                instructions = instructions,
                instructionSteps = instructionSteps,
                tips = tips,
                imageUrl = imageUrl,
                videoUrl = videoUrl,
                force = force,
                mechanic = mechanic,
                isCompound = isCompound,
                isUnilateral = isUnilateral
            )
            onDone()
        }
    }
}

// --------- Local UI helpers and defaults ---------

private val TYPE_OPTIONS = listOf("Strength", "Cardio", "Isometric", "Stretching")
private val LEVEL_OPTIONS = listOf("Beginner", "Intermediate", "Advanced")
private val FORCE_OPTIONS = listOf("Push", "Pull", "Static")
private val MECHANIC_OPTIONS = listOf("Compound", "Isolation")
private val DEFAULT_CATEGORIES = listOf("Chest", "Back", "Shoulders", "Arms", "Legs", "Core", "Cardio")
private val DEFAULT_EQUIPMENT = listOf("Barbell", "Dumbbell", "Machine", "Bodyweight", "Cable", "Kettlebell", "Band")
private val DEFAULT_MUSCLES = listOf(
    "Abdominals", "Abductors", "Adductors", "Biceps", "Calves", "Chest", "Forearms",
    "Glutes", "Hamstrings", "Lats", "Lower Back", "Middle Back", "Neck", "Quads",
    "Shoulders", "Triceps"
)

@Composable
@OptIn(ExperimentalLayoutApi::class)
private fun ChipRow(
    items: List<String>,
    onRemove: (String) -> Unit,
    onAddClicked: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items.forEach { item ->
                AssistChip(onClick = { onRemove(item) }, label = { Text(item) })
            }
        }
        OutlinedButton(onClick = onAddClicked) { Text("Add") }
    }
}

@Composable
private fun MultiSelectDialog(
    title: String,
    options: List<String>,
    initiallySelected: Set<String>,
    onConfirm: (Set<String>) -> Unit,
    onDismiss: () -> Unit
) {
    var selections by remember { mutableStateOf(initiallySelected.toMutableSet()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            LazyColumn(
                modifier = Modifier.heightIn(max = 420.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 8.dp)
            ) {
                items(options) { option ->
                    val display = option.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
                    val checked = option in selections
                    Row(
                        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Checkbox(
                            checked = checked,
                            onCheckedChange = {
                                if (checked) selections.remove(option) else selections.add(option)
                            }
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(display)
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(selections) }) { Text("Done") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DropdownField(
    label: String,
    value: String,
    options: List<String>,
    onSelect: (String) -> Unit,
    capitalizeDisplay: Boolean = false
) {
    var expanded by remember { mutableStateOf(false) }
    val displayedValue = if (capitalizeDisplay && value.isNotBlank()) value.replaceFirstChar { it.titlecase() } else value

    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
        OutlinedTextField(
            value = displayedValue,
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(MenuAnchorType.PrimaryNotEditable, enabled = true),
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = AppTextFieldDefaults.outlinedColors()
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { option ->
                val display = if (capitalizeDisplay) option.replaceFirstChar { it.titlecase() } else option
                DropdownMenuItem(text = { Text(display) }, onClick = { onSelect(option); expanded = false })
            }
        }
    }
}


