package com.moniz.domain.usecases.exercises

import com.moniz.domain.errors.ErrorMapper
import com.moniz.domain.models.exercises.Exercise
import com.moniz.domain.models.exercises.ExerciseCategory
import com.moniz.domain.repositories.ExerciseRepository
import com.moniz.domain.usecases.base.SuspendedUseCase
import javax.inject.Inject

class SetupAppMemoryUseCase @Inject constructor(
    private val exerciseRepository: ExerciseRepository,
    errorMapper: ErrorMapper
) : SuspendedUseCase<Unit, Unit>(errorMapper) {

    override suspend fun buildUseCase(params: Unit) {
        exerciseRepository.insertExercises(getAllDefaultExercises())
    }

    private fun getAllDefaultExercises(): List<Exercise> = listOf(
        chestExercises(),
        backExercises(),
        legsExercises(),
        shouldersExercises(),
        bicepsExercises(),
        tricepsExercises(),
        coreExercises()
    ).flatten()

    private fun chestExercises(): List<Exercise> = listOf(
        Exercise(title = "Machine Bench Press", category = ExerciseCategory.CHEST),
        Exercise(title = "Flat Bench Barbell Press", category = ExerciseCategory.CHEST),
        Exercise(title = "Incline Bench Barbell Press", category = ExerciseCategory.CHEST),
        Exercise(title = "Flat Bench Dumbbell Press", category = ExerciseCategory.CHEST),
        Exercise(title = "Incline Bench Dumbbell Press", category = ExerciseCategory.CHEST),
        Exercise(title = "Decline Bench Dumbbell Press", category = ExerciseCategory.CHEST),
        Exercise(title = "Flat Bench Dumbbell Fly", category = ExerciseCategory.CHEST),
        Exercise(title = "Incline Bench Dumbbell Fly", category = ExerciseCategory.CHEST),
        Exercise(title = "Decline Bench Dumbbell Fly", category = ExerciseCategory.CHEST),
        Exercise(title = "Cable Fly From High", category = ExerciseCategory.CHEST),
        Exercise(title = "Cable Fly Neutral", category = ExerciseCategory.CHEST),
        Exercise(title = "Cable Fly From Low", category = ExerciseCategory.CHEST),
        Exercise(title = "Push Ups", category = ExerciseCategory.CHEST),
        Exercise(title = "Push Ups Deficit", category = ExerciseCategory.CHEST),
    )

    private fun backExercises(): List<Exercise> = listOf(
        Exercise(title = "Machine Lat PullDown Supinated Grip", category = ExerciseCategory.BACK),
        Exercise(title = "Machine Lat PullDown Pronated Grip", category = ExerciseCategory.BACK),
        Exercise(title = "Machine Lat PullDown Neutral Grip", category = ExerciseCategory.BACK),
        Exercise(title = "Machine Row Supinated", category = ExerciseCategory.BACK),
        Exercise(title = "Machine Row Neutral", category = ExerciseCategory.BACK),
        Exercise(title = "Machine Row Supinated Unilateral", category = ExerciseCategory.BACK),
        Exercise(title = "Machine Row Neutral Unilateral", category = ExerciseCategory.BACK),
        Exercise(title = "Cable Kneeling High Row", category = ExerciseCategory.BACK),
        Exercise(title = "Landmine Row", category = ExerciseCategory.BACK),
        Exercise(title = "Dumbbell Row", category = ExerciseCategory.BACK),
        Exercise(title = "Pull Ups", category = ExerciseCategory.BACK),
    )

    private fun legsExercises(): List<Exercise> = listOf(
        Exercise(title = "Machine Leg Press", category = ExerciseCategory.LEGS),
        Exercise(title = "Machine Leg Press 45", category = ExerciseCategory.LEGS),
        Exercise(title = "Machine Leg Curl Seated", category = ExerciseCategory.LEGS),
        Exercise(title = "Machine Leg Curl Standing", category = ExerciseCategory.LEGS),
        Exercise(title = "Machine Pendulum Press", category = ExerciseCategory.LEGS),
        Exercise(title = "Machine Abductors", category = ExerciseCategory.LEGS),
        Exercise(title = "Machine Hip Thrust", category = ExerciseCategory.LEGS),
        Exercise(title = "Machine Adductors", category = ExerciseCategory.LEGS),
        Exercise(title = "Machine Abductors with Glute", category = ExerciseCategory.LEGS),
        Exercise(title = "Barbell Squat", category = ExerciseCategory.LEGS),
        Exercise(title = "Barbell Lunge", category = ExerciseCategory.LEGS),
        Exercise(title = "Bulgarian Deadlift", category = ExerciseCategory.LEGS),
        Exercise(title = "Walking Lunge", category = ExerciseCategory.LEGS),
        Exercise(title = "Machine Leg Extension", category = ExerciseCategory.LEGS),
        Exercise(title = "Machine Single Leg Extension", category = ExerciseCategory.LEGS),
        Exercise(title = "Deadlift", category = ExerciseCategory.LEGS),
        Exercise(title = "Romanian Deadlift", category = ExerciseCategory.LEGS),
    )

    private fun shouldersExercises(): List<Exercise> = listOf(
        Exercise(title = "Machine Shoulder Press", category = ExerciseCategory.SHOULDERS),
        Exercise(title = "Machine Lateral Raise", category = ExerciseCategory.SHOULDERS),
        Exercise(title = "Cable Unilateral Lateral Raise", category = ExerciseCategory.SHOULDERS),
        Exercise(title = "Cable Rear Delt Fly", category = ExerciseCategory.SHOULDERS),
        Exercise(title = "Cable Front Raise", category = ExerciseCategory.SHOULDERS),
    )

    private fun bicepsExercises(): List<Exercise> = listOf(
        Exercise(title = "Machine Preacher Curl", category = ExerciseCategory.BICEPS),
        Exercise(title = "Cable Curl", category = ExerciseCategory.BICEPS),
        Exercise(title = "Cable Unilateral Curl", category = ExerciseCategory.BICEPS),
        Exercise(title = "Machine Preacher Curl Unilateral", category = ExerciseCategory.BICEPS),
    )

    private fun tricepsExercises(): List<Exercise> = listOf(
        Exercise(title = "Cable Pushdown", category = ExerciseCategory.TRICEPS),
        Exercise(title = "Cable Overhead Extension", category = ExerciseCategory.TRICEPS),
        Exercise(title = "Cable Overhead Extension Unilateral", category = ExerciseCategory.TRICEPS),
    )

    private fun coreExercises(): List<Exercise> = listOf(
        Exercise(title = "Bench Crunch", category = ExerciseCategory.ABS),
        Exercise(title = "Bench Leg Raise", category = ExerciseCategory.ABS),
    )
}
