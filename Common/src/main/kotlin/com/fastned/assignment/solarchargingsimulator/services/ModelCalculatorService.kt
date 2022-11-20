package com.fastned.assignment.solarchargingsimulator.services

import org.springframework.stereotype.Service

@Service
interface ModelCalculatorService {
    /**
     * Calculate the output from start of a Grid to the given day in kilowatt
     */
    fun calculateSumOutputInKwh(gridAgeInDays: Int): Double

    /**
     * Calculate the output on a given day in kilowatt
     */
    fun calculateOutputInKwh(gridAgeInDays: Int): Double

    /**
     * Calculate the peak output on a given day in kilowatt
     */
    fun calculatePeakOutputInKw(gridAgeInDays: Int): Double

    fun isInstalling(gridAgeInDays: Int): Boolean

    fun isDecommissioned(gridAgeInDays: Int): Boolean

    @Suppress("unused")
    fun isActive(gridAgeInDays: Int): Boolean {
        return !isInstalling(gridAgeInDays) && !isDecommissioned(gridAgeInDays)
    }
}