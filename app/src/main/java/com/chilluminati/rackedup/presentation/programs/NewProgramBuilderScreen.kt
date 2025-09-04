@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.chilluminati.rackedup.presentation.programs

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.border
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.filled.Title
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.OutlinedTextFieldDefaults
import com.chilluminati.rackedup.presentation.components.AppTextFieldDefaults
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.material3.MenuAnchorType
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
 import com.chilluminati.rackedup.presentation.components.FullScreenImageDialog
 import com.chilluminati.rackedup.data.database.entity.Exercise
 import com.chilluminati.rackedup.data.database.entity.ProgramExercise
 import android.util.Log

@Composable
fun NewProgramBuilderScreen(
    onNavigateBack: () -> Unit,
    viewModel: ProgramsViewModel = hiltViewModel()
){
    val state by viewModel.builderState.collectAsState()
    val availableExercises by viewModel.availableExercises.collectAsState()
    val exerciseCategories by viewModel.exerciseCategories.collectAsState()

    // Clear error when user starts typing or making changes
    LaunchedEffect(state.programName, state.programType, state.programDays, state.programExercises) {
        if (state.error != null) {
            viewModel.clearError()
        }
    }

    // Initialize with one day if program days is empty
    LaunchedEffect(Unit) {
        if (state.programDays.isEmpty()) {
            viewModel.startNewProgram()
        }
    }

         

    var showExercisePickerForDay by remember { mutableStateOf<Int?>(null) }
    var previewExerciseId by remember { mutableStateOf<Long?>(null) }
    var showExitConfirmation by remember { mutableStateOf(false) }

         // Check if user has entered any meaningful data
     val hasUserData = remember(state) {
         state.programName.isNotBlank() ||
         state.programDescription.isNotBlank() ||
         state.programType.isNotBlank() ||
         state.durationEnabled ||
         state.programDays.any { it.name.isNotBlank() && it.name != "Day 1" } ||
         state.programExercises.values.any { it.isNotEmpty() } ||
         state.difficultyLevel != "Beginner" // Check if difficulty was changed from default
     }

    // Handle back navigation
    val handleBackNavigation = {
        if (hasUserData) {
            showExitConfirmation = true
        } else {
            onNavigateBack()
        }
    }

    val mainInteractionSource = remember { MutableInteractionSource() }
    val focusManager = LocalFocusManager.current
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                interactionSource = mainInteractionSource,
                indication = null
            ) {
                // Clear focus when clicking outside of fields
                focusManager.clearFocus()
            }
    ) {
        // Compact title bar
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 2.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = handleBackNavigation) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
                
                Text(
                    text = "Program Builder",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                
                Button(
                    onClick = { viewModel.saveProgramBuilder() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon(Icons.Filled.Save, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("Save")
                }
            }
        }

        // Error banner
        if (state.error != null) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Filled.Error,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(Modifier.width(12.dp))
                    Text(
                        text = state.error!!,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(
                        onClick = { viewModel.clearError() },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            Icons.Filled.Close,
                            contentDescription = "Dismiss error",
                            tint = MaterialTheme.colorScheme.onErrorContainer,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }

        // Main content
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentPadding = PaddingValues(bottom = WindowInsets.ime.asPaddingValues().calculateBottomPadding()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Program Info
            item {
                ElevatedCard(
                    modifier = Modifier.border(
                        BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                        shape = RoundedCornerShape(16.dp)
                    ),
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 3.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        // Header inside card
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.AutoMirrored.Filled.Assignment, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            Spacer(Modifier.width(12.dp))
                            Column(Modifier.weight(1f)) {
                                Text("Program Info", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                Text("Name, difficulty, type and duration", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                        HorizontalDivider()
                        var name by remember { mutableStateOf(state.programName) }
                        var description by remember { mutableStateOf(state.programDescription) }
                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it; viewModel.updateProgramInfo(name, description, state.difficultyLevel, state.programType, state.durationWeeks, state.durationEnabled) },
                            label = { Text("Program Name *") },
                            singleLine = true,
                            leadingIcon = { Icon(Icons.Filled.Title, contentDescription = null) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = AppTextFieldDefaults.outlinedColors(),
                            isError = state.error?.contains("Program name") == true && name.isBlank()
                        )
                        ExpandableDescriptionField(
                            value = description,
                            onValueChange = {
                                description = it
                                viewModel.updateProgramInfo(
                                    name,
                                    description,
                                    state.difficultyLevel,
                                    state.programType,
                                    state.durationWeeks,
                                    state.durationEnabled
                                )
                            },
                            colors = AppTextFieldDefaults.outlinedColors()
                        )
                        // Difficulty chips
                        DifficultyChips(
                            selected = state.difficultyLevel,
                            onSelect = { diff -> viewModel.updateProgramInfo(name, description, diff, state.programType, state.durationWeeks, state.durationEnabled) }
                        )

                        // Program Type (full width, below difficulty)
                        ProgramTypeDropdown(
                            selected = state.programType,
                            onChange = { type ->
                                viewModel.updateProgramInfo(name, description, state.difficultyLevel, type, state.durationWeeks, state.durationEnabled)
                            },
                            isError = state.error?.contains("program type") == true && state.programType.isBlank()
                        )

                        // Duration controls
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Filled.CalendarMonth, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                Spacer(Modifier.width(8.dp))
                                Text("Program Duration", style = MaterialTheme.typography.titleSmall)
                            }
                            Switch(
                                checked = state.durationEnabled,
                                onCheckedChange = { enabled ->
                                    viewModel.updateDurationEnabled(enabled)
                                    // Only call updateProgramInfo if we're disabling duration or if we need to update other fields
                                    if (!enabled) {
                                        viewModel.updateProgramInfo(name, description, state.difficultyLevel, state.programType, 0, enabled)
                                    }
                                }
                            )
                        }
                        if (state.durationEnabled) {
                            var weeks by remember { mutableStateOf(state.durationWeeks) }
                            Slider(
                                value = weeks.toFloat(),
                                onValueChange = { v ->
                                    weeks = v.toInt().coerceIn(1, 52)
                                    viewModel.updateProgramInfo(name, description, state.difficultyLevel, state.programType, weeks, true)
                                },
                                valueRange = 1f..52f,
                                steps = 50
                            )
                            Text("$weeks weeks", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Medium)
                        }

                                                 // Quick summary chips
                         Row(
                             modifier = Modifier.fillMaxWidth(),
                             horizontalArrangement = Arrangement.spacedBy(8.dp)
                         ) {
                             AssistChip(onClick = {}, label = { Text(state.difficultyLevel) })
                             if (state.programType.isNotBlank()) {
                                 AssistChip(onClick = {}, label = { Text(state.programType) })
                             }
                             if (state.durationEnabled) AssistChip(onClick = {}, label = { Text("${state.durationWeeks}w") })
                         }
                    }
                }
            }

            // Training days header + controls
            item {
                val headerInteractionSource = remember { MutableInteractionSource() }
                Row(
                    Modifier
                        .fillMaxWidth()
                        .clickable(
                            interactionSource = headerInteractionSource,
                            indication = null
                        ) {
                            focusManager.clearFocus()
                        },
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Training Days", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        val canAddDay = state.programDays.size < 7
                        val canRemoveDay = state.programDays.size > 1
                        OutlinedButton(
                            onClick = { viewModel.setProgramDays(state.programDays.map { it.name } + "Day ${state.programDays.size + 1}") },
                            enabled = canAddDay
                        ) { Text("Add Day") }
                        OutlinedButton(
                            onClick = { viewModel.removeProgramDay(state.programDays.size - 1) },
                            enabled = canRemoveDay
                        ) { Text("Remove Day") }
                    }
                }
            }

                         // Days list
             itemsIndexed(state.programDays) { dayIndex, day ->
                val dayExercises = state.programExercises[day.id].orEmpty()
                val needsExercises = !day.isRestDay && dayExercises.isEmpty()
                val hasError = state.error?.contains("exercises") == true && needsExercises
                
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = if (hasError) {
                            MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.1f)
                        } else {
                            MaterialTheme.colorScheme.surface
                        }
                    )
                ) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                            var dayName by remember(day.id) { mutableStateOf(day.name) }
                            OutlinedTextField(
                                value = dayName,
                                onValueChange = { dayName = it; 
                                    val names = state.programDays.mapIndexed { i, d -> if (i == dayIndex) dayName else d.name }
                                    viewModel.setProgramDays(names)
                                },
                                label = { Text("Day ${dayIndex + 1} *") },
                                modifier = Modifier.weight(1f),
                                colors = AppTextFieldDefaults.outlinedColors(),
                                isError = state.error?.contains("names") == true && dayName.isBlank()
                            )
                            Spacer(Modifier.width(8.dp))
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                IconButton(onClick = { viewModel.moveProgramDay(dayIndex, maxOf(0, dayIndex - 1)) }, enabled = dayIndex > 0) {
                                    Icon(Icons.Filled.ArrowUpward, contentDescription = null)
                                }
                                IconButton(onClick = { viewModel.moveProgramDay(dayIndex, minOf(state.programDays.lastIndex, dayIndex + 1)) }, enabled = dayIndex < state.programDays.lastIndex) {
                                    Icon(Icons.Filled.ArrowDownward, contentDescription = null)
                                }
                            }
                        }

                        // Rest day toggle
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Rest Day", style = MaterialTheme.typography.titleSmall)
                            Switch(
                                checked = day.isRestDay,
                                onCheckedChange = { enabled -> viewModel.toggleRestDay(dayIndex, enabled) }
                            )
                        }

                        // Exercises
                        if (day.isRestDay) {
                            Text("This is a rest day.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                        } else {
                            if (dayExercises.isEmpty()) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    if (hasError) {
                                        Icon(
                                            Icons.Filled.Error,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.error,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(Modifier.width(8.dp))
                                    }
                                    Text(
                                        "No exercises added yet",
                                        color = if (hasError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            } else {
                                dayExercises.forEachIndexed { exIndex, ex ->
                                    ExerciseRow(
                                        exercise = ex,
                                        getExerciseById = viewModel::getExerciseById,
                                        onMoveUp = { viewModel.moveExerciseWithinDay(dayIndex, exIndex, exIndex - 1) },
                                        onMoveDown = { viewModel.moveExerciseWithinDay(dayIndex, exIndex, exIndex + 1) },
                                        onRemove = { viewModel.removeExerciseFromProgramDay(dayIndex, exIndex) },
                                        onUpdate = { sets, reps, rest -> viewModel.updateExerciseConfiguration(dayIndex, exIndex, sets, reps, rest) },
                                        onPreview = { previewExerciseId = it }
                                    )
                                }
                            }

                            OutlinedButton(onClick = { showExercisePickerForDay = dayIndex }) {
                                Icon(Icons.Filled.Add, contentDescription = null)
                                Spacer(Modifier.width(6.dp))
                                Text("Add Exercise")
                            }
                        }
                    }
                }
            }

            // Bottom save buttons
            item {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(onClick = { viewModel.saveProgramBuilder() }, modifier = Modifier.weight(1f)) {
                        Icon(Icons.Filled.Save, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Save Program")
                    }
                    OutlinedButton(onClick = { viewModel.saveProgramBuilderAsTemplate() }, modifier = Modifier.weight(1f)) {
                        Text("Save as Template")
                    }
                }
            }
        }
    }

    // Exit confirmation dialog
    if (showExitConfirmation) {
        AlertDialog(
            onDismissRequest = { showExitConfirmation = false },
            title = { Text("Discard Program?") },
            text = { Text("You have unsaved changes. Are you sure you want to discard this program?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onNavigateBack()
                        showExitConfirmation = false
                    }
                ) {
                    Text("Discard")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showExitConfirmation = false }
                ) {
                    Text("Continue Editing")
                }
            }
        )
    }

    if (showExercisePickerForDay != null) {
        FullScreenExercisePickerRevamp(
            exercises = availableExercises,
            categories = exerciseCategories,
            onDismiss = { showExercisePickerForDay = null },
            onExerciseSelected = { exerciseId ->
                viewModel.addExerciseToProgramDay(showExercisePickerForDay!!, exerciseId)
                showExercisePickerForDay = null
            },
            onExerciseClicked = { exerciseId ->
                previewExerciseId = exerciseId
            }
        )
    }

    val previewExercise = previewExerciseId?.let { id -> viewModel.getExerciseById(id) }
    if (previewExercise != null) {
        ExercisePreviewSheet(
            exercise = previewExercise,
            onDismiss = { previewExerciseId = null }
        )
    }
}

