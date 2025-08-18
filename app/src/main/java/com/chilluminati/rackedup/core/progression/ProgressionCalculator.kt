package com.chilluminati.rackedup.core.progression

import com.chilluminati.rackedup.data.database.entity.ExerciseSet
import com.chilluminati.rackedup.data.database.entity.ProgramExercise
import kotlin.math.round

/**
 * Calculator for different progression models used in training programs
 */
object ProgressionCalculator {
    
    /**
     * Calculate the next weight/reps for an exercise based on progression scheme
     */
    fun calculateNextProgression(
        exercise: ProgramExercise,
        currentWeight: Double?,
        currentReps: Int?,
        lastSets: List<ExerciseSet>,
        oneRepMax: Double?
    ): ProgressionResult {
        return when (exercise.progressionScheme) {
            "Linear" -> calculateLinearProgression(exercise, currentWeight)
            "Double Progression" -> calculateDoubleProgression(exercise, currentWeight, currentReps, lastSets)
            "Percentage" -> calculatePercentageProgression(exercise, oneRepMax)
            "RPE" -> calculateRPEProgression(exercise, currentWeight, currentReps, lastSets)
            else -> ProgressionResult.NoProgression
        }
    }
    
    /**
     * Linear progression - add weight every session
     */
    private fun calculateLinearProgression(
        exercise: ProgramExercise,
        currentWeight: Double?
    ): ProgressionResult {
        val increment = exercise.progressionIncrement ?: 2.5
        val newWeight = (currentWeight ?: 0.0) + increment
        
        return ProgressionResult.WeightIncrease(
            newWeight = newWeight,
            reason = "Linear progression: +${increment}kg"
        )
    }
    
    /**
     * Double progression - increase reps first, then weight
     */
    private fun calculateDoubleProgression(
        exercise: ProgramExercise,
        currentWeight: Double?,
        currentReps: Int?,
        lastSets: List<ExerciseSet>
    ): ProgressionResult {
        val targetReps = parseRepRange(exercise.reps ?: "8-12")
        val lastRecordedReps = lastSets.lastOrNull()?.reps
        val currentActualReps = currentReps ?: lastRecordedReps ?: targetReps.min
        
        // If we hit the top of the rep range, increase weight and drop to bottom of range
        return if (currentActualReps >= targetReps.max) {
            val increment = exercise.progressionIncrement ?: 2.5
            val newWeight = (currentWeight ?: 0.0) + increment
            
            ProgressionResult.WeightIncrease(
                newWeight = newWeight,
                newReps = targetReps.min,
                reason = "Hit ${targetReps.max} reps, increasing weight and dropping to ${targetReps.min} reps"
            )
        } else {
            // Increase reps by 1
            ProgressionResult.RepIncrease(
                newReps = currentActualReps + 1,
                reason = "Add 1 rep (${currentActualReps + 1}/${targetReps.max})"
            )
        }
    }
    
    /**
     * Percentage-based progression - weight based on % of 1RM
     */
    private fun calculatePercentageProgression(
        exercise: ProgramExercise,
        oneRepMax: Double?
    ): ProgressionResult {
        val percentage = exercise.weightPercentage ?: 75.0
        val calculatedWeight = (oneRepMax ?: 100.0) * (percentage / 100.0)
        
        return ProgressionResult.PercentageBased(
            weight = roundToNearestPlate(calculatedWeight),
            percentage = percentage,
            reason = "${percentage.toInt()}% of 1RM"
        )
    }
    
    /**
     * RPE-based progression - adjust based on perceived exertion
     */
    private fun calculateRPEProgression(
        exercise: ProgramExercise,
        currentWeight: Double?,
        currentReps: Int?,
        lastSets: List<ExerciseSet>
    ): ProgressionResult {
        val targetRPE = exercise.rpeTarget ?: 8
        
        // Get the last recorded RPE for this exercise
        val lastRPE = lastSets.lastOrNull()?.rpe
        
        return when {
            lastRPE == null -> ProgressionResult.NoProgression
            lastRPE < targetRPE - 1 -> {
                // RPE too low, increase weight
                val increment = exercise.progressionIncrement ?: 2.5
                val newWeight = (currentWeight ?: 0.0) + increment
                ProgressionResult.WeightIncrease(
                    newWeight = newWeight,
                    reason = "RPE ${lastRPE} < target ${targetRPE}, increasing weight"
                )
            }
            lastRPE > targetRPE + 1 -> {
                // RPE too high, decrease weight
                val decrement = exercise.progressionIncrement ?: 2.5
                val newWeight = maxOf((currentWeight ?: 0.0) - decrement, 0.0)
                ProgressionResult.WeightDecrease(
                    newWeight = newWeight,
                    reason = "RPE ${lastRPE} > target ${targetRPE}, decreasing weight"
                )
            }
            else -> {
                // RPE in target range, maintain current weight (include reps context if available)
                val repsContext = currentReps?.let { ", reps=$it" } ?: ""
                ProgressionResult.Maintain(
                    reason = "RPE ${lastRPE} at target ${targetRPE}${repsContext}, maintaining current weight"
                )
            }
        }
    }
    
    /**
     * Calculate estimated 1RM from a set
     */
    fun calculateOneRepMax(weight: Double, reps: Int, rpe: Int? = null): Double {
        return when {
            reps == 1 -> weight
            rpe != null -> calculateOneRepMaxWithRPE(weight, reps, rpe)
            else -> calculateOneRepMaxEpley(weight, reps)
        }
    }
    
