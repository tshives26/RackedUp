package com.chilluminati.rackedup.presentation.programs

import com.chilluminati.rackedup.data.database.entity.Program
import com.chilluminati.rackedup.data.database.entity.ProgramDay
import com.chilluminati.rackedup.data.database.entity.ProgramExercise
import java.util.Date

/**
 * System for managing pre-built program templates
 * All exercises are aligned with the free-exercise-db (github.com/yuhonas/free-exercise-db)
 */
object ProgramTemplatesSystem {
    
    fun getAllTemplates(): List<ProgramTemplate> {
        return listOf(
            getBeginnerFullBodyTemplate(),
            getIntermediateUpperLowerTemplate(),
            getPushPullLegsTemplate(),
            getStrengthFocusedTemplate(),
            getHypertrophyTemplate(),
            getBodyweightCircuitTemplate(),
            getFunctionalFitnessTemplate(),
            getAthletePerformanceTemplate(),
            getPowerliftingTemplate(),
            getAdvancedBodybuildingTemplate()
        )
    }
    
    fun getTemplateById(id: String): ProgramTemplate? {
        return getAllTemplates().find { it.id == id }
    }
    
    // 1. BEGINNER - Full Body (3 days/week) - EASY
    private fun getBeginnerFullBodyTemplate(): ProgramTemplate {
        val dayA = ProgramDayTemplate(
            name = "Full Body A",
            exercises = listOf(
                ProgramExerciseTemplate(exerciseName = "Bodyweight Squat", sets = "3", reps = "8-12"),
                ProgramExerciseTemplate(exerciseName = "Push-Up", sets = "3", reps = "5-10"),
                ProgramExerciseTemplate(exerciseName = "Bent-Over Barbell Row", sets = "3", reps = "8-10"),
                ProgramExerciseTemplate(exerciseName = "Plank", sets = "3", reps = "20-30s"),
                ProgramExerciseTemplate(exerciseName = "Glute Bridge", sets = "3", reps = "10-15")
            )
        )
        val dayB = ProgramDayTemplate(
            name = "Full Body B",
            exercises = listOf(
                ProgramExerciseTemplate(exerciseName = "Goblet Squat", sets = "3", reps = "8-12"),
                ProgramExerciseTemplate(exerciseName = "Incline Push-Up", sets = "3", reps = "6-12"),
                ProgramExerciseTemplate(exerciseName = "Lat Pulldown", sets = "3", reps = "8-10"),
                ProgramExerciseTemplate(exerciseName = "Dead Bug", sets = "3", reps = "8 each side"),
                ProgramExerciseTemplate(exerciseName = "Step-up", sets = "3", reps = "8 each leg")
            )
        )
        
        return ProgramTemplate(
            id = "beginner_full_body",
            name = "Beginner Full Body",
            description = "Perfect introduction to strength training with bodyweight and basic movements. Builds foundation strength and movement patterns.",
            author = "RackedUp",
            difficultyLevel = "Beginner",
            programType = "Full Body",
            durationWeeks = 8,
            daysPerWeek = 3,
            goal = "General Fitness",
            days = listOf(dayA, dayB, dayA), // ABA pattern
            tags = listOf("Beginner", "Full Body", "Foundation", "Bodyweight"),
            estimatedDuration = 45
        )
    }
    
