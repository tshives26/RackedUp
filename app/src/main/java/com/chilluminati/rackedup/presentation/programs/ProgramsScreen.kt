package com.chilluminati.rackedup.presentation.programs

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.chilluminati.rackedup.R
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.chilluminati.rackedup.data.database.entity.ProgramDay
import com.chilluminati.rackedup.data.database.entity.ProgramExercise
import com.chilluminati.rackedup.presentation.components.GradientBackground
import com.chilluminati.rackedup.presentation.components.GlassmorphismCard
import com.chilluminati.rackedup.presentation.components.BouncyButton

/**
 * Programs screen showing workout routines and program builder
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Suppress("UNUSED_PARAMETER")
fun ProgramsScreen(
    onNavigateToActiveWorkout: (Long?) -> Unit,
    onNavigateToSelectProgramDay: (Long) -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: ProgramsViewModel = hiltViewModel()
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = remember { listOf("My Programs", "Templates", "Builder") }
    val builderState by viewModel.builderState.collectAsStateWithLifecycle()

    // Auto-switch to builder tab when creating a new program
    LaunchedEffect(builderState.isCreating) {
        if (builderState.isCreating) {
            selectedTab = 2 // Switch to Builder tab
        } else if (selectedTab == 2) {
            // If we're on the builder tab and not creating, switch back to first tab
            selectedTab = 0
        }
    }

    GradientBackground(
        modifier = modifier.fillMaxSize()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header - only show when not on builder tab
            if (selectedTab != 2) {
                ProgramsHeader(onCreateProgram = { viewModel.startNewProgram() })
            }

        // Tab layout (hidden while actively building a program)
        if (!(selectedTab == 2 && builderState.isCreating)) {
            PrimaryTabRow(selectedTabIndex = selectedTab) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        text = { Text(title) },
                        selected = selectedTab == index,
                        onClick = {
                            if (index == 2) {
                                // Open builder directly in Create New Program mode
                                viewModel.startNewProgram()
                                selectedTab = 2
                            } else {
                                selectedTab = index
                            }
                        }
                    )
                }
            }
        }

        // Content based on selected tab
        when (selectedTab) {
            0 -> MyProgramsTab(
                onNavigateToSelectProgramDay = onNavigateToSelectProgramDay,
                viewModel = viewModel
            )
            1 -> ProgramTemplatesTab(
                viewModel = viewModel,
                onTemplateUsed = { selectedTab = 0 }
            )
            2 -> ProgramBuilderTab(viewModel = viewModel)
        }
    }
    }
}

@Composable
private fun ProgramsHeader(onCreateProgram: () -> Unit) {
    GlassmorphismCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        backgroundAlpha = 0.15f
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(R.string.programs),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Create and follow structured workout programs",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            BouncyButton(
                onClick = onCreateProgram,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Create New Program",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun MyProgramsTab(
    onNavigateToSelectProgramDay: (Long) -> Unit,
    viewModel: ProgramsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val userPrograms = uiState.userPrograms
    val active = uiState.activeProgram
    val hasUserPrograms = userPrograms.isNotEmpty()
    
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        // Current Program (if any)
        item {
            Text(
                text = "Current Program",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }

        if (active == null) {
            // Show empty state for current program
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayCircleOutline,
                                contentDescription = null,
                                modifier = Modifier.size(48.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "No active program",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                            )
                            Text(
                                text = "Start a program to track your progress",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                            )
                        }
                    }
                }
            }
        } else {
            item {
                CurrentProgramCard(
                    programName = active.name,
                    description = active.description,
                    lastCompleted = uiState.activeProgramLastCompleted,
                    onStartWorkout = { onNavigateToSelectProgramDay(active.id) }
                )
            }
        }

        // My Custom Programs
        item {
            Text(
                text = "My Programs",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }

        if (!hasUserPrograms) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = null,
                                modifier = Modifier.size(48.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "No custom programs yet",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                            )
                            Text(
                                text = "Create your first program to get started",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            OutlinedButton(
                                onClick = { viewModel.startNewProgram() }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Create Program")
                            }
                        }
                    }
                }
            }
        } else {
            items(userPrograms) { program ->
                ProgramCard(
                    programName = program.name,
                    subtitle = if (program.durationWeeks != null) "${program.durationWeeks} weeks" else program.description ?: "",
                    isCustom = program.isCustom,
                    onStart = { onNavigateToSelectProgramDay(program.id) },
                    onPreview = { viewModel.loadProgramDetails(program.id) },
                    onSetActive = { viewModel.setActiveProgram(program) },
                    onEdit = { viewModel.beginEditProgram(program.id) }
                )
            }
        }
    }

    // Preview dialog
    val selectedProgram = uiState.selectedProgram
    if (selectedProgram != null) {
        ProgramPreviewSheet(
            program = selectedProgram,
            days = uiState.selectedProgramDays,
            exercisesByDay = uiState.selectedProgramExercises,
            getExerciseName = { id -> viewModel.getExerciseById(id)?.name ?: "Exercise $id" },
            onDismiss = { viewModel.clearSelection() },
            onEdit = {
                viewModel.beginEditProgram(selectedProgram.id)
                viewModel.clearSelection()
            },
            onDelete = { viewModel.deleteProgram(selectedProgram); viewModel.clearSelection() }
        )
    }
}

@Composable
private fun ProgramTemplatesTab(
    viewModel: ProgramsViewModel = hiltViewModel(),
    onTemplateUsed: () -> Unit
) {
    val templates = remember { ProgramTemplatesSystem.getAllTemplates() }
    var previewTemplate by remember { mutableStateOf<ProgramTemplate?>(null) }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        item {
            Text(
                text = "Popular Programs",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }

        items(templates) { tpl ->
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(tpl.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text(listOf("${tpl.durationWeeks} weeks", tpl.difficultyLevel, tpl.programType).joinToString(" • "), color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(tpl.description, style = MaterialTheme.typography.bodyMedium)
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = {
                                viewModel.createProgramFromTemplate(tpl.id)
                                onTemplateUsed()
                            },
                            modifier = Modifier.weight(1f)
                        ) { Text("Use Template") }
                        OutlinedButton(
                            onClick = { previewTemplate = tpl },
                            modifier = Modifier.weight(1f)
                        ) { Text("Preview") }
                    }
                }
            }
        }
    }

    // Template preview sheet
    val tpl = previewTemplate
    if (tpl != null) {
        TemplatePreviewSheet(
            template = tpl,
            onDismiss = { previewTemplate = null }
        )
    }
}

@Composable
private fun ProgramBuilderTab(
    viewModel: ProgramsViewModel = hiltViewModel()
) {
    val builderState by viewModel.builderState.collectAsState()
    
    if (builderState.isCreating) {
        // Use the new single-screen builder
        NewProgramBuilderScreen(
            onNavigateBack = { 
                viewModel.cancelProgramBuilder()
            },
            viewModel = viewModel
        )
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            item {
                BuilderOptionCard(
                    title = "Create New Program",
                    description = "Build a custom workout program from scratch with our visual builder",
                    icon = Icons.Default.Add,
                    onClick = { 
                        viewModel.startNewProgram()
                    }
                )
            }

            item {
                BuilderOptionCard(
                    title = "Copy Template",
                    description = "Start with a proven template and customize it to your needs",
                    icon = Icons.Default.ContentCopy,
                    onClick = { /* TODO: Show template picker */ }
                )
            }

            item {
                BuilderOptionCard(
                    title = "Import Program",
                    description = "Import a program from a file or another app",
                    icon = Icons.Default.Upload,
                    onClick = { /* TODO: Import functionality */ }
                )
            }
        }
    }
}

