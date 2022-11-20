package com.fastned.assignment.solarchargingsimulator

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SolarChargingSimulatorWebServerApplication

fun main(args: Array<String>) {
	runApplication<SolarChargingSimulatorWebServerApplication>(*args)
}
