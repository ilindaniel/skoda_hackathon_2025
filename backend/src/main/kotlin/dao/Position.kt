package org.example.dao

import org.jetbrains.exposed.sql.Table

object Position : Table("position") {
    val id = varchar("id", 11)
    val description = varchar("description", 100)

    override val primaryKey = PrimaryKey(id)
}