    // 2. INTERMEDIATE - Upper/Lower Split (4 days/week)
    private fun getIntermediateUpperLowerTemplate(): ProgramTemplate {
        val upperA = ProgramDayTemplate(
            name = "Upper Body A",
            exercises = listOf(
                ProgramExerciseTemplate(exerciseName = "Barbell Bench Press", sets = "4", reps = "6-8"),
                ProgramExerciseTemplate(exerciseName = "Bent-Over Barbell Row", sets = "4", reps = "6-8"),
                ProgramExerciseTemplate(exerciseName = "Standing Barbell Shoulder Press", sets = "3", reps = "8-10"),
                ProgramExerciseTemplate(exerciseName = "Pull-ups", sets = "3", reps = "5-8"),
                ProgramExerciseTemplate(exerciseName = "Standing Dumbbell Curl", sets = "3", reps = "10-12"),
                ProgramExerciseTemplate(exerciseName = "Bench Dip", sets = "3", reps = "8-12")
            )
        )
        val lowerA = ProgramDayTemplate(
            name = "Lower Body A",
            exercises = listOf(
                ProgramExerciseTemplate(exerciseName = "Barbell Back Squat", sets = "4", reps = "6-8"),
                ProgramExerciseTemplate(exerciseName = "Romanian Deadlift", sets = "3", reps = "8-10"),
                ProgramExerciseTemplate(exerciseName = "Bulgarian Split Squat", sets = "3", reps = "8 each leg"),
                ProgramExerciseTemplate(exerciseName = "Leg Curl", sets = "3", reps = "10-12"),
                ProgramExerciseTemplate(exerciseName = "Standing Calf Raise", sets = "4", reps = "12-15"),
                ProgramExerciseTemplate(exerciseName = "Plank", sets = "3", reps = "30-45s")
            )
        )
        val upperB = ProgramDayTemplate(
            name = "Upper Body B",
            exercises = listOf(
                ProgramExerciseTemplate(exerciseName = "Incline Barbell Bench Press", sets = "4", reps = "6-8"),
                ProgramExerciseTemplate(exerciseName = "Seated Cable Row", sets = "4", reps = "6-8"),
                ProgramExerciseTemplate(exerciseName = "Seated Dumbbell Shoulder Press", sets = "3", reps = "8-10"),
                ProgramExerciseTemplate(exerciseName = "Lat Pulldown", sets = "3", reps = "8-10"),
                ProgramExerciseTemplate(exerciseName = "Standing Hammer Curl", sets = "3", reps = "10-12"),
                ProgramExerciseTemplate(exerciseName = "Seated Dumbbell Overhead Triceps Extension", sets = "3", reps = "10-12")
            )
        )
        val lowerB = ProgramDayTemplate(
            name = "Lower Body B",
            exercises = listOf(
                ProgramExerciseTemplate(exerciseName = "Barbell Deadlift", sets = "4", reps = "5-6"),
                ProgramExerciseTemplate(exerciseName = "Barbell Front Squat", sets = "3", reps = "8-10"),
                ProgramExerciseTemplate(exerciseName = "Dumbbell Walking Lunge", sets = "3", reps = "10 each leg"),
                ProgramExerciseTemplate(exerciseName = "Leg Press", sets = "3", reps = "12-15"),
                ProgramExerciseTemplate(exerciseName = "Seated Calf Raise", sets = "4", reps = "12-15"),
                ProgramExerciseTemplate(exerciseName = "Russian Twist", sets = "3", reps = "15-20")
            )
        )
        
        return ProgramTemplate(
            id = "intermediate_upper_lower",
            name = "Intermediate Upper/Lower Split",
            description = "Balanced 4-day split focusing on upper and lower body development with progressive overload.",
            author = "RackedUp",
            difficultyLevel = "Intermediate",
            programType = "Upper/Lower Split",
            durationWeeks = 8,
            daysPerWeek = 4,
            goal = "Strength & Hypertrophy",
            days = listOf(upperA, lowerA, upperB, lowerB),
            tags = listOf("Intermediate", "Split", "Strength", "Hypertrophy"),
            estimatedDuration = 70
        )
    }
    
    // 3. INTERMEDIATE - Push/Pull/Legs (6 days/week)
    private fun getPushPullLegsTemplate(): ProgramTemplate {
        val pushDay = ProgramDayTemplate(
            name = "Push (Chest, Shoulders, Triceps)",
            exercises = listOf(
                ProgramExerciseTemplate(exerciseName = "Barbell Bench Press", sets = "4", reps = "6-8"),
                ProgramExerciseTemplate(exerciseName = "Standing Barbell Shoulder Press", sets = "3", reps = "8-10"),
                ProgramExerciseTemplate(exerciseName = "Incline Dumbbell Bench Press", sets = "3", reps = "8-10"),
                ProgramExerciseTemplate(exerciseName = "Dumbbell Lateral Raise", sets = "3", reps = "12-15"),
                ProgramExerciseTemplate(exerciseName = "Bench Dip", sets = "3", reps = "10-12"),
                ProgramExerciseTemplate(exerciseName = "Seated Dumbbell Overhead Triceps Extension", sets = "3", reps = "10-12")
            )
        )
        val pullDay = ProgramDayTemplate(
            name = "Pull (Back, Biceps)",
            exercises = listOf(
                ProgramExerciseTemplate(exerciseName = "Barbell Deadlift", sets = "4", reps = "5-6"),
                ProgramExerciseTemplate(exerciseName = "Pull-ups", sets = "4", reps = "6-8"),
                ProgramExerciseTemplate(exerciseName = "Bent-Over Barbell Row", sets = "3", reps = "8-10"),
                ProgramExerciseTemplate(exerciseName = "Lat Pulldown", sets = "3", reps = "8-10"),
                ProgramExerciseTemplate(exerciseName = "Standing Dumbbell Curl", sets = "3", reps = "10-12"),
                ProgramExerciseTemplate(exerciseName = "Standing Hammer Curl", sets = "3", reps = "10-12")
            )
        )
        val legsDay = ProgramDayTemplate(
            name = "Legs (Quads, Hamstrings, Glutes, Calves)",
            exercises = listOf(
                ProgramExerciseTemplate(exerciseName = "Barbell Back Squat", sets = "4", reps = "6-8"),
                ProgramExerciseTemplate(exerciseName = "Romanian Deadlift", sets = "3", reps = "8-10"),
                ProgramExerciseTemplate(exerciseName = "Leg Press", sets = "3", reps = "12-15"),
                ProgramExerciseTemplate(exerciseName = "Leg Curl", sets = "3", reps = "10-12"),
                ProgramExerciseTemplate(exerciseName = "Bulgarian Split Squat", sets = "3", reps = "10 each leg"),
                ProgramExerciseTemplate(exerciseName = "Standing Calf Raise", sets = "4", reps = "15-20")
            )
        )
        
        return ProgramTemplate(
            id = "push_pull_legs",
            name = "Push/Pull/Legs Split",
            description = "Classic 6-day bodybuilding split targeting specific muscle groups for maximum growth and recovery.",
            author = "RackedUp",
            difficultyLevel = "Intermediate",
            programType = "Push/Pull/Legs",
            durationWeeks = 8,
            daysPerWeek = 6,
            goal = "Hypertrophy",
            days = listOf(pushDay, pullDay, legsDay, pushDay, pullDay, legsDay),
            tags = listOf("Intermediate", "Bodybuilding", "Volume", "Split"),
            estimatedDuration = 75
        )
    }
    
