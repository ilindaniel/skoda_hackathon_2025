package org.example.dao

import org.jetbrains.exposed.sql.Table

object CurrentPosition : Table("current_position") {
    val id = integer("id").autoIncrement()
    val positionId = varchar("position_id", 11)
    val personalNumber = varchar("personal_number", 50).references(Employee.personalNumber)

    override val primaryKey = PrimaryKey(id)
}
