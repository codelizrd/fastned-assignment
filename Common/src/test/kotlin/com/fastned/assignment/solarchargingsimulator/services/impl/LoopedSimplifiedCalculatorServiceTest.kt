package com.fastned.assignment.solarchargingsimulator.services.impl

import org.junit.jupiter.api.Test
import strikt.api.*
import strikt.assertions.*

class LoopedSimplifiedCalculatorServiceTest {

    private val service = LoopedSimplifiedCalculatorService()

    @Test
    fun `calculatePeakOutputInKw for day 0` () {
        val outputOnDay = service.calculatePeakOutputInKw(0)
        expectThat(outputOnDay).isEqualTo(0.0)
    }

    @Test
    fun `calculatePeakOutputInKw for day 100` () {
        val outputOnDay = service.calculatePeakOutputInKw(100)
        expectThat(outputOnDay).isEqualTo(19.972602, 0.000001)
    }

    @Test
    fun `calculateOutputInKwh accounts for first installation day`() {
        val outputOnDay = service.calculateOutputInKwh(0)
        expectThat(outputOnDay).isEqualTo(0.0)
    }

    @Test
    fun `calculateOutputInKwh accounts for last installation day`() {
        val day = BaseSimplifiedCalculatorService.GRID_INSTALLATION_DAYS - 1
        val outputOnDay = service.calculateOutputInKwh(day)
        expectThat(outputOnDay).isEqualTo(0.0)
    }

    @Test
    fun `calculateOutputInKwh starts after installation`() {
        val day = BaseSimplifiedCalculatorService.GRID_INSTALLATION_DAYS
        val outputOnDay = service.calculateOutputInKwh(day)
        expectThat(outputOnDay).isEqualTo(54.749483, 0.000001)
    }

    @Test
    fun `calculateOutputInKwh stops on breakdown`() {
        val day = BaseSimplifiedCalculatorService.GRID_DECOMMISSION_DAYS
        val outputOnDay = service.calculateOutputInKwh(day)
        expectThat(outputOnDay).isEqualTo(0.0)
    }

    @Test
    fun `calculateSumOutputInKwh none during start`() {
        val day = 0
        val outputOnDay = service.calculateSumOutputInKwh(day)
        expectThat(outputOnDay).isEqualTo(0.0)
    }

    @Test
    fun `calculateSumOutputInKwh none on last day of installation`() {
        val day = BaseSimplifiedCalculatorService.GRID_INSTALLATION_DAYS - 1
        val outputOnDay = service.calculateSumOutputInKwh(day)
        expectThat(outputOnDay).isEqualTo(0.0)
    }

    @Test
    fun `calculateSumOutputInKwh on first day after installation finished`() {
        val day = BaseSimplifiedCalculatorService.GRID_INSTALLATION_DAYS
        val outputOnDay = service.calculateSumOutputInKwh(day)
        expectThat(outputOnDay).isEqualTo(54.749483, 0.000001)
    }

    @Test
    fun `calculateSumOutputInKwh five days after installation finished`() {
        val day = BaseSimplifiedCalculatorService.GRID_INSTALLATION_DAYS + 4
        val outputOnDay = service.calculateSumOutputInKwh(day)
        expectThat(outputOnDay).isEqualTo(273.7399136, 0.000001)
    }

    @Test
    fun `calculateSumOutputInKwh after breakdown`() {
        val day = BaseSimplifiedCalculatorService.GRID_DECOMMISSION_DAYS
        val outputOnDay = service.calculateSumOutputInKwh(day)
        expectThat(outputOnDay).isEqualTo(465467.082004, 0.000001)
    }

    @Test
    fun `calculateSumOutputInKwh one year after breakdown`() {
        val day = BaseSimplifiedCalculatorService.GRID_DECOMMISSION_DAYS + 365
        val outputOnDay = service.calculateSumOutputInKwh(day)
        expectThat(outputOnDay).isEqualTo(465467.082004, 0.000001)
    }


}