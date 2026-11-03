package com.moniz.repository.mappers

import com.moniz.data_db.entities.tables.TrainingEntity
import com.moniz.domain.models.training.Training

internal fun Training.toEntity(): TrainingEntity {
    return TrainingEntity(
        title = this.title,
        startingDay = this.startingDay,
        mondayEntry = this.mondayEntry?.toEntity(),
        tuesdayEntry = this.tuesdayEntry?.toEntity(),
        wednesdayEntry = this.wednesdayEntry?.toEntity(),
        thursdayEntry = this.thursdayEntry?.toEntity(),
        fridayEntry = this.fridayEntry?.toEntity(),
        saturdayEntry = this.saturdayEntry?.toEntity(),
        sundayEntry = this.sundayEntry?.toEntity(),
    )
}

internal fun TrainingEntity.toDomain(): Training {
    return Training(
        title = this.title,
        startingDay = this.startingDay,
        mondayEntry = this.mondayEntry?.toDomain(),
        tuesdayEntry = this.tuesdayEntry?.toDomain(),
        wednesdayEntry = this.wednesdayEntry?.toDomain(),
        thursdayEntry = this.thursdayEntry?.toDomain(),
        fridayEntry = this.fridayEntry?.toDomain(),
        saturdayEntry = this.saturdayEntry?.toDomain(),
        sundayEntry = this.sundayEntry?.toDomain(),
    )
}