private fun generateDayNamesLocal(daysPerWeek: Int, programType: String): List<String> {
    val base = when (programType) {
        "Push/Pull/Legs" -> listOf("Push", "Pull", "Legs")
        "Upper/Lower" -> listOf("Upper", "Lower")
        "Full Body", "Bodyweight" -> (1..daysPerWeek).map { "Full Body ${('A' + it - 1)}" }
        "Powerlifting" -> listOf("Squat", "Bench", "Deadlift", "OHP")
        else -> (1..daysPerWeek).map { "Day $it" }
    }
    return when (programType) {
        "Push/Pull/Legs", "Upper/Lower" -> buildList {
            var remaining = daysPerWeek
            while (remaining > 0) {
                val toAdd = minOf(remaining, base.size)
                addAll(base.take(toAdd))
                remaining -= toAdd
            }
        }
        else -> base.take(daysPerWeek)
    }
}

@Composable
private fun DifficultyDropdown(selected: String, onChange: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
            OutlinedTextField(
            readOnly = true,
            value = selected,
            onValueChange = {},
            label = { Text("Difficulty") },
            modifier = Modifier
                .fillMaxWidth()
                    .menuAnchor(MenuAnchorType.PrimaryNotEditable, enabled = true)
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            listOf("Beginner", "Intermediate", "Advanced").forEach { value ->
                DropdownMenuItem(text = { Text(value) }, onClick = { expanded = false; onChange(value) })
            }
        }
    }
}

