package com.chilluminati.rackedup.presentation.programs

import com.chilluminati.rackedup.data.database.entity.Program
import com.chilluminati.rackedup.data.database.entity.ProgramDay
import com.chilluminati.rackedup.data.database.entity.ProgramExercise
import java.util.Date

/**
 * System for managing pre-built program templates
 */
object ProgramTemplatesSystem {
    
    fun getAllTemplates(): List<ProgramTemplate> {
        return listOf(
            getStartingStrengthTemplate(),
            getStrongLifts5x5Template(),
            getPushPullLegsTemplate(),
            getUpperLowerTemplate(),
            get531BeginnerTemplate(),
            getGZCLPTemplate(),
            getFullBodyBeginnerTemplate(),
            getBodyweightTemplate()
        )
    }
    
    fun getTemplateById(id: String): ProgramTemplate? {
        return getAllTemplates().find { it.id == id }
    }
    
    private fun getStartingStrengthTemplate(): ProgramTemplate {
        val day1 = ProgramDayTemplate(
            name = "Day 1 – Monday (Workout A)",
            exercises = listOf(
                ProgramExerciseTemplate(exerciseName = "Squat", sets = "3", reps = "5"),
                ProgramExerciseTemplate(exerciseName = "Bench Press", sets = "3", reps = "5"),
                ProgramExerciseTemplate(exerciseName = "Deadlift", sets = "1", reps = "5")
            )
        )
        val day2 = ProgramDayTemplate(
            name = "Day 2 – Tuesday (Rest Day)",
            exercises = emptyList()
        )
        val day3 = ProgramDayTemplate(
            name = "Day 3 – Wednesday (Workout B)",
            exercises = listOf(
                ProgramExerciseTemplate(exerciseName = "Squat", sets = "3", reps = "5"),
                ProgramExerciseTemplate(exerciseName = "Overhead Press", sets = "3", reps = "5"),
                ProgramExerciseTemplate(exerciseName = "Power Clean", sets = "5", reps = "3")
            )
        )
        val day4 = ProgramDayTemplate(
            name = "Day 4 – Thursday (Rest Day)",
            exercises = emptyList()
        )

        val days = listOf(day1, day2, day3, day4)

        return ProgramTemplate(
            id = "starting_strength",
            name = "Starting Strength – Novice Linear Progression",
            description = "Mark Rippetoe's novice linear progression program focused on building basic strength with compound movements. Progression: Add 5 lbs per session (2.5 lbs for press).",
            author = "Mark Rippetoe",
            difficultyLevel = "Beginner",
            programType = "Powerlifting",
            durationWeeks = 12,
            daysPerWeek = 4,
            goal = "Strength",
            days = days,
            tags = listOf("Beginner", "Strength", "Linear Progression", "Compound"),
            estimatedDuration = 60
        )
    }
    
    private fun getStrongLifts5x5Template(): ProgramTemplate {
        val day1 = ProgramDayTemplate(
            name = "Day 1 – Monday (Workout A)",
            exercises = listOf(
                ProgramExerciseTemplate(exerciseName = "Squat", sets = "5", reps = "5"),
                ProgramExerciseTemplate(exerciseName = "Bench Press", sets = "5", reps = "5"),
                ProgramExerciseTemplate(exerciseName = "Barbell Row", sets = "5", reps = "5")
            )
        )
        val day2 = ProgramDayTemplate(
            name = "Day 2 – Tuesday (Rest Day)",
            exercises = emptyList()
        )
        val day3 = ProgramDayTemplate(
            name = "Day 3 – Wednesday (Workout B)",
            exercises = listOf(
                ProgramExerciseTemplate(exerciseName = "Squat", sets = "5", reps = "5"),
                ProgramExerciseTemplate(exerciseName = "Overhead Press", sets = "5", reps = "5"),
                ProgramExerciseTemplate(exerciseName = "Deadlift", sets = "1", reps = "5")
            )
        )
        val day4 = ProgramDayTemplate(
            name = "Day 4 – Thursday (Rest Day)",
            exercises = emptyList()
        )
        val days = listOf(day1, day2, day3, day4)

        return ProgramTemplate(
            id = "stronglifts_5x5",
            name = "StrongLifts 5×5",
            description = "Simple and effective beginner program alternating between two workouts, adding weight every session. Progression: Add 5 lbs each session (2.5 lbs for press).",
            author = "Mehdi Hadim",
            difficultyLevel = "Beginner",
            programType = "Powerlifting",
            durationWeeks = 12,
            daysPerWeek = 4,
            goal = "Strength",
            days = days,
            tags = listOf("Beginner", "Strength", "Simple", "Linear Progression"),
            estimatedDuration = 45
        )
    }
    
