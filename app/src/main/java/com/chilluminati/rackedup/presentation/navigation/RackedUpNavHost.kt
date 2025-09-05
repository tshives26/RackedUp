package com.chilluminati.rackedup.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.chilluminati.rackedup.presentation.dashboard.DashboardScreen
import com.chilluminati.rackedup.presentation.workouts.WorkoutsScreen
import com.chilluminati.rackedup.presentation.workouts.WorkoutDetailScreen
import com.chilluminati.rackedup.presentation.workouts.ActiveWorkoutScreen
import com.chilluminati.rackedup.presentation.progress.ProgressScreen
import com.chilluminati.rackedup.presentation.programs.ProgramsScreen
import com.chilluminati.rackedup.presentation.profile.ProfileScreen
import com.chilluminati.rackedup.presentation.profile.SettingsScreen
import com.chilluminati.rackedup.presentation.profile.OnboardingScreen
import com.chilluminati.rackedup.presentation.profile.EditProfileScreen
import com.chilluminati.rackedup.presentation.exercises.ExerciseLibraryScreen
import com.chilluminati.rackedup.presentation.exercises.ExerciseDetailScreen
import com.chilluminati.rackedup.presentation.exercises.ExerciseCreateScreen
import com.chilluminati.rackedup.presentation.workouts.ExerciseEditScreen
import com.chilluminati.rackedup.presentation.profile.DataManagementScreen
import com.chilluminati.rackedup.presentation.programs.NewProgramBuilderScreen
import androidx.hilt.navigation.compose.hiltViewModel
import com.chilluminati.rackedup.presentation.programs.ProgramsViewModel

/**
 * Main navigation host for the RackedUp app
 */
