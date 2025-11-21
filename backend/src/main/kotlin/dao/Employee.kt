package org.example.dao

import org.jetbrains.exposed.sql.Table

object Employee : Table("employee") {
    val personalNumber = varchar("personal_number", 50)
    val profession = varchar("profession", 50)
    val plannedProfession = varchar("planned_profession", 50)
    val username = varchar("user_name", 10)
    val plannedPositionId = varchar("planned_positon_id", 11)
    val plannedPosition = varchar("planned_positon", 50)

    override val primaryKey = PrimaryKey(personalNumber)
}