    private fun getPushPullLegsTemplate(): ProgramTemplate {
        val days = listOf(
            ProgramDayTemplate(
                name = "Push",
                exercises = listOf(
                    ProgramExerciseTemplate(exerciseName = "Bench Press", sets = "4x6-8", reps = "6-8"),
                    ProgramExerciseTemplate(exerciseName = "Overhead Press", sets = "3x8-10", reps = "8-10"),
                    ProgramExerciseTemplate(exerciseName = "Incline Dumbbell Press", sets = "3x8-10", reps = "8-10"),
                    ProgramExerciseTemplate(exerciseName = "Lateral Raises", sets = "3x12-15", reps = "12-15"),
                    ProgramExerciseTemplate(exerciseName = "Triceps Pushdowns", sets = "3x10-12", reps = "10-12")
                )
            ),
            ProgramDayTemplate(
                name = "Pull",
                exercises = listOf(
                    ProgramExerciseTemplate(exerciseName = "Pull-Ups", sets = "4x6-8", reps = "6-8"),
                    ProgramExerciseTemplate(exerciseName = "Barbell Row", sets = "4x6-8", reps = "6-8"),
                    ProgramExerciseTemplate(exerciseName = "Face Pull", sets = "3x12-15", reps = "12-15"),
                    ProgramExerciseTemplate(exerciseName = "Dumbbell Curl", sets = "3x10-12", reps = "10-12"),
                    ProgramExerciseTemplate(exerciseName = "Hammer Curl", sets = "3x10-12", reps = "10-12")
                )
            ),
            ProgramDayTemplate(
                name = "Legs",
                exercises = listOf(
                    ProgramExerciseTemplate(exerciseName = "Squat", sets = "4x6-8", reps = "6-8"),
                    ProgramExerciseTemplate(exerciseName = "Romanian Deadlift", sets = "3x8-10", reps = "8-10"),
                    ProgramExerciseTemplate(exerciseName = "Walking Lunge", sets = "3x10", reps = "10 per leg"),
                    ProgramExerciseTemplate(exerciseName = "Leg Curl", sets = "3x12-15", reps = "12-15"),
                    ProgramExerciseTemplate(exerciseName = "Calf Raise", sets = "4x12-15", reps = "12-15")
                )
            )
        )
        
        return ProgramTemplate(
            id = "push_pull_legs",
            name = "Push / Pull / Legs (3-day)",
            description = "Classic 3-day split focusing on push, pull, and legs.",
            author = "Community",
            difficultyLevel = "Intermediate",
            programType = "Bodybuilding",
            durationWeeks = 8,
            daysPerWeek = 3,
            goal = "Hypertrophy",
            days = days,
            tags = listOf("Intermediate", "Hypertrophy", "Split", "Balanced"),
            estimatedDuration = 75
        )
    }
    
