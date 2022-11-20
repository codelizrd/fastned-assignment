package com.fastned.assignment.solarchargingsimulator

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["com.fastned.assignment.solarchargingsimulator"])
class SolarChargingSimulatorCommandLineApplication

fun main(args: Array<String>) {
	runApplication<SolarChargingSimulatorCommandLineApplication>(*args)
}
