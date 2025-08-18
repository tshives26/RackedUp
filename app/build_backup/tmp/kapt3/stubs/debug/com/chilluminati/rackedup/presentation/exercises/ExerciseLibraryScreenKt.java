package com.chilluminati.rackedup.presentation.exercises;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000:\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a$\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0012\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00010\u0005H\u0003\u001a4\u0010\u0006\u001a\u00020\u00012\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00010\b2\u0012\u0010\t\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\u00010\u00052\b\b\u0002\u0010\u000b\u001a\u00020\fH\u0007\u001a,\u0010\r\u001a\u00020\u00012\u0006\u0010\u000e\u001a\u00020\u00032\u0006\u0010\u0002\u001a\u00020\u00032\u0012\u0010\u000f\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\u00010\u0005H\u0003\u001a\u001e\u0010\u0010\u001a\u00020\u00012\u0006\u0010\u0011\u001a\u00020\u00122\f\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00010\bH\u0003\u001a\u0010\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u0003H\u0002\u00a8\u0006\u0017"}, d2 = {"ExerciseCategoryChips", "", "selectedCategory", "", "onCategorySelected", "Lkotlin/Function1;", "ExerciseLibraryScreen", "onNavigateBack", "Lkotlin/Function0;", "onNavigateToExerciseDetail", "", "modifier", "Landroidx/compose/ui/Modifier;", "ExerciseList", "searchQuery", "onExerciseClick", "ExerciseListItem", "exercise", "Lcom/chilluminati/rackedup/presentation/exercises/ExerciseData;", "onClick", "getExerciseIcon", "Landroidx/compose/ui/graphics/vector/ImageVector;", "equipment", "app_debug"})
public final class ExerciseLibraryScreenKt {
    
    /**
     * Exercise library screen showing all available exercises
     */
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void ExerciseLibraryScreen(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onNavigateBack, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.Long, kotlin.Unit> onNavigateToExerciseDetail, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void ExerciseCategoryChips(java.lang.String selectedCategory, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onCategorySelected) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void ExerciseList(java.lang.String searchQuery, java.lang.String selectedCategory, kotlin.jvm.functions.Function1<? super java.lang.Long, kotlin.Unit> onExerciseClick) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void ExerciseListItem(com.chilluminati.rackedup.presentation.exercises.ExerciseData exercise, kotlin.jvm.functions.Function0<kotlin.Unit> onClick) {
    }
    
    private static final androidx.compose.ui.graphics.vector.ImageVector getExerciseIcon(java.lang.String equipment) {
        return null;
    }
}