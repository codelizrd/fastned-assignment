package com.fastned.assignment.solarchargingsimulator.command

import com.fasterxml.jackson.databind.ObjectMapper
import com.fastned.assignment.solarchargingsimulator.entities.Grid
import com.fastned.assignment.solarchargingsimulator.services.ModelCalculatorService
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import java.io.File
import kotlin.math.roundToInt

@Component
class CommandLineCalculatorCommand(
    val objectMapper: ObjectMapper,
    val calculatorService: ModelCalculatorService
    ): CommandLineRunner {

    override fun run(vararg args: String?) {
        if (args.size != 2) {
            println("Usage: <inputFile> <elapsedTimeInDays>")
            return
        }

        try {
            val file = File(args[0]!!)
            require(file.isFile) { "Provided <inputFile> is not a file" }

            val elapsedDays = args[1]!!.toInt()
            require(elapsedDays >= 0) { "Provided <elapsedTimeInDays> must be a positive number" }

            process(file, elapsedDays)

        } catch (e: NumberFormatException) {
            println("Provided <elapsedTimeInDays> is not a valid number")
        } catch (e: IllegalArgumentException) {
            println(e.message)
        }
    }

    fun process(file: File, elapsedDays: Int) {
        val grids = readGridsFromFile(file)
        val output = grids.sumOf { calculatorService.calculateSumOutputInKwh(it.age + elapsedDays) }

        println("Produced: ${output.roundToInt()} kWh")
        println("Network:")
        grids.forEach {
            println("\t${it.name} in use for ${it.age + elapsedDays} days")
        }
    }

    fun readGridsFromFile(file: File): List<Grid> {
        val tree = objectMapper.readTree(file)
        require (tree.isArray) { "Input file should be a JSON array of grid nodes" }

        return tree.map {
            require (it.has("name")) { "Grid nodes must have a name" }
            require (it.has("age")) { "Grid nodes must have an age" }

            Grid(it.get("name").textValue(), it.get("age").intValue())
        }
    }
}