@Composable
private fun CurrentProgramCard(
    programName: String,
    description: String?,
    lastCompleted: String?,
    onStartWorkout: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = programName,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
            
            Spacer(modifier = Modifier.height(12.dp))

            if (!description.isNullOrBlank()) {
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            if (!lastCompleted.isNullOrBlank()) {
                Text(
                    text = lastCompleted,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
            
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = onStartWorkout,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Start Workout")
                }
                // Removed per spec: manual finish action isn't supported anymore
            }
        }
    }
}

@Composable
private fun ProgramCard(
    programName: String,
    subtitle: String,
    isCustom: Boolean,
    onStart: () -> Unit,
    onPreview: () -> Unit = {},
    onSetActive: () -> Unit = {},
    onEdit: () -> Unit = {}
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = programName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                if (isCustom) {
                    IconButton(onClick = onEdit) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit",
                            modifier = Modifier.size(20.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onPreview,
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text("Preview", maxLines = 1, softWrap = false, overflow = TextOverflow.Ellipsis)
                }

                Button(
                    onClick = onStart,
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text("Start", maxLines = 1, softWrap = false, overflow = TextOverflow.Ellipsis)
                }

                OutlinedButton(
                    onClick = onSetActive,
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text("Set Active", maxLines = 1, softWrap = false, overflow = TextOverflow.Ellipsis)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProgramPreviewSheet(
    program: com.chilluminati.rackedup.data.database.entity.Program,
    days: List<ProgramDay>,
    exercisesByDay: Map<ProgramDay, List<ProgramExercise>>,
    getExerciseName: (Long) -> String,
    onDismiss: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    var showDeleteConfirm by remember { mutableStateOf(false) }
    ModalBottomSheet(
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.92f)
        ) {
            // Header
            TopAppBar(
                title = { Text("Preview Workout", style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                },
                actions = {
                    IconButton(onClick = onEdit) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit")
                    }
                    IconButton(onClick = { showDeleteConfirm = true }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                    }
                }
            )

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(top = 8.dp, bottom = 24.dp)
                ) {
                    // Program meta card
                    item {
                        Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)) {
                            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Column(Modifier.weight(1f)) {
                                        Text(program.name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                                        Text(listOfNotNull(
                                            program.durationWeeks?.let { "$it weeks" },
                                            program.difficultyLevel,
                                            program.programType
                                        ).joinToString(" • "), style = MaterialTheme.typography.bodyMedium)
                                    }
                                    Text(
                                        if (program.isActive) "Active" else "Inactive",
                                        style = MaterialTheme.typography.labelLarge,
                                        color = MaterialTheme.colorScheme.onSecondaryContainer
                                    )
                                }
                                if (!program.description.isNullOrBlank()) {
                                    Text(program.description, style = MaterialTheme.typography.bodyMedium)
                                }
                            }
                        }
                    }

                    // Days and exercises (sorted by dayNumber)
                    val sortedDays = days.sortedBy { it.dayNumber }
                    items(sortedDays.size) { idx ->
                        val day = sortedDays[idx]
                        Card {
                            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text("Day ${day.dayNumber}: ${day.name}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                if (day.isRestDay) {
                                    Text("Rest Day", color = MaterialTheme.colorScheme.onSurfaceVariant)
                                } else {
                                    val exercises = exercisesByDay[day].orEmpty()
                                    if (exercises.isEmpty()) {
                                        Text("No exercises", color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    } else {
                                        exercises.forEach { pe ->
                                            Row(
                                                Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    getExerciseName(pe.exerciseId),
                                                    modifier = Modifier.weight(1f),
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis
                                                )
                                                Text("${pe.sets} x ${pe.reps ?: "-"}")
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    item { Spacer(Modifier.height(8.dp)) }
                }

                if (showDeleteConfirm) {
                    DeleteConfirmationDialog(
                        onDismiss = { showDeleteConfirm = false },
                        onConfirm = {
                            showDeleteConfirm = false
                            onDelete()
                        }
                    )
                }
            }
        }
    }

@Composable
private fun DeleteConfirmationDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    var secondsLeft by remember { mutableStateOf(5) }
    LaunchedEffect(Unit) {
        for (i in 5 downTo 1) {
            secondsLeft = i
            kotlinx.coroutines.delay(1000)
        }
        secondsLeft = 0
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Delete Program?") },
        text = { Text("This will permanently delete the program and its workouts.") },
        confirmButton = {
            TextButton(onClick = onConfirm, enabled = secondsLeft == 0) {
                Text(if (secondsLeft == 0) "OK" else "OK (${secondsLeft})")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TemplatePreviewSheet(
    template: ProgramTemplate,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.92f)
        ) {
            TopAppBar(
                title = { Text("Preview Template", style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(top = 8.dp, bottom = 24.dp)
            ) {
                // Meta card
                item {
                    Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)) {
                        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(template.name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                            Text(
                                listOf(
                                    "${template.durationWeeks} weeks",
                                    template.difficultyLevel,
                                    template.programType
                                ).joinToString(" • "),
                                style = MaterialTheme.typography.bodyMedium
                            )
                            if (template.description.isNotBlank()) {
                                Text(template.description, style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }
                }

                // Days and exercises
                items(template.days.size) { idx ->
                    val day = template.days[idx]
                    Card {
                        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text("Day ${idx + 1}: ${day.name}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            if (day.exercises.isEmpty()) {
                                Text("Rest Day", color = MaterialTheme.colorScheme.onSurfaceVariant)
                            } else {
                                day.exercises.forEach { ex ->
                                    Row(
                                        Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            ex.exerciseName,
                                            modifier = Modifier.weight(1f),
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        val right = if (ex.sets.contains('x')) {
                                            ex.sets
                                        } else {
                                            ex.sets + (ex.reps?.let { " x $it" } ?: "")
                                        }
                                        Text(right)
                                    }
                                }
                            }
                        }
                    }
                }
                item { Spacer(Modifier.height(8.dp)) }
            }
        }
    }
}

private fun <T> List<T>?.orElseEmpty(): List<T> = this ?: emptyList()
@Composable
private fun BuilderOptionCard(
    title: String,
    description: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Go",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