@Composable
private fun DifficultyChips(selected: String, onSelect: (String) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Difficulty", style = MaterialTheme.typography.titleSmall)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("Beginner", "Intermediate", "Advanced").forEach { d ->
                FilterChip(
                    selected = selected == d,
                    onClick = { onSelect(d) },
                    label = { Text(d) }
                )
            }
        }
    }
}

@Composable
private fun ProgramTypeDropdown(selected: String, onChange: (String) -> Unit, isError: Boolean = false) {
    var expanded by remember { mutableStateOf(false) }
    val choices = listOf("Full Body", "Upper/Lower", "Push/Pull/Legs", "Powerlifting", "Bodybuilding", "Custom")
    Column {
        Text("Type *", style = MaterialTheme.typography.titleSmall)
        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
            OutlinedTextField(
                readOnly = true,
                value = selected.ifBlank { "" },
                onValueChange = {},
                placeholder = { Text("Choose one") },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(MenuAnchorType.PrimaryNotEditable, enabled = true),
                isError = isError,
                colors = if (isError) {
                    OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.error,
                        unfocusedBorderColor = MaterialTheme.colorScheme.error
                    )
                } else {
                    OutlinedTextFieldDefaults.colors()
                }
            )
            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                choices.forEach { value -> DropdownMenuItem(text = { Text(value) }, onClick = { expanded = false; onChange(value) }) }
            }
        }
    }
}