    // 4. INTERMEDIATE - Strength Focused (4 days/week)
    private fun getStrengthFocusedTemplate(): ProgramTemplate {
        val day1 = ProgramDayTemplate(
            name = "Squat Focus",
            exercises = listOf(
                ProgramExerciseTemplate(exerciseName = "Barbell Back Squat", sets = "5", reps = "3-5"),
                ProgramExerciseTemplate(exerciseName = "Barbell Bench Press", sets = "4", reps = "6-8"),
                ProgramExerciseTemplate(exerciseName = "Bent-Over Barbell Row", sets = "4", reps = "6-8"),
                ProgramExerciseTemplate(exerciseName = "Standing Barbell Shoulder Press", sets = "3", reps = "8-10"),
                ProgramExerciseTemplate(exerciseName = "Plank", sets = "3", reps = "45-60s")
            )
        )
        val day2 = ProgramDayTemplate(
            name = "Deadlift Focus",
            exercises = listOf(
                ProgramExerciseTemplate(exerciseName = "Barbell Deadlift", sets = "5", reps = "3-5"),
                ProgramExerciseTemplate(exerciseName = "Barbell Front Squat", sets = "4", reps = "6-8"),
                ProgramExerciseTemplate(exerciseName = "Pull-ups", sets = "4", reps = "5-8"),
                ProgramExerciseTemplate(exerciseName = "Seated Dumbbell Shoulder Press", sets = "3", reps = "8-10"),
                ProgramExerciseTemplate(exerciseName = "Russian Twist", sets = "3", reps = "15-20")
            )
        )
        val day3 = ProgramDayTemplate(
            name = "Bench Focus",
            exercises = listOf(
                ProgramExerciseTemplate(exerciseName = "Barbell Bench Press", sets = "5", reps = "3-5"),
                ProgramExerciseTemplate(exerciseName = "Barbell Back Squat", sets = "4", reps = "6-8"),
                ProgramExerciseTemplate(exerciseName = "Seated Cable Row", sets = "4", reps = "6-8"),
                ProgramExerciseTemplate(exerciseName = "Incline Dumbbell Bench Press", sets = "3", reps = "8-10"),
                ProgramExerciseTemplate(exerciseName = "Bench Dip", sets = "3", reps = "8-12")
            )
        )
        val day4 = ProgramDayTemplate(
            name = "Overhead Press Focus",
            exercises = listOf(
                ProgramExerciseTemplate(exerciseName = "Standing Barbell Shoulder Press", sets = "5", reps = "3-5"),
                ProgramExerciseTemplate(exerciseName = "Romanian Deadlift", sets = "4", reps = "6-8"),
                ProgramExerciseTemplate(exerciseName = "Lat Pulldown", sets = "4", reps = "6-8"),
                ProgramExerciseTemplate(exerciseName = "Standing Dumbbell Curl", sets = "3", reps = "10-12"),
                ProgramExerciseTemplate(exerciseName = "Leg Raise", sets = "3", reps = "10-15")
            )
        )
        
        return ProgramTemplate(
            id = "strength_focused",
            name = "Strength Focused Program",
            description = "Compound movement focused program for building maximum strength with lower rep ranges.",
            author = "RackedUp",
            difficultyLevel = "Intermediate",
            programType = "Strength",
            durationWeeks = 12,
            daysPerWeek = 4,
            goal = "Strength",
            days = listOf(day1, day2, day3, day4),
            tags = listOf("Intermediate", "Strength", "Compound", "Powerlifting"),
            estimatedDuration = 60
        )
    }
    
