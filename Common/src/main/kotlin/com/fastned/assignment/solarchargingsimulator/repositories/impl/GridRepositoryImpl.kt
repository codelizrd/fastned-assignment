package com.fastned.assignment.solarchargingsimulator.repositories.impl

import com.fastned.assignment.solarchargingsimulator.entities.Grid
import com.fastned.assignment.solarchargingsimulator.repositories.GridRepository
import org.springframework.stereotype.Repository

@Repository
class GridRepositoryImpl: GridRepository {

    private val memoryTable: MutableMap<String, Grid> = mutableMapOf()

    override fun getAll(): List<Grid> {
        return memoryTable.values.sortedBy { it.name }
    }

    override fun getById(id: String): Grid? {
        return memoryTable[id]
    }

    override fun getByName(name: String): Grid? {
        return memoryTable.values.firstOrNull { it.name == name }
    }

    override fun save(grid: Grid): Grid {
        memoryTable[grid.id] = grid
        return grid
    }

    override fun delete(grid: Grid) {
        memoryTable.remove(grid.id)
    }

    override fun clear() {
        memoryTable.clear()
    }
}