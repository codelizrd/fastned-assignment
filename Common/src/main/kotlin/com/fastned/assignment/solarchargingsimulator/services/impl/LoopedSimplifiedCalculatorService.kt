package com.fastned.assignment.solarchargingsimulator.services.impl

import org.springframework.stereotype.Service

/**
 * Calculator based on a simplified model:
 * - Each solar grid produces an optimal power output of 20*(1-(D/365*0,005))kW (D=age in days)
 * - 1000 hours of production per year
 * - age=0 day of placement, after 60 days production begins, after 25 years breaks down
 *
 * As the output of a grid slowly reduces we calculate the sum of the output for each day in a
 * given period (hence: looped)
 */
@Service
class LoopedSimplifiedCalculatorService: BaseSimplifiedCalculatorService() {

    override fun calculateSumOutputInKwh(gridAgeInDays: Int): Double {
        require(gridAgeInDays >= 0) { "gridAgeInDays must be a positive number, was $gridAgeInDays" }

        val calculateForDays = 0.rangeTo(gridAgeInDays) // The 'loop'.. I <3 Kotlin :)
        return calculateForDays.sumOf { calculateOutputInKwh(it) }
    }

    override fun calculateOutputInKwh(gridAgeInDays: Int): Double {
        require(gridAgeInDays >= 0) { "gridAgeInDays must be a positive number, was $gridAgeInDays" }

        return when {
            isInstalling(gridAgeInDays) -> 0.0
            isDecommissioned(gridAgeInDays) -> 0.0
            else -> calculatePeakOutputInKw(gridAgeInDays) * HOURS_OF_SUN_PER_DAY
        }
    }

    override fun calculatePeakOutputInKw(gridAgeInDays: Int): Double {
        require(gridAgeInDays >= 0) { "gridAgeInDays must be a positive number, was $gridAgeInDays" }

        return when {
            isInstalling(gridAgeInDays) -> 0.0
            isDecommissioned(gridAgeInDays) -> 0.0
            else -> {
                val degradation = gridAgeInDays * POWER_OUTPUT_DEGRADATION_FACTOR_PER_DAY
                GRID_PEAK_POWER * (1.0 - degradation)
            }
        }
    }

}