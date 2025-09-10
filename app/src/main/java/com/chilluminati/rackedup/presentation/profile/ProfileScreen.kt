package com.chilluminati.rackedup.presentation.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.chilluminati.rackedup.R
import com.chilluminati.rackedup.presentation.components.AccentSectionHeader
import com.chilluminati.rackedup.presentation.components.PlateCalculator
import com.chilluminati.rackedup.presentation.components.WeightConverter
import com.chilluminati.rackedup.presentation.components.GradientBackground
import com.chilluminati.rackedup.presentation.components.GlassmorphismCard
import androidx.hilt.navigation.compose.hiltViewModel
import com.chilluminati.rackedup.data.database.entity.UserProfile
import coil.compose.AsyncImage
import androidx.core.net.toUri
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Profile screen showing user info, settings, and app features
 */
@Composable
fun ProfileScreen(
    onNavigateToSettings: () -> Unit,
    onNavigateToDataManagement: () -> Unit,
    onNavigateToEditProfile: () -> Unit = {},
    onNavigateToHelpDocumentation: () -> Unit = {},
    viewModel: ProfileViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
    context: android.content.Context
) {
    val uiState by viewModel.uiState.collectAsState()
    GradientBackground(
        modifier = modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
        // Profile Header
        item {
            ProfileHeaderCard(
                profile = uiState.profile,
                onEditProfile = onNavigateToEditProfile
            )
        }

        // Quick Stats removed per spec

        // Achievements Section
        item {
            AchievementsPreviewCard()
        }

        // Tools Section
        item {
            ToolsSection()
        }

        // Settings Section
        item {
            SettingsSection(
                onNavigateToSettings = onNavigateToSettings,
                onNavigateToDataManagement = onNavigateToDataManagement
            )
        }

        // Help & Support Section
        item {
            HelpSupportSection(
                context = context,
                onNavigateToHelpDocumentation = onNavigateToHelpDocumentation
            )
        }
    }
    }
}

@Composable
private fun ToolsSection() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        // Match the visual treatment used for the Settings section heading
        Text(
            text = "Tools",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        PlateCalculator()
        WeightConverter()
    }
}

@Composable
private fun ProfileHeaderCard(
    profile: UserProfile?,
    onEditProfile: () -> Unit
) {
    GlassmorphismCard(
        modifier = Modifier.fillMaxWidth(),
        backgroundAlpha = 0.15f
    ) {
                    Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
            // Profile Avatar
            Card(
                modifier = Modifier.size(64.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    val uri = profile?.profileImageUrl?.toUri()
                    if (uri != null) {
                        AsyncImage(
                            model = uri,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            modifier = Modifier.size(32.dp),
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = profile?.name ?: "Fitness Enthusiast",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = profile?.createdAt?.let { 
                        val formatter = SimpleDateFormat("MMM yyyy", Locale.getDefault())
                        "Member since ${formatter.format(it)}"
                    } ?: "Member since Dec 2024",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            IconButton(onClick = onEditProfile) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit profile",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}

@Composable
private fun ProfileStatsCard() {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Your Stats",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))
            
            // Show empty state for new users
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.TrendingUp,
                    contentDescription = null,
                    modifier = Modifier.size(48.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "No workout data yet",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                )
                Text(
                    text = "Complete your first workout to see your statistics",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                )
            }
        }
    }
}

@Composable
private fun ProfileStatItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun AchievementsPreviewCard(
    achievementsViewModel: AchievementsViewModel = hiltViewModel()
) {
    var showDialog by remember { mutableStateOf(false) }
    val states by achievementsViewModel.achievements.collectAsState()
    val unlocked = states.count { it.isUnlocked }
    val total = states.size

    Card(
        onClick = { showDialog = true },
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(R.string.achievements),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                if (total > 0) {
                    Spacer(modifier = Modifier.height(2.dp))
                    AssistChipRow(unlocked = unlocked, total = total)
                }
            }
            Spacer(modifier = Modifier.height(12.dp))

            if (total > 0) {
                val progress = if (total == 0) 0f else unlocked.toFloat() / total.toFloat()
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.fillMaxWidth(),
                    trackColor = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.2f),
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            if (unlocked == 0) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.6f)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "No achievements yet",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f)
                    )
                    Text(
                        text = "Complete workouts to unlock achievements",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.6f)
                    )
                }
            } else {
                // Add horizontally scrollable achievement pills (same as Dashboard)
                val unlockedStates = states.filter { it.isUnlocked }.sortedByDescending { it.unlockedAt }
                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(unlockedStates) { st ->
                        AchievementPill(title = st.definition.title, date = st.unlockedAt)
                    }
                }
            }
        }
    }

    if (showDialog) {
        AchievementsDialog(
            states = states,
            unlocked = unlocked,
            total = total,
            onDismiss = { showDialog = false }
        )
    }
}