    /**
     * Epley formula for 1RM estimation
     */
    private fun calculateOneRepMaxEpley(weight: Double, reps: Int): Double {
        if (reps == 1) return weight
        return weight * (1 + (reps / 30.0))
    }
    
    /**
     * RPE-based 1RM calculation
     */
    private fun calculateOneRepMaxWithRPE(weight: Double, reps: Int, rpe: Int): Double {
        // RPE to percentage conversion table
        val rpePercentages = mapOf(
            10 to 1.0,   // Max effort
            9 to 0.955,  // 1 rep left
            8 to 0.91,   // 2-3 reps left
            7 to 0.865,  // 4+ reps left
            6 to 0.82,
            5 to 0.775,
            4 to 0.73,
            3 to 0.685,
            2 to 0.64,
            1 to 0.595
        )
        
        val percentage = rpePercentages[rpe] ?: 0.85
        val adjustedPercentage = percentage - ((reps - 1) * 0.025) // Adjust for reps
        
        return weight / adjustedPercentage
    }
    
    /**
     * Calculate training weight for next session
     */
    fun calculateTrainingWeight(
        oneRepMax: Double,
        targetPercentage: Double,
        week: Int = 1,
        progressionType: String = "Linear"
    ): Double {
        val baseWeight = oneRepMax * (targetPercentage / 100.0)
        
        return when (progressionType) {
            "Linear" -> {
                // Add 2.5kg per week for linear progression
                baseWeight + ((week - 1) * 2.5)
            }
            "Wave" -> {
                // Wave loading - increase intensity every 3 weeks
                val waveAdjustment = when (week % 3) {
                    1 -> 0.0
                    2 -> oneRepMax * 0.025  // +2.5%
                    0 -> oneRepMax * 0.05   // +5%
                    else -> 0.0
                }
                baseWeight + waveAdjustment
            }
            "Undulating" -> {
                // Daily undulating periodization
                val dayAdjustment = when (week % 7) {
                    1, 3, 5 -> oneRepMax * 0.05   // Higher intensity days
                    2, 4, 6 -> oneRepMax * -0.025 // Lower intensity days
                    0 -> 0.0 // Rest day
                    else -> 0.0
                }
                baseWeight + dayAdjustment
            }
            else -> baseWeight
        }
    }
    
    /**
     * Auto-deload calculation
     */
    fun shouldDeload(
        recentSets: List<ExerciseSet>,
        weeksInProgram: Int
    ): DeloadRecommendation {
        val failedSets = recentSets.count { set ->
            val targetReps = parseRepRange(set.reps?.toString() ?: "8").min
            (set.reps ?: 0) < targetReps
        }
        
        val totalSets = recentSets.size
        val failureRate = if (totalSets > 0) failedSets.toDouble() / totalSets else 0.0
        
        return when {
            failureRate > 0.4 -> DeloadRecommendation.Immediate("High failure rate: ${(failureRate * 100).toInt()}%")
            weeksInProgram % 4 == 0 -> DeloadRecommendation.Scheduled("Scheduled deload week")
            weeksInProgram > 8 && failureRate > 0.2 -> DeloadRecommendation.Recommended("Program fatigue detected")
            else -> DeloadRecommendation.None
        }
    }
    
    /**
     * Calculate deload weight (typically 80-90% of current)
     */
    fun calculateDeloadWeight(currentWeight: Double, deloadPercentage: Double = 0.85): Double {
        return roundToNearestPlate(currentWeight * deloadPercentage)
    }
    
    /**
     * Round weight to nearest standard plate increment
     */
    private fun roundToNearestPlate(weight: Double): Double {
        val increment = when {
            weight < 40 -> 1.25 // Small increments for light weights
            weight < 100 -> 2.5  // Standard increment
            else -> 5.0          // Larger increments for heavy weights
        }
        
        return round(weight / increment) * increment
    }
    
    /**
     * Parse rep range string like "8-12" or "5"
     */
    private fun parseRepRange(reps: String): RepRange {
        return try {
            if (reps.contains("-")) {
                val parts = reps.split("-")
                RepRange(parts[0].toInt(), parts[1].toInt())
            } else {
                val repCount = reps.toInt()
                RepRange(repCount, repCount)
            }
        } catch (e: Exception) {
            RepRange(8, 12) // Default fallback
        }
    }
}

/**
 * Result of progression calculation
 */
sealed class ProgressionResult {
    data class WeightIncrease(
        val newWeight: Double,
        val newReps: Int? = null,
        val reason: String
    ) : ProgressionResult()
    
    data class WeightDecrease(
        val newWeight: Double,
        val reason: String
    ) : ProgressionResult()
    
    data class RepIncrease(
        val newReps: Int,
        val reason: String
    ) : ProgressionResult()
    
    data class PercentageBased(
        val weight: Double,
        val percentage: Double,
        val reason: String
    ) : ProgressionResult()
    
    data class Maintain(
        val reason: String
    ) : ProgressionResult()
    
    object NoProgression : ProgressionResult()
}

/**
 * Deload recommendation
 */
sealed class DeloadRecommendation {
    data class Immediate(val reason: String) : DeloadRecommendation()
    data class Scheduled(val reason: String) : DeloadRecommendation()
    data class Recommended(val reason: String) : DeloadRecommendation()
    object None : DeloadRecommendation()
}

/**
 * Rep range data class
 */
data class RepRange(val min: Int, val max: Int)
