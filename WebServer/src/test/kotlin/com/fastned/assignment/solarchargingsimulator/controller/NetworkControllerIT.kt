package com.fastned.assignment.solarchargingsimulator.controller

import com.fastned.assignment.solarchargingsimulator.entities.Grid
import com.fastned.assignment.solarchargingsimulator.repositories.GridRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import strikt.api.expectThat
import strikt.assertions.isEqualTo

@SpringBootTest
@AutoConfigureMockMvc
internal class NetworkControllerIT {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var gridRepository: GridRepository

    @AfterEach
    fun afterEach() {
        gridRepository.clear()
    }

    @Test
    fun `loadNetwork loads file`() {
        val json = """
            [
              {"name": "Groningen", "age": 473},
              {"name": "Amsterdam", "age": 854},
              {"name": "Maastricht", "age": 253}
            ]
        """.trimIndent()

        this.mockMvc.perform(
            post("/solar-simulator/load")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andDo(print())
            .andExpect(status().isResetContent)

        // Check saved in repo
        expectThat(gridRepository.getAll().size).isEqualTo(3)
    }

    @Test
    fun `loadNetwork rejects invalid`() {
        val json = "[{\"age\": 666}]"

        this.mockMvc.perform(
            post("/solar-simulator/load")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andDo(print())
            .andExpect(status().is4xxClientError)
    }

    @Test
    fun `getNetworkAtElapsedDays returns network`() {
        gridRepository.save(Grid("Amsterdam", 100))

        this.mockMvc.perform(
            get("/solar-simulator/network/100")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.[0].name").value("Amsterdam"))
            .andExpect(jsonPath("$.[0].age").value(200))
    }

    @Test
    fun `getNetworkTotalOutputAtElapsedDays returns total output`() {
        gridRepository.save(Grid("Amsterdam", 100))

        this.mockMvc.perform(
                get("/solar-simulator/output/0")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
        )
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.total-output-in-kwh").value(2244))
            .andExpect(jsonPath("$.daily-output-in-kwh").value(54.71908425595816))
            .andExpect(jsonPath("$.peak-output-in-kw").value(20.0))
    }

    @Test
    fun `getNetworkTotalOutputAtElapsedDays returns number of daily outputs`() {
        gridRepository.save(Grid("Amsterdam", 100))

        this.mockMvc.perform(
            get("/solar-simulator/output/daily/10")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.length()").value(10))
    }
}