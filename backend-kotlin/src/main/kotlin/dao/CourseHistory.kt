package org.example.dao

import org.jetbrains.exposed.sql.Table

object CourseHistory : Table("course_history") {
    val id = integer("id").autoIncrement()
    val personalNumber = varchar("personal_number", 50).references(Employee.personalNumber)
    val objectId = varchar("object_id", 10)

    override val primaryKey = PrimaryKey(id)
}