    // 5. INTERMEDIATE - Hypertrophy (5 days/week)
    private fun getHypertrophyTemplate(): ProgramTemplate {
        val chest = ProgramDayTemplate(
            name = "Chest & Triceps",
            exercises = listOf(
                ProgramExerciseTemplate(exerciseName = "Barbell Bench Press", sets = "4", reps = "8-10"),
                ProgramExerciseTemplate(exerciseName = "Incline Dumbbell Bench Press", sets = "4", reps = "8-10"),
                ProgramExerciseTemplate(exerciseName = "Chest Fly", sets = "3", reps = "10-12"),
                ProgramExerciseTemplate(exerciseName = "Bench Dip", sets = "4", reps = "10-12"),
                ProgramExerciseTemplate(exerciseName = "Seated Dumbbell Overhead Triceps Extension", sets = "3", reps = "10-12"),
                ProgramExerciseTemplate(exerciseName = "Diamond Push-Up", sets = "3", reps = "8-12")
            )
        )
        val back = ProgramDayTemplate(
            name = "Back & Biceps",
            exercises = listOf(
                ProgramExerciseTemplate(exerciseName = "Barbell Deadlift", sets = "4", reps = "6-8"),
                ProgramExerciseTemplate(exerciseName = "Pull-ups", sets = "4", reps = "6-10"),
                ProgramExerciseTemplate(exerciseName = "Bent-Over Barbell Row", sets = "4", reps = "8-10"),
                ProgramExerciseTemplate(exerciseName = "Lat Pulldown", sets = "3", reps = "10-12"),
                ProgramExerciseTemplate(exerciseName = "Standing Dumbbell Curl", sets = "4", reps = "10-12"),
                ProgramExerciseTemplate(exerciseName = "Standing Hammer Curl", sets = "3", reps = "10-12")
            )
        )
        val shoulders = ProgramDayTemplate(
            name = "Shoulders & Abs",
            exercises = listOf(
                ProgramExerciseTemplate(exerciseName = "Standing Barbell Shoulder Press", sets = "4", reps = "8-10"),
                ProgramExerciseTemplate(exerciseName = "Dumbbell Lateral Raise", sets = "4", reps = "12-15"),
                ProgramExerciseTemplate(exerciseName = "Bent-Over Dumbbell Reverse Fly", sets = "4", reps = "12-15"),
                ProgramExerciseTemplate(exerciseName = "Barbell Upright Row", sets = "3", reps = "10-12"),
                ProgramExerciseTemplate(exerciseName = "Plank", sets = "4", reps = "45-60s"),
                ProgramExerciseTemplate(exerciseName = "Russian Twist", sets = "3", reps = "20-25")
            )
        )
        val legs = ProgramDayTemplate(
            name = "Legs",
            exercises = listOf(
                ProgramExerciseTemplate(exerciseName = "Barbell Back Squat", sets = "4", reps = "8-10"),
                ProgramExerciseTemplate(exerciseName = "Romanian Deadlift", sets = "4", reps = "8-10"),
                ProgramExerciseTemplate(exerciseName = "Leg Press", sets = "4", reps = "12-15"),
                ProgramExerciseTemplate(exerciseName = "Leg Curl", sets = "4", reps = "10-12"),
                ProgramExerciseTemplate(exerciseName = "Dumbbell Walking Lunge", sets = "3", reps = "12 each leg"),
                ProgramExerciseTemplate(exerciseName = "Standing Calf Raise", sets = "5", reps = "15-20")
            )
        )
        val arms = ProgramDayTemplate(
            name = "Arms",
            exercises = listOf(
                ProgramExerciseTemplate(exerciseName = "Close-Grip Barbell Bench Press", sets = "4", reps = "8-10"),
                ProgramExerciseTemplate(exerciseName = "Standing Dumbbell Curl", sets = "4", reps = "10-12"),
                ProgramExerciseTemplate(exerciseName = "Seated Dumbbell Overhead Triceps Extension", sets = "4", reps = "10-12"),
                ProgramExerciseTemplate(exerciseName = "Standing Hammer Curl", sets = "4", reps = "10-12"),
                ProgramExerciseTemplate(exerciseName = "Bench Dip", sets = "3", reps = "10-15"),
                ProgramExerciseTemplate(exerciseName = "Barbell Preacher Curl", sets = "3", reps = "10-12")
            )
        )
        
        return ProgramTemplate(
            id = "hypertrophy",
            name = "Hypertrophy Bodybuilding",
            description = "High-volume bodybuilding program designed for maximum muscle growth with isolation exercises.",
            author = "RackedUp",
            difficultyLevel = "Intermediate",
            programType = "Bodybuilding",
            durationWeeks = 8,
            daysPerWeek = 5,
            goal = "Hypertrophy",
            days = listOf(chest, back, shoulders, legs, arms),
            tags = listOf("Intermediate", "Bodybuilding", "Hypertrophy", "Volume"),
            estimatedDuration = 80
        )
    }
    
