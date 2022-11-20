package com.fastned.assignment.solarchargingsimulator.services.impl

import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Service

/**
 * Calculator based on a simplified model:
 * - Each solar grid produces an optimal power output of 20*(1-(D/365*0,005))kW (D=age in days)
 * - 1000 hours of production per year
 * - age=0 day of placement, after 60 days production begins, after 25 years breaks down
 *
 * This implementation uses integral calculus to calculate the total output in a given period.
 *   => because the calculation is based in on integration the deterioration of the panels
 *      becomes more graduate over the day. Note that we integrate over the whole day - so
 *      we assume the production is over the whole day; in reality it is more discrete around
 *      noon.
 *
 * The function to calculate the power on a given day for a grid can be divided by 20; when
 * to calculate over a period simply multiply again by 20, and the number of optimal sun per
 * day:
 *
 *  (1-(D/365*0.005))

 * Which can be simplified to (f):
 *  1 - (D / 73000)
 *
 * Primitive function (F):
 *  D - (D^2 / 146000) + C
 *
 * To calculate output over a given period:
 *  (F(upperBound) - F(lowerBound)) * 20 * number optimal sun per day
 *
 */
@Service
@Primary
class IntegralSimplifiedCalculatorService: BaseSimplifiedCalculatorService() {

    companion object {
        const val DETERIORATION_PRIMITIVE_FACTOR =
            (DAYS_IN_YEAR.toDouble() / POWER_OUTPUT_DEGRADATION_FACTOR_PER_YEAR) * 2
    }

    override fun calculateSumOutputInKwh(gridAgeInDays: Int): Double {
        require(gridAgeInDays >= 0) { "gridAgeInDays must be a positive number, was $gridAgeInDays" }

        if (gridAgeInDays < GRID_INSTALLATION_DAYS) return 0.0

        val lowerBound = GRID_INSTALLATION_DAYS // Only starts producing then
        val upperBound = (gridAgeInDays + 1).coerceAtMost(GRID_DECOMMISSION_DAYS)

        return calculateIntegral(upperBound, lowerBound)
    }

    override fun calculateOutputInKwh(gridAgeInDays: Int): Double {
        require(gridAgeInDays >= 0) { "gridAgeInDays must be a positive number, was $gridAgeInDays" }
        
        return when {
            isInstalling(gridAgeInDays) -> 0.0
            isDecommissioned(gridAgeInDays) -> 0.0
            else -> calculateIntegral(gridAgeInDays + 1, gridAgeInDays)
        }
    }

    override fun calculatePeakOutputInKw(gridAgeInDays: Int): Double {
        require(gridAgeInDays >= 0) { "gridAgeInDays must be a positive number, was $gridAgeInDays" }
        
        return when {
            isInstalling(gridAgeInDays) -> 0.0
            isDecommissioned(gridAgeInDays) -> 0.0
            else -> 20*(1-(gridAgeInDays/365*0.005)) // See requirement 1
        }
    }

    private fun calculateIntegral(upperBound: Int, lowerBound: Int): Double {
        return (fromPrimitive(upperBound) - fromPrimitive(lowerBound)) * GRID_PEAK_POWER * HOURS_OF_SUN_PER_DAY
    }

    private fun fromPrimitive(value: Int): Double {
        val l = value.toLong() // Integer might overflow
        return l - ((l * l) / DETERIORATION_PRIMITIVE_FACTOR) // See comment above; primitive function of model
    }




}