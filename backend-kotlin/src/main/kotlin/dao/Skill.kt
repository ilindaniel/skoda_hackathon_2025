package org.example.dao

import org.jetbrains.exposed.sql.Table

object Skill : Table("skill") {
    val id = integer("id").autoIncrement()
    val objectId = varchar("object_id", 10)
    val name = varchar("name", 100).nullable()

    override val primaryKey = PrimaryKey(id)
}