    // 6. BEGINNER - Bodyweight Circuit (3 days/week) - EASY
    private fun getBodyweightCircuitTemplate(): ProgramTemplate {
        val circuit1 = ProgramDayTemplate(
            name = "Upper Body Circuit",
            exercises = listOf(
                ProgramExerciseTemplate(exerciseName = "Push-Up", sets = "3", reps = "8-12"),
                ProgramExerciseTemplate(exerciseName = "Pike Push-Up", sets = "3", reps = "5-8"),
                ProgramExerciseTemplate(exerciseName = "Bench Dip", sets = "3", reps = "6-10"),
                ProgramExerciseTemplate(exerciseName = "Plank", sets = "3", reps = "20-30s"),
                ProgramExerciseTemplate(exerciseName = "Mountain Climber", sets = "3", reps = "10-15 each leg")
            )
        )
        val circuit2 = ProgramDayTemplate(
            name = "Lower Body Circuit",
            exercises = listOf(
                ProgramExerciseTemplate(exerciseName = "Bodyweight Squat", sets = "3", reps = "12-15"),
                ProgramExerciseTemplate(exerciseName = "Lunge", sets = "3", reps = "8-10 each leg"),
                ProgramExerciseTemplate(exerciseName = "Glute Bridge", sets = "3", reps = "12-15"),
                ProgramExerciseTemplate(exerciseName = "Standing Calf Raise", sets = "3", reps = "15-20"),
                ProgramExerciseTemplate(exerciseName = "Wall Sit", sets = "3", reps = "15-30s")
            )
        )
        val circuit3 = ProgramDayTemplate(
            name = "Full Body Circuit",
            exercises = listOf(
                ProgramExerciseTemplate(exerciseName = "Burpee", sets = "3", reps = "5-8"),
                ProgramExerciseTemplate(exerciseName = "Jump Squats", sets = "3", reps = "8-12"),
                ProgramExerciseTemplate(exerciseName = "Push-Up", sets = "3", reps = "8-12"),
                ProgramExerciseTemplate(exerciseName = "High Knees", sets = "3", reps = "20-30s"),
                ProgramExerciseTemplate(exerciseName = "Plank", sets = "3", reps = "20-40s")
            )
        )
        
        return ProgramTemplate(
            id = "bodyweight_circuit",
            name = "Bodyweight Circuit Training",
            description = "No equipment needed! Perfect for home workouts using only bodyweight exercises.",
            author = "RackedUp",
            difficultyLevel = "Beginner",
            programType = "Bodyweight",
            durationWeeks = 6,
            daysPerWeek = 3,
            goal = "General Fitness",
            days = listOf(circuit1, circuit2, circuit3),
            tags = listOf("Beginner", "Bodyweight", "Home", "Circuit"),
            estimatedDuration = 35
        )
    }
    
    // 7. INTERMEDIATE - Functional Fitness (4 days/week)
    private fun getFunctionalFitnessTemplate(): ProgramTemplate {
        val day1 = ProgramDayTemplate(
            name = "Movement & Power",
            exercises = listOf(
                ProgramExerciseTemplate(exerciseName = "Kettlebell Swing", sets = "4", reps = "15-20"),
                ProgramExerciseTemplate(exerciseName = "Box Jump", sets = "4", reps = "8-10"),
                ProgramExerciseTemplate(exerciseName = "Medicine Ball Slam", sets = "3", reps = "10-12"),
                ProgramExerciseTemplate(exerciseName = "Turkish Get-up", sets = "3", reps = "3 each side"),
                ProgramExerciseTemplate(exerciseName = "Bear Crawl", sets = "3", reps = "20-30s")
            )
        )
        val day2 = ProgramDayTemplate(
            name = "Strength & Stability",
            exercises = listOf(
                ProgramExerciseTemplate(exerciseName = "Goblet Squat", sets = "4", reps = "10-12"),
                ProgramExerciseTemplate(exerciseName = "Single-arm Row", sets = "3", reps = "8-10 each arm"),
                ProgramExerciseTemplate(exerciseName = "Overhead Carry", sets = "3", reps = "30-40s"),
                ProgramExerciseTemplate(exerciseName = "Single-leg Deadlift", sets = "3", reps = "6-8 each leg"),
                ProgramExerciseTemplate(exerciseName = "Plank", sets = "3", reps = "45-60s")
            )
        )
        val day3 = ProgramDayTemplate(
            name = "Conditioning",
            exercises = listOf(
                ProgramExerciseTemplate(exerciseName = "Burpee", sets = "4", reps = "8-10"),
                ProgramExerciseTemplate(exerciseName = "Jump Rope", sets = "4", reps = "45-60s"),
                ProgramExerciseTemplate(exerciseName = "Mountain Climber", sets = "4", reps = "15-20 each leg"),
                ProgramExerciseTemplate(exerciseName = "Battle Rope", sets = "3", reps = "30-45s"),
                ProgramExerciseTemplate(exerciseName = "Farmer's Walk", sets = "3", reps = "40-60s")
            )
        )
        val day4 = ProgramDayTemplate(
            name = "Recovery & Mobility",
            exercises = listOf(
                ProgramExerciseTemplate(exerciseName = "Cat-Cow Stretch", sets = "3", reps = "10-15"),
                ProgramExerciseTemplate(exerciseName = "Hip Circle", sets = "3", reps = "8 each direction"),
                ProgramExerciseTemplate(exerciseName = "Arm Circle", sets = "3", reps = "10 each direction"),
                ProgramExerciseTemplate(exerciseName = "Leg Swing", sets = "3", reps = "10 each leg"),
                ProgramExerciseTemplate(exerciseName = "Deep Breathing", sets = "3", reps = "60-90s")
            )
        )
        
        return ProgramTemplate(
            id = "functional_fitness",
            name = "Functional Fitness",
            description = "Real-world movement patterns combining strength, power, and conditioning for everyday activities.",
            author = "RackedUp",
            difficultyLevel = "Intermediate",
            programType = "Functional",
            durationWeeks = 8,
            daysPerWeek = 4,
            goal = "General Fitness",
            days = listOf(day1, day2, day3, day4),
            tags = listOf("Intermediate", "Functional", "Conditioning", "Movement"),
            estimatedDuration = 50
        )
    }
    
