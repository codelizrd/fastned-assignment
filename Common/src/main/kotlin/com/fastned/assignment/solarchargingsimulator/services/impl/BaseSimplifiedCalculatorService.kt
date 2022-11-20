package com.fastned.assignment.solarchargingsimulator.services.impl;

import com.fastned.assignment.solarchargingsimulator.services.ModelCalculatorService

abstract class BaseSimplifiedCalculatorService:  ModelCalculatorService {

    companion object {
        const val DAYS_IN_YEAR = 365
        const val POWER_OUTPUT_DEGRADATION_FACTOR_PER_YEAR = 0.005

        const val POWER_OUTPUT_DEGRADATION_FACTOR_PER_DAY = POWER_OUTPUT_DEGRADATION_FACTOR_PER_YEAR / DAYS_IN_YEAR

        const val HOURS_OF_SUN_PER_DAY = 1000.0 / DAYS_IN_YEAR
        const val GRID_PEAK_POWER = 20

        const val GRID_INSTALLATION_DAYS = 60
        const val GRID_DECOMMISSION_DAYS = 25 * DAYS_IN_YEAR
    }

    override fun isInstalling(gridAgeInDays: Int): Boolean {
        return gridAgeInDays < GRID_INSTALLATION_DAYS
    }

    override fun isDecommissioned(gridAgeInDays: Int): Boolean {
        return gridAgeInDays >= GRID_DECOMMISSION_DAYS
    }

}