@Composable
fun RackedUpNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = RackedUpDestination.Dashboard.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        // Main bottom navigation destinations
        composable(RackedUpDestination.Dashboard.route) {
            DashboardScreen(
                onNavigateToPrograms = {
                    navController.navigate(RackedUpDestination.Programs.route) {
                        // Pop up to the start destination and save state
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        // Avoid multiple copies of the same destination
                        launchSingleTop = true
                        // Restore state when re-selecting a previously selected item
                        restoreState = true
                    }
                },
                onNavigateToProgressHistory = {
                    navController.navigate(RackedUpDestination.Progress.routeWithTab("history")) {
                        // Keep other root destinations saved, but do not restore
                        // the previous Progress state so the History tab arg applies
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = false
                    }
                },
                onStartWorkout = { programId ->
                    if (programId != null) {
                        navController.navigate(RackedUpDestination.SelectProgramDay.createRoute(programId))
                    } else {
                        navController.navigate(RackedUpDestination.ActiveWorkoutQuick.route)
                    }
                }
            )
        }

        composable(RackedUpDestination.Workouts.route) {
            ExerciseLibraryScreen(
                onNavigateBack = { /* No-op: bottom tab root has no back */ },
                onNavigateToExerciseDetail = { exerciseId ->
                    navController.navigate(RackedUpDestination.ExerciseDetail.createRoute(exerciseId))
                },
                onNavigateToCreateExercise = {
                    navController.navigate(RackedUpDestination.ExerciseCreate.route)
                },
                showBackButton = false
            )
        }

        composable(
            route = RackedUpDestination.Progress.route,
            arguments = listOf(
                navArgument("tab") { type = NavType.StringType; defaultValue = "overview" }
            )
        ) { backStackEntry ->
            val tab = backStackEntry.arguments?.getString("tab")
            ProgressScreen(
                onNavigateToWorkoutDetail = { workoutId ->
                    navController.navigate(RackedUpDestination.WorkoutDetail.createRoute(workoutId))
                },
                onNavigateToWorkoutEdit = { workoutId ->
                    navController.navigate(RackedUpDestination.WorkoutDetail.createRoute(workoutId, edit = true))
                },
                onNavigateToPRs = {
                    // Instead of navigating, we'll handle tab switching internally in ProgressScreen
                    // This prevents the screen from being recreated and causing UI flashing
                },
                initialTab = tab
            )
        }

        composable(RackedUpDestination.Programs.route) { 
            ProgramsScreen(
                onNavigateToActiveWorkout = { workoutId ->
                    if (workoutId != null) {
                        navController.navigate(RackedUpDestination.ActiveWorkout.createRoute(workoutId))
                    } else {
                        navController.navigate(RackedUpDestination.ActiveWorkoutQuick.route)
                    }
                },
                onNavigateToSelectProgramDay = { programId ->
                    navController.navigate(RackedUpDestination.SelectProgramDay.createRoute(programId))
                },
                onNavigateToProgramBuilder = {
                    navController.navigate(RackedUpDestination.ProgramBuilder.route)
                }
            )
        }

        composable(RackedUpDestination.ProgramBuilder.route) {
            NewProgramBuilderScreen(
                onNavigateBack = { 
                    // Navigate specifically to Programs page to ensure proper destination
                    navController.navigate(RackedUpDestination.Programs.route) {
                        popUpTo(RackedUpDestination.Programs.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(RackedUpDestination.Profile.route) {
            ProfileScreen(
                onNavigateToSettings = {
                    navController.navigate(RackedUpDestination.Settings.route)
                },
                onNavigateToDataManagement = {
                    navController.navigate(RackedUpDestination.DataManagement.route)
                },
                onNavigateToEditProfile = {
                    navController.navigate(RackedUpDestination.EditProfile.route)
                }
            )
        }

        // Secondary destinations
        composable(
            route = RackedUpDestination.WorkoutDetail.route,
            arguments = listOf(
                navArgument("workoutId") { type = NavType.LongType },
                navArgument("edit") { 
                    type = NavType.BoolType
                    defaultValue = false
                }
            )
        ) { backStackEntry ->
            val workoutId = backStackEntry.arguments?.getLong("workoutId") ?: 0L
            val isEditMode = backStackEntry.arguments?.getBoolean("edit") ?: false
            WorkoutDetailScreen(
                workoutId = workoutId,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToExerciseDetail = { exerciseId ->
                    navController.navigate(RackedUpDestination.ExerciseDetail.createRoute(exerciseId))
                },
                onNavigateToExerciseEdit = { workoutExerciseId ->
                    navController.navigate(RackedUpDestination.ExerciseEdit.createRoute(workoutExerciseId))
                },
                isEditMode = isEditMode
            )
        }

        composable(
            route = RackedUpDestination.ExerciseDetail.route,
            arguments = listOf(
                navArgument("exerciseId") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val exerciseId = backStackEntry.arguments?.getLong("exerciseId") ?: 0L
            ExerciseDetailScreen(
                exerciseId = exerciseId,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = RackedUpDestination.ExerciseEdit.route,
            arguments = listOf(
                navArgument("workoutExerciseId") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val workoutExerciseId = backStackEntry.arguments?.getLong("workoutExerciseId") ?: 0L
            ExerciseEditScreen(
                workoutExerciseId = workoutExerciseId,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = RackedUpDestination.ActiveWorkout.route,
            arguments = listOf(
                navArgument("workoutId") { 
                    type = NavType.LongType
                }
            )
        ) { backStackEntry ->
            val workoutId = backStackEntry.arguments?.getLong("workoutId") ?: 0L
            
            ActiveWorkoutScreen(
                workoutId = workoutId,
                onNavigateBack = { navController.popBackStack() },
                onWorkoutComplete = {
                    navController.popBackStack()
                    navController.navigate(RackedUpDestination.Progress.route) {
                        // Pop up to the start destination and save state
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        // Avoid multiple copies of the same destination
                        launchSingleTop = true
                        // Restore state when re-selecting a previously selected item
                        restoreState = true
                    }
                },
                onNavigateToExerciseLibrary = {
                    navController.navigate(RackedUpDestination.ExerciseLibrary.route)
                }
            )
        }

        composable(RackedUpDestination.ActiveWorkoutQuick.route) {
            ActiveWorkoutScreen(
                workoutId = null,
                onNavigateBack = { navController.popBackStack() },
                onWorkoutComplete = {
                    navController.popBackStack()
                    navController.navigate(RackedUpDestination.Progress.route) {
                        // Pop up to the start destination and save state
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        // Avoid multiple copies of the same destination
                        launchSingleTop = true
                        // Restore state when re-selecting a previously selected item
                        restoreState = true
                    }
                },
                onNavigateToExerciseLibrary = {
                    navController.navigate(RackedUpDestination.ExerciseLibrary.route)
                }
            )
        }

        composable(
            route = RackedUpDestination.SelectProgramDay.route,
            arguments = listOf(
                navArgument("programId") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val programId = backStackEntry.arguments?.getLong("programId") ?: 0L
            com.chilluminati.rackedup.presentation.programs.SelectProgramDayScreen(
                programId = programId,
                onNavigateBack = { navController.popBackStack() },
                onDaySelectedStart = { workoutId ->
                    navController.popBackStack()
                    navController.navigate(RackedUpDestination.ActiveWorkout.createRoute(workoutId))
                }
            )
        }

        composable(RackedUpDestination.Settings.route) {
            SettingsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(RackedUpDestination.ExerciseLibrary.route) {
            val isSelectionMode = navController.previousBackStackEntry?.destination?.route == RackedUpDestination.ActiveWorkout.route ||
                                navController.previousBackStackEntry?.destination?.route == RackedUpDestination.ActiveWorkoutQuick.route
            
            ExerciseLibraryScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToExerciseDetail = { exerciseId ->
                    navController.navigate(RackedUpDestination.ExerciseDetail.createRoute(exerciseId))
                },
                onNavigateToCreateExercise = {
                    navController.navigate(RackedUpDestination.ExerciseCreate.route)
                },
                isSelectionMode = isSelectionMode,
                onExerciseSelected = if (isSelectionMode) { exerciseId ->
                    // Add the exercise to the active workout
                    // TODO: Implement exercise addition
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("selected_exercise_id", exerciseId)
                    navController.popBackStack()
                } else null
            )
        }

        composable(RackedUpDestination.ExerciseCreate.route) {
            ExerciseCreateScreen(
                onNavigateBack = { navController.popBackStack() },
                onCreated = { navController.popBackStack() }
            )
        }

        composable(RackedUpDestination.DataManagement.route) {
            DataManagementScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(RackedUpDestination.Onboarding.route) {
            OnboardingScreen(
                onOnboardingComplete = {
                    navController.popBackStack()
                }
            )
        }

        composable(RackedUpDestination.EditProfile.route) {
            EditProfileScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
