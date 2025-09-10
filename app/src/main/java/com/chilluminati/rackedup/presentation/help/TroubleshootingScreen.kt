package com.chilluminati.rackedup.presentation.help

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.chilluminati.rackedup.presentation.components.GradientBackground
import com.chilluminati.rackedup.presentation.components.GlassmorphismCard

/**
 * Troubleshooting screen with common issues and solutions
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TroubleshootingScreen(
    onNavigateBack: () -> Unit,
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
                        text = "Troubleshooting",
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
                    TroubleshootingIntroduction()
                }

                // Common Issues
                item {
                    Text(
                        text = "Common Issues",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }

                items(getCommonIssues()) { issue ->
                    IssueCard(issue = issue)
                }

                // Still Need Help
                item {
                    StillNeedHelpSection()
                }
            }
        }
    }
}

@Composable
private fun TroubleshootingIntroduction() {
    GlassmorphismCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Build,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Having Issues?",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Find solutions to common problems and get back to tracking your fitness goals.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun IssueCard(
    issue: TroubleshootingIssue
) {
    var isExpanded by remember { mutableStateOf(false) }
    
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
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = issue.icon,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                
                Spacer(modifier = Modifier.width(12.dp))
                
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = issue.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium
                    )
                    
                    Text(
                        text = issue.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                IconButton(
                    onClick = { isExpanded = !isExpanded }
                ) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = if (isExpanded) "Collapse" else "Expand"
                    )
                }
            }
            
            if (isExpanded) {
                Spacer(modifier = Modifier.height(12.dp))
                
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Solutions:",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Medium
                    )
                    
                    issue.solutions.forEach { solution ->
                        Row(
                            verticalAlignment = Alignment.Top
                        ) {
                            Text(
                                text = "• ",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                            
                            Text(
                                text = solution,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StillNeedHelpSection() {
    GlassmorphismCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.ContactSupport,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Still Need Help?",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "If you couldn't find a solution to your problem, our support team is here to help.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            val context = androidx.compose.ui.platform.LocalContext.current
            
            Button(
                onClick = { 
                    val intent = android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse("https://forms.gle/bHfMDc7qsnUn6F4j6"))
                    context.startActivity(intent)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Contact Support")
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

private data class TroubleshootingIssue(
    val title: String,
    val description: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val solutions: List<String>
)

private fun getCommonIssues(): List<TroubleshootingIssue> {
    return listOf(
        TroubleshootingIssue(
            title = "App is slow or crashes",
            description = "The app is running slowly or crashing unexpectedly",
            icon = Icons.Default.Warning,
            solutions = listOf(
                "Restart the app completely",
                "Clear app cache in Android settings",
                "Restart your device",
                "Check available storage space (need at least 100MB)",
                "Update to the latest app version",
                "Disable battery optimization for RackedUp"
            )
        ),
        TroubleshootingIssue(
            title = "Workouts not saving",
            description = "Completed workouts are not being saved to your history",
            icon = Icons.Default.Save,
            solutions = listOf(
                "Check device storage space",
                "Ensure app has storage permissions",
                "Try creating a new workout",
                "Restart the app",
                "Check for app updates",
                "Verify you're on the correct profile"
            )
        ),
        TroubleshootingIssue(
            title = "Rest timer not working",
            description = "The rest timer doesn't start or notify you when complete",
            icon = Icons.Default.Timer,
            solutions = listOf(
                "Check notification permissions for RackedUp",
                "Ensure app isn't in battery optimization mode",
                "Check sound and notification settings",
                "Try manual timer start instead of auto-start",
                "Restart the app",
                "Check do not disturb settings"
            )
        ),
        TroubleshootingIssue(
            title = "Missing workouts or data",
            description = "Previous workouts or data have disappeared",
            icon = Icons.Default.VisibilityOff,
            solutions = listOf(
                "Check if you're on the correct profile",
                "Look in workout history with different filters",
                "Check if data was accidentally deleted",
                "Restore from backup if available",
                "Check if you recently switched profiles",
                "Contact support if data loss persists"
            )
        ),
        TroubleshootingIssue(
            title = "Import/Export not working",
            description = "Unable to import or export workout data",
            icon = Icons.Default.FileDownload,
            solutions = listOf(
                "Check file permissions on your device",
                "Ensure file format is correct (CSV for import)",
                "Try saving to a different location",
                "Check available storage space",
                "Verify file isn't corrupted",
                "Try exporting to internal storage first"
            )
        ),
        TroubleshootingIssue(
            title = "App won't start",
            description = "RackedUp crashes immediately when opening",
            icon = Icons.Default.Error,
            solutions = listOf(
                "Restart your device",
                "Clear app data and cache",
                "Uninstall and reinstall the app",
                "Check if device meets minimum requirements (Android 8.0+)",
                "Free up storage space",
                "Check for system updates"
            )
        ),
        TroubleshootingIssue(
            title = "Charts not showing data",
            description = "Progress charts are empty or not displaying correctly",
            icon = Icons.Default.BarChart,
            solutions = listOf(
                "Ensure you have completed workouts with data",
                "Check date range filters",
                "Try refreshing the progress screen",
                "Restart the app",
                "Check if you're on the correct profile",
                "Verify workout data was saved correctly"
            )
        )
    )
}