@Composable
private fun ExpandableDescriptionField(
    value: String,
    onValueChange: (String) -> Unit,
    colors: androidx.compose.material3.TextFieldColors? = null
) {
    var focused by remember { mutableStateOf(false) }
    val expanded = focused || value.isNotBlank()
    val useSecondLineTransform = expanded && value.isNotBlank().not() || (expanded && value.length < 36 && !value.contains('\n'))
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text("Description (optional)") },
        leadingIcon = { Icon(Icons.Filled.Description, contentDescription = null) },
        singleLine = !expanded,
        minLines = if (expanded) 3 else 1,
        visualTransformation = if (useSecondLineTransform) SecondLineWhenShortTransformation(true) else VisualTransformation.None,
        modifier = Modifier
            .fillMaxWidth()
            .onFocusEvent { focused = it.isFocused },
        colors = colors ?: OutlinedTextFieldDefaults.colors()
    )
}

private class SecondLineWhenShortTransformation(
    private val enabled: Boolean
) : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        if (!enabled) return TransformedText(text, OffsetMapping.Identity)
        val prefix = "\n"
        val out = AnnotatedString(prefix + text.text)
        val mapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int = offset + 1
            override fun transformedToOriginal(offset: Int): Int = (offset - 1).coerceAtLeast(0)
        }
        return TransformedText(out, mapping)
    }
}