    // 8. ADVANCED - Athlete Performance (5 days/week) - HARD
    private fun getAthletePerformanceTemplate(): ProgramTemplate {
        val power = ProgramDayTemplate(
            name = "Power & Explosiveness",
            exercises = listOf(
                ProgramExerciseTemplate(exerciseName = "Barbell Clean and Jerk", sets = "5", reps = "3-5"),
                ProgramExerciseTemplate(exerciseName = "Barbell Snatch", sets = "5", reps = "2-3"),
                ProgramExerciseTemplate(exerciseName = "Box Jump", sets = "4", reps = "5-6"),
                ProgramExerciseTemplate(exerciseName = "Medicine Ball Slam", sets = "4", reps = "8-10"),
                ProgramExerciseTemplate(exerciseName = "Battle Rope", sets = "4", reps = "30s")
            )
        )
        val strength = ProgramDayTemplate(
            name = "Maximum Strength",
            exercises = listOf(
                ProgramExerciseTemplate(exerciseName = "Barbell Back Squat", sets = "5", reps = "2-4"),
                ProgramExerciseTemplate(exerciseName = "Barbell Deadlift", sets = "5", reps = "2-4"),
                ProgramExerciseTemplate(exerciseName = "Barbell Bench Press", sets = "4", reps = "3-5"),
                ProgramExerciseTemplate(exerciseName = "Standing Barbell Shoulder Press", sets = "4", reps = "3-5"),
                ProgramExerciseTemplate(exerciseName = "Pull-ups", sets = "4", reps = "5-8")
            )
        )
        val speed = ProgramDayTemplate(
            name = "Speed & Agility",
            exercises = listOf(
                ProgramExerciseTemplate(exerciseName = "Sprint", sets = "6", reps = "40-60m"),
                ProgramExerciseTemplate(exerciseName = "Lateral Shuffle", sets = "4", reps = "20-30s"),
                ProgramExerciseTemplate(exerciseName = "High Knees", sets = "4", reps = "20-30s"),
                ProgramExerciseTemplate(exerciseName = "Burpee", sets = "4", reps = "6-8"),
                ProgramExerciseTemplate(exerciseName = "Jump Rope", sets = "4", reps = "60s")
            )
        )
        val conditioning = ProgramDayTemplate(
            name = "Metabolic Conditioning",
            exercises = listOf(
                ProgramExerciseTemplate(exerciseName = "Kettlebell Swing", sets = "5", reps = "20-25"),
                ProgramExerciseTemplate(exerciseName = "Rowing Machine", sets = "4", reps = "500m"),
                ProgramExerciseTemplate(exerciseName = "Assault Bike", sets = "4", reps = "45s"),
                ProgramExerciseTemplate(exerciseName = "Mountain Climber", sets = "4", reps = "20 each leg"),
                ProgramExerciseTemplate(exerciseName = "Farmer's Walk", sets = "4", reps = "50m")
            )
        )
        val recovery = ProgramDayTemplate(
            name = "Active Recovery",
            exercises = listOf(
                ProgramExerciseTemplate(exerciseName = "Light Jog", sets = "1", reps = "15-20min"),
                ProgramExerciseTemplate(exerciseName = "Dynamic Stretching", sets = "1", reps = "10-15min"),
                ProgramExerciseTemplate(exerciseName = "Foam Rolling", sets = "1", reps = "10-15min"),
                ProgramExerciseTemplate(exerciseName = "Yoga Flow", sets = "1", reps = "15-20min"),
                ProgramExerciseTemplate(exerciseName = "Deep Breathing", sets = "3", reps = "2-3min")
            )
        )
        
        return ProgramTemplate(
            id = "athlete_performance",
            name = "Elite Athlete Performance",
            description = "High-intensity program for competitive athletes focusing on power, speed, strength, and conditioning.",
            author = "RackedUp",
            difficultyLevel = "Advanced",
            programType = "Athletic Performance",
            durationWeeks = 12,
            daysPerWeek = 5,
            goal = "Athletic Performance",
            days = listOf(power, strength, speed, conditioning, recovery),
            tags = listOf("Advanced", "Athletic", "Power", "Speed", "Elite"),
            estimatedDuration = 90
        )
    }
    