    private fun getUpperLowerTemplate(): ProgramTemplate {
        val days = listOf(
            ProgramDayTemplate(
                name = "Upper A",
                exercises = listOf(
                    ProgramExerciseTemplate(exerciseName = "Bench Press", sets = "4x6-8", reps = "6-8"),
                    ProgramExerciseTemplate(exerciseName = "Overhead Press", sets = "3x8-10", reps = "8-10"),
                    ProgramExerciseTemplate(exerciseName = "Pull-Ups", sets = "4x6-8", reps = "6-8"),
                    ProgramExerciseTemplate(exerciseName = "Barbell Row", sets = "3x8-10", reps = "8-10"),
                    ProgramExerciseTemplate(exerciseName = "Triceps Pushdown", sets = "3x10-12", reps = "10-12"),
                    ProgramExerciseTemplate(exerciseName = "Dumbbell Curl", sets = "3x10-12", reps = "10-12")
                )
            ),
            ProgramDayTemplate(
                name = "Lower A",
                exercises = listOf(
                    ProgramExerciseTemplate(exerciseName = "Squat", sets = "4x6-8", reps = "6-8"),
                    ProgramExerciseTemplate(exerciseName = "Romanian Deadlift", sets = "3x8-10", reps = "8-10"),
                    ProgramExerciseTemplate(exerciseName = "Walking Lunge", sets = "3x10", reps = "10 per leg"),
                    ProgramExerciseTemplate(exerciseName = "Leg Press", sets = "3x10-12", reps = "10-12"),
                    ProgramExerciseTemplate(exerciseName = "Calf Raise", sets = "4x12-15", reps = "12-15")
                )
            ),
            ProgramDayTemplate(
                name = "Upper B",
                exercises = listOf(
                    ProgramExerciseTemplate(exerciseName = "Overhead Press", sets = "4x6-8", reps = "6-8"),
                    ProgramExerciseTemplate(exerciseName = "Incline Bench Press", sets = "3x8-10", reps = "8-10"),
                    ProgramExerciseTemplate(exerciseName = "Barbell Row", sets = "4x6-8", reps = "6-8"),
                    ProgramExerciseTemplate(exerciseName = "Pull-Ups", sets = "3x8-10", reps = "8-10"),
                    ProgramExerciseTemplate(exerciseName = "Skullcrusher", sets = "3x10-12", reps = "10-12"),
                    ProgramExerciseTemplate(exerciseName = "Hammer Curl", sets = "3x10-12", reps = "10-12")
                )
            ),
            ProgramDayTemplate(
                name = "Lower B",
                exercises = listOf(
                    ProgramExerciseTemplate(exerciseName = "Deadlift", sets = "3x5", reps = "5"),
                    ProgramExerciseTemplate(exerciseName = "Front Squat", sets = "3x8-10", reps = "8-10"),
                    ProgramExerciseTemplate(exerciseName = "Bulgarian Split Squat", sets = "3x10", reps = "10 per leg"),
                    ProgramExerciseTemplate(exerciseName = "Leg Curl", sets = "3x10-12", reps = "10-12"),
                    ProgramExerciseTemplate(exerciseName = "Calf Raise", sets = "4x12-15", reps = "12-15")
                )
            )
        )
        
        return ProgramTemplate(
            id = "upper_lower",
            name = "Upper / Lower Split (4-day)",
            description = "4-day program alternating between upper and lower body.",
            author = "Community",
            difficultyLevel = "Intermediate",
            programType = "Bodybuilding",
            durationWeeks = 8,
            daysPerWeek = 4,
            goal = "Hypertrophy",
            days = days,
            tags = listOf("Intermediate", "Hypertrophy", "Frequency", "Split"),
            estimatedDuration = 70
        )
    }
    
    private fun get531BeginnerTemplate(): ProgramTemplate {
        val days = listOf(
            ProgramDayTemplate(
                name = "Day 1 – Monday",
                exercises = listOf(
                    ProgramExerciseTemplate(exerciseName = "Squat (5/3/1 sets)", sets = "3+", reps = "per wave"),
                    ProgramExerciseTemplate(exerciseName = "Bench Press", sets = "5x5"),
                    ProgramExerciseTemplate(exerciseName = "Barbell Row", sets = "5x10"),
                    ProgramExerciseTemplate(exerciseName = "Dips", sets = "5x10")
                )
            ),
            ProgramDayTemplate(
                name = "Day 2 – Tuesday",
                exercises = listOf(
                    ProgramExerciseTemplate(exerciseName = "Deadlift (5/3/1 sets)", sets = "3+", reps = "per wave"),
                    ProgramExerciseTemplate(exerciseName = "Overhead Press", sets = "5x5"),
                    ProgramExerciseTemplate(exerciseName = "Chin-Ups", sets = "5x10"),
                    ProgramExerciseTemplate(exerciseName = "Walking Lunge", sets = "5x10", reps = "10 per leg")
                )
            ),
            ProgramDayTemplate(
                name = "Day 4 – Thursday",
                exercises = listOf(
                    ProgramExerciseTemplate(exerciseName = "Bench Press (5/3/1 sets)", sets = "3+", reps = "per wave"),
                    ProgramExerciseTemplate(exerciseName = "Squat", sets = "5x5"),
                    ProgramExerciseTemplate(exerciseName = "Barbell Row", sets = "5x10"),
                    ProgramExerciseTemplate(exerciseName = "Push-Ups", sets = "5x10")
                )
            ),
            ProgramDayTemplate(
                name = "Day 5 – Friday",
                exercises = listOf(
                    ProgramExerciseTemplate(exerciseName = "Overhead Press (5/3/1 sets)", sets = "3+", reps = "per wave"),
                    ProgramExerciseTemplate(exerciseName = "Deadlift", sets = "5x5"),
                    ProgramExerciseTemplate(exerciseName = "Chin-Ups", sets = "5x10"),
                    ProgramExerciseTemplate(exerciseName = "Bulgarian Split Squat", sets = "5x10", reps = "10 per leg")
                )
            )
        )
        
        return ProgramTemplate(
            id = "531_beginner",
            name = "5/3/1 for Beginners (4-day)",
            description = "Jim Wendler's 5/3/1 program adapted for beginners. (Main lift follows 5/3/1 %s, 2nd lift 5×5 @ 65–75% Training Max)",
            author = "Jim Wendler",
            difficultyLevel = "Beginner",
            programType = "Powerlifting",
            durationWeeks = 12,
            daysPerWeek = 4,
            goal = "Strength",
            days = days,
            tags = listOf("Beginner", "Strength", "Percentage", "Proven"),
            estimatedDuration = 60
        )
    }
    