@Composable
private fun ExerciseRow(
    exercise: ProgramExercise,
    getExerciseById: (Long) -> Exercise?,
    onMoveUp: () -> Unit,
    onMoveDown: () -> Unit,
    onRemove: () -> Unit,
    onUpdate: (String, String, Int) -> Unit,
    onPreview: (Long) -> Unit
) {
    val details = getExerciseById(exercise.exerciseId)
    var sets by remember { mutableStateOf(exercise.sets) }
    var reps by remember { mutableStateOf(exercise.reps ?: "10") }
    var rest by remember { mutableStateOf(exercise.restTimeSeconds ?: 60) }

    Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
        Column(Modifier.padding(12.dp)) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                if (!details?.imageUrl.isNullOrBlank()) {
                    AsyncImage(
                        model = details?.imageUrl,
                        contentDescription = details?.name,
                        modifier = Modifier.size(40.dp).clickable { onPreview(exercise.exerciseId) }
                    )
                }
                val subtitle = (details?.category ?: "").replaceFirstChar { ch ->
                    if (ch.isLowerCase()) ch.titlecase(java.util.Locale.getDefault()) else ch.toString()
                }
                Column(
                    Modifier
                        .weight(1f)
                        .padding(horizontal = 12.dp)
                        .clickable { onPreview(exercise.exerciseId) }
                ) {
                    Text(details?.name ?: "Exercise ${exercise.exerciseId}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text(subtitle, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    IconButton(onClick = onMoveUp) { Icon(Icons.Filled.ArrowUpward, contentDescription = null) }
                    IconButton(onClick = onMoveDown) { Icon(Icons.Filled.ArrowDownward, contentDescription = null) }
                }
                IconButton(onClick = onRemove) { Icon(Icons.Filled.Delete, contentDescription = null, tint = MaterialTheme.colorScheme.error) }
            }
            Spacer(Modifier.height(8.dp))
            Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                // Till Failure toggle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Till Failure", style = MaterialTheme.typography.titleSmall)
                    Switch(
                        checked = exercise.tillFailure,
                        onCheckedChange = { checked ->
                            if (checked) {
                                onUpdate(sets, "Till Failure", rest)
                            } else {
                                onUpdate(sets, "10", rest)
                            }
                        }
                    )
                }

                // Sets and Reps inputs
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = sets,
                        onValueChange = { newValue ->
                            val filtered = newValue.filter { it.isDigit() }
                            sets = filtered
                            onUpdate(filtered, reps, rest)
                        },
                        label = { Text("Sets") },
                        modifier = Modifier.weight(1f),
                        colors = AppTextFieldDefaults.outlinedColors(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    OutlinedTextField(
                        value = if (exercise.tillFailure) "Failure" else reps,
                        onValueChange = { newValue ->
                            if (!exercise.tillFailure) {
                                val filtered = newValue.filter { it.isDigit() }
                                reps = filtered
                                onUpdate(sets, filtered, rest)
                            }
                        },
                        label = { Text("Reps") },
                        modifier = Modifier.weight(1f),
                        colors = AppTextFieldDefaults.outlinedColors(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        enabled = !exercise.tillFailure,
                        readOnly = exercise.tillFailure
                    )
                }
            }
        }
    }
}