@Composable
private fun AchievementPill(title: String, date: java.util.Date?) {
    ElevatedCard(
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Star, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.width(8.dp))
            Column {
                Text(title, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold)
                Text(
                    date?.let { java.text.SimpleDateFormat("MMM d, yyyy", java.util.Locale.getDefault()).format(it) } ?: "",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
fun AchievementsDialog(
    states: List<com.chilluminati.rackedup.data.repository.AchievementsRepository.State>,
    unlocked: Int,
    total: Int,
    onDismiss: () -> Unit
) {
    val sorted = remember(states) { 
        states.sortedWith(
            compareByDescending<com.chilluminati.rackedup.data.repository.AchievementsRepository.State> { it.isUnlocked }
                .thenBy { it.definition.category.name }
                .thenBy { it.definition.title }
        ) 
    }
    var showOnly by remember { mutableStateOf(0) } // 0 = All, 1 = Unlocked, 2 = Locked
    var categoryFilter by remember { mutableStateOf<com.chilluminati.rackedup.data.repository.AchievementsRepository.Category?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Close") }
        },
        title = { Text(text = "${stringResource(R.string.achievements)} ($unlocked/$total)") },
        text = {
            Box(modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 420.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    // Filters row
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.horizontalScroll(rememberScrollState())) {
                        FilterChip(selected = showOnly == 0, onClick = { showOnly = 0 }, label = { Text("All") })
                        FilterChip(selected = showOnly == 1, onClick = { showOnly = 1 }, label = { Text("Unlocked") })
                        FilterChip(selected = showOnly == 2, onClick = { showOnly = 2 }, label = { Text("Locked") })
                        Spacer(modifier = Modifier.width(12.dp))
                        com.chilluminati.rackedup.data.repository.AchievementsRepository.Category.values().forEach { cat ->
                            FilterChip(
                                selected = categoryFilter == cat,
                                onClick = { categoryFilter = if (categoryFilter == cat) null else cat },
                                label = { Text(cat.name) }
                            )
                        }
                    }

                    val filtered = sorted.filter { st ->
                        (showOnly == 0 || (showOnly == 1 && st.isUnlocked) || (showOnly == 2 && !st.isUnlocked)) &&
                        (categoryFilter == null || st.definition.category == categoryFilter)
                    }

                    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        // Create a flat list with category headers and achievement items
                        val items = mutableListOf<Any>()
                        val groups = filtered.groupBy { it.definition.category }
                        groups.forEach { (category, list) ->
                            items.add(category) // Add category as header
                            items.addAll(list) // Add all achievements in this category
                        }
                        
                        items(items, key = { item ->
                            when (item) {
                                is com.chilluminati.rackedup.data.repository.AchievementsRepository.Category -> "category_${item.name}"
                                is com.chilluminati.rackedup.data.repository.AchievementsRepository.State -> "achievement_${item.definition.id}"
                                else -> item.hashCode().toString()
                            }
                        }) { item ->
                            when (item) {
                                is com.chilluminati.rackedup.data.repository.AchievementsRepository.Category -> {
                                    // Category header
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(categoryIcon(item), contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(text = item.name, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                                    }
                                }
                                is com.chilluminati.rackedup.data.repository.AchievementsRepository.State -> {
                                    // Achievement item
                                    AchievementListItem(
                                        title = item.definition.title,
                                        description = item.definition.description,
                                        isUnlocked = item.isUnlocked,
                                        unlockedAt = item.unlockedAt
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}

@Composable
private fun AchievementListItem(
    title: String,
    description: String,
    isUnlocked: Boolean,
    unlockedAt: java.util.Date?
) {
    val alpha = if (isUnlocked) 1f else 0.55f
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(alpha),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Card(
            modifier = Modifier.size(40.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isUnlocked)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = if (isUnlocked)
                        MaterialTheme.colorScheme.onPrimary
                    else
                        MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            if (isUnlocked && unlockedAt != null) {
                Text(
                    text = java.text.SimpleDateFormat("MMM d, yyyy", java.util.Locale.getDefault()).format(unlockedAt),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
            }
        }
        if (isUnlocked) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun AssistChipRow(unlocked: Int, total: Int) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        AssistChip(
            onClick = {},
            label = { Text("$unlocked unlocked") },
            leadingIcon = {
                Icon(Icons.Default.CheckCircle, contentDescription = null)
            }
        )
        AssistChip(
            onClick = {},
            label = { Text("$total total") },
            leadingIcon = {
                Icon(Icons.Default.Star, contentDescription = null)
            }
        )
    }
}

private fun categoryIcon(category: com.chilluminati.rackedup.data.repository.AchievementsRepository.Category) = when (category) {
    com.chilluminati.rackedup.data.repository.AchievementsRepository.Category.Milestones -> Icons.Default.EmojiEvents
    com.chilluminati.rackedup.data.repository.AchievementsRepository.Category.Consistency -> Icons.Default.HourglassBottom
    com.chilluminati.rackedup.data.repository.AchievementsRepository.Category.Volume -> Icons.Default.FitnessCenter
    com.chilluminati.rackedup.data.repository.AchievementsRepository.Category.Sets -> Icons.Default.GridOn
    com.chilluminati.rackedup.data.repository.AchievementsRepository.Category.Duration -> Icons.Default.Schedule
    com.chilluminati.rackedup.data.repository.AchievementsRepository.Category.Strength -> Icons.Default.Whatshot
    com.chilluminati.rackedup.data.repository.AchievementsRepository.Category.TimeOfDay -> Icons.Default.NightsStay
    com.chilluminati.rackedup.data.repository.AchievementsRepository.Category.Programs -> Icons.AutoMirrored.Filled.List
    com.chilluminati.rackedup.data.repository.AchievementsRepository.Category.AdvancedTracking -> Icons.Default.Analytics
}

@Composable
private fun AchievementBadge(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isUnlocked: Boolean
) {
    Card(
        modifier = Modifier.size(48.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isUnlocked) 
                MaterialTheme.colorScheme.primary 
            else 
                MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = if (isUnlocked) 
                    MaterialTheme.colorScheme.onPrimary 
                else 
                    MaterialTheme.colorScheme.outline
            )
        }
    }
}

@Composable
private fun SettingsMenuItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    description: String,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun AppInfoCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "RackedUp",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Version 1.0.0",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "The ultimate gym companion for serious athletes",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun SettingsSection(
    onNavigateToSettings: () -> Unit,
    onNavigateToDataManagement: () -> Unit
) {
    Column {
        Text(
            text = "Settings",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        
        Card(
            onClick = onNavigateToSettings,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "App Settings",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Go to settings",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Card(
            onClick = onNavigateToDataManagement,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Storage,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Data Management",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Go to data management",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun HelpSupportSection(
    context: android.content.Context,
    onNavigateToHelpDocumentation: () -> Unit = {}
) {
    Column {
        Text(
            text = "Help & Support",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        
        Card(
            onClick = onNavigateToHelpDocumentation,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Help,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Help & Documentation",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Open help",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Card(
            onClick = { 
                val intent = android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse("https://forms.gle/bHfMDc7qsnUn6F4j6"))
                context.startActivity(intent)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Feedback,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Send Feedback",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Send feedback",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
