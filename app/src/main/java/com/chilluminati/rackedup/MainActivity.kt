package com.chilluminati.rackedup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.chilluminati.rackedup.presentation.components.RackedUpBottomNavigation
import com.chilluminati.rackedup.presentation.navigation.RackedUpDestination
import com.chilluminati.rackedup.presentation.navigation.RackedUpNavHost
import com.chilluminati.rackedup.presentation.profile.OnboardingScreen
import com.chilluminati.rackedup.presentation.profile.ProfileViewModel
import com.chilluminati.rackedup.presentation.profile.SettingsViewModel
import com.chilluminati.rackedup.presentation.workouts.WorkoutsViewModel
import com.chilluminati.rackedup.presentation.theme.RackedUpTheme
import com.chilluminati.rackedup.data.repository.SettingsRepository
import dagger.hilt.android.AndroidEntryPoint

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.LaunchedEffect

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        setContent {
            // Keep splash until onboarding state is resolved AND minimum time has passed
            val profileViewModel: ProfileViewModel = hiltViewModel()
            val settingsViewModel: SettingsViewModel = hiltViewModel()
            val onboardingState by profileViewModel.onboardingState.collectAsStateWithLifecycle()
            val settingsState by settingsViewModel.uiState.collectAsStateWithLifecycle()
            
            // Simple splash screen logic
            var showSplash by remember { mutableStateOf(true) }
            
            LaunchedEffect(Unit) {
                // Show splash for at least 1 second
                kotlinx.coroutines.delay(1000L)
                
                // Then check if onboarding is complete
                if (!onboardingState.isLoading) {
                    showSplash = false
                }
            }
            
            // Also check when onboarding state changes
            LaunchedEffect(onboardingState.isLoading) {
                if (!onboardingState.isLoading) {
                    // Add a small delay to ensure minimum splash time
                    kotlinx.coroutines.delay(100L)
                    showSplash = false
                }
            }
            
            if (showSplash) {
                // Show splash screen with theme-appropriate background
                val backgroundColor = when (settingsState.themeMode) {
                    SettingsRepository.ThemeMode.DARK -> Color(0xFF1A1A1A) // Dark gray
                    else -> Color.White
                }
                
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(backgroundColor),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.rackedup),
                        contentDescription = "RackedUp Logo",
                        modifier = Modifier.size(200.dp),
                        contentScale = ContentScale.Fit
                    )
                }
            } else {
                RackedUpAppWithTheme()
            }
        }
    }
}

@Composable
fun RackedUpAppWithTheme(
    settingsViewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by settingsViewModel.uiState.collectAsStateWithLifecycle()
    
    RackedUpTheme(
        themeMode = uiState.themeMode,
        dynamicColor = uiState.dynamicColor,
        colorTheme = uiState.colorTheme
    ) {
        RackedUpApp()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RackedUpApp(
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val onboardingState by viewModel.onboardingState.collectAsState()
    
    // Show onboarding if not completed
    if (!onboardingState.hasCompletedOnboarding) {
        OnboardingScreen(
            onOnboardingComplete = {
                // Onboarding completed, the app will automatically navigate to main flow
            }
        )
    } else {
        // Show main app flow
        val navController = rememberNavController()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        
        // Observe active workout session to surface a global resume banner
        val workoutsViewModel: WorkoutsViewModel = hiltViewModel()
        val activeSession by workoutsViewModel.activeSessionFlow.collectAsStateWithLifecycle(initialValue = null)
        val isOnActiveWorkout = currentDestination?.route == RackedUpDestination.ActiveWorkout.route ||
                currentDestination?.route == RackedUpDestination.ActiveWorkoutQuick.route
        val workoutId = activeSession?.workoutId
        val shouldShowResumeBanner = workoutId != null && workoutId != 0L && !isOnActiveWorkout

        // Determine if we should show the bottom navigation
        val showBottomNav = RackedUpDestination.bottomNavDestinations.any { 
            it.route == currentDestination?.route 
        }

        Box(modifier = Modifier.fillMaxSize()) {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                bottomBar = {
                    if (showBottomNav) {
                        RackedUpBottomNavigation(navController = navController)
                    }
                },
                containerColor = MaterialTheme.colorScheme.background
            ) { innerPadding ->
                RackedUpNavHost(
                    navController = navController,
                    modifier = Modifier.padding(innerPadding)
                )
            }

            // Floating resume button - only show if there's an active workout
            if (shouldShowResumeBanner && workoutId != null) {
                ExtendedFloatingActionButton(
                    onClick = {
                        navController.navigate(RackedUpDestination.ActiveWorkout.createRoute(workoutId))
                    },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(
                            bottom = if (showBottomNav) 128.dp else 16.dp,
                            end = 16.dp
                        )
                        .navigationBarsPadding(),
                    icon = { Icon(Icons.Default.PlayArrow, contentDescription = null) },
                    text = { Text("Resume") },
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    elevation = FloatingActionButtonDefaults.elevation(
                        defaultElevation = 8.dp,
                        pressedElevation = 12.dp
                    )
                )
            }
        }
    }
}