    private fun getGZCLPTemplate(): ProgramTemplate {
        val days = listOf(
            ProgramDayTemplate(
                name = "Day 1 – Monday (Squat Focus)",
                exercises = listOf(
                    ProgramExerciseTemplate(exerciseName = "Squat", sets = "4x4-6", reps = "4-6"),
                    ProgramExerciseTemplate(exerciseName = "Front Squat", sets = "3x8-10", reps = "8-10"),
                    ProgramExerciseTemplate(exerciseName = "Leg Press", sets = "3x12-15", reps = "12-15"),
                    ProgramExerciseTemplate(exerciseName = "Calf Raise", sets = "3x15-20", reps = "15-20")
                )
            ),
            ProgramDayTemplate(
                name = "Day 2 – Tuesday (Bench Focus)",
                exercises = listOf(
                    ProgramExerciseTemplate(exerciseName = "Bench Press", sets = "4x4-6", reps = "4-6"),
                    ProgramExerciseTemplate(exerciseName = "Overhead Press", sets = "3x8-10", reps = "8-10"),
                    ProgramExerciseTemplate(exerciseName = "Lateral Raise", sets = "3x12-15", reps = "12-15"),
                    ProgramExerciseTemplate(exerciseName = "Triceps Pushdown", sets = "3x12-15", reps = "12-15")
                )
            ),
            ProgramDayTemplate(
                name = "Day 4 – Thursday (Deadlift Focus)",
                exercises = listOf(
                    ProgramExerciseTemplate(exerciseName = "Deadlift", sets = "4x3-5", reps = "3-5"),
                    ProgramExerciseTemplate(exerciseName = "Romanian Deadlift", sets = "3x8-10", reps = "8-10"),
                    ProgramExerciseTemplate(exerciseName = "Barbell Row", sets = "3x12-15", reps = "12-15"),
                    ProgramExerciseTemplate(exerciseName = "Face Pull", sets = "3x15-20", reps = "15-20")
                )
            ),
            ProgramDayTemplate(
                name = "Day 5 – Friday (Overhead Press Focus)",
                exercises = listOf(
                    ProgramExerciseTemplate(exerciseName = "Overhead Press", sets = "4x4-6", reps = "4-6"),
                    ProgramExerciseTemplate(exerciseName = "Incline Bench Press", sets = "3x8-10", reps = "8-10"),
                    ProgramExerciseTemplate(exerciseName = "Pull-Ups", sets = "3x8-10", reps = "8-10"),
                    ProgramExerciseTemplate(exerciseName = "Barbell Curl", sets = "3x12-15", reps = "12-15")
                )
            )
        )
        
        return ProgramTemplate(
            id = "gzclp",
            name = "GZCLP (4-day)",
            description = "Cody Lefever's linear progression program with tiered training approach.",
            author = "Cody Lefever",
            difficultyLevel = "Intermediate",
            programType = "Powerlifting",
            durationWeeks = 12,
            daysPerWeek = 4,
            goal = "Strength",
            days = days,
            tags = listOf("Intermediate", "Strength", "Tiered", "Progressive"),
            estimatedDuration = 75
        )
    }
    