@Composable
private fun FullScreenExercisePickerRevamp(
    exercises: List<Exercise>,
    categories: List<String>,
    onDismiss: () -> Unit,
    onExerciseSelected: (Long) -> Unit,
    onExerciseClicked: (Long) -> Unit
) {
    var search by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("All") }
    var selectedEquipment by remember { mutableStateOf("All") }
    var selectedMechanic by remember { mutableStateOf("All") }
    var selectedForce by remember { mutableStateOf("All") }
    var selectedPrimary by remember { mutableStateOf("All") }

    // Build filter option lists from provided exercises
    val categoryOptions = remember(exercises, categories) {
        val fromData = exercises.map { it.category }
            .filter { it.isNotBlank() }
            .distinct()
            .sorted()
        listOf("All") + (if (categories.isNotEmpty()) categories else fromData)
    }
    val equipmentOptions = remember(exercises) {
        listOf("All") + exercises.map { it.equipment }
            .filter { it.isNotBlank() }
            .distinct()
            .sorted()
    }
    val mechanicOptions = remember(exercises) {
        listOf("All") + exercises.mapNotNull { it.mechanic }
            .filter { it.isNotBlank() }
            .distinct()
            .sorted()
    }
    val forceOptions = remember(exercises) {
        listOf("All") + exercises.mapNotNull { it.force }
            .filter { it.isNotBlank() }
            .distinct()
            .sorted()
    }
    val primaryMuscleOptions = remember(exercises) {
        listOf("All") + exercises.flatMap { it.muscleGroups }
            .filter { it.isNotBlank() }
            .distinct()
            .sorted()
    }

    val filtered = remember(
        exercises,
        search,
        selectedCategory,
        selectedEquipment,
        selectedMechanic,
        selectedForce,
        selectedPrimary
    ) {
        exercises.filter { ex ->
            val matchesSearch = if (search.isBlank()) {
                true
            } else {
                val searchWords = search.trim().split("\\s+".toRegex()).filter { it.isNotBlank() }
                searchWords.all { word ->
                    ex.name.contains(word, ignoreCase = true)
                }
            }
            val matchesCategory = selectedCategory == "All" || ex.category.equals(selectedCategory, ignoreCase = true)
            val matchesEquipment = selectedEquipment == "All" || ex.equipment.equals(selectedEquipment, ignoreCase = true)
            val matchesMechanic = selectedMechanic == "All" || (ex.mechanic?.equals(selectedMechanic, ignoreCase = true) == true)
            val matchesForce = selectedForce == "All" || (ex.force?.equals(selectedForce, ignoreCase = true) == true)
            val matchesPrimary = selectedPrimary == "All" || ex.muscleGroups.any { it.equals(selectedPrimary, ignoreCase = true) }
            matchesSearch && matchesCategory && matchesEquipment && matchesMechanic && matchesForce && matchesPrimary
        }
    }
    androidx.compose.ui.window.Dialog(onDismissRequest = onDismiss, properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false)) {
        Surface(modifier = Modifier.fillMaxWidth(0.98f).fillMaxHeight(0.92f)) {
            Column(Modifier.fillMaxSize()) {
                TopAppBar(title = { Text("Select Exercise") }, navigationIcon = {
                    IconButton(onClick = onDismiss) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null) }
                })
                Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(value = search, onValueChange = { search = it }, label = { Text("Search") }, modifier = Modifier.fillMaxWidth())

                    // Filters card matching Exercise Library screen
                    var filtersExpanded by remember { mutableStateOf(false) }
                    ElevatedCard(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(Modifier.fillMaxWidth().padding(12.dp)) {
                            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Filters",
                                    style = MaterialTheme.typography.titleSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.weight(1f)
                                )
                                IconButton(onClick = { filtersExpanded = !filtersExpanded }) {
                                    Icon(
                                        imageVector = if (filtersExpanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                                        contentDescription = if (filtersExpanded) "Collapse filters" else "Expand filters"
                                    )
                                }
                            }

                            if (filtersExpanded) {
                                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                    // Category chips
                                    FilterChipRowSimple(
                                        value = selectedCategory,
                                        options = categoryOptions,
                                        onSelected = { selectedCategory = it }
                                    )
                                    // Equipment chips
                                    FilterChipRowSimple(
                                        value = selectedEquipment,
                                        options = equipmentOptions,
                                        onSelected = { selectedEquipment = it }
                                    )
                                    // Force chips
                                    FilterChipRowSimple(
                                        value = selectedForce,
                                        options = forceOptions,
                                        onSelected = { selectedForce = it }
                                    )
                                    // Mechanic chips
                                    FilterChipRowSimple(
                                        value = selectedMechanic,
                                        options = mechanicOptions,
                                        onSelected = { selectedMechanic = it }
                                    )
                                    // Primary muscle chips
                                    FilterChipRowSimple(
                                        value = selectedPrimary,
                                        options = primaryMuscleOptions,
                                        onSelected = { selectedPrimary = it }
                                    )
                                }
                            }
                        }
                    }
                    LazyColumn(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        itemsIndexed(filtered) { _, ex ->
                            Card {
                                Row(
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    if (!ex.imageUrl.isNullOrBlank()) {
                                        AsyncImage(model = ex.imageUrl, contentDescription = ex.name, modifier = Modifier.size(48.dp))
                                    }
                                    Column(
                                        Modifier
                                            .weight(1f)
                                            .padding(horizontal = 12.dp)
                                            .clickable { onExerciseClicked(ex.id) }
                                    ) {
                                        Text(ex.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                        val subCategory = ex.category.takeIf { it.isNotBlank() && it.lowercase() != "null" }?.let { titleCaseAllWords(it) }
                                        val subEquipment = ex.equipment.takeIf { it.isNotBlank() && it.lowercase() != "null" }?.let { titleCaseAllWords(it) }
                                        Text(listOfNotNull(subCategory, subEquipment).joinToString(" • "), color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                    ElevatedButton(
                                        onClick = { onExerciseSelected(ex.id) },
                                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                                        colors = ButtonDefaults.elevatedButtonColors(
                                            containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                            contentColor = MaterialTheme.colorScheme.onSurface
                                        )
                                    ) {
                                        Icon(Icons.Filled.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                                        Spacer(Modifier.width(4.dp))
                                        Text("Add")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FilterChipRowSimple(
    value: String,
    options: List<String>,
    onSelected: (String) -> Unit
) {
    androidx.compose.foundation.lazy.LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(options.size) { index ->
            val option = options[index]
            FilterChip(
                selected = value == option,
                onClick = { onSelected(option) },
                label = { Text(titleCaseAllWords(option)) },
                leadingIcon = if (value == option) {
                    { Icon(Icons.Filled.Check, contentDescription = null, modifier = Modifier.size(FilterChipDefaults.IconSize)) }
                } else null
            )
        }
    }
}

@Composable
fun ExercisePreviewSheet(
    exercise: Exercise,
    onDismiss: () -> Unit
) {
    var showFullImage by remember { mutableStateOf(false) }
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (!exercise.imageUrl.isNullOrBlank()) {
                    AsyncImage(
                        model = exercise.imageUrl,
                        contentDescription = exercise.name,
                        modifier = Modifier.size(64.dp).clickable { showFullImage = true }
                    )
                    Spacer(Modifier.width(12.dp))
                }
                Column(Modifier.weight(1f)) {
                    Text(exercise.name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    val subtitle = (exercise.category.ifBlank { exercise.exerciseType }).replaceFirstChar { ch ->
                        if (ch.isLowerCase()) ch.titlecase(java.util.Locale.getDefault()) else ch.toString()
                    }
                    Text(subtitle, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
                if (!exercise.instructions.isNullOrBlank()) {
                Text(exercise.instructions, style = MaterialTheme.typography.bodyMedium)
            }
            Spacer(Modifier.height(8.dp))
        }
    }
    if (showFullImage && !exercise.imageUrl.isNullOrBlank()) {
        FullScreenImageDialog(imageUrl = exercise.imageUrl, contentDescription = exercise.name) {
            showFullImage = false
        }
    }
}

private fun titleCaseAllWords(input: String): String {
    if (input.isBlank()) return input
    return input.split(" ").filter { it.isNotBlank() }.joinToString(" ") { word ->
        word.replaceFirstChar { ch ->
            if (ch.isLowerCase()) ch.titlecase(java.util.Locale.getDefault()) else ch.toString()
        }
    }
}