    // 9. ADVANCED - Powerlifting (4 days/week) - HARD
    private fun getPowerliftingTemplate(): ProgramTemplate {
        val squat = ProgramDayTemplate(
            name = "Squat Day",
            exercises = listOf(
                ProgramExerciseTemplate(exerciseName = "Barbell Back Squat", sets = "6", reps = "1-3"),
                ProgramExerciseTemplate(exerciseName = "Barbell Front Squat", sets = "4", reps = "4-6"),
                ProgramExerciseTemplate(exerciseName = "Romanian Deadlift", sets = "4", reps = "6-8"),
                ProgramExerciseTemplate(exerciseName = "Dumbbell Walking Lunge", sets = "3", reps = "8 each leg"),
                ProgramExerciseTemplate(exerciseName = "Plank", sets = "3", reps = "60s")
            )
        )
        val bench = ProgramDayTemplate(
            name = "Bench Day",
            exercises = listOf(
                ProgramExerciseTemplate(exerciseName = "Barbell Bench Press", sets = "6", reps = "1-3"),
                ProgramExerciseTemplate(exerciseName = "Close-Grip Barbell Bench Press", sets = "4", reps = "4-6"),
                ProgramExerciseTemplate(exerciseName = "Standing Barbell Shoulder Press", sets = "4", reps = "6-8"),
                ProgramExerciseTemplate(exerciseName = "Bench Dip", sets = "4", reps = "8-10"),
                ProgramExerciseTemplate(exerciseName = "Cable Face Pull", sets = "3", reps = "12-15")
            )
        )
        val deadlift = ProgramDayTemplate(
            name = "Deadlift Day",
            exercises = listOf(
                ProgramExerciseTemplate(exerciseName = "Barbell Deadlift", sets = "6", reps = "1-3"),
                ProgramExerciseTemplate(exerciseName = "Deficit Deadlift", sets = "4", reps = "4-6"),
                ProgramExerciseTemplate(exerciseName = "Bent-Over Barbell Row", sets = "4", reps = "6-8"),
                ProgramExerciseTemplate(exerciseName = "Pull-ups", sets = "4", reps = "6-8"),
                ProgramExerciseTemplate(exerciseName = "Leg Raise", sets = "3", reps = "10-12")
            )
        )
        val accessory = ProgramDayTemplate(
            name = "Accessory Day",
            exercises = listOf(
                ProgramExerciseTemplate(exerciseName = "Pause Squat", sets = "4", reps = "4-6"),
                ProgramExerciseTemplate(exerciseName = "Incline Barbell Bench Press", sets = "4", reps = "6-8"),
                ProgramExerciseTemplate(exerciseName = "Seated Cable Row", sets = "4", reps = "8-10"),
                ProgramExerciseTemplate(exerciseName = "Standing Barbell Shoulder Press", sets = "3", reps = "8-10"),
                ProgramExerciseTemplate(exerciseName = "Good Morning", sets = "3", reps = "10-12")
            )
        )
        
        return ProgramTemplate(
            id = "powerlifting",
            name = "Competitive Powerlifting",
            description = "Specialized program for powerlifting competition preparation focusing on squat, bench press, and deadlift.",
            author = "RackedUp",
            difficultyLevel = "Advanced",
            programType = "Powerlifting",
            durationWeeks = 16,
            daysPerWeek = 4,
            goal = "Strength",
            days = listOf(squat, bench, deadlift, accessory),
            tags = listOf("Advanced", "Powerlifting", "Competition", "Strength", "Elite"),
            estimatedDuration = 105
        )
    }
    
