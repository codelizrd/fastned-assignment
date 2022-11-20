@file:Suppress("unused", "unused")

package com.fastned.assignment.solarchargingsimulator.repositories

import com.fastned.assignment.solarchargingsimulator.entities.Grid
import org.springframework.stereotype.Repository

@Repository
interface GridRepository {
    fun getAll(): List<Grid>
    fun getById(id: String): Grid?
    fun getByName(name: String): Grid?

    fun save(grid: Grid): Grid
    @Suppress("unused")
    fun delete(grid: Grid)
    fun clear()
}