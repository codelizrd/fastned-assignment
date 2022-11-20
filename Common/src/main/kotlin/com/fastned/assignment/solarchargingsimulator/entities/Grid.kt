package com.fastned.assignment.solarchargingsimulator.entities

import java.util.*

data class Grid internal constructor(val id: String, val name: String, val age: Int) {
    constructor(name: String, age: Int): this(UUID.randomUUID().toString(), name, age)
}