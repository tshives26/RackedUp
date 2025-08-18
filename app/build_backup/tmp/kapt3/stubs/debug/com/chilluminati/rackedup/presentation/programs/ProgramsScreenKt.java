package com.chilluminati.rackedup.presentation.programs;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u00008\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\u001a6\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0007\u001a\u00020\u00032\f\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00010\tH\u0003\u001a\u001e\u0010\n\u001a\u00020\u00012\u0014\u0010\u000b\u001a\u0010\u0012\u0006\u0012\u0004\u0018\u00010\r\u0012\u0004\u0012\u00020\u00010\fH\u0003\u001a\b\u0010\u000e\u001a\u00020\u0001H\u0003\u001a6\u0010\u000f\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0010\u001a\u00020\u00032\u0006\u0010\u0011\u001a\u00020\u00032\u0006\u0010\u0012\u001a\u00020\u00132\f\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00010\tH\u0003\u001a\u001e\u0010\u0015\u001a\u00020\u00012\u0014\u0010\u000b\u001a\u0010\u0012\u0006\u0012\u0004\u0018\u00010\r\u0012\u0004\u0012\u00020\u00010\fH\u0003\u001a\u0016\u0010\u0016\u001a\u00020\u00012\f\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00010\tH\u0003\u001a(\u0010\u0018\u001a\u00020\u00012\u0014\u0010\u000b\u001a\u0010\u0012\u0006\u0012\u0004\u0018\u00010\r\u0012\u0004\u0012\u00020\u00010\f2\b\b\u0002\u0010\u0019\u001a\u00020\u001aH\u0007\u00a8\u0006\u001b"}, d2 = {"CurrentProgramCard", "", "programName", "", "currentWeek", "", "totalWeeks", "nextWorkout", "onStartWorkout", "Lkotlin/Function0;", "MyProgramsTab", "onNavigateToActiveWorkout", "Lkotlin/Function1;", "", "ProgramBuilderTab", "ProgramCard", "duration", "type", "isCustom", "", "onStart", "ProgramTemplatesTab", "ProgramsHeader", "onCreateProgram", "ProgramsScreen", "modifier", "Landroidx/compose/ui/Modifier;", "app_debug"})
public final class ProgramsScreenKt {
    
    /**
     * Programs screen showing workout routines and program builder
     */
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void ProgramsScreen(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.Long, kotlin.Unit> onNavigateToActiveWorkout, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void ProgramsHeader(kotlin.jvm.functions.Function0<kotlin.Unit> onCreateProgram) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void MyProgramsTab(kotlin.jvm.functions.Function1<? super java.lang.Long, kotlin.Unit> onNavigateToActiveWorkout) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void ProgramTemplatesTab(kotlin.jvm.functions.Function1<? super java.lang.Long, kotlin.Unit> onNavigateToActiveWorkout) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void ProgramBuilderTab() {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void CurrentProgramCard(java.lang.String programName, int currentWeek, int totalWeeks, java.lang.String nextWorkout, kotlin.jvm.functions.Function0<kotlin.Unit> onStartWorkout) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void ProgramCard(java.lang.String programName, java.lang.String duration, java.lang.String type, boolean isCustom, kotlin.jvm.functions.Function0<kotlin.Unit> onStart) {
    }
}