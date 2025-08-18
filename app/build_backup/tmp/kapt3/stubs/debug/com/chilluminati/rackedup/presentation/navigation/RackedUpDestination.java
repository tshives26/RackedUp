package com.chilluminati.rackedup.presentation.navigation;

/**
 * Sealed class representing all destinations in the app
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000L\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0015\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b7\u0018\u0000 \u00122\u00020\u0001:\f\u0011\u0012\u0013\u0014\u0015\u0016\u0017\u0018\u0019\u001a\u001b\u001cB)\b\u0004\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0001\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\tR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0011\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0011\u0010\b\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\r\u0082\u0001\u000b\u001d\u001e\u001f !\"#$%&\'\u00a8\u0006("}, d2 = {"Lcom/chilluminati/rackedup/presentation/navigation/RackedUpDestination;", "", "route", "", "titleRes", "", "selectedIcon", "Landroidx/compose/ui/graphics/vector/ImageVector;", "unselectedIcon", "(Ljava/lang/String;ILandroidx/compose/ui/graphics/vector/ImageVector;Landroidx/compose/ui/graphics/vector/ImageVector;)V", "getRoute", "()Ljava/lang/String;", "getSelectedIcon", "()Landroidx/compose/ui/graphics/vector/ImageVector;", "getTitleRes", "()I", "getUnselectedIcon", "ActiveWorkout", "Companion", "Dashboard", "DataManagement", "ExerciseDetail", "ExerciseLibrary", "Profile", "Programs", "Progress", "Settings", "WorkoutDetail", "Workouts", "Lcom/chilluminati/rackedup/presentation/navigation/RackedUpDestination$ActiveWorkout;", "Lcom/chilluminati/rackedup/presentation/navigation/RackedUpDestination$Dashboard;", "Lcom/chilluminati/rackedup/presentation/navigation/RackedUpDestination$DataManagement;", "Lcom/chilluminati/rackedup/presentation/navigation/RackedUpDestination$ExerciseDetail;", "Lcom/chilluminati/rackedup/presentation/navigation/RackedUpDestination$ExerciseLibrary;", "Lcom/chilluminati/rackedup/presentation/navigation/RackedUpDestination$Profile;", "Lcom/chilluminati/rackedup/presentation/navigation/RackedUpDestination$Programs;", "Lcom/chilluminati/rackedup/presentation/navigation/RackedUpDestination$Progress;", "Lcom/chilluminati/rackedup/presentation/navigation/RackedUpDestination$Settings;", "Lcom/chilluminati/rackedup/presentation/navigation/RackedUpDestination$WorkoutDetail;", "Lcom/chilluminati/rackedup/presentation/navigation/RackedUpDestination$Workouts;", "app_debug"})
public abstract class RackedUpDestination {
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String route = null;
    private final int titleRes = 0;
    @org.jetbrains.annotations.NotNull()
    private final androidx.compose.ui.graphics.vector.ImageVector selectedIcon = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.compose.ui.graphics.vector.ImageVector unselectedIcon = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.util.List<com.chilluminati.rackedup.presentation.navigation.RackedUpDestination> bottomNavDestinations = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.chilluminati.rackedup.presentation.navigation.RackedUpDestination.Companion Companion = null;
    
    private RackedUpDestination(java.lang.String route, @androidx.annotation.StringRes()
    int titleRes, androidx.compose.ui.graphics.vector.ImageVector selectedIcon, androidx.compose.ui.graphics.vector.ImageVector unselectedIcon) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getRoute() {
        return null;
    }
    
    public final int getTitleRes() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.compose.ui.graphics.vector.ImageVector getSelectedIcon() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.compose.ui.graphics.vector.ImageVector getUnselectedIcon() {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0017\u0010\u0003\u001a\u00020\u00042\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u0006\u00a2\u0006\u0002\u0010\u0007\u00a8\u0006\b"}, d2 = {"Lcom/chilluminati/rackedup/presentation/navigation/RackedUpDestination$ActiveWorkout;", "Lcom/chilluminati/rackedup/presentation/navigation/RackedUpDestination;", "()V", "createRoute", "", "programId", "", "(Ljava/lang/Long;)Ljava/lang/String;", "app_debug"})
    public static final class ActiveWorkout extends com.chilluminati.rackedup.presentation.navigation.RackedUpDestination {
        @org.jetbrains.annotations.NotNull()
        public static final com.chilluminati.rackedup.presentation.navigation.RackedUpDestination.ActiveWorkout INSTANCE = null;
        
        private ActiveWorkout() {
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String createRoute(@org.jetbrains.annotations.Nullable()
        java.lang.Long programId) {
            return null;
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u0017\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007\u00a8\u0006\b"}, d2 = {"Lcom/chilluminati/rackedup/presentation/navigation/RackedUpDestination$Companion;", "", "()V", "bottomNavDestinations", "", "Lcom/chilluminati/rackedup/presentation/navigation/RackedUpDestination;", "getBottomNavDestinations", "()Ljava/util/List;", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.util.List<com.chilluminati.rackedup.presentation.navigation.RackedUpDestination> getBottomNavDestinations() {
            return null;
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/chilluminati/rackedup/presentation/navigation/RackedUpDestination$Dashboard;", "Lcom/chilluminati/rackedup/presentation/navigation/RackedUpDestination;", "()V", "app_debug"})
    public static final class Dashboard extends com.chilluminati.rackedup.presentation.navigation.RackedUpDestination {
        @org.jetbrains.annotations.NotNull()
        public static final com.chilluminati.rackedup.presentation.navigation.RackedUpDestination.Dashboard INSTANCE = null;
        
        private Dashboard() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/chilluminati/rackedup/presentation/navigation/RackedUpDestination$DataManagement;", "Lcom/chilluminati/rackedup/presentation/navigation/RackedUpDestination;", "()V", "app_debug"})
    public static final class DataManagement extends com.chilluminati.rackedup.presentation.navigation.RackedUpDestination {
        @org.jetbrains.annotations.NotNull()
        public static final com.chilluminati.rackedup.presentation.navigation.RackedUpDestination.DataManagement INSTANCE = null;
        
        private DataManagement() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\t\n\u0000\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006\u00a8\u0006\u0007"}, d2 = {"Lcom/chilluminati/rackedup/presentation/navigation/RackedUpDestination$ExerciseDetail;", "Lcom/chilluminati/rackedup/presentation/navigation/RackedUpDestination;", "()V", "createRoute", "", "exerciseId", "", "app_debug"})
    public static final class ExerciseDetail extends com.chilluminati.rackedup.presentation.navigation.RackedUpDestination {
        @org.jetbrains.annotations.NotNull()
        public static final com.chilluminati.rackedup.presentation.navigation.RackedUpDestination.ExerciseDetail INSTANCE = null;
        
        private ExerciseDetail() {
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String createRoute(long exerciseId) {
            return null;
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/chilluminati/rackedup/presentation/navigation/RackedUpDestination$ExerciseLibrary;", "Lcom/chilluminati/rackedup/presentation/navigation/RackedUpDestination;", "()V", "app_debug"})
    public static final class ExerciseLibrary extends com.chilluminati.rackedup.presentation.navigation.RackedUpDestination {
        @org.jetbrains.annotations.NotNull()
        public static final com.chilluminati.rackedup.presentation.navigation.RackedUpDestination.ExerciseLibrary INSTANCE = null;
        
        private ExerciseLibrary() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/chilluminati/rackedup/presentation/navigation/RackedUpDestination$Profile;", "Lcom/chilluminati/rackedup/presentation/navigation/RackedUpDestination;", "()V", "app_debug"})
    public static final class Profile extends com.chilluminati.rackedup.presentation.navigation.RackedUpDestination {
        @org.jetbrains.annotations.NotNull()
        public static final com.chilluminati.rackedup.presentation.navigation.RackedUpDestination.Profile INSTANCE = null;
        
        private Profile() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/chilluminati/rackedup/presentation/navigation/RackedUpDestination$Programs;", "Lcom/chilluminati/rackedup/presentation/navigation/RackedUpDestination;", "()V", "app_debug"})
    public static final class Programs extends com.chilluminati.rackedup.presentation.navigation.RackedUpDestination {
        @org.jetbrains.annotations.NotNull()
        public static final com.chilluminati.rackedup.presentation.navigation.RackedUpDestination.Programs INSTANCE = null;
        
        private Programs() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/chilluminati/rackedup/presentation/navigation/RackedUpDestination$Progress;", "Lcom/chilluminati/rackedup/presentation/navigation/RackedUpDestination;", "()V", "app_debug"})
    public static final class Progress extends com.chilluminati.rackedup.presentation.navigation.RackedUpDestination {
        @org.jetbrains.annotations.NotNull()
        public static final com.chilluminati.rackedup.presentation.navigation.RackedUpDestination.Progress INSTANCE = null;
        
        private Progress() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/chilluminati/rackedup/presentation/navigation/RackedUpDestination$Settings;", "Lcom/chilluminati/rackedup/presentation/navigation/RackedUpDestination;", "()V", "app_debug"})
    public static final class Settings extends com.chilluminati.rackedup.presentation.navigation.RackedUpDestination {
        @org.jetbrains.annotations.NotNull()
        public static final com.chilluminati.rackedup.presentation.navigation.RackedUpDestination.Settings INSTANCE = null;
        
        private Settings() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\t\n\u0000\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006\u00a8\u0006\u0007"}, d2 = {"Lcom/chilluminati/rackedup/presentation/navigation/RackedUpDestination$WorkoutDetail;", "Lcom/chilluminati/rackedup/presentation/navigation/RackedUpDestination;", "()V", "createRoute", "", "workoutId", "", "app_debug"})
    public static final class WorkoutDetail extends com.chilluminati.rackedup.presentation.navigation.RackedUpDestination {
        @org.jetbrains.annotations.NotNull()
        public static final com.chilluminati.rackedup.presentation.navigation.RackedUpDestination.WorkoutDetail INSTANCE = null;
        
        private WorkoutDetail() {
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String createRoute(long workoutId) {
            return null;
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/chilluminati/rackedup/presentation/navigation/RackedUpDestination$Workouts;", "Lcom/chilluminati/rackedup/presentation/navigation/RackedUpDestination;", "()V", "app_debug"})
    public static final class Workouts extends com.chilluminati.rackedup.presentation.navigation.RackedUpDestination {
        @org.jetbrains.annotations.NotNull()
        public static final com.chilluminati.rackedup.presentation.navigation.RackedUpDestination.Workouts INSTANCE = null;
        
        private Workouts() {
        }
    }
}