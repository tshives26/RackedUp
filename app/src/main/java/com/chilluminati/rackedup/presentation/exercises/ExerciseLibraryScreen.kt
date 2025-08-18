package com.chilluminati.rackedup.presentation.exercises

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.saveable.rememberSaveable
import coil.compose.AsyncImage
import androidx.compose.ui.input.nestedscroll.nestedScroll
import com.chilluminati.rackedup.R
import com.chilluminati.rackedup.data.database.entity.Exercise
import com.chilluminati.rackedup.presentation.components.EmptyStateCard
import com.chilluminati.rackedup.presentation.components.AppTextFieldDefaults
import com.chilluminati.rackedup.presentation.components.SecondaryButton

/**
 * Exercise library screen showing all available exercises
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseLibraryScreen(
    onNavigateBack: () -> Unit,
    onNavigateToExerciseDetail: (Long) -> Unit,
    onExerciseSelected: ((Long) -> Unit)? = null, // For selecting exercises during active workout
    isSelectionMode: Boolean = false,
    showBackButton: Boolean = true,
    onNavigateToCreateExercise: () -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: ExerciseLibraryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
    
    // Show error snackbar if there's an error
    LaunchedEffect(uiState.error) {
        uiState.error?.let {
            // In a real app, you'd show a snackbar here
            viewModel.clearError()
        }
    }

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(if (isSelectionMode) "Select Exercise" else stringResource(R.string.exercise_library))
                },
                navigationIcon = if (showBackButton) {
                    {
                        IconButton(onClick = onNavigateBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(R.string.back)
                            )
                        }
                    }
                } else {
                    {}
                },
                actions = {
                    if (!isSelectionMode) {
                        val favoritesOnly by viewModel.showFavoritesOnly.collectAsStateWithLifecycle()
                        IconButton(onClick = { viewModel.toggleFavoritesOnly() }) {
                            Icon(
                                imageVector = if (favoritesOnly) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Toggle favorites"
                            )
                        }
                        IconButton(onClick = onNavigateToCreateExercise) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = stringResource(R.string.add_exercise)
                            )
                        }
                    }
                },
                scrollBehavior = scrollBehavior,
                windowInsets = if (showBackButton) TopAppBarDefaults.windowInsets else WindowInsets(0, 0, 0, 0)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .padding(innerPadding)
                .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 0.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.updateSearchQuery(it) },
                label = { Text("Search exercises") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null
                    )
                },
                modifier = Modifier
                    .fillMaxWidth(),
                singleLine = true,
                colors = AppTextFieldDefaults.colors()
            )

            // Collapsible Filters (Category, Equipment, Mechanic, Primary) as one unit
            val categories by viewModel.categories.collectAsStateWithLifecycle()
            val equipment by viewModel.selectedEquipment.collectAsStateWithLifecycle()
            val equipmentOptions by viewModel.equipmentOptions.collectAsStateWithLifecycle()
            val force by viewModel.selectedForce.collectAsStateWithLifecycle()
            val forceOptions by viewModel.forceOptions.collectAsStateWithLifecycle()
            val mechanic by viewModel.selectedMechanic.collectAsStateWithLifecycle()
            val mechanicOptions by viewModel.mechanicOptions.collectAsStateWithLifecycle()
            val primary by viewModel.selectedPrimaryMuscle.collectAsStateWithLifecycle()
            val primaryOptions by viewModel.primaryMuscleOptions.collectAsStateWithLifecycle()

            var filtersExpanded by rememberSaveable { mutableStateOf(false) }

            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Filters",
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(onClick = { filtersExpanded = !filtersExpanded }) {
                            Icon(
                                imageVector = if (filtersExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                contentDescription = if (filtersExpanded) "Collapse filters" else "Expand filters"
                            )
                        }
                    }

                    if (filtersExpanded) {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            // Category chips (no title)
                            ExerciseCategoryChips(
                                selectedCategory = selectedCategory,
                                categories = categories,
                                onCategorySelected = { viewModel.updateSelectedCategory(it) }
                            )

                            // Equipment chips (no title)
                            ChipRow(
                                value = equipment,
                                options = equipmentOptions,
                                onSelected = { viewModel.updateSelectedEquipment(it) }
                            )

                            // Force chips (no title)
                            ChipRow(
                                value = force,
                                options = forceOptions,
                                onSelected = { viewModel.updateSelectedForce(it) }
                            )

                            // Mechanic chips (no title)
                            ChipRow(
                                value = mechanic,
                                options = mechanicOptions,
                                onSelected = { viewModel.updateSelectedMechanic(it) }
                            )

                            // Primary muscle chips (no title)
                            ChipRow(
                                value = primary,
                                options = primaryOptions,
                                onSelected = { viewModel.updateSelectedPrimaryMuscle(it) }
                            )
                        }
                    }
                }
            }

            // Exercise List
            ExerciseList(
                exercises = uiState.exercises,
                isLoading = uiState.isLoading,
                onExerciseCardClick = onNavigateToExerciseDetail,
                onAddExercise = if (isSelectionMode && onExerciseSelected != null) onExerciseSelected else null,
                isSelectionMode = isSelectionMode,
                modifier = Modifier
                    .weight(1f)
            )
        }
    }
}

@Composable
private fun ExerciseCategoryChips(
    selectedCategory: String,
    categories: List<String>,
    onCategorySelected: (String) -> Unit
) {

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = categories,
            key = { it }
        ) { category ->
            FilterChip(
                onClick = { onCategorySelected(category) },
                label = { Text(category) },
                selected = selectedCategory == category,
                leadingIcon = if (selectedCategory == category) {
                    {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            modifier = Modifier.size(FilterChipDefaults.IconSize)
                        )
                    }
                } else null
            )
        }
    }
}

@Composable
private fun ExerciseList(
    exercises: List<Exercise>,
    isLoading: Boolean,
    onExerciseCardClick: (Long) -> Unit,
    onAddExercise: ((Long) -> Unit)? = null,
    isSelectionMode: Boolean = false,
    modifier: Modifier = Modifier
) {

    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (isLoading) {
            item {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        } else if (exercises.isEmpty()) {
            item {
                EmptyStateCard(
                    title = stringResource(R.string.no_exercises_found),
                    description = "No exercises available. Try adjusting your search or category filter.",
                    icon = Icons.Default.SearchOff
                )
            }
        } else {
            items(
                items = exercises,
                key = { it.id },
                contentType = { _ -> "exercise" }
            ) { exercise ->
                ExerciseListItem(
                    exercise = exercise,
                    isSelectionMode = isSelectionMode,
                    onCardClick = { onExerciseCardClick(exercise.id) },
                    onAddClick = onAddExercise?.let { add -> { add(exercise.id) } }
                )
            }
        }
    }
}

@Composable
private fun ExerciseListItem(
    exercise: Exercise,
    isSelectionMode: Boolean = false,
    onCardClick: () -> Unit,
    onAddClick: (() -> Unit)? = null
) {
    Card(
        onClick = onCardClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (!exercise.imageUrl.isNullOrEmpty()) {
                AsyncImage(
                    model = exercise.imageUrl,
                    contentDescription = exercise.name,
                    modifier = Modifier.size(48.dp)
                )
            } else {
                Card(
                    modifier = Modifier.size(48.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = getExerciseIcon(exercise.equipment),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = exercise.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                )
                Text(
                    text = listOfNotNull(
                        exercise.category.replaceFirstChar { it.uppercase() },
                        exercise.equipment.replaceFirstChar { it.uppercase() }
                    ).joinToString("  •  "),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                )
            }
            
            if (isSelectionMode && onAddClick != null) {
                ElevatedButton(
                    onClick = onAddClick,
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add exercise",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Add")
                }
            } else {
                val level = exercise.difficultyLevel.replaceFirstChar { it.uppercase() }
                AssistChip(
                    onClick = { },
                    label = { Text(level) },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = when (level) {
                            "Beginner" -> MaterialTheme.colorScheme.secondaryContainer
                            "Intermediate" -> MaterialTheme.colorScheme.tertiaryContainer
                            "Advanced" -> MaterialTheme.colorScheme.errorContainer
                            else -> MaterialTheme.colorScheme.surfaceVariant
                        }
                    )
                )
            }
        }
    }
}

@Composable
private fun ChipRow(
    value: String,
    options: List<String>,
    onSelected: (String) -> Unit
) {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(
            items = options,
            key = { it }
        ) { option ->
            FilterChip(
                onClick = { onSelected(option) },
                label = { Text(option) },
                selected = value == option,
                leadingIcon = if (value == option) {
                    {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            modifier = Modifier.size(FilterChipDefaults.IconSize)
                        )
                    }
                } else null
            )
        }
    }
}

private fun getExerciseIcon(equipment: String): androidx.compose.ui.graphics.vector.ImageVector {
    return when (equipment) {
                        "Barbell" -> Icons.Default.FitnessCenter
                "Dumbbell" -> Icons.Default.FitnessCenter
        "Machine" -> Icons.Default.Tune
        "Bodyweight" -> Icons.Default.Person
        "Cable" -> Icons.Default.Cable
        else -> Icons.Default.FitnessCenter
    }
}


