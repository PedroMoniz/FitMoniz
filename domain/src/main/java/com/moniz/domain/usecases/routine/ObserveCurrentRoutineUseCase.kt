package com.moniz.domain.usecases.routine

import com.moniz.domain.errors.ErrorMapper
import com.moniz.domain.usecases.base.DataResultFlowUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveCurrentRoutineUseCase @Inject constructor(
    errorMapper: ErrorMapper,
    private val agenciesRepository: AgenciesRepository
) : DataResultFlowUseCase<None, List<Agency>>(errorMapper) {

    override fun buildUseCase(params: None): Flow<DataResult<List<Agency>>> {
        return agenciesRepository.observeAgencies()
    }
}
