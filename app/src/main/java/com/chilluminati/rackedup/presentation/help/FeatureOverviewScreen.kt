package com.chilluminati.rackedup.presentation.help

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.chilluminati.rackedup.presentation.components.GradientBackground
import com.chilluminati.rackedup.presentation.components.GlassmorphismCard

/**
 * Feature Overview screen showing all app features with Learn More buttons
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeatureOverviewScreen(
    onNavigateBack: () -> Unit,
    onLearnMore: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    GradientBackground(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Top App Bar
            TopAppBar(
                title = { 
                    Text(
                        text = "Feature Overview",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
                )
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Introduction
                item {
                    IntroductionSection()
                }

                // Core Features
                item {
                    Text(
                        text = "Core Features",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }

                items(getCoreFeatures()) { feature ->
                    FeatureCard(
                        feature = feature,
                        onLearnMore = onLearnMore
                    )
                }

                // Advanced Features
                item {
                    Text(
                        text = "Advanced Features",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }

                items(getAdvancedFeatures()) { feature ->
                    FeatureCard(
                        feature = feature,
                        onLearnMore = onLearnMore
                    )
                }

                // Tools & Utilities
                item {
                    Text(
                        text = "Tools & Utilities",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }

                items(getToolsFeatures()) { feature ->
                    FeatureCard(
                        feature = feature,
                        onLearnMore = onLearnMore
                    )
                }
            }
        }
    }
}

@Composable
private fun IntroductionSection() {
    GlassmorphismCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Discover RackedUp's Features",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Explore the powerful features that make RackedUp the ultimate fitness tracking companion.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun FeatureCard(
    feature: AppFeature,
    onLearnMore: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = feature.icon,
                    contentDescription = null,
                    modifier = Modifier.size(32.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                
                Spacer(modifier = Modifier.width(16.dp))
                
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = feature.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium
                    )
                    
                    Text(
                        text = feature.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            if (feature.features.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    feature.features.forEach { subFeature ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                            
                            Spacer(modifier = Modifier.width(8.dp))
                            
                            Text(
                                text = subFeature,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            OutlinedButton(
                onClick = { onLearnMore(feature.learnMoreKey) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Learn More")
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

private data class AppFeature(
    val title: String,
    val description: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val features: List<String>,
    val learnMoreKey: String
)

private fun getCoreFeatures(): List<AppFeature> {
    return listOf(
        AppFeature(
            title = "Workout Tracking",
            description = "Log your workouts with precision and track every set, rep, and weight.",
            icon = Icons.Default.FitnessCenter,
            features = listOf(
                "Real-time workout logging",
                "Rest timer with notifications",
                "Exercise library with search",
                "Workout history and editing"
            ),
            learnMoreKey = "workout_tracking"
        ),
        AppFeature(
            title = "Progress Analytics",
            description = "Visualize your progress with comprehensive charts and analytics.",
            icon = Icons.Default.TrendingUp,
            features = listOf(
                "Volume and strength progression charts",
                "Personal records tracking",
                "Body measurements trends",
                "Workout consistency metrics"
            ),
            learnMoreKey = "progress_analytics"
        ),
        AppFeature(
            title = "Program Builder",
            description = "Create custom workout programs with built-in templates and progression models.",
            icon = Icons.Default.Assignment,
            features = listOf(
                "Visual program builder",
                "Built-in program templates",
                "Progression models (linear, percentage-based)",
                "Program scheduling and tracking"
            ),
            learnMoreKey = "program_builder"
        ),
        AppFeature(
            title = "Multi-Profile Support",
            description = "Manage multiple user profiles on one device for families or different training phases.",
            icon = Icons.Default.People,
            features = listOf(
                "Create unlimited profiles",
                "Data isolation between profiles",
                "Easy profile switching",
                "Individual progress tracking"
            ),
            learnMoreKey = "multi_profile"
        )
    )
}

private fun getAdvancedFeatures(): List<AppFeature> {
    return listOf(
        AppFeature(
            title = "Data Management",
            description = "Full control over your data with backup, restore, and export options.",
            icon = Icons.Default.Storage,
            features = listOf(
                "Local and cloud backup",
                "CSV export for workouts",
                "OpenScale import compatibility",
                "Data privacy controls"
            ),
            learnMoreKey = "data_management"
        ),
        AppFeature(
            title = "Customization",
            description = "Personalize your experience with themes, units, and preferences.",
            icon = Icons.Default.Palette,
            features = listOf(
                "11 color themes",
                "Dark and light modes",
                "Unit preferences (lbs/kg, miles/km)",
                "Notification settings"
            ),
            learnMoreKey = "customization"
        ),
        AppFeature(
            title = "Offline-First Design",
            description = "Works completely offline with no internet required for core functionality.",
            icon = Icons.Default.OfflineBolt,
            features = listOf(
                "No internet required",
                "Local data storage",
                "Privacy-focused design",
                "Fast performance"
            ),
            learnMoreKey = "offline_design"
        )
    )
}

private fun getToolsFeatures(): List<AppFeature> {
    return listOf(
        AppFeature(
            title = "Plate Calculator",
            description = "Calculate the exact weight plates needed for any target weight.",
            icon = Icons.Default.Calculate,
            features = listOf(
                "Multiple bar types (Standard, Women's, Technique)",
                "Optimal plate combinations",
                "Visual barbell layout",
                "Weight calculation from plates"
            ),
            learnMoreKey = "plate_calculator"
        ),
        AppFeature(
            title = "Weight Converter",
            description = "Quick conversion between pounds and kilograms.",
            icon = Icons.Default.SwapHoriz,
            features = listOf(
                "Bidirectional conversion",
                "Real-time updates",
                "Precise calculations",
                "Simple interface"
            ),
            learnMoreKey = "weight_converter"
        ),
        AppFeature(
            title = "Exercise Library",
            description = "Comprehensive exercise database with search and filtering.",
            icon = Icons.Default.List,
            features = listOf(
                "Search and filter exercises",
                "Exercise categories",
                "Custom exercise creation",
                "Exercise variations"
            ),
            learnMoreKey = "exercise_library"
        )
    )
}
