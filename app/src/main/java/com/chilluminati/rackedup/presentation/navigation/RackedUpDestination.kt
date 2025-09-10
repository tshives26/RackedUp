package com.chilluminati.rackedup.presentation.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.automirrored.outlined.Assignment
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.ui.graphics.vector.ImageVector
import com.chilluminati.rackedup.R

/**
 * Sealed class representing all destinations in the app
 */
sealed class RackedUpDestination(
    val route: String,
    @StringRes val titleRes: Int,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    object Dashboard : RackedUpDestination(
        route = "dashboard",
        titleRes = R.string.dashboard,
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home
    )

    object Workouts : RackedUpDestination(
        route = "workouts",
        titleRes = R.string.exercises,
        selectedIcon = Icons.AutoMirrored.Filled.MenuBook,
        unselectedIcon = Icons.AutoMirrored.Outlined.MenuBook
    )

    object Progress : RackedUpDestination(
        route = "progress?tab={tab}",
        titleRes = R.string.progress,
        selectedIcon = Icons.Filled.Analytics,
        unselectedIcon = Icons.Outlined.Analytics
    ) {
        fun routeWithTab(tab: String? = null): String =
            if (tab.isNullOrBlank()) "progress" else "progress?tab=$tab"
    }

    object Programs : RackedUpDestination(
        route = "programs",
        titleRes = R.string.programs,
        selectedIcon = Icons.AutoMirrored.Filled.Assignment,
        unselectedIcon = Icons.AutoMirrored.Outlined.Assignment
    )

    object Profile : RackedUpDestination(
        route = "profile",
        titleRes = R.string.profile,
        selectedIcon = Icons.Filled.Person,
        unselectedIcon = Icons.Outlined.Person
    )

    // Secondary destinations (not in bottom nav)
    object WorkoutDetail : RackedUpDestination(
        route = "workout_detail/{workoutId}?edit={edit}",
        titleRes = R.string.workout_detail,
        selectedIcon = Icons.Filled.FitnessCenter,
        unselectedIcon = Icons.Outlined.FitnessCenter
    ) {
        fun createRoute(workoutId: Long, edit: Boolean = false) = "workout_detail/$workoutId?edit=$edit"
    }

    object ExerciseDetail : RackedUpDestination(
        route = "exercise_detail/{exerciseId}",
        titleRes = R.string.exercise_detail,
        selectedIcon = Icons.Filled.Info,
        unselectedIcon = Icons.Outlined.Info
    ) {
        fun createRoute(exerciseId: Long) = "exercise_detail/$exerciseId"
    }

    object ExerciseEdit : RackedUpDestination(
        route = "exercise_edit/{workoutExerciseId}",
        titleRes = R.string.exercise_detail,
        selectedIcon = Icons.Filled.Edit,
        unselectedIcon = Icons.Outlined.Edit
    ) {
        fun createRoute(workoutExerciseId: Long) = "exercise_edit/$workoutExerciseId"
    }

    object ActiveWorkout : RackedUpDestination(
        route = "active_workout/{workoutId}",
        titleRes = R.string.active_workout,
        selectedIcon = Icons.Filled.PlayArrow,
        unselectedIcon = Icons.Outlined.PlayArrow
    ) {
        fun createRoute(workoutId: Long? = null) = 
            if (workoutId != null) "active_workout/$workoutId" else "active_workout"
    }

    object ActiveWorkoutQuick : RackedUpDestination(
        route = "active_workout",
        titleRes = R.string.active_workout,
        selectedIcon = Icons.Filled.PlayArrow,
        unselectedIcon = Icons.Outlined.PlayArrow
    )

    object SelectProgramDay : RackedUpDestination(
        route = "select_program_day/{programId}",
        titleRes = R.string.programs,
        selectedIcon = Icons.Filled.FitnessCenter,
        unselectedIcon = Icons.Outlined.FitnessCenter
    ) {
        fun createRoute(programId: Long) = "select_program_day/$programId"
    }

    object Settings : RackedUpDestination(
        route = "settings",
        titleRes = R.string.settings,
        selectedIcon = Icons.Filled.Settings,
        unselectedIcon = Icons.Outlined.Settings
    )

    object ExerciseLibrary : RackedUpDestination(
        route = "exercise_library",
        titleRes = R.string.exercise_library,
        selectedIcon = Icons.AutoMirrored.Filled.MenuBook,
        unselectedIcon = Icons.AutoMirrored.Outlined.MenuBook
    )

    object ExerciseCreate : RackedUpDestination(
        route = "exercise_create",
        titleRes = R.string.add_exercise,
        selectedIcon = Icons.Filled.Add,
        unselectedIcon = Icons.Outlined.Add
    )

    object DataManagement : RackedUpDestination(
        route = "data_management",
        titleRes = R.string.data_management,
        selectedIcon = Icons.Filled.Storage,
        unselectedIcon = Icons.Outlined.Storage
    )

    object Onboarding : RackedUpDestination(
        route = "onboarding",
        titleRes = R.string.onboarding,
        selectedIcon = Icons.Filled.Person,
        unselectedIcon = Icons.Outlined.Person
    )

    object EditProfile : RackedUpDestination(
        route = "edit_profile",
        titleRes = R.string.edit_profile,
        selectedIcon = Icons.Filled.Edit,
        unselectedIcon = Icons.Outlined.Edit
    )

    object ProgramBuilder : RackedUpDestination(
        route = "program_builder",
        titleRes = R.string.programs,
        selectedIcon = Icons.Filled.Add,
        unselectedIcon = Icons.Outlined.Add
    )

    // Help & Documentation destinations
    object HelpDocumentation : RackedUpDestination(
        route = "help_documentation",
        titleRes = R.string.help_documentation,
        selectedIcon = Icons.Filled.Help,
        unselectedIcon = Icons.Outlined.Help
    )

    object QuickStartGuide : RackedUpDestination(
        route = "quick_start_guide",
        titleRes = R.string.quick_start_guide,
        selectedIcon = Icons.Filled.PlayArrow,
        unselectedIcon = Icons.Outlined.PlayArrow
    )

    object FeatureOverview : RackedUpDestination(
        route = "feature_overview",
        titleRes = R.string.feature_overview,
        selectedIcon = Icons.Filled.Explore,
        unselectedIcon = Icons.Outlined.Explore
    )

    object Troubleshooting : RackedUpDestination(
        route = "troubleshooting",
        titleRes = R.string.troubleshooting,
        selectedIcon = Icons.Filled.Build,
        unselectedIcon = Icons.Outlined.Build
    )


    companion object {
        // Bottom navigation destinations
        val bottomNavDestinations = listOf(
            Dashboard,
            Workouts,
            Progress,
            Programs,
            Profile
        )
    }
}