    // 10. ADVANCED - Bodybuilding (6 days/week) - HARD
    private fun getAdvancedBodybuildingTemplate(): ProgramTemplate {
        val chest = ProgramDayTemplate(
            name = "Chest Specialization",
            exercises = listOf(
                ProgramExerciseTemplate(exerciseName = "Barbell Bench Press", sets = "5", reps = "6-8"),
                ProgramExerciseTemplate(exerciseName = "Incline Dumbbell Bench Press", sets = "4", reps = "8-10"),
                ProgramExerciseTemplate(exerciseName = "Decline Bench Press", sets = "4", reps = "8-10"),
                ProgramExerciseTemplate(exerciseName = "Chest Fly", sets = "4", reps = "10-12"),
                ProgramExerciseTemplate(exerciseName = "Cable Crossover", sets = "4", reps = "12-15"),
                ProgramExerciseTemplate(exerciseName = "Push-Up", sets = "3", reps = "15-20")
            )
        )
        val back = ProgramDayTemplate(
            name = "Back Specialization",
            exercises = listOf(
                ProgramExerciseTemplate(exerciseName = "Barbell Deadlift", sets = "5", reps = "5-6"),
                ProgramExerciseTemplate(exerciseName = "Pull-ups", sets = "5", reps = "6-10"),
                ProgramExerciseTemplate(exerciseName = "Bent-Over Barbell Row", sets = "4", reps = "8-10"),
                ProgramExerciseTemplate(exerciseName = "Lat Pulldown", sets = "4", reps = "10-12"),
                ProgramExerciseTemplate(exerciseName = "Seated Cable Row", sets = "4", reps = "10-12"),
                ProgramExerciseTemplate(exerciseName = "Hyperextension", sets = "3", reps = "12-15")
            )
        )
        val shoulders = ProgramDayTemplate(
            name = "Shoulder Specialization",
            exercises = listOf(
                ProgramExerciseTemplate(exerciseName = "Standing Barbell Shoulder Press", sets = "5", reps = "6-8"),
                ProgramExerciseTemplate(exerciseName = "Seated Dumbbell Shoulder Press", sets = "4", reps = "8-10"),
                ProgramExerciseTemplate(exerciseName = "Dumbbell Lateral Raise", sets = "5", reps = "12-15"),
                ProgramExerciseTemplate(exerciseName = "Bent-Over Dumbbell Reverse Fly", sets = "5", reps = "12-15"),
                ProgramExerciseTemplate(exerciseName = "Barbell Upright Row", sets = "4", reps = "10-12"),
                ProgramExerciseTemplate(exerciseName = "Cable Face Pull", sets = "4", reps = "15-20")
            )
        )
        val arms = ProgramDayTemplate(
            name = "Arm Specialization",
            exercises = listOf(
                ProgramExerciseTemplate(exerciseName = "Close-Grip Barbell Bench Press", sets = "4", reps = "6-8"),
                ProgramExerciseTemplate(exerciseName = "Standing Dumbbell Curl", sets = "5", reps = "8-10"),
                ProgramExerciseTemplate(exerciseName = "Seated Dumbbell Overhead Triceps Extension", sets = "4", reps = "8-10"),
                ProgramExerciseTemplate(exerciseName = "Standing Hammer Curl", sets = "4", reps = "10-12"),
                ProgramExerciseTemplate(exerciseName = "Bench Dip", sets = "4", reps = "10-12"),
                ProgramExerciseTemplate(exerciseName = "Barbell Preacher Curl", sets = "4", reps = "10-12")
            )
        )
        val legs = ProgramDayTemplate(
            name = "Leg Specialization",
            exercises = listOf(
                ProgramExerciseTemplate(exerciseName = "Barbell Back Squat", sets = "5", reps = "6-8"),
                ProgramExerciseTemplate(exerciseName = "Romanian Deadlift", sets = "4", reps = "8-10"),
                ProgramExerciseTemplate(exerciseName = "Leg Press", sets = "5", reps = "12-15"),
                ProgramExerciseTemplate(exerciseName = "Leg Curl", sets = "4", reps = "10-12"),
                ProgramExerciseTemplate(exerciseName = "Bulgarian Split Squat", sets = "4", reps = "10 each leg"),
                ProgramExerciseTemplate(exerciseName = "Standing Calf Raise", sets = "6", reps = "15-20")
            )
        )
        val core = ProgramDayTemplate(
            name = "Core & Conditioning",
            exercises = listOf(
                ProgramExerciseTemplate(exerciseName = "Plank", sets = "4", reps = "60-90s"),
                ProgramExerciseTemplate(exerciseName = "Russian Twist", sets = "4", reps = "20-25"),
                ProgramExerciseTemplate(exerciseName = "Leg Raise", sets = "4", reps = "12-15"),
                ProgramExerciseTemplate(exerciseName = "Mountain Climber", sets = "4", reps = "20 each leg"),
                ProgramExerciseTemplate(exerciseName = "Dead Bug", sets = "3", reps = "10 each side"),
                ProgramExerciseTemplate(exerciseName = "Bicycle Crunch", sets = "3", reps = "15-20")
            )
        )
        
        return ProgramTemplate(
            id = "advanced_bodybuilding",
            name = "Advanced Bodybuilding",
            description = "High-volume specialization program for advanced bodybuilders seeking maximum muscle development.",
            author = "RackedUp",
            difficultyLevel = "Advanced",
            programType = "Bodybuilding",
            durationWeeks = 12,
            daysPerWeek = 6,
            goal = "Hypertrophy",
            days = listOf(chest, back, shoulders, arms, legs, core),
            tags = listOf("Advanced", "Bodybuilding", "Specialization", "Volume", "Elite"),
            estimatedDuration = 120
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