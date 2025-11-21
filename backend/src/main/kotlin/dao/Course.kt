package org.example.dao

import org.jetbrains.exposed.sql.Table

object Course : Table("course") {
    val id = varchar("id", 10)
    val name = varchar("name", 100)

    override val primaryKey = PrimaryKey(id)
}