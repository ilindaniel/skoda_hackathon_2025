package org.example.dao

import org.jetbrains.exposed.sql.Table

object Qualification : Table("qualification") {
    val id = integer("id").autoIncrement()
    val personalNumber = varchar("personal_number", 50).references(Employee.personalNumber)
    val description = varchar("description", 100)

    override val primaryKey = PrimaryKey(id)
}