    private fun getFullBodyBeginnerTemplate(): ProgramTemplate {
        val dayExercises = listOf(
            ProgramExerciseTemplate(exerciseName = "Squat", sets = "3x8-10", reps = "8-10"),
            ProgramExerciseTemplate(exerciseName = "Bench Press", sets = "3x8-10", reps = "8-10"),
            ProgramExerciseTemplate(exerciseName = "Barbell Row", sets = "3x8-10", reps = "8-10"),
            ProgramExerciseTemplate(exerciseName = "Overhead Press", sets = "3x8-10", reps = "8-10"),
            ProgramExerciseTemplate(exerciseName = "Romanian Deadlift", sets = "3x8-10", reps = "8-10"),
            ProgramExerciseTemplate(exerciseName = "Plank", sets = "3x30-60s")
        )
        val days = listOf(
            ProgramDayTemplate(name = "Day 1 – Monday", exercises = dayExercises),
            ProgramDayTemplate(name = "Day 3 – Wednesday", exercises = dayExercises),
            ProgramDayTemplate(name = "Day 5 – Friday", exercises = dayExercises)
        )
        
        return ProgramTemplate(
            id = "full_body_beginner",
            name = "Full Body Beginner (3-day)",
            description = "Complete beginner program targeting all muscle groups each session.",
            author = "Community",
            difficultyLevel = "Beginner",
            programType = "Full Body",
            durationWeeks = 8,
            daysPerWeek = 3,
            goal = "General Fitness",
            days = days,
            tags = listOf("Beginner", "Full Body", "General", "Simple"),
            estimatedDuration = 60
        )
    }
    
    private fun getBodyweightTemplate(): ProgramTemplate {
        val days = listOf(
            ProgramDayTemplate(
                name = "Day 1 – Monday (Push)",
                exercises = listOf(
                    ProgramExerciseTemplate(exerciseName = "Push-Ups", sets = "4x12-20", reps = "12-20"),
                    ProgramExerciseTemplate(exerciseName = "Pike Push-Ups", sets = "3x8-12", reps = "8-12"),
                    ProgramExerciseTemplate(exerciseName = "Diamond Push-Ups", sets = "3x8-12", reps = "8-12"),
                    ProgramExerciseTemplate(exerciseName = "Bench Dips", sets = "3x12-20", reps = "12-20")
                )
            ),
            ProgramDayTemplate(
                name = "Day 3 – Wednesday (Pull/Core)",
                exercises = listOf(
                    ProgramExerciseTemplate(exerciseName = "Inverted Rows", sets = "4x8-12", reps = "8-12"),
                    ProgramExerciseTemplate(exerciseName = "Towel Rows", sets = "3x10-12", reps = "10-12"),
                    ProgramExerciseTemplate(exerciseName = "Superman Hold", sets = "3x30s"),
                    ProgramExerciseTemplate(exerciseName = "Hollow Body Hold", sets = "3x30s")
                )
            ),
            ProgramDayTemplate(
                name = "Day 5 – Friday (Legs)",
                exercises = listOf(
                    ProgramExerciseTemplate(exerciseName = "Squats", sets = "4x15-20", reps = "15-20"),
                    ProgramExerciseTemplate(exerciseName = "Bulgarian Split Squats", sets = "3x10", reps = "10 per leg"),
                    ProgramExerciseTemplate(exerciseName = "Glute Bridge", sets = "3x15-20", reps = "15-20"),
                    ProgramExerciseTemplate(exerciseName = "Calf Raises", sets = "3x20-25", reps = "20-25")
                )
            )
        )
        
        return ProgramTemplate(
            id = "bodyweight",
            name = "Bodyweight Training (3-day)",
            description = "Bodyweight program requiring no equipment.",
            author = "Community",
            difficultyLevel = "Beginner",
            programType = "Bodyweight",
            durationWeeks = 8,
            daysPerWeek = 3,
            goal = "General Fitness",
            days = days,
            tags = listOf("Bodyweight", "Home", "No Equipment", "Beginner"),
            estimatedDuration = 45
        )
    }
}

/**
 * Data class representing a program template
 */
data class ProgramTemplate(
    val id: String,
    val name: String,
    val description: String,
    val author: String,
    val difficultyLevel: String,
    val programType: String,
    val durationWeeks: Int,
    val daysPerWeek: Int,
    val goal: String,
    val days: List<ProgramDayTemplate>,
    val tags: List<String>,
    val estimatedDuration: Int, // minutes
    val notes: String? = null
)

/**
 * Data class representing a day template within a program
 */
data class ProgramDayTemplate(
    val name: String,
    val exercises: List<ProgramExerciseTemplate>,
    val description: String? = null,
    val muscleGroups: List<String> = emptyList()
)

/**
 * Data class representing an exercise template within a day
 */
data class ProgramExerciseTemplate(
    val exerciseName: String,
    val sets: String,
    val reps: String? = null,
    val weightPercentage: Double? = null,
    val restTimeSeconds: Int? = null,
    val rpeTarget: Int? = null,
    val progressionScheme: String? = null,
    val progressionIncrement: Double? = null,
    val isSuperset: Boolean = false,
    val supersetId: String? = null,
    val notes: String? = null
)

 
