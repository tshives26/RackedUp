package com.chilluminati.rackedup.presentation.workouts;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u00004\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\f\u001a&\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00010\u0007H\u0003\u001a=\u0010\b\u001a\u00020\u00012\b\u0010\t\u001a\u0004\u0018\u00010\n2\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00010\u00072\f\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u00010\u00072\b\b\u0002\u0010\r\u001a\u00020\u000eH\u0007\u00a2\u0006\u0002\u0010\u000f\u001a\b\u0010\u0010\u001a\u00020\u0001H\u0003\u001a\u001e\u0010\u0011\u001a\u00020\u00012\u0006\u0010\u0012\u001a\u00020\u00132\f\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00010\u0007H\u0003\u001a\u001e\u0010\u0015\u001a\u00020\u00012\u0006\u0010\u0016\u001a\u00020\u00132\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00010\u0007H\u0003\u001a \u0010\u0017\u001a\u00020\u00012\u0006\u0010\u0016\u001a\u00020\u00132\u0006\u0010\u0018\u001a\u00020\u00032\u0006\u0010\u0019\u001a\u00020\u0003H\u0003\u001a\u0018\u0010\u001a\u001a\u00020\u00012\u0006\u0010\u001b\u001a\u00020\u00032\u0006\u0010\u001c\u001a\u00020\u0003H\u0003\u001a\u0010\u0010\u001d\u001a\u00020\u00012\u0006\u0010\u001e\u001a\u00020\u0013H\u0003\u00a8\u0006\u001f"}, d2 = {"ActiveExerciseCard", "", "exerciseName", "", "isCompleted", "", "onStartRest", "Lkotlin/Function0;", "ActiveWorkoutScreen", "programId", "", "onNavigateBack", "onWorkoutComplete", "modifier", "Landroidx/compose/ui/Modifier;", "(Ljava/lang/Long;Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function0;Landroidx/compose/ui/Modifier;)V", "CurrentWorkoutSummary", "RestTimerCard", "restTimer", "", "onTimerComplete", "SetInputRow", "setNumber", "SetSummaryRow", "weight", "reps", "StatItem", "label", "value", "WorkoutTimerCard", "workoutTimer", "app_debug"})
public final class ActiveWorkoutScreenKt {
    
    /**
     * Active workout session screen with live tracking
     */
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void ActiveWorkoutScreen(@org.jetbrains.annotations.Nullable()
    java.lang.Long programId, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onNavigateBack, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onWorkoutComplete, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void WorkoutTimerCard(int workoutTimer) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void RestTimerCard(int restTimer, kotlin.jvm.functions.Function0<kotlin.Unit> onTimerComplete) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void CurrentWorkoutSummary() {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void ActiveExerciseCard(java.lang.String exerciseName, boolean isCompleted, kotlin.jvm.functions.Function0<kotlin.Unit> onStartRest) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void SetInputRow(int setNumber, kotlin.jvm.functions.Function0<kotlin.Unit> onStartRest) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void SetSummaryRow(int setNumber, java.lang.String weight, java.lang.String reps) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void StatItem(java.lang.String label, java.lang.String value) {
    }
}