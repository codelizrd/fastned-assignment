package com.fastned.assignment.solarchargingsimulator.controller

import com.fastned.assignment.solarchargingsimulator.entities.Grid
import com.fastned.assignment.solarchargingsimulator.repositories.GridRepository
import com.fastned.assignment.solarchargingsimulator.rest.api.LoadApi
import com.fastned.assignment.solarchargingsimulator.rest.api.NetworkApi
import com.fastned.assignment.solarchargingsimulator.rest.api.OutputApi
import com.fastned.assignment.solarchargingsimulator.rest.model.GridDto
import com.fastned.assignment.solarchargingsimulator.rest.model.GridWithOutputDto
import com.fastned.assignment.solarchargingsimulator.rest.model.NetworkOutputDto
import com.fastned.assignment.solarchargingsimulator.services.ModelCalculatorService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestBody
import kotlin.math.roundToInt

@Controller
class NetworkController(
    private val gridRepository: GridRepository,
    private val calculatorService: ModelCalculatorService
) : LoadApi, NetworkApi, OutputApi {
    override fun loadNetwork(@RequestBody gridDto: List<GridDto>): ResponseEntity<Unit> {
        gridRepository.clear()

        gridDto.forEach {
            gridRepository.save(Grid(name = it.name, age = it.age))
        }

        return ResponseEntity.status(HttpStatus.RESET_CONTENT).body(null)
    }

    override fun getNetworkAtElapsedDays(elapsedDays: Int): ResponseEntity<List<GridWithOutputDto>> {
        val elapsedNetwork = gridRepository.getAll()
            .map {
                GridWithOutputDto(
                    name = it.name,
                    age = it.age + elapsedDays,
                    status = getGridDtoState(it.age + elapsedDays),
                    totalOutputInKwh = calculatorService.calculateSumOutputInKwh(it.age + elapsedDays).roundToInt(),
                    dailyOutputInKwh = calculatorService.calculateOutputInKwh(it.age + elapsedDays),
                    peakOutputInKw = calculatorService.calculatePeakOutputInKw(it.age + elapsedDays)
                )
            }

        return ResponseEntity.ok(elapsedNetwork)
    }

    private fun getGridDtoState(onDay: Int): GridWithOutputDto.Status {
        return when {
            onDay < 0 -> GridWithOutputDto.Status.PLANNED
            calculatorService.isInstalling(onDay) -> GridWithOutputDto.Status.INSTALLING
            calculatorService.isDecommissioned(onDay) -> GridWithOutputDto.Status.DECOMMISSIONED
            else -> GridWithOutputDto.Status.PRODUCTION
        }
    }

    override fun getNetworkTotalOutputAtElapsedDays(elapsedDays: Int): ResponseEntity<NetworkOutputDto> {
        val network = gridRepository.getAll()

        val networkOutput = NetworkOutputDto(
            totalOutputInKwh = network.sumOf {
                calculatorService.calculateSumOutputInKwh(it.age + elapsedDays).roundToInt()
            },
            dailyOutputInKwh = network.sumOf {
                calculatorService.calculateOutputInKwh(it.age + elapsedDays)
            },
            peakOutputInKw = network.sumOf {
                calculatorService.calculatePeakOutputInKw(it.age + elapsedDays)
            },
        )

        return ResponseEntity.ok(networkOutput)
    }

    override fun getNetworkDailyOutputAtElapsedDays(elapsedDays: Int, stepDays: Int?): ResponseEntity<List<Double>> {
        val grids = gridRepository.getAll()

        val dailyOutputs = (1).rangeTo(elapsedDays)
            .step(stepDays ?: 1)
            .map { day ->
                grids.sumOf { grid -> calculatorService.calculateOutputInKwh(grid.age + day) }
            }

        return ResponseEntity.ok(dailyOutputs)
    }

}