package org.example.dao

import org.jetbrains.exposed.sql.Table

object Skill : Table("skill") {
    val id = varchar("id", 20)
    val name = varchar("name", 100).nullable()

    override val primaryKey = PrimaryKey(id)
}
