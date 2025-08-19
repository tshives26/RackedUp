package com.chilluminati.rackedup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
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
import dagger.hilt.android.AndroidEntryPoint



@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        // Create a ViewModel instance
        val viewModel: ProfileViewModel by viewModels()
        
        // Keep the splash screen visible until we've checked onboarding status
        splashScreen.setKeepOnScreenCondition {
            viewModel.onboardingState.value.isLoading
        }
        
        setContent {
            RackedUpAppWithTheme()
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
    
    // Show main app flow by default, onboarding only if explicitly needed
    if (onboardingState.isLoading) {
        // Keep splash screen visible during loading
        return
    }
    
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